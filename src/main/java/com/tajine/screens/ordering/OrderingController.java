package com.tajine.screens.ordering;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.tajine.domain.Customer;
import com.tajine.domain.Meal;
import com.tajine.domain.MealCategory;
import com.tajine.domain.Order;
import com.tajine.domain.OrderContent;
import com.tajine.screens.commons.FXController;
import com.tajine.screens.commons.InputMsgBox;
import com.tajine.screens.customers.CreateCustomer;
import com.tajine.services.CustomerService;
import com.tajine.services.MealCategoriesService;
import com.tajine.services.OrdersService;
import com.tajine.services.SettingsService;
import com.tajine.utils.FXUtils;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRException;


	
@Component
public class OrderingController extends FXController {

	@Autowired
	private MealCategoriesService mealCategoriesService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CreateCustomer createCustomer;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private SettingsService settingsService;
	@FXML
	private FlowPane categoriesPane;
	@FXML
	private ListView<Meal> categoryRelatedMeals;
	@FXML
	private TableView<OrderContent> composedOrderContentsTableView;
	@FXML
	private Button printButton;
	@FXML
	private TextField totalPaidField;
	@FXML
	private TextField customerAddressField;
	@FXML
	private TextField customerNameField;
	@FXML
	private TextField customerPhoneField;
	@FXML
	private VBox customerDataWrapper;
	@FXML
	private TextField deliveryFeesField;
	@FXML
	private ComboBox<String> orderTypeCombo;
	@FXML
	private ComboBox<Customer> customerCombo;
	@FXML
	private Label totalPriceLabel; //calculated
	@FXML
	private Label remainLabel; //calculated
	@FXML
	private Button addCustomerButton;
	@FXML
	private Button deleteItemFromOrderButton;

	private SimpleBooleanProperty showCustomerData = new SimpleBooleanProperty(false);
	private List<ToggleButton> categoriesButtons = new ArrayList<>();
	private ObservableList<MealCategory> categories;
	private Order composedOrder = new Order();

	@FXML
	private void initialize() {
		setTextFieldsToNumerical();
		this.getCategories();
		this.renderCategoriesToggleButtons(false);
		this.initializeComposedOrderContentsTableView();
		this.initializeCustomerComboBox();
		this.initializeOrderTypeComboBox();

		this.customerCombo.setOnAction(e-> {
				ComboBox<Customer> source =  (ComboBox<Customer>)e.getSource();
				if (source.getValue() != null) {
					this.customerAddressField.setText(source.getValue().getAddress());
					this.customerPhoneField.setText(source.getValue().getPhone());
					this.customerNameField.setText(source.getValue().getName());
				}
			});
		this.customerCombo.getEditor().setOnKeyReleased(e->{
			TextField source = (TextField) e.getSource();
			if(customerCombo.getSelectionModel().getSelectedItem() == null) {
				this.customerPhoneField.setText(this.customerCombo.getEditor().getText());
			} else {
				this.customerPhoneField.setText(this.customerCombo.getValue().getPhone());

			}
		});
		this.deleteItemFromOrderButton.disableProperty().bind(Bindings.isEmpty(this.composedOrderContentsTableView.getSelectionModel().getSelectedItems()));
		this.customerDataWrapper.visibleProperty().bind(this.showCustomerData);
	}


	
	private void updateRemain() {
		
		this.remainLabel.setText(String.valueOf(
							Integer.valueOf(this.totalPriceLabel.getText()) - Integer
											.valueOf(this.totalPaidField.getText())));
	}
	
	private void setTextFieldsToNumerical() {
		this.totalPaidField.textProperty().addListener((observer, oldVal, newVal) -> {
				
				if (!newVal.matches("\\d*")) {
					this.totalPaidField.setText(newVal.replaceAll("[^\\d]", ""));
				}
				try {
					if (!totalPriceLabel.getText().isEmpty() || totalPriceLabel.getText() != null
					    || totalPriceLabel.getText() != "") {
						this.updateRemain();
					}
				} catch (NumberFormatException e) {
				}
			});

		this.deliveryFeesField.textProperty().addListener((observer, oldVal, newVal) -> {
				
				if (!newVal.matches("\\d*")) {
					this.deliveryFeesField.setText(newVal.replaceAll("[^\\d]", ""));
				}
				
			});
		


	}
	
	@FXML
	private void refresh() throws IOException {
		this.refreshCategories();
		this.initializeCustomerComboBox();
	}

	
	private void getCategories() {
		if (this.mealCategoriesService.findAllMealCategories() == null) {
			this.categories = FXCollections.observableArrayList(new ArrayList<>());
			return;
		}
		this.categories = FXCollections.observableArrayList(this.mealCategoriesService.findAllMealCategories());
	}

	private void renderCategoriesToggleButtons(boolean refresh) {

		if (refresh) {
			this.categoriesPane.getChildren().clear();
			this.categoriesButtons.clear();
		}

		if (this.categories != null) {
			for (MealCategory cat : this.categories) {
				ToggleButton toggleButton = new ToggleButton(cat.getTitle());
				toggleButton.setMinWidth(50);
				toggleButton.setMinHeight(50);
				toggleButton.setPrefWidth(100);
				// toggleButton.setPrefSize(100, 70);
				toggleButton.setMaxWidth(200);
				toggleButton.setMaxHeight(300);
				toggleButton.setWrapText(true);
				toggleButton.setFont(new Font(15));
				setCategoryToggleButtonOnActionListener(toggleButton);
				this.categoriesButtons.add(toggleButton);
			}
		}

		for (ToggleButton toggleButton : this.categoriesButtons) {
			this.categoriesPane.getChildren().add(toggleButton);
		}
	}

	private void setCategoryToggleButtonOnActionListener(ToggleButton toggleButton) {
		toggleButton.setOnAction(e -> {
			for (ToggleButton t : this.categoriesButtons) {
				if (!((ToggleButton) e.getSource()).equals(t))
					t.setSelected(false);
			}
			this.viewAssociatedMeals(((ToggleButton) e.getSource()).getText());
		});
	}

	private void viewAssociatedMeals(String categoryTitle) {

		MealCategory category = this.mealCategoriesService.findCategoryByTitle(categoryTitle);
		List<Meal> meals = category.getMeals();
		this.categoryRelatedMeals.setItems(FXCollections.observableArrayList(meals));
		this.categoryRelatedMeals.setCellFactory((param) -> {
			return new ListCell<>() {
				@Override
				protected void updateItem(Meal item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(item.getTitle());
					}
				}
			};
		});
		this.categoryRelatedMeals.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					try {
						Meal meal = ((ListView<Meal>) event.getSource()).getSelectionModel()
								.getSelectedItem();
						OrderingController.this.onMealDoubleClick(meal);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	private void refreshCategories() {
		//get here works as a refresh also, because of the observablelist observability;
		this.getCategories();
		this.renderCategoriesToggleButtons(true);
		this.categoryRelatedMeals.getItems().clear();
	}

	/*-------------------------composing the order------------------------------*/



	private void initializeComposedOrderContentsTableView() {

		this.composedOrderContentsTableView.setEditable(true);
		TableColumn<OrderContent, String> mealTitleColumn = new TableColumn<>("الصنف");
		mealTitleColumn.setCellValueFactory((val) -> {
			return new SimpleStringProperty(val.getValue().getMeal().getTitle());
		});
		TableColumn<OrderContent, Integer> quantityColumn = new TableColumn<>("الكمية");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		quantityColumn.setOnEditCommit((event) -> {
			this.composedOrderContentsTableView.getSelectionModel().getSelectedItem().setQuantity(event.getNewValue());
			calculateTotalPrice();
			this.composedOrderContentsTableView.refresh();
		});
		TableColumn<OrderContent, String> priceColumn = new TableColumn<>("السعر");
		priceColumn.setCellValueFactory((val) -> {
			return new SimpleStringProperty(String
					.valueOf(val.getValue().getMeal().getPrice() * val.getValue().getQuantity()));
		});
		TableColumn<OrderContent, String> noteColumn = new TableColumn<>("ملاحظات");
		noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
		noteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		noteColumn.setOnEditCommit((event) -> {
			this.composedOrderContentsTableView.getSelectionModel().getSelectedItem().setNote(event.getNewValue());
		});
		this.composedOrderContentsTableView.getColumns().addAll(mealTitleColumn, quantityColumn, priceColumn,
				noteColumn);
		
	}

	private void initializeOrderTypeComboBox() {
		this.orderTypeCombo.getItems().add("دليفري");
		this.orderTypeCombo.getItems().add("تيك أواي");
		this.orderTypeCombo.getItems().add("صالة");
		this.orderTypeCombo.setOnAction(e -> {
				if (((ComboBox) e.getSource()).getSelectionModel().getSelectedItem() == "دليفري") {
					this.showCustomerData.set(true);
				} else {
					this.showCustomerData.set(false);
				}
		});
	}

	@FXML
	public void addNewOrder() {
		this.composedOrderContentsTableView.getItems().clear();
		this.composedOrder = new Order();
		this.totalPaidField.clear();
		this.customerAddressField.clear();
		this.totalPriceLabel.setText("");
		//this.customerCombo.getSelectionModel().clearSelection();
		this.remainLabel.setText("");
	}

	//add meal to the order
	private void onMealDoubleClick(Meal meal) throws IOException {
		if (meal == null)
			return;
		try {
			int quantity = Integer.parseInt(new InputMsgBox("الكمية").getData());

			OrderContent orderItem = new OrderContent();
			orderItem.setMeal(meal);
			orderItem.setQuantity(quantity);
			this.addItemToComposedOrder(orderItem);
		} catch (Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ ما، تأكد من إدخال البيانات بشكل سليم");
			e.printStackTrace();
		}
	}

	private void addItemToComposedOrder(OrderContent item) {
		this.composedOrderContentsTableView.getItems().add(item);
		this.calculateTotalPrice();
	}

	private void calculateTotalPrice() {
		int total = 0;
		for (OrderContent oc : this.composedOrderContentsTableView.getItems()) {
			total += oc.getQuantity() * oc.getMeal().getPrice();
		}
		this.totalPriceLabel.setText(String.valueOf(total));
	}

	private void initializeCustomerComboBox() {
		OrderingController.this.customerCombo.getItems().clear();

		Thread thread = new Thread( new Runnable() {

				@Override
				public void run() {
					List<Customer> customers = OrderingController.this.customerService.getAllCustomer();
					OrderingController.this.customerCombo.setItems(FXCollections.observableArrayList(customers));
					OrderingController.this.customerCombo.setConverter(new StringConverter<Customer>() {
							@Override
							public String toString(Customer object) {
								if (object != null) {
									return object.getName();
								} else {
									return null;
								}
							}

							@Override
							public Customer fromString(String string) {
								return OrderingController.this.customerCombo.getItems().stream()
									.filter(object -> object.getName().equals(string)).findFirst()
									.orElse(null);
							}
						});
					ComboBoxUtils.autoCompleteComboBoxPlus(OrderingController.this.customerCombo, (typedText, itemToCompare) -> itemToCompare
									       .getName().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare
									       .getPhone().toLowerCase().contains(typedText.toLowerCase()) );
					
				}
				
			});
		thread.start();

	}

	@FXML
	private void deleteItemFromOrderAction() {
		this.composedOrderContentsTableView.getItems()
				.remove(this.composedOrderContentsTableView.getSelectionModel().getSelectedItem());
		this.calculateTotalPrice();
 	}
	
	@FXML
	private boolean saveOrderAction() throws JRException {

		if (this.composedOrderContentsTableView.getItems().size() == 0) {
			FXUtils.ALERT_ERROR("أضف محتوى للطلب");
			return false;
		}
		if (this.composedOrder.getId() != null) { //it means already processed;
			return true;
		}
		try {

			this.composedOrder = new Order();
			String address;
			Customer customer = new Customer();
			int totalPrice;
			int deliveryFees;
			int totalPaid;
			String orderType;
			int orderTypeIntegr;
			List<OrderContent> orderContents;


			

			//TODO(WALID): EXTRACT THIS VALUE INTO A VARIABLE;
			if (this.totalPaidField.getText() == null || this.totalPaidField.getText().isEmpty()) {
				FXUtils.ALERT_ERROR("يلزم كتابة المبلغ المدفوع.");
				return false;
			}
		       

			totalPaid = Integer.valueOf(this.totalPaidField.getText());

			totalPrice = Integer.valueOf(this.totalPriceLabel.getText().trim());
			orderType = this.orderTypeCombo.getSelectionModel().getSelectedItem();

			if (orderType == null || this.orderTypeCombo.getSelectionModel().isEmpty()) {
				FXUtils.ALERT_ERROR("يلزم اختيار نوع الطلب.");
				return false;
			}

			
			// orderTypeIntegr = orderType.equals("دليفري")
				// ? 1 : 2;
			orderTypeIntegr = Integer.parseInt(FXUtils.orderTypeToNumber(orderType));
			
			if(orderTypeIntegr == 1) {
				customer = this.customerCombo.getSelectionModel().getSelectedItem();

				if (customer == null) {
					FXUtils.ALERT_ERROR("يلزم اختيار عميل للمتابعة");
					return false;

				}
				address = this.customerAddressField.getText();

				if (this.customerAddressField.getText() == null || this.customerAddressField.getText().isEmpty()) {
					this.composedOrder.setAddress(customer.getAddress());
				} else {
					this.composedOrder.setAddress(address);
				}

				if(this.deliveryFeesField.getText() == null
				   || this.deliveryFeesField.getText().isEmpty()){
					FXUtils.ALERT_ERROR("يلزم كتابة تكاليف الدليفري، لأن الطلب دليفري");
					return false;
				}

				try {
					deliveryFees = Integer.parseInt(this.deliveryFeesField.getText());
					this.composedOrder.setDeliveryFees(deliveryFees);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					FXUtils.ALERT_ERROR("خطأ في كتابة تكاليف الدليفري");
					return false;
				}
				this.composedOrder.setDeliveryFees(deliveryFees);

			}

			
			orderContents = new ArrayList<>(this.composedOrderContentsTableView.getItems());
			for (OrderContent oc : orderContents) {
				oc.setOrder(composedOrder);
			}


			if(orderTypeIntegr != Integer.parseInt((FXUtils.orderTypeToNumber("دليفري"))) ) {
				customer = this.customerService.getTakeAwayCustomer();
			}
			this.composedOrder.setAdmin(this.cashier);
			this.composedOrder.setCustomer(customer);
			this.composedOrder.setOrderContents(new HashSet<>(orderContents));
			this.composedOrder.setTotalPrice(totalPrice);
			this.composedOrder.setTotalPaid(totalPaid);
			this.composedOrder.setOrderType(orderTypeIntegr);

		} catch (Exception e) {
			e.printStackTrace();
			FXUtils.ALERT_ERROR(
					"حدث خطأ ما أثناء المعالجة، تأكد من ملء البيانات الرقمية والنصية بشكل سليم.");
		}

		Order result = this.orderService.createOrder(composedOrder);
		if (result != null) {
			this.composedOrder.setId(result.getId());
			FXUtils.ALERT_SUCCESS("تم بنجاح");
			return true;
		}

		return false;
	}


	@FXML
	private void saveOrderAndPrintAction() throws JRException {

		// if (this.composedOrderContentsTableView.getItems().size() == 0) {
		// 	FXUtils.ALERT_ERROR("أضف محتوى للطلب");
		// 	return;
		// }
		// if (this.composedOrder.getId() != null) { //it means already processed;
		// 	this.printAction(this.getCounter());
		// 	return;
		// }
		// try {

		// 	this.composedOrder = new Order();
		// 	String address;
		// 	Customer customer;
		// 	int totalPrice;
		// 	int totalPaid;
		// 	String orderType;
		// 	int orderTypeIntegr;
		// 	int deliveryFees;
		// 	List<OrderContent> orderContents;

		// 	address = this.customerAddressField.getText();
		// 	customer = this.customerCombo.getSelectionModel().getSelectedItem();
		// 	if (customer == null) {
		// 		FXUtils.ALERT_ERROR("يلزم اختيار عميل للمتابعة");
		// 		return;

		// 	}

		// 	//TODO(WALID): EXTRACT THIS VALUE INTO A VARIABLE;
		// 	if (this.totalPaidField.getText() == null || this.totalPaidField.getText().isEmpty()) {

		// 		FXUtils.ALERT_ERROR("يلزم كتابة المبلغ المدفوع.");
		// 		return;
		// 	}

		// 	totalPaid = Integer.valueOf(this.totalPaidField.getText());

		// 	totalPrice = Integer.valueOf(this.totalPriceLabel.getText().trim());
		// 	orderType = this.orderTypeCombo.getSelectionModel().getSelectedItem();

		// 	if (orderType == null || this.orderTypeCombo.getSelectionModel().isEmpty()) {
		// 		FXUtils.ALERT_ERROR("يلزم اختيار نوع الطلب.");
		// 		return;
		// 	}
			
		// 	orderTypeIntegr = orderType.equals("دليفري")
		// 		? 1 : 2;

		// 	if(orderTypeIntegr == 1) {
		// 		if(this.deliveryFeesField.getText() == null
		// 			|| this.deliveryFeesField.getText().isEmpty()){
		// 			FXUtils.ALERT_ERROR("يلزم كتابة تكاليف الدليفري، لأن الطلب دليفري");
		// 			return;
		// 		}

		// 		try {
		// 			deliveryFees = Integer.parseInt(this.deliveryFeesField.getText());
		// 			this.composedOrder.setDeliveryFees(deliveryFees);
		// 		} catch (NumberFormatException e) {
		// 			e.printStackTrace();
		// 			FXUtils.ALERT_ERROR("خطأ في كتابة تكاليف الدليفري");
		// 			return;
		// 		}
		// 		this.composedOrder.setDeliveryFees(deliveryFees);

		// 	}

		// 	orderContents = new ArrayList<>(this.composedOrderContentsTableView.getItems());
		// 	for (OrderContent oc : orderContents) {
		// 		oc.setOrder(composedOrder);
		// 	}

		// 	if (this.customerAddressField.getText() == null || this.customerAddressField.getText().isEmpty()) {
		// 		this.composedOrder.setAddress(customer.getAddress());
		// 	} else {
		// 		this.composedOrder.setAddress(address);
		// 	}

		// 	this.composedOrder.setAdmin(this.cashier);
		// 	this.composedOrder.setCustomer(customer);
		// 	this.composedOrder.setOrderContents(new HashSet<>(orderContents));
		// 	this.composedOrder.setTotalPrice(totalPrice);
		// 	this.composedOrder.setTotalPaid(totalPaid);
		// 	this.composedOrder.setOrderType(orderTypeIntegr);

		// } catch (Exception e) {
		// 	FXUtils.ALERT_ERROR(
		// 			"حدث خطأ ما أثناء المعالجة، تأكد من ملء البيانات الرقمية والنصية بشكل سليم.");
		// }

		if(this.saveOrderAction()) {
			this.printAction(this.getCounter());
		}

	}


	
	@FXML
	private void printAction(String counter) throws JRException {
		if (this.composedOrder.getId() == null) {
			FXUtils.ALERT_ERROR("قم بحفظ الطلب قبل طباعته.");
			return;
		}

		if (this.composedOrder.getOrderType() == 1) {
			this.printOuterReciptDelivery(counter);
		} else {
			this.printOuterReciptTakeAway(counter);
		}

		this.printInnerRecipt(counter);
		return;
	}


	
	private void printOuterReciptTakeAway(String counter) {
		Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						OuterRecipt outerRecipt = new OuterRecipt();
						outerRecipt.printRecipt(
									OrderingController.this.settingsService
									.getSetting("outerPrinter"),
									OrderingController.this.composedOrder,
									FXController.cashier.getFullName(), counter);
					} catch (JRException e) {
						e.printStackTrace();
					}
				}
			});
		thread1.start();
	}

	
	private void printOuterReciptDelivery(String counter) {
		Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						OuterReciptDelivery outerReciptDelivery = new OuterReciptDelivery();
						outerReciptDelivery.printRecipt(
									OrderingController.this.settingsService
									.getSetting("outerPrinter"),
									OrderingController.this.composedOrder,
									FXController.cashier.getFullName(), counter);
					} catch (JRException e) {
						e.printStackTrace();
					}
				}
			});
		thread1.start();
	}

	private void printInnerRecipt(String counter) {

		//the first inner printer
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				InnerRecipt innerRecipt = new InnerRecipt();
				try {
					innerRecipt.printRecipt(
							OrderingController.this.settingsService
									.getSetting("innerPrinter"),
							OrderingController.this.composedOrder,
							FXController.cashier.getFullName(), counter);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		});
		
		//the second inner printer
		Thread thread3 = new Thread(new Runnable() {
			@Override
			public void run() {
				InnerRecipt innerRecipt = new InnerRecipt();
				try {
					innerRecipt.printRecipt(
							OrderingController.this.settingsService
									.getSetting("innerPrinter2"),
							OrderingController.this.composedOrder,
							FXController.cashier.getFullName(), counter);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		});

		thread2.start();
		thread3.start();
	}
	
	private String getCounter() {
		String counter = this.settingsService.getSetting("dailyOrderCounter");
		int Icounter = Integer.parseInt(counter);
		int newIcounter = Icounter + 1;
		this.settingsService.setSetting("dailyOrderCounter", String.valueOf(newIcounter));
		return String.valueOf(Icounter);
	}

	
	@FXML
	public void addNewCustomerAction() throws IOException {
		this.createCustomer.open(null);
		Customer resultCustomer =this.createCustomer.getCustomer();
		if (resultCustomer != null) {
			this.initializeCustomerComboBox();
			this.customerCombo.setValue(resultCustomer);

		}
		
		
	}

	@FXML
	private void directAddClientAction() {

		String name = this.customerNameField.getText();
		String phone = this.customerPhoneField.getText();
		String address = this.customerAddressField.getText();

		Customer result =this.createCustomer.addCustomerAction(name, phone, address);
		if(result != null) {
			FXUtils.ALERT_SUCCESS("تم إضافة المستخدم واختياره.");
			this.initializeCustomerComboBox();
			this.customerCombo.setValue(result);
		}
	}

	@FXML
	private void clearCustomerData() {
		this.customerCombo.getSelectionModel().clearSelection();
		this.customerPhoneField.clear();
		this.customerAddressField.clear();
		this.customerNameField.clear();
	}
}




/*********************************************************************************************/




class ComboBoxUtils {

    public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }

    public static<T> void autoCompleteComboBoxPlus(ComboBox<T> comboBox, AutoCompleteComparator<T> comparatorMethod) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                //comboBox.getEditor().setText(null);
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    if (comboBox.getEditor().getText() != null) {
                        moveCaret(comboBox.getEditor().getText().length());
                    }
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.DELETE) {
                    if (comboBox.getEditor().getText() != null) {
                        moveCaretToPos = true;
                        caretPos = comboBox.getEditor().getCaretPosition();
                    }
                } else if (event.getCode() == KeyCode.ENTER) {
                	return;
                }

                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.getCode().equals(KeyCode.SHIFT) || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (aData != null && comboBox.getEditor().getText() != null && comparatorMethod.matches(comboBox.getEditor().getText(), aData)) {
                        list.add(aData);
                    }
                }
                String t = "";
                if (comboBox.getEditor().getText() != null) {
                    t = comboBox.getEditor().getText();
                }

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static<T> T getComboBoxValue(ComboBox<T> comboBox){
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }

}
