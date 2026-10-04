package com.cth.job.core.dto;

import com.cth.job.core.enums.PaymentProvider;
import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
    String transactionId,
    PaymentProvider provider,
    boolean success,
    String status,
    String gatewayTransactionId,
    BigDecimal amount,
    String currency,
    String message,
    Instant timestamp
) {}
