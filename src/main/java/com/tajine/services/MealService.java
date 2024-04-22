package com.tajine.services;

import com.tajine.domain.Meal;
import com.tajine.domain.OrderContent;
import com.tajine.repositories.MealRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {
        @Autowired
        private MealRepository mealRepository;

        public Meal createMeal(Meal meal) throws Exception {

		
		Meal restult = this.mealRepository.save(meal);
                if(restult.getId() != null) {
                        return restult;
                }
                return null;
        }

        public Meal getMeal(Meal meal) {
                return this.mealRepository.findById(meal.getId()).get();
        }


	//TODO(WALID): MAKE DOMAIN OBJECTS IMPLEMENTS PERSISTABLE
	//TO DETERMINE WHEATHER IS ENTITY IS NEW OR NOT;
        public Meal updateMeal(Meal meal) {
                Meal original = mealRepository.findById(meal.getId()).get();
		original.setTitle(meal.getTitle());
		original.setCategory(meal.getCategory());
		original.setPrice(meal.getPrice());
		return  this.mealRepository.save(original);
        }
	
	public void deleteMeal(Meal meal) {
		this.mealRepository.delete(meal);
	}

        public List<Meal> findAllMeals() {
                return (List<Meal>)this.mealRepository.findAll();
        }

        public void ToggleActive(Meal meal) {
                meal.setActive(!meal.isActive());
        }
}
