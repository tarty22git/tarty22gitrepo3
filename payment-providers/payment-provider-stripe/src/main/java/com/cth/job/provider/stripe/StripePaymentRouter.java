package com.cth.job.provider.stripe;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.provider.common.AbstractPaymentGatewayRouter;

public class StripePaymentRouter extends AbstractPaymentGatewayRouter {

    public StripePaymentRouter() {
        super(PaymentProvider.STRIPE, 3);
    }

    public StripePaymentRouter(int initialWorkers) {
        super(PaymentProvider.STRIPE, initialWorkers);
    }

    @Override
    protected PaymentHandler createWorkerHandler(int workerIndex) {
        return new StripePaymentHandler(workerIndex);
    }
}
