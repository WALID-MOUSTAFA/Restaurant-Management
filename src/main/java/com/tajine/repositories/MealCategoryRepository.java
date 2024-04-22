package com.tajine.repositories;

import com.tajine.domain.MealCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MealCategoryRepository extends CrudRepository<MealCategory, Long> {
        MealCategory findByTitle(String title);
}
