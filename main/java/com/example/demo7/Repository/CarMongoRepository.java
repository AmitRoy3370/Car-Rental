package com.example.demo7.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo7.Model.Car;

public interface CarMongoRepository extends MongoRepository<Car, String> {
	
	public List<Car> findCarByModel(String model);
	public List<Car> findCarByBrand(String brand);
	public List<Car> findCarByType(String type);
	public List<Car> findCarByAvailability(String availability);
	public List<Car> findByPriceLessThanEqual(double price);

}
