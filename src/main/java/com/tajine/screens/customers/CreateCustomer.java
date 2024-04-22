package com.tajine.screens.customers;

import java.io.IOException;

import com.tajine.domain.Customer;
import com.tajine.screens.commons.FXController;
import com.tajine.services.CustomerService;
import com.tajine.utils.FXUtils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class CreateCustomer extends FXController{

	
	
	
	@Autowired
	private CustomerService customerService;
	@FXML
	private TextField customerNameField;
	@FXML
	private TextField customerPhoneField;
	@FXML
	private TextField customerAddressField;
	
	private Customer customer;



		@Override
		public void open(Stage stage) throws IOException {
			this.openWindowFromFXML("CreateCustomer.fxml", stage, this.getClass(), 500, 500);
			
		}
	

	
	@FXML
	private void addCustomerAction() {
		Customer customer;
		String name;
		String phone;
		String address;

		try {
			customer = new Customer();
			name = this.customerNameField.getText();
			phone = this.customerPhoneField.getText();
			address = this.customerAddressField.getText();
			customer.setName(name);
			customer.setPhone(phone);
			customer.setAddress(address);
			try {
				Customer result = this.customerService.createCustomer(customer);
				if(result.getId() != null){
					this.setCustomer(result);
					this.stage.close();
				} else {
					//TODO(WALID): HANDLE;
				}
			} catch (ConstraintViolationException e) {
				FXUtils.ALERT_ERROR("يوجد عميل بهذا الاسم مسبقاً.");
				e.printStackTrace();
			}
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ ما أثناء الإضافة، تأكد من صحة البيانات المدخلة");
			e.printStackTrace();
			return;
		} 
		
	}

	
	public Customer addCustomerAction(String name, String phone, String address) {
		Customer customer;

		try {
			customer = new Customer();
			customer.setName(name);
			customer.setPhone(phone);
			customer.setAddress(address);
			try {
				Customer result = this.customerService.createCustomer(customer);
				if(result.getId() != null){
					return result;
				} else {
					//TODO(WALID): HANDLE;
				}
			} catch (ConstraintViolationException e) {
				FXUtils.ALERT_ERROR("يوجد عميل بهذا الاسم مسبقاً.");
				e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ ما أثناء الإضافة، تأكد من صحة البيانات المدخلة");
			e.printStackTrace();
			return null;
		} 
		return null;
	}

	
	public Customer getCustomer() {
		return customer;
	}



	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


}
