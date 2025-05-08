package com.example.demo7.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo7.Model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {

	public List<Payment> findByUserId(String userId);

	public List<Payment> findByCarId(String carId);

	public List<Payment> findByBookingId(String bookingId);

	public List<Payment> findByHaveToPayLessThanEqual(double haveToPay);

	public List<Payment> findByActuallyPayLessThanEqual(double actuallyPay);

	public List<Payment> findByReturnedDate(Date returnedDate);

}
