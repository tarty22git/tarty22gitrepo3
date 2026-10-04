package com.cth.job.core.dto;

import com.cth.job.core.enums.PaymentProvider;
import java.math.BigDecimal;
import java.util.Map;

public record PaymentRequest(
    String transactionId,
    PaymentProvider provider,
    BigDecimal amount,
    String currency,
    String payerId,
    String description,
    Map<String, String> metadata
) {}
