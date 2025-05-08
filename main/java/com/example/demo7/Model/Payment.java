package com.example.demo7.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Payment")
public class Payment {

	@Id
	String id;

	String userId, carId, bookingId;
	Date returnedDate;
	double haveToPay, actuallyPay;

	public Payment(String userId, String carId, String bookingId, Date returnedDate, double haveToPay,
			double actuallyPay) {
		super();
		this.userId = userId;
		this.carId = carId;
		this.bookingId = bookingId;
		this.returnedDate = returnedDate;
		this.haveToPay = haveToPay;
		this.actuallyPay = actuallyPay;
	}

	public Payment() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public Date getReturnedDate() {
		return returnedDate;
	}

	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}

	public double getHaveToPay() {
		return haveToPay;
	}

	public void setHaveToPay(double haveToPay) {
		this.haveToPay = haveToPay;
	}

	public double getActuallyPay() {
		return actuallyPay;
	}

	public void setActuallyPay(double actuallyPay) {
		this.actuallyPay = actuallyPay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", userId=" + userId + ", carId=" + carId + ", bookingId=" + bookingId
				+ ", returnedDate=" + returnedDate + ", haveToPay=" + haveToPay + ", actuallyPay=" + actuallyPay + "]";
	}

}
