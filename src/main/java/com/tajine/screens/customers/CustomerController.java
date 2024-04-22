package com.tajine.screens.customers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.tajine.domain.Customer;
import com.tajine.services.CustomerService;
import com.tajine.utils.FXUtils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;




@Component
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private EditCustomerController editCustomerController;
	@FXML
	private TableView<Customer> customersTableView;
	@FXML
	private TextField customerNameField;
	@FXML
	private TextField customerPhoneField;
	@FXML
	private TextField customerAddressField;
	@FXML
	private Button deleteCustomerButton;
	@FXML
	private Button editCustomerButton;
	@FXML
	private TextField searchField;

	private SimpleBooleanProperty searchFlag = new SimpleBooleanProperty(false);
	private List<Customer> customers= new ArrayList<>();

	
	
	@FXML
	private void initialize() {
		this.initalizeCustomerTableView();
		this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
				this.doSearch(newValue);
			});

		this.deleteCustomerButton.disableProperty()
			.bind(Bindings.isEmpty(this.customersTableView.getSelectionModel().getSelectedItems()));
		this.editCustomerButton.disableProperty()
			.bind(Bindings.isEmpty(this.customersTableView.getSelectionModel().getSelectedItems()));
		
	}
	
	

	private void initalizeCustomerTableView() {
		TableColumn<Customer, String> nameColumn = new TableColumn<>("الاسم");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<Customer, String> phoneColumn = new TableColumn<>("رقم الهاتف");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		TableColumn<Customer, String> addressColumn = new TableColumn<>("العنوان");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		
		this.customersTableView.getColumns().addAll(nameColumn, phoneColumn, addressColumn);
		this.asyncSetCustomerTableItmes();
	}

	private void asyncSetCustomerTableItmes() {
		Task<List<Customer>> task = new Task<List<Customer>>() {
				@Override
				protected List<Customer> call() throws Exception {
					List<Customer> result =  CustomerController.this.customerService.getAllCustomer();
					CustomerController.this.customers = result;
					return result;
				}
				@Override
				protected void succeeded() {
					CustomerController.this.customersTableView.setItems(FXCollections.observableArrayList(this.getValue()));
				}
				@Override
				protected void failed() {
					this.getException().printStackTrace();
					
				}
			};
		Thread thread = new Thread(task);
		thread.start();
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
					FXUtils.ALERT_SUCCESS("تمت الإضافة.");
					this.customersTableView.getItems().add(result);
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

	
	@FXML
	private void editCustomerAction() throws IOException {
		this.editCustomerController.setTargetCustomer(this.customersTableView.getSelectionModel().getSelectedItem());
		this.editCustomerController.open(null);
	}

	@FXML
	private void deleteCustomerAction() {
		try {
			Customer customer = this.customersTableView.getSelectionModel()
				.getSelectedItem();
			if(FXUtils.ALERT_CONFIRM("حذف العميل: " + customer.getName())){
				this.customerService.deleteCustomer(customer);
				try {
					this.customersTableView.getItems().remove(customer);
				} catch (Exception e) {e.printStackTrace();}
			}
				
		} catch (ConstraintViolationException e) {
			FXUtils.ALERT_ERROR("توجد طلبات مسجلة باسم هذا العميل.");
			e.printStackTrace();
		}

		if(this.searchFlag.getValue() == true) {
			this.customers.remove(this.customersTableView.getSelectionModel().getSelectedItem());
			this.doSearch(this.searchField.getText());
		}
		   
	}



		private void doSearch(String q) {
			//initialize tableView with existed items
			this.customersTableView.setItems(FXCollections.observableArrayList(this.customers));
		if (q.isEmpty() || q == null) {
			this.searchFlag.set(false);
			
			return;
		}
		this.searchFlag.set(true);
		ObservableList<Customer> items= this.customersTableView.getItems();
		FilteredList<Customer> filteredList = new FilteredList<>(items);
		// this.customersTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<Customer>() {
				@Override
				public boolean test(Customer customer) {
					return customer.getName().contains(q)
						|| customer.getPhone().contains(q)
						|| customer.getAddress().contains(q);
				}
				
			});
		SortedList<Customer> customersSortedList = new SortedList<>(filteredList);
		this.customersTableView.setItems(customersSortedList);
		customersSortedList.comparatorProperty().bind(customersTableView.comparatorProperty());

	}

}

