package com.tajine.screens.commons;

import java.io.IOException;

import com.tajine.domain.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class FXController {

        @Autowired
        protected ApplicationContext ac;
        protected static Stage stage;
	protected static Scene scene;
        protected String location;
	protected static Admin cashier;

	
        protected void open(Stage stage) throws IOException {};


        private FXMLLoader getLoader(String location) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/"+location));
                return loader;
        }


        //NOTE(WALID): sometimes you need to open a window outside the spring container, here we go;
        public void openWindowFromFXMLNoSpring(String location,
                                          Stage stage,
                                          FXController fxController,
                                          int width,
                                          int height) throws IOException {
                if (stage == null) {
                        stage = new Stage();
                }
                FXMLLoader loader = this.getLoader(location);
                if(fxController != null) {
                        loader.setController(fxController);
                }
                Parent root = loader.load();
		Scene scene = new Scene(root, width, height);
		this.scene = scene;
		this.scene = scene;
                stage.setScene(scene);
                this.stage = stage;
                this.location = location;
                stage.showAndWait();
        }

        protected void openWindowFromFXML(String location,
                                          Stage stage,
                                          Class controllerBean,
                                          int width,
                                          int height) throws IOException {
		if (stage == null) {
			stage = new Stage();
		}
		FXMLLoader loader = this.getLoader(location);
                if(controllerBean != null) {
                        FXController c = (FXController)this.ac.getBean(controllerBean);
                        loader.setController(c);
                }
                Parent root = loader.load();
                stage.setScene(new Scene(root, width, height));
                this.stage = stage;
                this.location = location;
                stage.showAndWait();
        }

        //this duplication because it can't call showAndWait on the primary stage;
        protected void openPrimaryWindowFromFXML(String location,
                                          Stage stage,
                                          Class controllerBean,
                                          int width,
                                          int height) throws IOException {
                if (stage == null) {
                        stage = new Stage();
                }
                FXMLLoader loader = this.getLoader(location);
                if(controllerBean != null) {
                        FXController c = (FXController)this.ac.getBean(controllerBean);
                        loader.setController(c);
                }
                Parent root = loader.load();
                stage.setScene(new Scene(root, width, height));
		stage.setMaximized(true);
		this.stage = stage;
	       
                this.location = location;
                stage.show();
        }


        protected void swapWithFXML(Pane container, String location, Class fxController) throws IOException {
                container.getChildren().clear();
                FXMLLoader loader = this.getLoader(location);
                loader.setController( this.ac.getBean(fxController));
                VBox root = loader.load();
                AnchorPane.setBottomAnchor(root, 0.0);
                AnchorPane.setTopAnchor(root, 0.0);
                AnchorPane.setLeftAnchor(root, 0.0);
                AnchorPane.setRightAnchor(root, 0.0);
                container.getChildren().add(root);

        }


}
