package com.cth.job.provider.razorpay;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;
import com.cth.job.core.enums.PaymentProvider;

import java.time.Instant;
import java.util.UUID;

public class RazorpayPaymentHandler implements PaymentHandler {

    private final String handlerId;

    public RazorpayPaymentHandler(int workerIndex) {
        this.handlerId = "razorpay-worker-" + workerIndex;
    }

    @Override
    public String getHandlerId() {
        return handlerId;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        String gatewayTxId = "tx_razorpay_" + UUID.randomUUID().toString().substring(0, 8);
        return new PaymentResponse(
            request.transactionId(),
            PaymentProvider.RAZORPAY,
            true,
            "SUCCESS",
            gatewayTxId,
            request.amount(),
            request.currency(),
            "Processed successfully by Razorpay Handler [" + handlerId + "]",
            Instant.now()
        );
    }

    @Override
    public RefundResponse processRefund(RefundRequest request) {
        return new RefundResponse(
            request.refundId(),
            request.originalTransactionId(),
            PaymentProvider.RAZORPAY,
            true,
            "REFUNDED",
            request.amount(),
            "Refunded by Razorpay Handler [" + handlerId + "]",
            Instant.now()
        );
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
