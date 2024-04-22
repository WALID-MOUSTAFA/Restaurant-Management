package com.tajine.repositories;

import com.tajine.domain.Customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	public Customer findByName(String name);
}
