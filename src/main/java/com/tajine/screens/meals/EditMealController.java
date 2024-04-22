package com.tajine.screens.meals;

import java.io.IOException;

import com.tajine.domain.Meal;
import com.tajine.domain.MealCategory;
import com.tajine.screens.commons.FXController;
import com.tajine.services.MealCategoriesService;
import com.tajine.services.MealService;
import com.tajine.utils.FXUtils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@Component
public class EditMealController extends FXController {

	@Autowired
	private MealService mealService;
        @Autowired
	private MealCategoriesService mealCategoriesService;
	private Meal targetMeal;
	@FXML
        private ComboBox<MealCategory> mealCategoryComboBox;
        @FXML
        private TextField mealTitle;
        @FXML
        private TextField mealPrice;

	
	@Override
        protected void open(Stage stage) throws IOException {
                this.openWindowFromFXML("EditMeal.fxml",
                        stage, this.getClass(), 500, 500);
        }


	@FXML
	private void initialize() {
		this.initializeForm();
	}


	@FXML
	private void doEdit() {
		String title;
		int price;
		MealCategory category;

		try {
			title = this.mealTitle.getText();
			try {
				price = Integer.valueOf(this.mealPrice.getText());
			} catch (NumberFormatException e) {
				FXUtils.ALERT_ERROR("ادخل البيانات الرقمية بشكل سليم");
				e.printStackTrace();
				return;
			}
		
			category = this.mealCategoryComboBox.getValue();
			targetMeal.setTitle(title);
			targetMeal.setPrice(price);
			targetMeal.setCategory(category);
			try {
				this.mealService.updateMeal(targetMeal);
			}catch (ConstraintViolationException e) {
				FXUtils.ALERT_ERROR("يوجد صنف بهذا الإسم");
				e.printStackTrace();
				return;
			}
		} catch(Exception e) {
			FXUtils.ALERT_ERROR("حدث خطأ أثناء الإضافة، تأكد من البيانات");
			e.printStackTrace();
			return;
		}
		this.stage.close();
		
	}

	private void initializeForm() {
		this.initializeMealCategoriesComboBox();
		this.mealTitle.setText(this.targetMeal.getTitle());
		this.mealPrice.setText(String.valueOf(this.targetMeal.getPrice()));
		this.mealCategoryComboBox.setValue(this.targetMeal.getCategory());
	}

	private void initializeMealCategoriesComboBox() {
                ObservableList<MealCategory> categories =
                        FXCollections.observableArrayList(mealCategoriesService.findAllMealCategories());
                this.mealCategoryComboBox.setItems(categories);
                mealCategoryComboBox.setConverter(new StringConverter<MealCategory>() {
				@Override
				public String toString(MealCategory object) {
					return object.getTitle();
				}

				@Override
				public MealCategory fromString(String string) {
					return null;
				}
			});
        }


	public Meal getTargetMeal() {
		return targetMeal;
	}


	public void setTargetMeal(Meal targetMeal) {
		this.targetMeal = targetMeal;
	}



	
}
