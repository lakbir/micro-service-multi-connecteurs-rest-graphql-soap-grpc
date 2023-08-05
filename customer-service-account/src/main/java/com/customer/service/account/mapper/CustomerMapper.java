package com.customer.service.account.mapper;

import com.customer.customerdataservice.stub.CustomerServiceOuterClass;
import com.customer.service.account.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public Customer fromSoapCustomer(net.api.customerdataservice.web.Customer customer){
        return modelMapper.map(customer, Customer.class);
    }

    public Customer fromGrpcCustomer(CustomerServiceOuterClass.Customer customer){
        return modelMapper.map(customer, Customer.class);
    }
}
