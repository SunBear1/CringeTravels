package messageHandlers;

import DTO.LiveEventsDTO;
import config.Config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import database.DatabaseHandler;
import events.ReservationEvent;

import java.io.IOException;
import java.net.ConnectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeoutException;

public class LiveEventsHandler implements Runnable {
    private final static String EXCHANGE = "live-events";
    private final static String ROUTING_KEY = "live-events-for-gateway";
    private DatabaseHandler databaseHandler;
    private Channel channel;

    public LiveEventsHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        Config.setConfigFactory(factory);
        try (com.rabbitmq.client.Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            this.channel = channel;

            System.out.println("Connection to RabbitMQ with LiveEvents established.");

            while (true) {
                ;
            }

        } catch (ConnectException e) {
            System.err.println("Error: Connection refused. Make sure RabbitMQ is running.");
        } catch (TimeoutException e) {
            System.err.println("Error: Connection timeout occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: I/O exception occurred.");
            e.printStackTrace();
        }
    }

    private String extractTransportType(String input) {
        Pattern pattern = Pattern.compile("-(TRAIN|PLANE)-");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String extracted = matcher.group(1);
            return extracted.toLowerCase();
        }

        return null;
    }

    public void sendMessage(ReservationEvent reservationEvent) {
        String trip_offer_id = reservationEvent.getTrip_offer_id();
        String room_type = reservationEvent.getRoom_type();
        String connection_to = reservationEvent.getConnection_id_to();

        LiveEventsDTO liveEventDTO = databaseHandler.getTripbyId(trip_offer_id);
        System.out.println(liveEventDTO.getCountry());
        liveEventDTO.setRoomType(room_type);
        liveEventDTO.setTransportType(extractTransportType(connection_to));
        System.out.println(liveEventDTO.getTransportType());
        databaseHandler.saveLiveEventsDTO(liveEventDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = objectMapper.writeValueAsString(liveEventDTO);
            System.out.println("UGABUGA");
            channel.basicPublish(EXCHANGE, ROUTING_KEY, null, json.getBytes());
            System.out.println("[MQ PUBLISH] Published message to LiveEvents exchange " +
                    ROUTING_KEY + " with payload: " + json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
