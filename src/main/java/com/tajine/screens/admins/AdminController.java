package com.tajine.screens.admins;

import java.io.IOException;
import java.util.List;

import com.tajine.domain.Admin;
import com.tajine.screens.commons.FXController;
import com.tajine.services.AdminService;
import com.tajine.utils.FXUtils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class AdminController extends FXController {

	@Autowired
	private AdminService adminService;
	@Autowired
	EditAdminController editAdminController;
	@FXML
	private TableView<Admin> adminsTable;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField fullNameField;
	@FXML
	private TextField searchField;


	
	@FXML
	private void initialize() {
		this.initializeAdminsTableView();
		
	}

	private void initializeAdminsTableView() {
		TableColumn<Admin, String> usernameColumn = new TableColumn<>("اسم المستخدم");
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
		TableColumn<Admin, String> fullnameColumn = new TableColumn<>("الاسم الكامل");
		fullnameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		TableColumn<Admin, String> passwordColumn = new TableColumn<>("كلمة المرور");
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		TableColumn<Admin, String> roleColumn = new TableColumn<>("النوع");
		roleColumn.setCellValueFactory((val) -> {
				return new SimpleStringProperty(val.getValue().getRole() == 0 ? "super" : "normal");
			});
		
		this.adminsTable.getColumns().addAll(usernameColumn, fullnameColumn, passwordColumn, roleColumn );
		this.asyncSetMealsTableItems();
					     
	}

	@FXML
	private void addAdminAction() {
		String username;
		String fullname;
		String password;
		Admin admin;
		

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

		admin = new Admin();
		admin.setUsername(username);
		admin.setFullName(fullname);
		admin.setPassword(password);
		admin.setRole(1);

		Admin result = this.adminService.createAdmin(admin);
		if (result != null) {
			FXUtils.ALERT_SUCCESS("تمت الإضافة بنجاح");
			this.adminsTable.getItems().add(result);
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

	private void asyncSetMealsTableItems() {
		Task<List<Admin>> task = new Task<List<Admin>>() {
				
				@Override
				protected List<Admin> call() throws Exception {
					return AdminController.this.adminService.getAllAdmins();
				}
				
				@Override
				protected void succeeded() {
					AdminController.this.adminsTable
						.setItems(FXCollections.observableArrayList(this.getValue()));
					
				}
				
			};
		Thread thread = new Thread(task);
		thread.start();
        }

	@FXML
	private void editAdminAction() throws IOException{
		this.editAdminController.setTargetAdmin(this.adminsTable.getSelectionModel().getSelectedItem());
		editAdminController.open(null);
		this.adminsTable.refresh(); //the native refresh;
	}


	@FXML
	private void deleteAdminAction() {
		Admin admin = this.adminsTable.getSelectionModel().getSelectedItem();
		try {
			if (FXUtils.ALERT_CONFIRM("حذف المستخدم؟")) {
				if (this.adminService.deleteAdmin(admin)) {
					this.adminsTable.getItems().remove(admin);
				}
			}
		}catch (ConstraintViolationException e){
			FXUtils.ALERT_ERROR("لا يمكن حذف المستخدم لأنه مرتبط بأوردرات!");
			e.printStackTrace();
		}

	}
}
