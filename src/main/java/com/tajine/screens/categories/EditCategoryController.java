package com.tajine.screens.categories;

import com.tajine.domain.MealCategory;
import com.tajine.screens.commons.FXController;
import com.tajine.services.MealCategoriesService;
import com.tajine.utils.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EditCategoryController extends FXController {

        @Autowired
        private MealCategoriesService mealCategoriesService;

        @FXML
        private TextField categoryName;

        private MealCategory targetMealCategory;

        @Override
        protected void open(Stage stage) throws IOException {
                this.openWindowFromFXML("EditCategory.fxml",
                        stage, this.getClass(), 500, 500);
        }
        
        @FXML
        private void initialize() {
                this.categoryName.setText(this.targetMealCategory.getTitle());
        }

        @FXML
        private void doEdit() {
                try {
                        this.targetMealCategory.setTitle(this.categoryName.getText());
                        if(this.mealCategoriesService.updateCategory(this.targetMealCategory) != null) {
                                this.setTargetMealCategory(this.targetMealCategory);
                                this.stage.close();
                        } else {
                                this.setTargetMealCategory(null);
                        }
                } catch (ConstraintViolationException e){
                        FXUtils.ALERT_ERROR("يوجد فئة بهذا الاسم موجودة مسبقاً");
                        e.printStackTrace();
                }
        }

        public MealCategory getTargetMealCategory() {
                return targetMealCategory;
        }

        public void setTargetMealCategory(MealCategory targetMealCategory) {
                this.targetMealCategory = targetMealCategory;
        }
}
