package com.rash.ecom.order_service.service;

import com.rash.ecom.order_service.dto.OrderLineItemsDto;
import com.rash.ecom.order_service.dto.OrderRequest;
import com.rash.ecom.order_service.model.Order;
import com.rash.ecom.order_service.model.OrderLineItems;
import com.rash.ecom.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapDtoToEntity)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);
        orderRepository.save(order);
        log.info("Order {} placed successfully", order.getId());
    }

    private OrderLineItems mapDtoToEntity(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }
}
