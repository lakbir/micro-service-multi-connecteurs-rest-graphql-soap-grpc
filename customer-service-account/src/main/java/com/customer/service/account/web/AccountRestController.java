package com.customer.service.account.web;

import com.customer.customerdataservice.stub.CustomerServiceGrpc;
import com.customer.customerdataservice.stub.CustomerServiceOuterClass;
import com.customer.service.account.feign.CustomerRestController;
import com.customer.service.account.mapper.CustomerMapper;
import com.customer.service.account.model.Customer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.api.customerdataservice.web.CustomerSoapService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account-service")
public class AccountRestController {

    private RestTemplate restTemplate;
    private CustomerRestController customerRestController;
    private CustomerSoapService customerSoapService;
    private CustomerMapper customerMapper;
    @GrpcClient(value = "customerService")
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;

    public AccountRestController(RestTemplate restTemplate, CustomerRestController customerRestController, CustomerSoapService customerSoapService, CustomerMapper customerMapper) {
        this.restTemplate = restTemplate;
        this.customerRestController = customerRestController;
        this.customerSoapService = customerSoapService;
        this.customerMapper = customerMapper;
    }

    // ************************* Client REST *************************
    // consommation d'un rest api en utilisant resttemplate
    @GetMapping("/customers")
    public List<Customer> getListCoustomers(){
        Customer[] customers = restTemplate.getForObject("http://localhost:8083/customers", Customer[].class);
        return List.of(customers);
    }

    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        Customer customer = restTemplate.getForObject("http://localhost:8083/customers/"+id, Customer.class);
        return customer;
    }

    // consommation d'un rest api en utilisant webclient
    @GetMapping("/customers/v2")
    public Flux<Customer> getListCoustomersV2(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        Flux<Customer> customerFlux = webClient.get()
                .uri("/customers")
                .retrieve()
                .bodyToFlux(Customer.class);
        return customerFlux;
    }

    @GetMapping("/customers/v2/{id}")
    public Mono<Customer> getCustomerByIdV2(@PathVariable Long id){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        Mono<Customer> customerFlux = webClient.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(Customer.class);
        return customerFlux;
    }

    // consommation d'un rest api en utilisant openfeign
    @GetMapping("/customers/v3")
    public List<Customer> getListCoustomersV3(){
               return customerRestController.getCustomers();
    }

    @GetMapping("/customers/v3/{id}")
    public Customer getCustomerByIdV3(@PathVariable(name = "id") Long id){
        return customerRestController.getCustomerById(id);
    }



    // ******************************* Client GraphQL ****************************

    @GetMapping("/gql/customers")
    public Mono<List<Customer>> getListCoustomersGql(){
        HttpGraphQlClient graphQlClient=HttpGraphQlClient.builder()
                .url("http://localhost:8083/graphql")
                .build();
        var httpRequestDocument= """
                 query {
                     allCustomers{
                       name,email, id
                     }
                   }
                """;
        Mono<List<Customer>> customers = graphQlClient.document(httpRequestDocument)
                .retrieve("allCustomers")
                .toEntityList(Customer.class);
        return customers;
    }

    @GetMapping("/gql/customers/{id}")
    public Mono<Customer> getCustomerByIdGql(@PathVariable Long id){
        HttpGraphQlClient graphQlClient=HttpGraphQlClient.builder()
                .url("http://localhost:8083/graphql")
                .build();
        var httpRequestDocument= """
                 query($id:Int) {
                    customerById(id:$id){
                      id, name, email
                    }
                  }
                """;
        Mono<Customer> customerById = graphQlClient.document(httpRequestDocument)
                .variable("id",id)
                .retrieve("customerById")
                .toEntity(Customer.class);
        return customerById;
    }

    // ******************************* Soap Client ****************************

    @GetMapping("/soap/customers")
    List<Customer> getCustomers(){
        List<net.api.customerdataservice.web.Customer> soapCustomers = customerSoapService.customerList();
        return soapCustomers.stream().map(customerMapper::fromSoapCustomer).collect(Collectors.toList());
    }

    @GetMapping("/soap/customerById/{id}")
    public Customer getCustomerByIdFromSoap(@PathVariable(name = "id") Long id){
        net.api.customerdataservice.web.Customer customerSoap = customerSoapService.customerById(id);
        return customerMapper.fromSoapCustomer(customerSoap);
    }

    @GetMapping("/grpc/customers")
    public List<Customer> grpcCustomers(){
        CustomerServiceOuterClass.GetAllCustomersRequest request =
                CustomerServiceOuterClass.GetAllCustomersRequest.newBuilder().build();

        CustomerServiceOuterClass.GetCustomersResponse allCustomers = customerServiceBlockingStub.getAllCustomers(request);

        return allCustomers.getCustomersList().stream().map(customerMapper::fromGrpcCustomer).collect(Collectors.toList());
    }

    @GetMapping("/grpc/customers/{id}")
    public Customer getGrpcCustomer(@PathVariable() Long id){
        CustomerServiceOuterClass.GetCustomerByIdRequest request = CustomerServiceOuterClass.GetCustomerByIdRequest.newBuilder()
                .setCustomerId(id)
                .build();
        CustomerServiceOuterClass.GetCustomerByIdResponse customerById = customerServiceBlockingStub.getCustomerById(request);
        return customerMapper.fromGrpcCustomer(customerById.getCustomer());
    }
}
