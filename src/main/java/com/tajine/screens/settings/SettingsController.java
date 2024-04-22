package com.tajine.screens.settings;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.tajine.domain.Setting;
import com.tajine.screens.commons.FXController;
import com.tajine.services.SettingsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SettingsController extends FXController {

	@Autowired
	private SettingsService settingsService;

	@FXML
	private ComboBox<String> outerPrinterCombo;
	@FXML
	private ComboBox<String> innerPrinterCombo;
	@FXML
	private ComboBox<String> innerPrinter2Combo;
	
	private void initializeOuterPrinterCombo() {
		this.outerPrinterCombo.setValue(settingsService.getSettingValue("outerPrinter"));
			
		PrintService[] printServices = PrintServiceLookup
			.lookupPrintServices(null, null);

		for (PrintService ps : printServices) {
			this.outerPrinterCombo.getItems().add(ps.getName());
		}
	}
	private void initializeInnerPrinterCombo() {
		this.innerPrinterCombo.setValue(settingsService.getSettingValue("innerPrinter"));
		this.innerPrinter2Combo.setValue(settingsService.getSettingValue("innerPrinter2"));

		PrintService[] printServices = PrintServiceLookup
			.lookupPrintServices(null, null);

		for (PrintService ps : printServices) {
			this.innerPrinterCombo.getItems().add(ps.getName());
			this.innerPrinter2Combo.getItems().add(ps.getName());
		}
	}
	
	@FXML
	private void initialize() {
		this.settingsService.initializeSettingsFirstTime(); //make sure that settings entry available;
		this.initializeOuterPrinterCombo();
		this.initializeInnerPrinterCombo();

		this.outerPrinterCombo.setOnAction(e -> {
				this.settingsService.setSetting("outerPrinter",
								this.outerPrinterCombo.getSelectionModel().getSelectedItem()) ;

		});
		this.innerPrinterCombo.setOnAction(e -> {
			this.settingsService.setSetting("innerPrinter",
				this.innerPrinterCombo.getSelectionModel().getSelectedItem()) ;

		});
		
		this.innerPrinter2Combo.setOnAction(e -> {
				this.settingsService.setSetting("innerPrinter2",
					this.innerPrinter2Combo.getSelectionModel().getSelectedItem()) ;
		});
	}


	@FXML
	private void restartCounter() {
		this.settingsService.setSetting("dailyOrderCounter", "100");
	}
	
}
