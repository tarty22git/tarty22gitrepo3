package com.cth.job.provider.klarna;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.provider.common.AbstractPaymentGatewayRouter;

public class KlarnaPaymentRouter extends AbstractPaymentGatewayRouter {

    public KlarnaPaymentRouter() {
        super(PaymentProvider.KLARNA, 2);
    }

    public KlarnaPaymentRouter(int initialWorkers) {
        super(PaymentProvider.KLARNA, initialWorkers);
    }

    @Override
    protected PaymentHandler createWorkerHandler(int workerIndex) {
        return new KlarnaPaymentHandler(workerIndex);
    }
}
