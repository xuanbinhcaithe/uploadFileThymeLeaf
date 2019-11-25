package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/customer")
    public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer customer,UriComponentsBuilder builder) {
        customerService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    @GetMapping(value = "/customer")
    public ResponseEntity<List<Customer>> listCustomer() {
        List<Customer> customers = (List<Customer>) customerService.findAll();
        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }
    @GetMapping(value = "/customer/{id}")
    public ResponseEntity<Customer> customer(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    @PutMapping(value = "/customer/{id}")
    public ResponseEntity<Customer> update(@PathVariable("id") Long id,@RequestBody Customer customer) {
        Customer oldCustomer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        oldCustomer.setFirstName(customer.getFirstName());
        oldCustomer.setLastName(customer.getLastName());
        oldCustomer.setEmail(customer.getEmail());
        customerService.save(oldCustomer);
        return new ResponseEntity<>(oldCustomer,HttpStatus.OK);
    }
    @DeleteMapping(value = "/customer/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        customerService.remove(customer.getId());
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    @DeleteMapping(value = "/customer")
    public ResponseEntity<Void> deleteAllCustomer() {
        customerService.removeAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
