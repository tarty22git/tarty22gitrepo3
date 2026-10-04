package com.cth.job.provider.paypal;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.provider.common.AbstractPaymentGatewayRouter;

public class PaypalPaymentRouter extends AbstractPaymentGatewayRouter {

    public PaypalPaymentRouter() {
        super(PaymentProvider.PAYPAL, 3);
    }

    public PaypalPaymentRouter(int initialWorkers) {
        super(PaymentProvider.PAYPAL, initialWorkers);
    }

    @Override
    protected PaymentHandler createWorkerHandler(int workerIndex) {
        return new PaypalPaymentHandler(workerIndex);
    }
}
