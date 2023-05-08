package com.cringe.travels.trips.trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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
        String connectionIdTO = jsonObject.getString("connection_id_to");
        String connectionIdFrom = jsonObject.getString("connection_id_from");
        String operationType = jsonObject.getString("operation_type");
        int headCount = jsonObject.getInt("head_count");


        for (int i = 0; i < listIds.length(); i++) {
            String id = listIds.getString(i);
            Optional<Trip> tripOpt = repository.findById(id);
            if (tripOpt.isPresent()) {
                Trip trip = tripOpt.get();
                trip.getFrom().forEach((key, transport) -> {
                    if (transport.getPlane() != null) {
                        if (transport.getPlane().getId().equals(connectionIdFrom)) {
                            int seatsLeft = transport.getPlane().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdFrom + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getPlane().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdFrom + "\n seats left:" + seatsLeft);
                        }
                    }

                    if (transport.getTrain() != null) {
                        if (transport.getTrain().getId().equals(connectionIdFrom)) {
                            int seatsLeft = transport.getTrain().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdFrom + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getTrain().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdFrom + "\n seats left:" + seatsLeft);
                        }
                    }
                });


                trip.getTo().forEach((key, transport) -> {
                    if (transport.getPlane() != null) {
                        if (transport.getPlane().getId().equals(connectionIdTO)) {
                            int seatsLeft = transport.getPlane().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdTO + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getPlane().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdTO + "\n seats left:" + seatsLeft);
                        }
                    }

                    if (transport.getTrain() != null) {
                        if (transport.getTrain().getId().equals(connectionIdTO)) {
                            int seatsLeft = transport.getTrain().getSeatsLeft();
                            logger.info("BEFORE: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdTO + "\n seats left:" + seatsLeft);
                            if (operationType.equals("add")) {
                                seatsLeft = seatsLeft + headCount;
                            } else {
                                seatsLeft = seatsLeft - headCount;
                            }
                            transport.getTrain().setSeatsLeft(seatsLeft);
                            repository.save(trip);
                            logger.info("NOW: TRIP: " + trip.getId() + "\n Connection ID:" + connectionIdTO + "\n seats left:" + seatsLeft);
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

                trip.getTo().forEach((key, transport) -> {
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

    public Trip getbyTripId(String id) {
        return repository.findByTripID(id);
    }

    public Trip getByOfferId(String id) {
        return repository.findByOfferID(id);
    }

    public List<Trip> getFilteredTrips(Integer adults, Integer kidsTo3Yo, Integer kidsTo10Yo, Integer kidsTo18Yo,
                                       Date dateFrom, Date dateTo, List<String> departureRegion,
                                       List<String> arrivalRegion, List<String> transport, String order, List<String> diet,
                                       Integer max_price){
        int head_count = 0;
        if (adults != null)
            head_count += adults;
        if (kidsTo3Yo != null)
            head_count += kidsTo3Yo;
        if (kidsTo10Yo != null)
            head_count += kidsTo10Yo;
        if (kidsTo18Yo != null)
            head_count += kidsTo18Yo;

        String room_type = getRoomTypeForPeople(head_count);
        String query = "{ \"hotel.rooms." + room_type + ".available\": { $gt: 0 }";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (dateFrom != null)
            query = query + ",date_from: { $gte: ISODate('" + formatter.format(dateFrom) + "') }";
        if (dateTo != null)
             query = query + ",date_to: { $lte: ISODate('" + formatter.format(dateTo) + "') }";

        query = query + " }";
        return repository.findTripsByCustomQuery(query);
    }


    public String getRoomTypeForPeople(Integer head_count){
        if (head_count == 1){
             return "studio";
        } else if (head_count == 2){
            return "small";
        } else if (head_count == 3) {
            return "medium";
        } else if (head_count == 4) {
            return "large";
        } else {
            return "apartment";
        }
    }

}
