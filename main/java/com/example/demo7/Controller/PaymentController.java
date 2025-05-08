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

import com.example.demo7.Model.Booking;
import com.example.demo7.Model.Payment;
import com.example.demo7.Model.User;
import com.example.demo7.Repository.BookingRepository;
import com.example.demo7.Repository.CarMongoRepository;
import com.example.demo7.Repository.PaymentRepository;
import com.example.demo7.Repository.UserMongoRepository;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	PaymentRepository paymentRepo;

	@Autowired
	BookingRepository bookingRepo;

	@Autowired
	CarMongoRepository carRepo;

	@Autowired
	UserMongoRepository userRepo;

	@PostMapping("/addPayment")
	public ResponseEntity<?> addPayment(@RequestBody Payment payment, @RequestParam String requestId) {

		if (requestId == null) {

			return ResponseEntity.status(400).body("bad request");

		}

		if (payment == null) {

			return ResponseEntity.status(400).body("invalid input, we found nothing to add at here.");

		}

		if (payment.getUserId().isEmpty()) {

			return ResponseEntity.status(400).body("user information is missing at here");

		}

		if (payment.getCarId().isEmpty()) {

			return ResponseEntity.status(400).body("car information is missing at here.");

		}

		if (payment.getBookingId().isEmpty()) {

			return ResponseEntity.status(400).body("Booking information is missing at here");

		}

		if (payment.getReturnedDate() == null) {

			payment.setReturnedDate(new Date());

		}

		if (payment.getActuallyPay() <= 0.0) {

			return ResponseEntity.status(400).body("Invalid payment amount");

		}

		User requestedUser = userRepo.findById(requestId).isEmpty() ? null : userRepo.findById(requestId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can add payment status at here.");

		}

		if (userRepo.findById(payment.getUserId()).isEmpty()) {

			return ResponseEntity.status(400).body("Your payment user doesn't exist");

		}

		if (carRepo.findById(payment.getCarId()).isEmpty()) {

			return ResponseEntity.status(400).body("your payment car doesn't exist");

		}

		if (bookingRepo.findById(payment.getBookingId()).isEmpty()) {

			return ResponseEntity.status(400).body("Your payment booking doesn't exist");

		}

		Booking bookingInfo = bookingRepo.findById(payment.getBookingId()).get();

		if (bookingInfo.getStartDate().after(payment.getReturnedDate())) {

			return ResponseEntity.status(400).body("Invalid payment input");

		} else if (bookingInfo.getEndDate().after(payment.getReturnedDate())) {

			payment.setHaveToPay(bookingInfo.getTotalCost());

		} else {

			payment.setHaveToPay(bookingInfo.getTotalCost() * 2);

		}

		Payment addedPayment = paymentRepo.save(payment);

		if (addedPayment == null) {

			return ResponseEntity.status(501).body("payment not added!!!");

		}

		return ResponseEntity.status(201).header("paymentToken", addedPayment.getId()).body(addedPayment);

	}

	@GetMapping("/showAll")
	public ResponseEntity<?> showAllPayment(@RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findAll();

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("no payment find at here!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByUser")
	public ResponseEntity<?> searchByUser(@RequestParam String userId, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByUserId(userId);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this user!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByCar")
	public ResponseEntity<?> searchByCar(@RequestParam String carId, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByCarId(carId);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this car!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByBooking")
	public ResponseEntity<?> searchByBooking(@RequestParam String bookingId, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByBookingId(bookingId);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this car!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByHaveToPay")
	public ResponseEntity<?> searchByHaveToPay(@RequestParam Double haveToPay, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByHaveToPayLessThanEqual(haveToPay);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this price!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByActuallyPay")
	public ResponseEntity<?> searchByActuallyPay(@RequestParam Double ActuallyPay, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByActuallyPayLessThanEqual(ActuallyPay);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this price!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@GetMapping("/searchByReturnedDate")
	public ResponseEntity<?> searchByReturnedDate(@RequestParam @DateTimeFormat Date returnedDate,
			@RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		List<Payment> allPayments = paymentRepo.findByReturnedDate(returnedDate);

		if (allPayments.isEmpty()) {

			return ResponseEntity.status(404).body("No booking find for this date!!!");

		}

		return ResponseEntity.status(200).body(allPayments);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updatePayment(@RequestBody Payment payment, @RequestParam String id,
			@RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		if (id == null) {

			return ResponseEntity.status(400).body("bad request");

		}

		if (payment == null) {

			return ResponseEntity.status(400).body("invalid input, we found nothing to add at here.");

		}

		Payment existingPayment = paymentRepo.findById(id).get();

		if (existingPayment == null) {

			return ResponseEntity.status(404).body("Your payment not find!!!");

		}

		if (!payment.getUserId().isEmpty()) {

			if (userRepo.findById(payment.getUserId()).isEmpty()) {

			} else {

				existingPayment.setUserId(payment.getUserId());

			}

		}

		if (!payment.getCarId().isEmpty()) {

			if (carRepo.findById(payment.getCarId()).isEmpty()) {

			} else {

				existingPayment.setCarId(payment.getCarId());

			}
		}

		if (!payment.getBookingId().isEmpty()) {

			if (bookingRepo.findById(payment.getBookingId()).isEmpty()) {

			} else {

				existingPayment.setBookingId(payment.getBookingId());

			}

		}

		if (payment.getReturnedDate() != null) {

			Booking bookingInfo = bookingRepo.findById(payment.getBookingId()).get();

			if (bookingInfo.getStartDate().after(payment.getReturnedDate())) {

				// return ResponseEntity.status(400).body("Invalid payment input");

			} else if (bookingInfo.getEndDate().after(payment.getReturnedDate())) {

				// payment.setHaveToPay(bookingInfo.getTotalCost());

				existingPayment.setReturnedDate(payment.getReturnedDate());

			}

			// existingPayment.setReturnedDate(payment.getReturnedDate());

		}

		if (payment.getActuallyPay() <= 0.0) {

			// return ResponseEntity.status(401).body("Invalid payment amount");

		} else {

			existingPayment.setActuallyPay(payment.getActuallyPay());

		}

		existingPayment = paymentRepo.save(existingPayment);

		if (existingPayment == null) {

			return ResponseEntity.status(501).body("Failed to update it!!!");

		}

		return ResponseEntity.status(200).header("paymentToken", existingPayment.getId()).body(existingPayment);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePayment(@RequestParam String id, @RequestParam String requestedId) {

		User requestedUser = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (requestedUser == null) {

			return ResponseEntity.status(404).body("requested user not find!!!");

		}

		if (requestedUser.getRole().toString().equals("USER")) {

			return ResponseEntity.status(401).body("Only admin can watch it!!!");

		}

		long count = paymentRepo.count();

		paymentRepo.deleteById(id);

		return count == paymentRepo.count() ? ResponseEntity.status(501).body("failed to delete")
				: ResponseEntity.status(200).body("Successfully deleted");

	}

}
