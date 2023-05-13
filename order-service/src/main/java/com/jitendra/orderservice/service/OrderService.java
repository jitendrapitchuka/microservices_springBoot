package com.jitendra.orderservice.service;

import com.jitendra.orderservice.dto.InventoryResponse;
import com.jitendra.orderservice.dto.OrderLineItemsDto;
import com.jitendra.orderservice.dto.OrderRequest;
import com.jitendra.orderservice.model.Order;
import com.jitendra.orderservice.model.OrderLineItems;
import com.jitendra.orderservice.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());



        List<OrderLineItems> orderLineItems=orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes= order.getOrderLineItemsList().stream().map(orderLineItem -> orderLineItem.getSkuCode() ).toList();

        InventoryResponse[] inventoryResponsesArray=webClientBuilder.build().get().uri("http://inventory-service/api/inventory",uriBuilder ->
                        uriBuilder.queryParam("skuCode",skuCodes).build()).retrieve()
                .bodyToMono(InventoryResponse[].class).block();

        boolean allProductsInStock=Arrays.stream(inventoryResponsesArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductsInStock)
        orderRepository.save(order);
        else
            throw new IllegalArgumentException("product is not in stock");
    }


    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
