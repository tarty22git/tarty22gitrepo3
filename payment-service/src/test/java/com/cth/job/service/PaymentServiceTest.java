package com.cth.job.service;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.provider.stripe.StripePaymentRouter;
import com.cth.job.service.registry.PaymentGatewayRegistry;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void testRegistryAndRouting() {
        StripePaymentRouter stripeRouter = new StripePaymentRouter(3);
        PaymentGatewayRegistry registry = new PaymentGatewayRegistry(List.of(stripeRouter));

        assertEquals(1, registry.getAllRouters().size());
        assertEquals(3, stripeRouter.getWorkerPoolSize());

        PaymentRequest req = new PaymentRequest("TX-123", PaymentProvider.STRIPE, new BigDecimal("100.00"), "USD", "payer@test.com", "Test", null);
        PaymentResponse resp = registry.processPayment(req);

        assertTrue(resp.success());
        assertEquals("SUCCESS", resp.status());
    }
}
