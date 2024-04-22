package com.tajine.services;

import com.tajine.domain.Meal;
import com.tajine.domain.MealCategory;
import com.tajine.domain.Order;
import com.tajine.domain.OrderContent;
import com.tajine.utils.FXUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TestOrders {

        @Autowired private MealCategoriesService mealCategoriesService;
        @Autowired private OrdersService ordersService;
        @Autowired private OrderContentsService orderContentsService;
        @Autowired private MealService mealService;
        private Meal meal;
        @Autowired
        EntityManager entityManager;

        public void prepareMeals() throws Exception {
                MealCategory mealCategory = new MealCategory();
                mealCategory.setTitle("fortest");
                MealCategory result = this.mealCategoriesService.createCategory(mealCategory);


                Meal meal  = new Meal();
                meal.setCategory(result);
                meal.setPrice(200);
                meal.setTitle("lahma");
                Meal mealresult = this.mealService.createMeal(meal);

                this.meal  = mealresult;
        }
        
        @Test
        public void testCreateOrder() {
                OrderContent orderContent = new OrderContent();
                orderContent.setMeal(mealService.getMeal(meal));
                orderContent.setPrice(300);
                Order order = new Order();
                orderContent.setOrder(order);
                order.setOrderType(1);
                order.setCustomer(null);
                order.setAddress("jkfdjk");
                order.setTotalPrice(200);
                order.setTotalPrice(400);
                order.setAdmin(null);

                Set<OrderContent> s = new HashSet<>();
                s.add(orderContent);
                order.setOrderContents(s);
                Order result  = this.ordersService.createOrder(order);
                Assertions.assertNotNull(result.getId());
        }

	@Test
	public void testOrderTypeConversion() {
		String orderType = FXUtils.orderTypeToString("2");
		Assertions.assertEquals(orderType, "تيك أواي");
	}
}
