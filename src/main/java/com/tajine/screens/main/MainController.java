package com.tajine.screens.main;

import java.io.IOException;

import com.tajine.screens.admins.AdminController;
import com.tajine.screens.categories.CategoryController;
import com.tajine.screens.commons.FXController;
import com.tajine.screens.customers.CustomerController;
import com.tajine.screens.meals.MealController;
import com.tajine.screens.ordering.OrderingController;
import com.tajine.screens.orders.OrderController;
import com.tajine.screens.settings.SettingsController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@Component
public class MainController extends FXController {

        @FXML
        private AnchorPane canvas;
	@FXML
	private AnchorPane orderingCanvas;
	@Autowired
	private com.tajine.screens.login.LoginController loginController;
	
        @Override
        public void open(Stage stage) throws IOException {
		this.openPrimaryWindowFromFXML("Main.fxml",
                        stage, MainController.class, 700, 500);
		stage.setTitle("الرئيسية");

        }

	@FXML
	private void initialize() throws IOException{
		if (!this.loginController.login()) {
			Platform.exit();
		}
		this.loadOrdersScreen();
		loadOrderingScreen();
	}

        @FXML
        private void loadOrdersScreen() throws IOException {
		if(this.cashier.getRole() != 0) return;
                this.swapWithFXML(canvas, "Order.fxml", OrderController.class);
		if(this.stage != null) {
			this.stage.setTitle("الطلبات");
		}
        }

        @FXML
        private void loadCategoriesScreen() throws IOException {
		if(this.cashier.getRole() != 0) return;
                this.swapWithFXML(canvas, "Category.fxml", CategoryController.class);
		this.stage.setTitle("الفئات");
        }

        @FXML
        private void loadMealsScreen() throws IOException {
		if(this.cashier.getRole() != 0) return;
                this.swapWithFXML(canvas, "Meal.fxml", MealController.class);
		this.stage.setTitle("الأصناف");
        }

	@FXML
        private void loadCustomersScreen() throws IOException {
		this.swapWithFXML(canvas, "Customer.fxml", CustomerController.class);
		this.stage.setTitle("العملاء");
        }

	@FXML
	private void loadSettingsScreen() throws IOException {
                if(this.cashier.getRole() != 0) return;
		this.swapWithFXML(canvas, "Settings.fxml", SettingsController.class);
		this.stage.setTitle("الإعدادات");
	}

        @FXML
        private void loadAdminsScreen() throws IOException {
                if(this.cashier.getRole() != 0) return;
                this.swapWithFXML(canvas, "Admin.fxml", AdminController.class);
		this.stage.setTitle("المستخدمين");
        }


        /*-----------------------------------sales------------------------------------------------*/

	private void loadOrderingScreen() throws IOException{
		this.swapWithFXML(orderingCanvas, "Ordering.fxml", OrderingController.class);

	}
	

	@FXML
	private void tabChangeAction(Event event) {
		if(((Tab)event.getSource()).getText() != null) {
			if(this.stage != null) {
				this.stage.setTitle(((Tab) event.getSource()).getText());
			}
		}
	}
	
		

	
}
