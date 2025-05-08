package com.example.demo7.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection="cars")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Car {

	@Id
	private String id;
	
	private String model, brand, type, availability;
	
	private double price;

	public Car(String model, String brand, String type, String availability, double price) {
		super();
		this.model = model;
		this.brand = brand;
		this.type = type;
		this.availability = availability;
		this.price = price;
	}

	public Car() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
