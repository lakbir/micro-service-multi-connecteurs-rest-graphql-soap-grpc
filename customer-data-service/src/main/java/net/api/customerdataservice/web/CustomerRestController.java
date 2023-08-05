package net.api.customerdataservice.web;

import lombok.AllArgsConstructor;
import net.api.customerdataservice.entities.Customer;
import net.api.customerdataservice.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerRestController {
    private CustomerRepository customerRepository;
    @GetMapping("/customers")
    public List<Customer> customerList(){
        return customerRepository.findAll();
    }
    @GetMapping("/customers/{id}")
    public Customer customerById(@PathVariable Long id){
        return customerRepository.findById(id).get();
    }
    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer){
        customerRepository.save(customer);
        return customer;
    }
}
