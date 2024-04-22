package com.tajine;

import java.io.IOException;

import com.tajine.screens.main.MainController;
import com.tajine.screens.login.LoginController;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

public class TajineFXApplication extends Application {

    private ConfigurableApplicationContext applicationContext;
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder()
                .sources(TajineApplication.class)
                .headless(false)
                .run(getParameters().getRaw().toArray(new String[0]));
    }
	
    @Override
    public void start(Stage primaryStage) throws IOException {
	    // primaryStage.setMaximized(true);
	    this.applicationContext.getBean(MainController.class).open(primaryStage);
    }

	@Override
	public void stop() throws Exception {
		this.applicationContext.close();
		super.stop();
	}

	


}

