package com.tajine.screens.commons;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class InputMsgBox extends FXController{

        @FXML
        protected Label  msgLabel;


        public InputMsgBox(String msgText) {
                this.msgText = msgText;
        }

        @FXML
        private void initialize() {
                this.msgLabel.setText(msgText);
        }

        private String msgText;
        @FXML
        protected TextField textField;
        protected String data;



        @Override
        protected void open(Stage stage) throws IOException {
                this.openWindowFromFXMLNoSpring("InputMsgBox.fxml",
                        stage, this, 300, 200);
        }

        @FXML
        public void setData() {
                this.data = this.textField.getText();
                this.stage.close();
        }

        public String getData() throws IOException {
                this.open(null);
                return this.data;
        }
}
