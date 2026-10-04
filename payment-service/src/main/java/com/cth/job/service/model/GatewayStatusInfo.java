package com.cth.job.service.model;

import com.cth.job.core.enums.PaymentProvider;

public class GatewayStatusInfo {
    private PaymentProvider provider;
    private boolean running;
    private int workerPoolSize;
    private long totalProcessedCount;
    private long totalRefundCount;

    public GatewayStatusInfo(PaymentProvider provider, boolean running, int workerPoolSize, long totalProcessedCount, long totalRefundCount) {
        this.provider = provider;
        this.running = running;
        this.workerPoolSize = workerPoolSize;
        this.totalProcessedCount = totalProcessedCount;
        this.totalRefundCount = totalRefundCount;
    }

    public PaymentProvider getProvider() { return provider; }
    public void setProvider(PaymentProvider provider) { this.provider = provider; }

    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }

    public int getWorkerPoolSize() { return workerPoolSize; }
    public void setWorkerPoolSize(int workerPoolSize) { this.workerPoolSize = workerPoolSize; }

    public long getTotalProcessedCount() { return totalProcessedCount; }
    public void setTotalProcessedCount(long totalProcessedCount) { this.totalProcessedCount = totalProcessedCount; }

    public long getTotalRefundCount() { return totalRefundCount; }
    public void setTotalRefundCount(long totalRefundCount) { this.totalRefundCount = totalRefundCount; }
}
