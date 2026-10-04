package com.cth.job.service.registry;

import com.cth.job.core.contract.PaymentRouter;
import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;
import com.cth.job.core.enums.PaymentProvider;
import com.cth.job.service.model.GatewayStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentGatewayRegistry {

    private final Map<PaymentProvider, PaymentRouter> routerRegistry = new ConcurrentHashMap<>();
    private final Map<PaymentProvider, AtomicLong> processedTxCount = new ConcurrentHashMap<>();
    private final Map<PaymentProvider, AtomicLong> processedRefundCount = new ConcurrentHashMap<>();

    @Autowired
    public PaymentGatewayRegistry(List<PaymentRouter> routers) {
        if (routers != null) {
            for (PaymentRouter router : routers) {
                registerRouter(router);
            }
        }
    }

    public void registerRouter(PaymentRouter router) {
        routerRegistry.put(router.getProvider(), router);
        processedTxCount.putIfAbsent(router.getProvider(), new AtomicLong(0));
        processedRefundCount.putIfAbsent(router.getProvider(), new AtomicLong(0));
    }

    public PaymentRouter getRouter(PaymentProvider provider) {
        PaymentRouter router = routerRegistry.get(provider);
        if (router == null) {
            throw new IllegalArgumentException("No registered payment gateway router found for provider: " + provider);
        }
        return router;
    }

    public Collection<PaymentRouter> getAllRouters() {
        return routerRegistry.values();
    }

    public List<GatewayStatusInfo> getGatewayStatuses() {
        List<GatewayStatusInfo> list = new ArrayList<>();
        for (PaymentProvider provider : PaymentProvider.values()) {
            PaymentRouter router = routerRegistry.get(provider);
            if (router != null) {
                long txCount = processedTxCount.getOrDefault(provider, new AtomicLong(0)).get();
                long refundCount = processedRefundCount.getOrDefault(provider, new AtomicLong(0)).get();
                list.add(new GatewayStatusInfo(provider, router.isRunning(), router.getWorkerPoolSize(), txCount, refundCount));
            } else {
                list.add(new GatewayStatusInfo(provider, false, 0, 0, 0));
            }
        }
        return list;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        PaymentRouter router = getRouter(request.provider());
        PaymentResponse response = router.routePayment(request);
        if (response.success()) {
            processedTxCount.get(request.provider()).incrementAndGet();
        }
        return response;
    }

    public RefundResponse processRefund(RefundRequest request) {
        PaymentRouter router = getRouter(request.provider());
        RefundResponse response = router.routeRefund(request);
        if (response.success()) {
            processedRefundCount.get(request.provider()).incrementAndGet();
        }
        return response;
    }

    public void startGateway(PaymentProvider provider) {
        PaymentRouter router = getRouter(provider);
        router.start();
    }

    public void stopGateway(PaymentProvider provider) {
        PaymentRouter router = getRouter(provider);
        router.stop();
    }

    public void resizeWorkerPool(PaymentProvider provider, int newSize) {
        PaymentRouter router = getRouter(provider);
        router.resizeWorkerPool(newSize);
    }
}
