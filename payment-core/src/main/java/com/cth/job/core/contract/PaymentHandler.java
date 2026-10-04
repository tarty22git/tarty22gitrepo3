package com.cth.job.core.contract;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;

public interface PaymentHandler {
    String getHandlerId();
    PaymentResponse processPayment(PaymentRequest request);
    RefundResponse processRefund(RefundRequest request);
    boolean isAvailable();
}
