package com.tajine.services;

import com.tajine.domain.Meal;
import com.tajine.domain.MealCategory;
import com.tajine.repositories.MealCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealCategoriesService {
        @Autowired
        MealCategoryRepository mealCategoryRepository;

        public MealCategory createCategory(MealCategory mealCategory) {
                MealCategory result = this.mealCategoryRepository.save(mealCategory);
                if (result.getId() != null) {
                        return result;
                } else {
                        return null;
                }
        }


        public MealCategory findCategoryByTitle(String title){
                //TODO(WALID): NEEDS TO BE CHECKED;
                return this.mealCategoryRepository.findByTitle(title);
        }

        public MealCategory updateCategory(MealCategory mealCategory) {
                //TODO(WALID): CHECK OPTIONAL;
                MealCategory original = this.mealCategoryRepository.findById(mealCategory.getId()).get();
                original.setTitle(mealCategory.getTitle());
                this.mealCategoryRepository.save(original);
                if(original.getTitle().equals(mealCategory.getTitle())) {
                        return original;
                } else {
                        return null;
                }
        }

        public List<MealCategory> findAllMealCategories() {

                List<MealCategory> result =  (List<MealCategory>) this.mealCategoryRepository.findAll();
                return  result.size() == 0 ? new ArrayList<>() : result;
        }

        public void deleteCategory(MealCategory mealCategory) {
                this.mealCategoryRepository.delete(mealCategory);
        }


}
