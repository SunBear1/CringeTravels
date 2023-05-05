package com.cringe.travels.trips.trip;

import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final TripRepository repository;
    Logger logger = LoggerFactory.getLogger(TripService.class);


    TripService(TripRepository repository) {
        this.repository = repository;
    }

    public List<Trip> getAll() {
        return repository.findAll();
    }

    public List<Trip> getAllActive() {
        return repository.findAllActiveTrips();
    }

    public void updateHotelsRooms(JSONObject jsonObject) {

        JSONArray listIds = jsonObject.getJSONArray("trip_offers_id");
        String operationType = jsonObject.getString("operation_type");
        String roomType = jsonObject.getString("room_type");

        for (int i = 0; i < listIds.length(); i++) {
            String id = listIds.getString(i);
            Optional<Trip> tripOpt = repository.findById(id);
            if (tripOpt.isPresent()) {
                Trip trip = tripOpt.get();
                int freeSeats = trip.getHotel().getRooms().get(roomType).getAvailable();
                logger.info("BEFORE: TRIP: " + trip.getId() + "\n Room type:" + roomType + "\n seats left:" + freeSeats);
                if (operationType.equals("add")) {
                    freeSeats++;
                } else {
                    freeSeats--;
                }
                trip.getHotel().getRooms().get(roomType).setAvailable(freeSeats);
                repository.save(trip);
                logger.info("NOW: TRIP: " + trip.getId() + "\n Room type:" + roomType + "\n seats left:" + freeSeats);
            } else {
                logger.info("NO TRIP IN DB WITH ID:" + id);
            }
        }
    }

    public void updateTripStatus(JSONObject jsonObject) {

        JSONArray listIds = jsonObject.getJSONArray("trip_offers_id");
        boolean isHotelBookedUp = jsonObject.getBoolean("is_hotel_booked_up");

        for (int i = 0; i < listIds.length(); i++) {
            String id = listIds.getString(i);
            Optional<Trip> tripOpt = repository.findById(id);
            if (tripOpt.isPresent()) {
                Trip trip = tripOpt.get();
                trip.setBookedUp(isHotelBookedUp);
                repository.save(trip);
                logger.info("UPDATED TRIP STATUS IN TRIP: " + trip.getId() + "\n bookedUp:" + isHotelBookedUp);

            } else {
                logger.info("NO TRIP IN DB WITH ID:" + id);
            }
        }
    }

    public void updateTransport(JSONObject jsonObject) {

        JSONArray listIds = jsonObject.getJSONArray("trip_offers_id");
        String connectionId = jsonObject.getString("connection_id");
        String operationType = jsonObject.getString("operation_type");
        int headCount = jsonObject.getInt("head_count");


        for (int i = 0; i < listIds.length(); i++) {
            String id = listIds.getString(i);
            Optional<Trip> tripOpt = repository.findById(id);
            if (tripOpt.isPresent()) {
                Trip trip = tripOpt.get();
                trip.getFrom().forEach((key, transport) -> {
                    if (transport.getPlane() != null) {
                        if (transport.getPlane().getId().equals(connectionId)) {
                            int seatsLeft = transport.getPlane().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionId + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getPlane().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionId + "\n seats left:" + seatsLeft);
                        }
                    }

                    if (transport.getTrain() != null) {
                        if (transport.getTrain().getId().equals(connectionId)) {
                            int seatsLeft = transport.getTrain().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionId + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getTrain().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionId + "\n seats left:" + seatsLeft);
                        }
                    }
                });
            } else {
                logger.info("NO TRIP IN DB WITH ID:" + id);
            }
        }
    }

    public void updateTransportStatus(JSONObject jsonObject) {

        JSONArray listIds = jsonObject.getJSONArray("trip_offers_id");
        String connectionId = jsonObject.getString("connection_id");
        boolean transportBookedUp = jsonObject.getBoolean("is_transport_booked_up");


        for (int i = 0; i < listIds.length(); i++) {
            String id = listIds.getString(i);
            Optional<Trip> tripOpt = repository.findById(id);
            if (tripOpt.isPresent()) {
                Trip trip = tripOpt.get();
                trip.getFrom().forEach((key, transport) -> {
                    if (transport.getPlane() != null) {
                        if (transport.getPlane().getId().equals(connectionId)) {
                            transport.getPlane().setTransportBookedUp(transportBookedUp);
                            repository.save(trip);
                            logger.info("UPDATED TRANSPORT STATUS IN TRIP: " + trip.getId() + "\n connection id: " + connectionId + "\n bookedUp:" + transportBookedUp);
                        }
                    }

                    if (transport.getTrain() != null) {
                        if (transport.getTrain().getId().equals(connectionId)) {
                            transport.getTrain().setTransportBookedUp(transportBookedUp);
                            repository.save(trip);
                            logger.info("UPDATED TRANSPORT STATUS IN TRIP: " + trip.getId());
                        }
                    }

                });
            } else {
                logger.info("NO TRIP IN DB WITH ID:" + id);
            }
        }
    }

    public Trip getbyId(String id) {
        return repository.findByTripID(id);
    }
}
