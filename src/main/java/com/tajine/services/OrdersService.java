package com.tajine.services;

import java.util.List;

import com.tajine.domain.Order;
import com.tajine.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
        @Autowired
        OrderRepository orderRepository;

	public Order createOrder(Order order) {
		return this.orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return (List<Order>) this.orderRepository.findAll();
	}

	public void deleteOrder(Order order) {
		this.orderRepository.delete(order);
	}
}
