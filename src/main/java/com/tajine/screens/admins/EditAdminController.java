package com.tajine.screens.admins;

import java.io.IOException;

import com.tajine.domain.Admin;
import com.tajine.screens.commons.FXController;
import com.tajine.services.AdminService;
import com.tajine.utils.FXUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class EditAdminController extends FXController {

	@Autowired
	private AdminService adminService;
	private Admin targetAdmin;
	
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField fullNameField;
	
	@Override
	protected void open(Stage stage) throws IOException {
		this.openWindowFromFXML("EditAdmin.fxml",
					stage, this.getClass(), 500, 500);
	}
	

	@FXML
	private void initialize() {
		this.initializeForm();
	}

	private void initializeForm() {
		this.usernameField.setText(this.targetAdmin.getUsername());
		this.fullNameField.setText(this.targetAdmin.getFullName());
		this.passwordField.setText(this.targetAdmin.getPassword());
	}



	@FXML
	private void doEdit() {
		String username;
		String fullname;
		String password;

		try {
			username = this.usernameField.getText();
			fullname = this.fullNameField.getText();
			password = this.passwordField.getText();
			
			if(username == null
			   || fullname == null
			   || password == null) {
				FXUtils.ALERT_ERROR("ادخل البيانات بصورة سليمة");
				return;
			}

			targetAdmin.setUsername(username);
			targetAdmin.setFullName(fullname);
			targetAdmin.setPassword(password);
			// targetAdmin.setRole(1);

		Admin result = this.adminService.updateAdmin(targetAdmin);
		if (result != null) {
			this.setTargetAdmin(result);
			FXUtils.ALERT_SUCCESS("تم التعديل بنجاح.");
			this.stage.close();
		} else {
			FXUtils.ALERT_ERROR("حدث خطأ أثناء الإضافة");
			return;
		}
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ أثناء الإضافة");
			e.printStackTrace();
			return;
		} 

	}


	
	public Admin getTargetAdmin() {
		return targetAdmin;
	}

	public void setTargetAdmin(Admin targetAdmin) {
		this.targetAdmin = targetAdmin;
	}
	

       
}
