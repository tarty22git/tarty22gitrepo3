package com.cth.job.service.service;

import com.cth.job.service.entity.AuditLog;
import com.cth.job.service.entity.PaymentTransaction;
import com.cth.job.service.model.GatewayStatusInfo;
import com.cth.job.service.registry.PaymentGatewayRegistry;
import com.cth.job.service.repository.AuditLogRepository;
import com.cth.job.service.repository.PaymentTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final PaymentTransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final PaymentGatewayRegistry gatewayRegistry;

    @Autowired
    public ReportService(PaymentTransactionRepository transactionRepository,
                          AuditLogRepository auditLogRepository,
                          PaymentGatewayRegistry gatewayRegistry) {
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
        this.gatewayRegistry = gatewayRegistry;
    }

    public Map<String, Object> generateOnlineReport(String reportType) {
        Map<String, Object> report = new HashMap<>();
        report.put("reportType", reportType);
        report.put("generatedAt", java.time.LocalDateTime.now().toString());

        switch (reportType.toUpperCase()) {
            case "TRANSACTIONS":
                List<PaymentTransaction> txs = transactionRepository.findAll();
                report.put("totalRecords", txs.size());
                report.put("data", txs);
                break;
            case "AUDIT":
                List<AuditLog> logs = auditLogRepository.findAll();
                report.put("totalRecords", logs.size());
                report.put("data", logs);
                break;
            case "GATEWAYS":
            default:
                List<GatewayStatusInfo> statuses = gatewayRegistry.getGatewayStatuses();
                report.put("totalRecords", statuses.size());
                report.put("data", statuses);
                break;
        }
        return report;
    }

    public byte[] exportReportCsv(String reportType) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if ("TRANSACTIONS".equalsIgnoreCase(reportType)) {
            writer.println("Transaction ID,Provider,Amount,Currency,Payer ID,Status,Gateway Tx ID,Created By,Created At");
            for (PaymentTransaction tx : transactionRepository.findAll()) {
                writer.printf("%s,%s,%.2f,%s,%s,%s,%s,%s,%s\n",
                        tx.getTransactionId(),
                        tx.getProvider(),
                        tx.getAmount(),
                        tx.getCurrency(),
                        tx.getPayerId() != null ? tx.getPayerId() : "",
                        tx.getStatus(),
                        tx.getGatewayTxId() != null ? tx.getGatewayTxId() : "",
                        tx.getCreatedBy() != null ? tx.getCreatedBy() : "",
                        tx.getCreatedAt() != null ? tx.getCreatedAt().format(dtf) : "");
            }
        } else if ("AUDIT".equalsIgnoreCase(reportType)) {
            writer.println("Log ID,Username,Action,Details,Timestamp");
            for (AuditLog log : auditLogRepository.findAll()) {
                writer.printf("%d,%s,%s,%s,%s\n",
                        log.getId(),
                        log.getUsername() != null ? log.getUsername() : "",
                        log.getAction(),
                        log.getDetails() != null ? log.getDetails().replaceAll(",", " ") : "",
                        log.getTimestamp() != null ? log.getTimestamp().format(dtf) : "");
            }
        } else {
            writer.println("Provider,Running,Worker Pool Size,Total Processed Tx,Total Refund Tx");
            for (GatewayStatusInfo info : gatewayRegistry.getGatewayStatuses()) {
                writer.printf("%s,%b,%d,%d,%d\n",
                        info.getProvider(),
                        info.isRunning(),
                        info.getWorkerPoolSize(),
                        info.getTotalProcessedCount(),
                        info.getTotalRefundCount());
            }
        }

        writer.flush();
        return out.toByteArray();
    }
}
