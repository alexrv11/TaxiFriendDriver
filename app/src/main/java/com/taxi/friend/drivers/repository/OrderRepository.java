package com.taxi.friend.drivers.repository;

import com.taxi.friend.drivers.models.Order;
import com.taxi.friend.drivers.models.OrderStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface OrderRepository {


    @PATCH("orders/{id}")
    Call<Order> updateOrderStatus(@Path("id") String id, @Body OrderStatus orderStatus);
}
