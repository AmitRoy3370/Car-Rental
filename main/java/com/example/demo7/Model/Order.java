package com.example.demo7.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Order")
public class Order {

	@Id
	String id;

	String userId, carId;
	int forHowManyDay;
	Date date;

	public Order(String userId, String carId, int forHowManyDay, Date date) {
		super();
		this.userId = userId;
		this.carId = carId;
		this.forHowManyDay = forHowManyDay;
		this.date = date;
	}

	public Order() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getForHowManyDay() {
		return forHowManyDay;
	}

	public void setForHowManyDay(int forHowManyDay) {
		this.forHowManyDay = forHowManyDay;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", carId=" + carId + ", forHowManyDay=" + forHowManyDay
				+ ", date=" + date + "]";
	}

}
