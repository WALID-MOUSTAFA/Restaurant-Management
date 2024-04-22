package com.tajine.screens.orders;

import java.io.IOException;
import java.sql.Date; //I'm using sql.date instead of util.date for the valueOf method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.tajine.screens.commons.FXController;

import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


@Component
public class FilterByDateTime extends FXController {

	@FXML
	private TextField fromField;

	@FXML
	private TextField toField;

	@FXML
	private Button submitButton; 

	private DateRange dateRange = new DateRange();

	@FXML
	private void initialize() {
	// 	this.stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
	// 			@Override
	// 			public void handle(KeyEvent event) {
	// 				System.out.println("it listens");
	// 				switch (event.getCode()) {
	// 				case ENTER:
	// 					// FilterByDateTime.this.setData();
	// 				}
	// 			}
	// 		});

		if(this.toField.getText() == null || this.toField.getText().isEmpty()){
			this.toField.setText("2022-01-01 06:00:00");
		}
		if(this.fromField.getText() == null || this.fromField.getText().isEmpty()){
			this.fromField.setText("2022-01-01 06:00:00");
		}
	}

	@FXML
	private void setData() {
		this.dateRange.setFrom(this.fromField.getText());
		this.dateRange.setTo(this.toField.getText());
		//TODO(WALID): THIS IS A TEMP WORK AROUND;
		if(this.toField.getText() == "2021-01-01 12:00:00" || this.fromField.getText() == "2021-01-01 12:00:00") {
			this.dateRange.setIsNew(false);
		} else {
			this.dateRange.setIsNew(true);
		}
		this.stage.close();
	}


	public DateRange getData() {
		return this.dateRange;
	}

	@Override
	public void open(Stage stage) throws IOException {
		this.openWindowFromFXML("FilterByDateTime.fxml",
						stage, FilterByDateTime.class , 300, 200);
	}

	
	public boolean compare(String date, DateRange dateRange) {
		String from = dateRange.getFrom();
		String to = dateRange.getTo();
		DateTimeFormatter dateTimeFormatter=
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateDate = LocalDateTime.parse(date, dateTimeFormatter);
		LocalDateTime fromDate = LocalDateTime.parse(from, dateTimeFormatter);
		LocalDateTime toDate= LocalDateTime.parse(to, dateTimeFormatter);

		long fromUnixTime = fromDate.atZone(ZoneId.systemDefault()).toEpochSecond();
		long toUnixTime = toDate.atZone(ZoneId.systemDefault()).toEpochSecond();
		long dateUnixTime = dateDate.atZone(ZoneId.systemDefault()).toEpochSecond();
		
		return dateUnixTime >= fromUnixTime && dateUnixTime <= toUnixTime;
	}
	
	

}

