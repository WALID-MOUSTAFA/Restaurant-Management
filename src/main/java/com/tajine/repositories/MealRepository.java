package com.tajine.repositories;
import java.util.Optional;


import com.tajine.domain.Meal;
import org.springframework.data.repository.CrudRepository;

public interface MealRepository extends CrudRepository<Meal, Long> {
	public Optional<Meal> findOneByTitle(String title);
}
