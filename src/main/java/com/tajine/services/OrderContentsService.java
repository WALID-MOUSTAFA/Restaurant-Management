package com.tajine.services;
import com.tajine.domain.OrderContent;
import com.tajine.repositories.OrderContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderContentsService {
	@Autowired
        private OrderContentRepository orderContentRepository;

	public OrderContent createOrderContent(OrderContent orderContent) {
	        //TODO(WALID): ADD CHECKS AND CATCH EXCEPTIONS;
	        return this.orderContentRepository.save(orderContent);
        }
}
