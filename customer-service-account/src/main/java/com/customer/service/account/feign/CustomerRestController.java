package com.customer.service.account.feign;

import com.customer.service.account.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "http://localhost:8083",value = "customer-rest-client")
public interface CustomerRestController {

    @GetMapping("/customers")
    public List<Customer> getCustomers();

    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable(name = "id") Long id);
}
