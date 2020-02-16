package com.taxi.friend.drivers.services;

import com.taxi.friend.drivers.models.Order;
import com.taxi.friend.drivers.models.OrderStatus;
import com.taxi.friend.drivers.repository.OrderRepository;
import com.taxi.friend.drivers.repository.RestClient;

import retrofit2.Call;

public class OrderService {

    public Call<Order> updateOrderStatus(String id, OrderStatus orderStatus) {
        OrderRepository service = RestClient.createRestClient().create(OrderRepository.class);

        return service.updateOrderStatus(id, orderStatus);
    }
}
