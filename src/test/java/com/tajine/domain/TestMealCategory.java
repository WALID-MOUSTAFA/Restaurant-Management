package com.tajine.domain;

import com.tajine.screens.orders.FilterByDateTime;
import com.tajine.services.MealCategoriesService;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
public class TestMealCategory {

        @Autowired
        MealCategoriesService mealCategoriesService;

        @Test
        public void testCreateCategory() {
                MealCategory mealCategory = new MealCategory();
                mealCategory.setTitle("drinks");
                this.mealCategoriesService.createCategory(mealCategory);
                Assertions.assertNotNull(mealCategory.getId());
                System.out.println("---------"+mealCategory.getId());
        }

        @Test
        public void testFindByTitle() {
                MealCategory mealCategory = this.mealCategoriesService.findCategoryByTitle("drinks");
                Assertions.assertNotNull(mealCategory.getId());
        }

        @Test
        public void testt() throws IOException {
                FilterByDateTime filter = new FilterByDateTime();
                filter.open(new Stage());
        }
}
