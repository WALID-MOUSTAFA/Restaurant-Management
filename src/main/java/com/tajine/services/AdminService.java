package com.tajine.services;

import java.util.List;

import com.tajine.domain.Admin;
import com.tajine.repositories.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
        @Autowired
        private AdminRepository adminRepository;

        public Admin createAdmin(Admin admin) {
                Admin result = this.adminRepository.save(admin);
                if (result.getId() != null ){
                        return result;
                } else {
                        return null;
                }
        }

        public Admin updateAdmin(Admin admin) {
                Admin original = this.adminRepository.findById(admin.getId()).get();
                original.setUsername((admin.getUsername()));
                original.setFullName(admin.getFullName());
		original.setPassword(admin.getPassword());
                original.setRole(admin.getRole());
                this.adminRepository.save(original);
                return original;
        }

	public List<Admin> getAllAdmins() {
		return (List<Admin>)this.adminRepository.findAll();
		
	}

	public boolean login(String username, String password) {
		Admin admin = this.adminRepository.findByUsernameAndPassword(username, password);

		if(admin != null) {
			return true;
		}
		return false;
	}

	public Admin getAdminByUsernameAndPasswor(String username, String password){
                return this.adminRepository.findByUsernameAndPassword(username, password);
        }

        public boolean deleteAdmin(Admin admin) {
                if(admin.getRole() == 0) {
                        return false;
                }
                this.adminRepository.delete(admin);
                return true;
        }
}

