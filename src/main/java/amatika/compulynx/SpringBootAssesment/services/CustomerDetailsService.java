package amatika.compulynx.SpringBootAssesment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


import amatika.compulynx.SpringBootAssesment.models.Customer;
import amatika.compulynx.SpringBootAssesment.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class CustomerDetailsService implements UserDetailsService 
{

    @Autowired
    private CustomerRepository customerRepository;

    /*@Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException 
    {
        Customer customer = customerRepository.findByCustomerId(customerId);                
        return new org.springframework.security.core.userdetails.User(
                customer.getCustomerId(), customer.getPin(), new ArrayList<>());
    }*/
    
    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found with id: " + customerId);
        }
        // Assuming roles are handled elsewhere; here, it's hardcoded to USER
        return new User(
        		customer.getCustomerId(),
        		customer.getPin(),
        		new ArrayList<>()
        		);
    }
}
