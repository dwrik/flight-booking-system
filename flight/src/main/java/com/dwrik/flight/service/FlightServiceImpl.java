package com.dwrik.flight.service;

import com.dwrik.flight.exception.UnknownFlightException;
import com.dwrik.flight.model.Flight;
import com.dwrik.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepository flightRepository;

	@Override
	@Transactional(readOnly = true)
	public Iterable<Flight> getAllFlights() {
		return flightRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Flight getFlightById(Long id) {
		Optional<Flight> result = flightRepository.findById(id);

		if (result.isEmpty()) {
			throw new UnknownFlightException("flight not found");
		}

		return result.get();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Flight> getFlightsUsingSourceAndDestinationAndDate(String source, String destination, Date date) {
		return flightRepository.findBySourceAndDestinationAndDate(source, destination, date);
	}

	@Override
	@Transactional
	public Flight reserveSeat(Long id) {
		Optional<Flight> result = flightRepository.findById(id);

		if (result.isEmpty()) {
			throw new UnknownFlightException("flight not found");
		}

		Flight flight = result.get();
		flight.setRemainingSeats(flight.getRemainingSeats() - 1);
		return flightRepository.save(flight);
	}

	@Override
	@Transactional
	public void vacateSeat(Long id) {
		Optional<Flight> result = flightRepository.findById(id);

		if (result.isEmpty()) {
			throw new UnknownFlightException("flight not found");
		}

		Flight flight = result.get();
		flight.setRemainingSeats(flight.getRemainingSeats() + 1);
		flightRepository.save(flight);
	}
}
