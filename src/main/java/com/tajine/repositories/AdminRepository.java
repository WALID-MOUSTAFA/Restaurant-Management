package com.tajine.repositories;

import com.tajine.domain.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {
	public Admin findByUsernameAndPassword(String usrename, String password);
}
