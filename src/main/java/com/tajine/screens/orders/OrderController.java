package com.tajine.screens.orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.tajine.domain.Customer;
import com.tajine.domain.Order;
import com.tajine.domain.OrderContent;
import com.tajine.screens.commons.FXController;
import com.tajine.services.OrdersService;
import com.tajine.services.SettingsService;
import com.tajine.utils.FXUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JRException;

@Component
public class OrderController extends FXController {
	@Autowired
	private OrdersService orderService;
	@Autowired
	private SettingsService settingsService;
	@Autowired
	private OrderContentsController orderContentsController;
	@Autowired
	private FilterByDateTime filterByDateTime;
	
	@FXML
	private TableView<Order> ordersTableView;
	@FXML
	private Button deleteOrderButton;
	@FXML
	private TextField searchField;
	@FXML
	private Button printActionButton;
	@FXML
	private Button printMealStatsActionButton;
	@FXML
	private Label ordersPriceLabel;
	@FXML
	private Label ordersCountLabel;
	
	private SimpleBooleanProperty searchFlag = new SimpleBooleanProperty(false);
	private List<Order> orders = new ArrayList<>();


	
	@FXML
	private void initialize() {
		this.initializeOrdersTableView();
		this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
				this.doSearch(newValue);
			});
		this.printActionButton.disableProperty().bind(this.searchFlag.not());
		//this.deleteOrderButton.disableProperty().bind(this.searchFlag);
		this.printMealStatsActionButton.disableProperty().bind(this.searchFlag.not());
		
	}

	private void initializeOrdersTableView() {
		this.ordersTableView.getColumns().clear();
		TableColumn<Order, Long> idColumn = new TableColumn<>("id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Order, String> typeColumn = new TableColumn<>("النوع");
		typeColumn.setCellValueFactory(val-> {
			// return val.getValue().getOrderType() == 1 ? new SimpleStringProperty("دليفري")
					// : new SimpleStringProperty("تيك أواي");
				
			return new SimpleStringProperty(FXUtils.orderTypeToString(String.valueOf(val.getValue().getOrderType())));
			});

		TableColumn<Order, String> customerNameColumn = new TableColumn<>("اسم العميل");
		customerNameColumn.setCellValueFactory((val) -> {
			if (val != null){
				if (val.getValue().getCustomer().getName() != null) {
					return new SimpleStringProperty(val.getValue().getCustomer().getName());
				} else {
					return new SimpleStringProperty("");
				}
		} else{
			return new SimpleStringProperty("");
		}
			});

		TableColumn<Order, String> addressColumn = new TableColumn<>("العنوان");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		TableColumn<Order, Integer> deliveryFeesColumn = new TableColumn<>("تكاليف الدليفري");
		deliveryFeesColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryFees"));
		TableColumn<Order, Integer> totalPriceColumn = new TableColumn<>("السعر الكلي");
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		TableColumn<Order, Integer> totalPaidColumn = new TableColumn<>("المبلغ المدفوع");
		totalPaidColumn.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
		TableColumn<Order, String> adminColumn = new TableColumn<>("الكاشير");
		adminColumn.setCellValueFactory((val) -> {
				return new SimpleStringProperty(val.getValue().getAdmin().getFullName());
				
			});
		TableColumn<Order, String> createdAtColumn = new TableColumn<>("وقت الإضافة");
		createdAtColumn.setCellValueFactory(new PropertyValueFactory("createdAt"));
		this.ordersTableView.getColumns().addAll(idColumn, typeColumn, customerNameColumn, addressColumn,deliveryFeesColumn,  totalPriceColumn,
							 totalPaidColumn, adminColumn, createdAtColumn);

		this.ordersTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						OrderController.this.orderContentsController
							.setOrder(OrderController.this.ordersTableView.getSelectionModel().getSelectedItem());
						try {
							OrderController.this.orderContentsController.open(null);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			});
		
		this.asyncSetOrdersTableItems();
					     
	}

	private void asyncSetOrdersTableItems() {
		Task<List<Order>> task = new Task<>() {

				@Override
				protected List<Order> call() throws Exception {
					return OrderController.this.orderService.getAllOrders();
					
				}
				
				@Override
				protected void succeeded() {
					OrderController.this.orders.clear();
					OrderController.this.orders.addAll(this.getValue());
					OrderController.this.ordersTableView.setItems
						(FXCollections.observableArrayList(this.getValue()));
				OrderController.this.ordersCountLabel
						.setText(String.valueOf(OrderController.this.orders.size()));
				}
				
			};
		Thread thread = new Thread(task);
		thread.start();
	}


	@FXML
	private void printAction() {
		String printerName = this.settingsService.getSetting("outerPrinter");
		new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						List<Order> orders = OrderController.this.ordersTableView.getItems();
						int total = 0;
						for(Order order : orders) {
							total+=order.getTotalPrice();
						}
						
						new orderFilterPrint().printRecipt(printerName,orders, total);
						
					} catch (JRException e) {
						e.printStackTrace();
					}
				}
			}).start();
	}

	@FXML
	private void onFilterAction() throws IOException {
		this.ordersTableView.setItems(FXCollections.observableArrayList(this.orders));
		this.filterByDateTime.open(null);
		DateRange dateRange = filterByDateTime.getData();
		if(dateRange.getIsNew() == false) {
			return;
		}
		this.searchFlag.set(true);
		ObservableList<Order> items= this.ordersTableView.getItems();
		FilteredList<Order> filteredList = new FilteredList<>(items);
		filteredList.setPredicate(new Predicate<Order>() {
				@Override
				public boolean test(Order order) {
					String dateTime = order.getCreatedAt().toString();
					String dateWithoutParthsOfSeconds = dateTime.split("\\.")[0];
					System.out.println(dateWithoutParthsOfSeconds);
					return OrderController.this.filterByDateTime.compare(dateWithoutParthsOfSeconds, dateRange);
				}
			});
		
		OrderController.this.ordersCountLabel
			.setText(String.valueOf(filteredList.size()));
		SortedList<Order> orderSortedList = new SortedList<>(filteredList);
		this.ordersTableView.setItems(orderSortedList);
		orderSortedList.comparatorProperty().bind(ordersTableView.comparatorProperty());

		this.calculateSelectedPrice();

		
	}

	
	private void doSearch(String q) {
		//initialize tableView with existed items
		this.ordersTableView.setItems(FXCollections.observableArrayList(this.orders));
		if (q.isEmpty() || q == null) {
			this.searchFlag.set(false);
			this.ordersPriceLabel.setText("");
			OrderController.this.ordersCountLabel
				.setText(String.valueOf(this.ordersTableView.getItems().size()));
			return;
		}
		this.searchFlag.set(true);
		ObservableList<Order> items= this.ordersTableView.getItems();
		FilteredList<Order> filteredList = new FilteredList<>(items);
		// this.ordersTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<Order>() {
				@Override
				public boolean test(Order order) {
					return order.getAdmin().getFullName().contains(q)
						|| order.getCustomer().getName().contains(q)
						|| order.getCreatedAt().toString().contains(q)
						|| order.getId().toString().contains(q);
				}
				
			});
		OrderController.this.ordersCountLabel
			.setText(String.valueOf(filteredList.size()));
		SortedList<Order> orderSortedList = new SortedList<>(filteredList);
		this.ordersTableView.setItems(orderSortedList);
		orderSortedList.comparatorProperty().bind(ordersTableView.comparatorProperty());

		this.calculateSelectedPrice();
	}

	private void calculateSelectedPrice() {
		List<Order> orders = OrderController.this.ordersTableView.getItems();
		int total = 0;
		for(Order order : orders) {
			total+=order.getTotalPrice();
		}
		this.ordersPriceLabel.setText(String.valueOf(total));

	}

	@FXML
	private void printMealStatsAction() throws JRException {
		Map<String, Integer> stats = new HashMap<>();
		List<Order> orders = new ArrayList<>(this.ordersTableView.getItems());
		for (Order o : orders) {
			for (OrderContent oc : o.getOrderContents()) {
				if(stats.get(oc.getMeal().getTitle()) != null) { //exists
					stats.put(oc.getMeal().getTitle(), stats.get(oc.getMeal().getTitle())+ oc.getQuantity());
				} else {
					stats.put(oc.getMeal().getTitle(), oc.getQuantity());
				}
			}
		}

		Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						new MealStatsPrint().printRecipt(OrderController.this.settingsService.getSetting("outerPrinter"), stats);
					} catch (JRException e) {
						e.printStackTrace();
					}

				}
			});
		thread.start();
	}
		

	@FXML
	private void refreshOrders() {
		this.ordersTableView.setItems(FXCollections.observableArrayList(this.orders));
		this.ordersCountLabel
			.setText(String.valueOf(this.ordersTableView.getItems().size()));
		this.calculateSelectedPrice();

	}

	@FXML
	private void deleteOrderAction() {
		if (FXUtils.ALERT_CONFIRM("هل تريد حذف هذا الطلب؟")) {

			this.orderService.deleteOrder(this.ordersTableView.getSelectionModel().getSelectedItem());
			try {
				this.ordersTableView.getItems()
					.remove(this.ordersTableView.getSelectionModel().getSelectedItem());

			} catch (Exception e) {

			}
			//NOTE(WAL
			if(this.searchFlag.getValue() == true) {
				this.orders.remove(this.ordersTableView.getSelectionModel().getSelectedItem());
				this.doSearch(this.searchField.getText());
			}
		}
	}	
}
