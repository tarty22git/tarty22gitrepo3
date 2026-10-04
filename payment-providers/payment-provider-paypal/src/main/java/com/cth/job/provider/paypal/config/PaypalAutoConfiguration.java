package com.cth.job.provider.paypal.config;

import com.cth.job.provider.paypal.PaypalPaymentRouter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PaypalAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaypalPaymentRouter paypalPaymentRouter() {
        return new PaypalPaymentRouter(3);
    }
}
