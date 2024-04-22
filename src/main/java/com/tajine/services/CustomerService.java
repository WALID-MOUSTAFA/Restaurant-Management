package com.tajine.services;

import com.tajine.domain.Customer;
import com.tajine.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer createCustomer(Customer customer) {
		Customer result = this.customerRepository.save(customer);
		return result.getId() == null ? null : result;
	}


	public Customer updateCustomer(Customer customer) {
		Customer original = this.customerRepository.findById(customer.getId()).get();
		original.setName(customer.getName());
		original.setPhone(customer.getPhone());
		original.setAddress(customer.getAddress());
		return this.customerRepository.save(original);
	}


	public List<Customer> getAllCustomer() {
		return (List<Customer>) this.customerRepository.findAll();
	}
	
		
	public void deleteCustomer(Customer customer) {
		this.customerRepository.delete(customer);
	}

	public Customer getTakeAwayCustomer() {
		Customer customer = this.customerRepository.findByName("take_away_customer");
		if (customer == null) {
			Customer customer2 = new Customer();
			customer2.setName("take_away_customer");
			customer2.setPhone("-");
			customer2.setAddress("-");
			return this.createCustomer(customer2);
		}
		return customer;
	}
}
