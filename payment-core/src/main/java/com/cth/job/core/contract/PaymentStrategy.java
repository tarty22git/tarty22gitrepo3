package com.cth.job.core.contract;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;
import com.cth.job.core.enums.PaymentProvider;

public interface PaymentStrategy {
    PaymentProvider getProvider();
    PaymentResponse process(PaymentRequest request);
    RefundResponse refund(RefundRequest request);
    boolean isSupported(PaymentProvider provider);
}
