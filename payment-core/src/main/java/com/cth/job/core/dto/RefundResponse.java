package com.cth.job.core.dto;

import com.cth.job.core.enums.PaymentProvider;
import java.math.BigDecimal;
import java.time.Instant;

public record RefundResponse(
    String refundId,
    String originalTransactionId,
    PaymentProvider provider,
    boolean success,
    String status,
    BigDecimal amount,
    String message,
    Instant timestamp
) {}
