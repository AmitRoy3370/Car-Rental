package com.example.demo7.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo7.Model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

	public List<Order> findByUserId(String userId);

	public List<Order> findByCarId(String carId);

	public List<Order> findByForHowManyDayLessThanEqual(int forHowManyDay);

	public List<Order> findByDate(Date date);

}
