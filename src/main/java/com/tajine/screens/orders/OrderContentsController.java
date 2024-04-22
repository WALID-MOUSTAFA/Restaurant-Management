package com.tajine.screens.orders;

import java.io.IOException;

import com.tajine.domain.Order;
import com.tajine.domain.OrderContent;
import com.tajine.screens.commons.FXController;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

@Component
public class OrderContentsController extends FXController {

	private Order order;
	@FXML
	private TableView<OrderContent> orderContentsTableView;
	
	@Override
	protected void open(Stage stage) throws IOException {
		this.openWindowFromFXML("OrderContents.fxml", stage, this.getClass(), 700, 700);
	}

	@FXML
	private void initialize() {
		this.initializeOrderContentsTableView();
	}

	private void initializeOrderContentsTableView() {
		this.orderContentsTableView.getColumns().clear();
		TableColumn<OrderContent, String> mealTitleColumn = new TableColumn<>("اسم الصنف");
		mealTitleColumn.setCellValueFactory((val) -> {
				return new SimpleStringProperty(val.getValue().getMeal().getTitle());
			});
		TableColumn<OrderContent, Integer> quantityColumn = new TableColumn<>("الكمية");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		TableColumn<OrderContent, String> noteColumn = new TableColumn<>("ملاحظات");
		noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

		
		this.orderContentsTableView.getColumns().addAll(mealTitleColumn, quantityColumn, noteColumn);
		
		this.asyncSetOrdersContentsTableItems();
	}

	private void asyncSetOrdersContentsTableItems() {
		this.orderContentsTableView.setItems(FXCollections.observableArrayList(this.order.getOrderContents()));
	}


	public void setOrder(Order order){
		this.order = order ;
	}

	public Order getOrder() {
		return this.order;
	}
		


}
