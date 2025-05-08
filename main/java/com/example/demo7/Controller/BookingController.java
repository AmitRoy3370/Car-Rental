package com.example.demo7.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.example.demo7.Model.Booking;
import com.example.demo7.Model.User;
import com.example.demo7.Repository.BookingRepository;
import com.example.demo7.Repository.CarMongoRepository;
import com.example.demo7.Repository.UserMongoRepository;

@RestController
@RequestMapping("/Booking")
public class BookingController {

	@Autowired
	BookingRepository bookingRepo;

	@Autowired
	UserMongoRepository userRepo;

	@Autowired
	CarMongoRepository carRepo;

	@PostMapping("/add")
	public ResponseEntity<?> addBooking(@RequestBody Booking booking, @RequestParam String requestedId) {

		if (booking.getUserId() == null) {

			return ResponseEntity.status(401).body("missing user id");

		}

		if (userRepo.findById(booking.getUserId()).isEmpty()) {

			return ResponseEntity.status(401).body("user does not exist");

		}

		if (booking.getCarId() == null) {

			return ResponseEntity.status(401).body("car id is missing");

		}

		if (carRepo.findById(booking.getCarId()).isEmpty()) {

			return ResponseEntity.status(401).body("user doesn't exist");

		}

		if (booking.getStatus() == null) {

			return ResponseEntity.status(401).body("status is missing");

		}

		if (booking.getStartDate() == null) {

			return ResponseEntity.status(401).body("start date is missing");

		}

		if (booking.getEndDate() == null) {

			return ResponseEntity.status(401).body("missing end date");

		}

		if (booking.getStartDate().after(booking.getEndDate())) {

			return ResponseEntity.status(401).body("invalid date");

		}

		User user = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (user == null) {

			return ResponseEntity.status(400).body("requestedUser not find!!!");

		}

		if (user.getRole().toString().equals("USER")) {

			return ResponseEntity.status(500).body("Only admin can attach the booking");

		}

		Booking bookedCar = bookingRepo.save(booking);

		if (bookedCar == null) {

			return ResponseEntity.status(501).body("failed to add your booking");

		}

		return ResponseEntity.status(201).body(bookedCar);

	}

	@GetMapping("/showAll")
	public ResponseEntity<?> showAllBooking() {

		var statusCode = bookingRepo.count() > 0 ? 200 : 404;

		return ResponseEntity.status(statusCode).body(bookingRepo.findAll());

	}

	@GetMapping("/findByStatus")
	public ResponseEntity<?> showAllBookingByStatus(@RequestParam String status) {

		return bookingRepo.findBookingByStatus(status).isEmpty() ? ResponseEntity.status(404).body("no booking find")
				: ResponseEntity.status(200).body(bookingRepo.findBookingByStatus(status));

	}

	@GetMapping("/findByUser")
	public ResponseEntity<?> showAllBookingByUser(@RequestParam String userId) {

		return bookingRepo.findByUserId(userId).isEmpty() ? ResponseEntity.status(404).body("no booking find")
				: ResponseEntity.status(200).body(bookingRepo.findByUserId(userId));

	}

	@GetMapping("/findByCar")
	public ResponseEntity<?> showAllBookingByCar(@RequestParam String carId) {

		return bookingRepo.findByCarId(carId).isEmpty() ? ResponseEntity.status(404).body("no booking find")
				: ResponseEntity.status(200).body(bookingRepo.findByCarId(carId));

	}

	@GetMapping("/findByStartDate")
	public ResponseEntity<?> showAllBookingByStartDate(@RequestParam String startDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(startDate);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		if (date == null) {

			return ResponseEntity.status(400).body("bad date");

		}

		return bookingRepo.findByStartDate(date).isEmpty() ? ResponseEntity.status(404).body("no booking find")
				: ResponseEntity.status(200).body(bookingRepo.findByStartDate(date));

	}

	@GetMapping("/findByEndDate")
	public ResponseEntity<?> showAllBookingByEndDate(@RequestParam String endDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (date == null) {

			return ResponseEntity.status(401).body("bad date");

		}

		return bookingRepo.findByEndDate(date).isEmpty() ? ResponseEntity.status(404).body("no booking find")
				: ResponseEntity.status(200).body(bookingRepo.findByEndDate(date));

	}

	@GetMapping("/findByPrice")
	public ResponseEntity<?> showBookingByPrice(@RequestParam double price) {

		var statusCode = bookingRepo.findByTotalCostLessThanEqual(price).isEmpty() ? 404 : 200;

		return ResponseEntity.status(statusCode).body(bookingRepo.findByTotalCostLessThanEqual(price));

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteBooking(@RequestParam String id, @RequestParam String requestedId) {

		if (id == null) {

			return ResponseEntity.status(401).body("invalid url");

		}

		if (!bookingRepo.findById(id).isPresent()) {

			return ResponseEntity.status(404).body("booking not find");

		}

		User user = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (user == null) {

			return ResponseEntity.status(404).body("requested id not find!!!");

		}

		if (user.getRole().toString().equals("USER")) {

			return ResponseEntity.status(500).body("Only admin can delete the booking");

		}

		long count = bookingRepo.count();

		bookingRepo.deleteById(id);

		return count != bookingRepo.count() ? ResponseEntity.status(200).body("successfully deleted")
				: ResponseEntity.status(501).body("not deleted");

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateBooking(@RequestBody Booking booking, @RequestParam String id,
			@RequestParam String requestedId) {

		if (id == null) {

			return ResponseEntity.status(401).body("invalid url");

		}

		if (!bookingRepo.findById(id).isPresent()) {

			return ResponseEntity.status(404).body("booking not find");

		}

		Booking existingBooking = bookingRepo.findById(id).isEmpty() ? null : bookingRepo.findById(id).get();

		if (booking.getUserId() != null) {

			existingBooking.setUserId(booking.getUserId());

		}

		if (booking.getCarId() != null) {

			existingBooking.setCarId(booking.getCarId());

		}

		if (booking.getStatus().toString() != null) {

			existingBooking.setStatus(booking.getStatus());

		}

		if (booking.getStartDate() != null) {

			existingBooking.setStartDate(booking.getStartDate());

		}

		if (booking.getEndDate() != null) {

			existingBooking.setEndDate(booking.getEndDate());

		}

		if (existingBooking.getStartDate().after(existingBooking.getEndDate())) {

			return ResponseEntity.status(401).body("invalid date");

		}

		if (userRepo.findById(existingBooking.getUserId()).isEmpty()) {

			return ResponseEntity.status(401).body("user doesn't exist");

		}

		if (carRepo.findById(existingBooking.getCarId()).isEmpty()) {

			return ResponseEntity.status(401).body("car doesn't exist");

		}

		User user = userRepo.findById(requestedId).isEmpty() ? null : userRepo.findById(requestedId).get();

		if (user == null) {

			return ResponseEntity.status(404).body("requested id not find!!!");

		}

		if (user.getRole().toString().equals("USER")) {

			return ResponseEntity.status(500).body("Only admin can delete the booking");

		}

		existingBooking = bookingRepo.save(existingBooking);

		return existingBooking == null ? ResponseEntity.status(501).body("not updated")
				: ResponseEntity.status(200).body(existingBooking);

	}

}
