package com.cth.job.core.contract;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;
import com.cth.job.core.enums.PaymentProvider;

public interface PaymentRouter {
    PaymentProvider getProvider();
    PaymentResponse routePayment(PaymentRequest request);
    RefundResponse routeRefund(RefundRequest request);
    int getWorkerPoolSize();
    void resizeWorkerPool(int newSize);
    boolean isRunning();
    void start();
    void stop();
}
