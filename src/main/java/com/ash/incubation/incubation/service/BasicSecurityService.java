package com.ash.incubation.incubation.service;

import com.ash.incubation.incubation.entity.UserInfo;
import com.ash.incubation.incubation.model.Customer;
import com.ash.incubation.incubation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BasicSecurityService {

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public String welcomeGreetsOnLanding() {
        return "Welcome To Spring Security Session";
    }

    public List<Customer> findAllCustomer() {
        List custList = new ArrayList();
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Pratap");
        customer.setType("Retail");
        customer.setAddress("New Delhi");
        custList.add(customer);
        return  custList;
    }

    public Customer findCustomerById(int id) {
        return new Customer(2,"Ashwarya","WholeSeller","Pune");
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added in System";
    }
}
