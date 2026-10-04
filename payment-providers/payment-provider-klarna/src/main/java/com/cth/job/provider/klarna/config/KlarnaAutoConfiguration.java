package com.cth.job.provider.klarna.config;

import com.cth.job.provider.klarna.KlarnaPaymentRouter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class KlarnaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KlarnaPaymentRouter klarnaPaymentRouter() {
        return new KlarnaPaymentRouter(2);
    }
}
