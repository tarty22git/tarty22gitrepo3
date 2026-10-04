package com.cth.job.provider.razorpay.config;

import com.cth.job.provider.razorpay.RazorpayPaymentRouter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RazorpayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RazorpayPaymentRouter razorpayPaymentRouter() {
        return new RazorpayPaymentRouter(4);
    }
}
