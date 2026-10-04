package com.cth.job.core.dto;

import com.cth.job.core.enums.PaymentProvider;
import java.math.BigDecimal;

public record RefundRequest(
    String refundId,
    String originalTransactionId,
    PaymentProvider provider,
    BigDecimal amount,
    String reason
) {}
