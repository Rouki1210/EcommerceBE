package com.example.ecommerceBE.Config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String secret;

    @Bean
    public PayPalHttpClient client(){
        PayPalEnvironment env = new PayPalEnvironment.Sandbox(clientId, secret);
        return new PayPalHttpClient(env);
    }
}
