package com.cth.job.provider.common;

import com.cth.job.core.contract.PaymentHandler;
import com.cth.job.core.contract.PaymentRouter;
import com.cth.job.core.contract.PaymentStrategy;
import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.core.dto.PaymentResponse;
import com.cth.job.core.dto.RefundRequest;
import com.cth.job.core.dto.RefundResponse;
import com.cth.job.core.enums.PaymentProvider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPaymentGatewayRouter implements PaymentRouter, PaymentStrategy {

    private final PaymentProvider provider;
    private final List<PaymentHandler> workerPool = new CopyOnWriteArrayList<>();
    private final AtomicInteger roundRobinIndex = new AtomicInteger(0);
    private final AtomicBoolean active = new AtomicBoolean(true);

    public AbstractPaymentGatewayRouter(PaymentProvider provider, int initialPoolSize) {
        this.provider = provider;
        resizeWorkerPool(initialPoolSize);
    }

    protected abstract PaymentHandler createWorkerHandler(int workerIndex);

    @Override
    public PaymentProvider getProvider() {
        return provider;
    }

    @Override
    public boolean isSupported(PaymentProvider provider) {
        return this.provider == provider;
    }

    @Override
    public boolean isRunning() {
        return active.get();
    }

    @Override
    public void start() {
        active.set(true);
    }

    @Override
    public void stop() {
        active.set(false);
    }

    @Override
    public int getWorkerPoolSize() {
        return workerPool.size();
    }

    @Override
    public synchronized void resizeWorkerPool(int newSize) {
        if (newSize < 1) {
            newSize = 1;
        }
        int currentSize = workerPool.size();
        if (newSize > currentSize) {
            for (int i = currentSize; i < newSize; i++) {
                workerPool.add(createWorkerHandler(i + 1));
            }
        } else if (newSize < currentSize) {
            for (int i = currentSize - 1; i >= newSize; i--) {
                workerPool.remove(i);
            }
        }
    }

    private PaymentHandler getNextAvailableHandler() {
        if (!active.get()) {
            throw new IllegalStateException("Gateway " + provider + " is currently STOPPED.");
        }
        if (workerPool.isEmpty()) {
            throw new IllegalStateException("No active worker handlers configured for gateway " + provider);
        }
        int index = Math.abs(roundRobinIndex.getAndIncrement() % workerPool.size());
        return workerPool.get(index);
    }

    @Override
    public PaymentResponse routePayment(PaymentRequest request) {
        PaymentHandler handler = getNextAvailableHandler();
        return handler.processPayment(request);
    }

    @Override
    public RefundResponse routeRefund(RefundRequest request) {
        PaymentHandler handler = getNextAvailableHandler();
        return handler.processRefund(request);
    }

    @Override
    public PaymentResponse process(PaymentRequest request) {
        return routePayment(request);
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        return routeRefund(request);
    }
}
