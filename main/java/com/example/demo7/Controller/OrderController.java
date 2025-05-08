package com.example.demo7.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo7.Model.Order;
import com.example.demo7.Repository.CarMongoRepository;
import com.example.demo7.Repository.OrderRepository;
import com.example.demo7.Repository.UserMongoRepository;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	UserMongoRepository userRepo;

	@Autowired
	CarMongoRepository carRepo;

	@PostMapping("/add")
	public ResponseEntity<?> addOrder(@RequestBody Order order) {

		if (order == null) {

			return ResponseEntity.status(400).body("false request!!!");

		}

		if (order.getUserId() == null) {

			return ResponseEntity.status(400).body("no user id at here!!!");

		}

		if (order.getCarId() == null) {

			return ResponseEntity.status(400).body("no car id at here!!!");

		}

		if (order.getDate() == null) {

			order.setDate(new Date());

		}

		if (userRepo.findById(order.getUserId()).isEmpty()) {

			return ResponseEntity.status(404).body("user not find!!!");

		}

		if (carRepo.findById(order.getCarId()).isEmpty()) {

			return ResponseEntity.status(404).body("car not find!!!");

		}

		Order requestedOrder = orderRepo.save(order);

		return ResponseEntity.status(201).header("orderToken", requestedOrder.getId()).body(requestedOrder);

	}

	@GetMapping("/showAll")
	public ResponseEntity<?> showOrders() {

		List<Order> list = orderRepo.findAll();

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("no order find at here!!!");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/findByUser")
	public ResponseEntity<?> findByUser(@RequestParam String userId) {

		if (userId == null) {

			return ResponseEntity.status(400).body("false request!!!");

		}

		List<Order> list = orderRepo.findByUserId(userId);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("no order find!!!");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/findByCar")
	public ResponseEntity<?> findByCar(@RequestParam String carId) {

		if (carId == null) {

			return ResponseEntity.status(400).body("false request!!!");

		}

		List<Order> list = orderRepo.findByCarId(carId);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("no order find!!!");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/searchByOrderDay")
	public ResponseEntity<?> findByOrderDay(@RequestParam Integer forHowManyDay) {

		List<Order> list = orderRepo.findByForHowManyDayLessThanEqual(forHowManyDay);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("no order find at here!!!");

		} else {

			return ResponseEntity.status(200).body(list);

		}

	}

	@GetMapping("/findByDate")
	public ResponseEntity<?> findByDate(@RequestParam @DateTimeFormat Date date) {

		if (date == null) {

			return ResponseEntity.status(400).body("false request!!!");

		}

		List<Order> list = orderRepo.findByDate(date);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("no order find!!!");

		}

		return ResponseEntity.status(200).body(list);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateOrder(@RequestBody Order order, @RequestParam String id) {

		if (id == null) {

			return ResponseEntity.status(400).body("no order id at here!!!");

		}

		if (order == null) {

			return ResponseEntity.status(400).body("nothing to update at here!!!");

		}

		Order existingOrder = orderRepo.findById(id).isEmpty() ? null : orderRepo.findById(id).get();

		if (existingOrder == null) {

			return ResponseEntity.status(404).body("invalid id!!!");

		}

		if (order.getUserId() != null) {

			if (userRepo.findById(order.getUserId()).isEmpty()) {

				// return ResponseEntity.status(404).body("car not find!!!");

			} else {

				existingOrder.setUserId(order.getUserId());

			}

		}

		if (order.getCarId() != null) {

			if (carRepo.findById(order.getCarId()).isEmpty()) {

				// return ResponseEntity.status(404).body("car not find!!!");

			} else {

				existingOrder.setCarId(order.getCarId());

			}

		}

		if (order.getForHowManyDay() > 0) {

			existingOrder.setForHowManyDay(order.getForHowManyDay());

		}

		if (order.getDate() != null) {

			existingOrder.setDate(order.getDate());

		}

		existingOrder = orderRepo.save(existingOrder);

		return ResponseEntity.status(200).header("orderToekn", existingOrder.getId()).body(existingOrder);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteOrder(@RequestParam String id) {

		if (id == null) {

			return ResponseEntity.status(400).body("no id at here!!!");

		}

		if (orderRepo.findById(id).isEmpty()) {

			return ResponseEntity.status(404).body("no order find at here!!!");

		}

		long count = orderRepo.count();

		orderRepo.deleteById(id);

		return count == orderRepo.count() ? ResponseEntity.status(501).body("failed to delete")
				: ResponseEntity.status(200).body("successfully deleted");

	}

}
