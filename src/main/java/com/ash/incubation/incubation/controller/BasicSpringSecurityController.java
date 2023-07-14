package com.ash.incubation.incubation.controller;

import com.ash.incubation.incubation.model.AuthRequest;
import com.ash.incubation.incubation.model.Customer;
import com.ash.incubation.incubation.service.BasicSecurityService;
import com.ash.incubation.incubation.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class BasicSpringSecurityController {

    @Autowired
    BasicSecurityService service;

    @Autowired
    JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String entryToApplication(){
        return service.welcomeGreetsOnLanding();
    }

    @GetMapping("/allCustomer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Customer> findAllCustomer(){
        return service.findAllCustomer();

    }

    @GetMapping("customerId/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Customer findCustomerById(@PathVariable int id){
        return service.findCustomerById(id);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        }else{
            throw new UsernameNotFoundException("invalid User");
        }

    }
}
