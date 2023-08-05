package net.api.customerdataservice.web;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 4.0.0
 * 2023-08-05T17:33:21.677+02:00
 * Generated source version: 4.0.0
 *
 */
@WebService(targetNamespace = "http://web.customerdataservice.api.net/", name = "CustomerSoapService")
@XmlSeeAlso({ObjectFactory.class})
public interface CustomerSoapService {

    @WebMethod
    @RequestWrapper(localName = "customerById", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.CustomerById")
    @ResponseWrapper(localName = "customerByIdResponse", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.CustomerByIdResponse")
    @WebResult(name = "return", targetNamespace = "")
    public net.api.customerdataservice.web.Customer customerById(

        @WebParam(name = "id", targetNamespace = "")
        java.lang.Long id
    );

    @WebMethod
    @RequestWrapper(localName = "customerList", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.CustomerList")
    @ResponseWrapper(localName = "customerListResponse", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.CustomerListResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.util.List<net.api.customerdataservice.web.Customer> customerList()
;

    @WebMethod
    @RequestWrapper(localName = "saveCustomer", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.SaveCustomer")
    @ResponseWrapper(localName = "saveCustomerResponse", targetNamespace = "http://web.customerdataservice.api.net/", className = "net.api.customerdataservice.web.SaveCustomerResponse")
    @WebResult(name = "return", targetNamespace = "")
    public net.api.customerdataservice.web.Customer saveCustomer(

        @WebParam(name = "customer", targetNamespace = "")
        net.api.customerdataservice.web.CustomerRequest customer
    );
}
