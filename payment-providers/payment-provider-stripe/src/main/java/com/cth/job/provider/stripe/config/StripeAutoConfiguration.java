package com.cth.job.provider.stripe.config;

import com.cth.job.provider.stripe.StripePaymentRouter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class StripeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StripePaymentRouter stripePaymentRouter() {
        return new StripePaymentRouter(3);
    }
}
