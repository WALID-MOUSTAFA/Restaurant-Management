package com.tajine.screens.login;

import java.io.IOException;

import com.tajine.domain.Admin;
import com.tajine.screens.commons.FXController;
import com.tajine.services.AdminService;
import com.tajine.utils.FXUtils;

import javafx.scene.control.PasswordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class LoginController extends FXController {
	@Autowired
	private AdminService adminService;

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	private boolean result = false;


	@Override
	protected void open(Stage stage) throws IOException {
		this.openWindowFromFXML("Login.fxml", stage, this.getClass(), 400, 300);
	}


	@FXML
	private void loginAction() {
		String username;
		String password;
		username = this.usernameField.getText();
		password = this.passwordField.getText();

		if (username == null
			|| password == null) {
			FXUtils.ALERT_ERROR("ادخل البيانات بصورة سليمة");
			return;

		}

		if (this.adminService.login(username, password)) {
			this.result = true;
			this.stage.close();
		} else {
			FXUtils.ALERT_ERROR("خطأ في البيانات، أو اسم المستخدم غير موجود");
			return;
		}

	}
	public boolean login() throws IOException {
		Stage loginStage = new Stage();
		loginStage.setTitle("تسجيل الدخول");
		this.open(loginStage);
		if(result) {
			Admin admin = this.adminService.getAdminByUsernameAndPasswor(this.usernameField.getText(), this.passwordField.getText());
			this.cashier = admin;
		}
		return result;
	}
	
}
