package com.cth.job.service.service;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.service.entity.AuditLog;
import com.cth.job.service.entity.MakerCheckerRequest;
import com.cth.job.service.entity.PaymentTransaction;
import com.cth.job.service.registry.PaymentGatewayRegistry;
import com.cth.job.service.repository.AuditLogRepository;
import com.cth.job.service.repository.MakerCheckerRequestRepository;
import com.cth.job.service.repository.PaymentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MakerCheckerService {

    private final MakerCheckerRequestRepository requestRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final PaymentGatewayRegistry gatewayRegistry;
    private final ObjectMapper objectMapper;

    @Autowired
    public MakerCheckerService(MakerCheckerRequestRepository requestRepository,
                               PaymentTransactionRepository transactionRepository,
                               AuditLogRepository auditLogRepository,
                               PaymentGatewayRegistry gatewayRegistry) {
        this.requestRepository = requestRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
        this.gatewayRegistry = gatewayRegistry;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public MakerCheckerRequest submitPaymentRequest(PaymentRequest request, String makerUser) {
        try {
            String json = objectMapper.writeValueAsString(request);
            MakerCheckerRequest mcReq = new MakerCheckerRequest();
            mcReq.setRequestType("PAYMENT");
            mcReq.setMakerUser(makerUser);
            mcReq.setStatus("PENDING");
            mcReq.setPayloadJson(json);
            mcReq.setRemarks("Payment request initiated by maker: " + makerUser);

            MakerCheckerRequest saved = requestRepository.save(mcReq);

            PaymentTransaction tx = new PaymentTransaction();
            tx.setTransactionId(request.transactionId());
            tx.setProvider(request.provider().name());
            tx.setAmount(request.amount());
            tx.setCurrency(request.currency());
            tx.setPayerId(request.payerId());
            tx.setStatus("PENDING_APPROVAL");
            tx.setDescription(request.description());
            tx.setCreatedBy(makerUser);
            transactionRepository.save(tx);

            auditLogRepository.save(new AuditLog(makerUser, "MAKER_SUBMIT_PAYMENT", "Submitted transaction " + request.transactionId()));
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit Maker-Checker request", e);
        }
    }

    public List<MakerCheckerRequest> getPendingRequests() {
        return requestRepository.findByStatus("PENDING");
    }

    public List<MakerCheckerRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    @Transactional
    public MakerCheckerRequest approveRequest(Long requestId, String checkerUser, String remarks) {
        MakerCheckerRequest mcReq = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));

        if (!"PENDING".equalsIgnoreCase(mcReq.getStatus())) {
            throw new IllegalStateException("Request is not in PENDING state");
        }
        if (mcReq.getMakerUser().equalsIgnoreCase(checkerUser)) {
            throw new IllegalArgumentException("Maker and Checker cannot be the same user!");
        }

        mcReq.setStatus("APPROVED");
        mcReq.setCheckerUser(checkerUser);
        mcReq.setRemarks(remarks);
        mcReq.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(mcReq);

        if ("PAYMENT".equalsIgnoreCase(mcReq.getRequestType())) {
            try {
                PaymentRequest paymentRequest = objectMapper.readValue(mcReq.getPayloadJson(), PaymentRequest.class);
                PaymentResponse response = gatewayRegistry.processPayment(paymentRequest);

                PaymentTransaction tx = transactionRepository.findByTransactionId(paymentRequest.transactionId())
                        .orElseGet(() -> {
                            PaymentTransaction newTx = new PaymentTransaction();
                            newTx.setTransactionId(paymentRequest.transactionId());
                            return newTx;
                        });

                tx.setStatus(response.success() ? "SUCCESS" : "FAILED");
                tx.setGatewayTxId(response.gatewayTransactionId());
                transactionRepository.save(tx);

                auditLogRepository.save(new AuditLog(checkerUser, "CHECKER_APPROVE_PAYMENT", "Approved tx " + paymentRequest.transactionId() + " Status: " + tx.getStatus()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to process payment upon approval", e);
            }
        }
        return mcReq;
    }

    @Transactional
    public MakerCheckerRequest rejectRequest(Long requestId, String checkerUser, String remarks) {
        MakerCheckerRequest mcReq = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));

        if (!"PENDING".equalsIgnoreCase(mcReq.getStatus())) {
            throw new IllegalStateException("Request is not in PENDING state");
        }

        mcReq.setStatus("REJECTED");
        mcReq.setCheckerUser(checkerUser);
        mcReq.setRemarks(remarks);
        mcReq.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(mcReq);

        if ("PAYMENT".equalsIgnoreCase(mcReq.getRequestType())) {
            try {
                PaymentRequest paymentRequest = objectMapper.readValue(mcReq.getPayloadJson(), PaymentRequest.class);
                transactionRepository.findByTransactionId(paymentRequest.transactionId()).ifPresent(tx -> {
                    tx.setStatus("REJECTED");
                    transactionRepository.save(tx);
                });
            } catch (Exception ignored) {}
        }

        auditLogRepository.save(new AuditLog(checkerUser, "CHECKER_REJECT_PAYMENT", "Rejected request ID " + requestId));
        return mcReq;
    }
}
