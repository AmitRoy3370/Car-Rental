package com.example.demo7.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo7.Model.Car;
import com.example.demo7.Repository.CarMongoRepository;

@RestController
@RequestMapping("/car")
public class CarController {
	
	@Autowired
	CarMongoRepository carRepo;
	
	@PostMapping("/addCar")
	public ResponseEntity<?> addCar(@RequestBody Car car) {
		
		if(car == null) {
			
			return ResponseEntity.status(401).body("Wrong User input!!!");
			
		}
		
		Car addedCar = carRepo.save(car);
		
		if(addedCar == null) {
			
			return ResponseEntity.status(501).body("Failed to add the car in our system");
			
		}
		
		return ResponseEntity.status(201).body(car);
		
	}
	
	@GetMapping("/searchCarById")
	public ResponseEntity<?> searchCar(@RequestParam String id) {
		
		Optional<Car> searchedCar = carRepo.findById(id);
		
		if(!searchedCar.isPresent()) {
			
			return ResponseEntity.status(404).body("Your searching car is not available");
			
		}
		
		return ResponseEntity.status(200).body(searchedCar);
		
	}
	
	@GetMapping("/showAll")
	public ResponseEntity<?> showAllCar() {
		
		var allCars = carRepo.findAll();
		
		if(allCars == null) {
			
			return ResponseEntity.status(404).body("There is not available car right now");
			
		}
		
		return ResponseEntity.status(200).body(allCars);
		
	}
	
	@GetMapping("/searchByBrand")
	public ResponseEntity<?> showAllSameBrandCar(@RequestParam String brand) {
		
		var allCar = carRepo.findCarByBrand(brand);
		
		if(allCar == null) {
			
			return ResponseEntity.status(404).body("Your searching car is not available");
			
		}
		
		return ResponseEntity.status(200).body(allCar);
		
	}
	
	@GetMapping("/searchByModel")
	public ResponseEntity<?> showAllSameModelCar(@RequestParam String model) {
		
		var allCar = carRepo.findCarByModel(model);
		
		if(allCar == null) {
			
			return ResponseEntity.status(404).body("Your searching car is not available");
			
		}
		
		return ResponseEntity.status(200).body(allCar);
		
	}
	
	@GetMapping("/searchByType")
	public ResponseEntity<?> showAllSameTypeCar(@RequestParam String type) {
		
		var allCar = carRepo.findCarByType(type);
		
		if(allCar.isEmpty()) {
			
			return ResponseEntity.status(404).body("Your searching car is not available");
			
		}
		
		return ResponseEntity.status(200).body(allCar);
		
	}
	
	@GetMapping("/searchByCost")
	public ResponseEntity<?> showAllSameCostCar(@RequestParam String cost) {
		
		try {
			
			double price = Double.parseDouble(cost);
			
			System.out.print(price);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(400).body(e);
			
		}
		
		var allCar = carRepo.findByPriceLessThanEqual(Double.parseDouble(cost));
		
		if(allCar.isEmpty()) {
			
			return ResponseEntity.status(404).body("Your searching car is not available");
			
		}
		
		return ResponseEntity.status(200).body(allCar);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateCar(@RequestParam String id, @RequestBody Car car) {
		
		Car existingCar = carRepo.findById(id).get();
		
		if(existingCar == null) {
			
			return ResponseEntity.status(404).body("Your updated car is not available right now");
			
		}
		
		car.setId(existingCar.getId());
		
		if(car.getAvailability() == null) {
			
			car.setAvailability(existingCar.getAvailability());
			
		}
		
		if(car.getBrand() == null) {
			
			car.setBrand(existingCar.getBrand());
			
		}
		
		if(car.getModel() == null) {
			
			car.setModel(existingCar.getModel());
			
		}
		
		if(car.getPrice() == 0.00) {
			
			car.setPrice(existingCar.getPrice());
			
		}
		
		if(car.getType() == null) {
			
			car.setType(existingCar.getType());
			
		}
		
		existingCar = carRepo.save(car);
		
		return ResponseEntity.status(200).header("updatedCar", existingCar.toString()).body("Car is updated successfully");
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> removeCar(@RequestParam String id) {
		
		if(!carRepo.findById(id).isPresent()) {
			
			return ResponseEntity.status(404).body("Your searching car do not find");
			
		}
		
		long count = carRepo.count();
		
		Optional<Car> removedCar = carRepo.findById(id);
		
		carRepo.deleteById(id);
		
		return count != carRepo.count() ? ResponseEntity.status(200).header("removedCar", removedCar.get().toString()).body("Car is deleted") : ResponseEntity.status(501).body("failed to remove this car");
		
		
	}

}
