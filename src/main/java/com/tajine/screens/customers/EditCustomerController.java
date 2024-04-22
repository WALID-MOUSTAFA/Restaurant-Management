package com.tajine.screens.customers;

import java.io.IOException;

import com.tajine.domain.Customer;
import com.tajine.screens.commons.FXController;
import com.tajine.services.CustomerService;
import com.tajine.utils.FXUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class EditCustomerController extends FXController {

	@Autowired
	private CustomerService customerService;
	@FXML
	private TextField customerNameField;
	@FXML
	private TextField customerPhoneField;
	@FXML
	private TextField customerAddressField;
	private Customer targetCustomer;

	
	
	@Override
	protected void open(Stage stage) throws IOException {
		this.openWindowFromFXML("EditCustomer.fxml", stage, this.getClass(), 500, 500);
	}

	@FXML
	private void initialize() {
		this.customerNameField.setText(this.targetCustomer.getName());
		this.customerPhoneField.setText(this.targetCustomer.getPhone());
		this.customerAddressField.setText(this.targetCustomer.getAddress());
	}

	public Customer getTargetCustomer() {
		return this.targetCustomer;
	}

	public void setTargetCustomer(Customer customer) {
		this.targetCustomer = customer;
	}

	@FXML
	private void doEdit() {
		String name;
		String phone;
		String address;
		
		name = this.customerNameField.getText();
		phone = this.customerPhoneField.getText();
		address = this.customerAddressField.getText();
	        targetCustomer.setName(name);
		targetCustomer.setPhone(phone);
		targetCustomer.setAddress(address);
		
		try {
			Customer result = this.customerService.updateCustomer(targetCustomer);
			this.stage.close();
			
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ ما أثناء الإضافة، تأكد من صحة البيانات المدخلة");
			e.printStackTrace();
		}
	}
}
