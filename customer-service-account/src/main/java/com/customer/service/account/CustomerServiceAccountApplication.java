package com.customer.service.account;

import net.api.customerdataservice.web.CustomerSoapService;
import net.api.customerdataservice.web.CustomerWS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceAccountApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    CustomerSoapService customerSoapService(){
        return new CustomerWS().getCustomerSoapServicePort();
    }
}
