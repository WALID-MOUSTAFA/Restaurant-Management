package com.tajine.utils;

import com.tajine.screens.ordering.OrderingController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class FXUtils {

	public static  boolean ALERT_CONFIRM(String msg){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();
		alert.setContentText("تأكيد");
		if(alert.getResult() == ButtonType.YES) {
			return true;
		} else {
			return false;
		}
	}

	
	public static void ALERT_SUCCESS(String msg){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(msg);
		alert.showAndWait();
	}


	public static void ALERT_ERROR(String msg){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText("خطأ!");
		alert.setContentText(msg);
		alert.show();
	}


	public static void ALERT_NEWORDER(String msg, OrderingController oc){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(msg);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.OK) {
			oc.addNewOrder();
		}  else {
			alert.close();
		}
	}

	public static String orderTypeToString(String orderTypeNumber) {
		switch(orderTypeNumber)  {
		case "1": return "دليفري";
		case "2": return "تيك أواي";
		case "3": return "صالة";
		default: return "تيك أواي";
		}
	}

	public static String orderTypeToNumber(String orderType) {
		switch(orderType)  {
		case "دليفري": return "1";
		case "تيك أواي" : return "2";
		case "صالة" : return "3";
		default: return "2";
		}
	}
}
