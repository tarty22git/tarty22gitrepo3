package com.cth.job.provider.razorpay;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.provider.common.AbstractPaymentGatewayRouter;

public class RazorpayPaymentRouter extends AbstractPaymentGatewayRouter {

    public RazorpayPaymentRouter() {
        super(PaymentProvider.RAZORPAY, 4);
    }

    public RazorpayPaymentRouter(int initialWorkers) {
        super(PaymentProvider.RAZORPAY, initialWorkers);
    }

    @Override
    protected PaymentHandler createWorkerHandler(int workerIndex) {
        return new RazorpayPaymentHandler(workerIndex);
    }
}
