package com.example.demo7.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo7.Model.Booking;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
	
	public List<Booking> findBookingByStatus(String status);
	public List<Booking> findByTotalCostLessThanEqual(double price);
	public List<Booking> findByStartDate(Date startDate);
	public List<Booking> findByEndDate(Date endDate);
	public List<Booking> findByUserId(String userId);
	public List<Booking> findByCarId(String carId);

}
