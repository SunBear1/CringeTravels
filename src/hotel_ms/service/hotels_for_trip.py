import logging

from service.errors import UnprocessableEntityError
from sql.postgresql_client import PostgreSQLClient

logger = logging.getLogger("hotels")


def get_hotel_for_offer(trip_offer_id: str) -> str:
    hotel_id_query = f"SELECT HotelID FROM Offers WHERE TripOfferID='{trip_offer_id}';"

    pg_client = PostgreSQLClient()
    hotel_query = pg_client.execute_query_for_database(query=hotel_id_query)

    if not hotel_query:
        raise UnprocessableEntityError(f"Trip offer with ID {trip_offer_id} does not exist.")
    logger.info(f"Hotel ID of the offer {trip_offer_id} is {hotel_query[0][0]}.")
    return hotel_query[0][0]


def get_offers_for_hotel(hotel_id: str):
    trip_offer_id_query = f"SELECT TripOfferID FROM Offers WHERE HotelID = '{hotel_id}';"

    pg_client = PostgreSQLClient()
    offers_query = pg_client.execute_query_for_database(query=trip_offer_id_query)

    result = [offer[0] for offer in offers_query]
    logger.info(f"Other trip offers for {hotel_id} are {result}.")
    return result


def update_left_rooms_in_hotel(hotel_id: str, room_type: str, operation: str, rooms_amount: int):
    set_part_of_query = f"{room_type}roomsleft = {room_type}roomsleft - {rooms_amount}"
    if operation == "add":
        set_part_of_query = f"{room_type}roomsleft = {room_type}roomsleft + {rooms_amount}"

    update_rooms_query = f"UPDATE RoomsLeft SET {set_part_of_query} WHERE HotelID='{hotel_id}';"

    pg_client = PostgreSQLClient()
    pg_client.execute_query_for_database(query=update_rooms_query, fetch_data=False)


def check_if_hotel_booked_up(hotel_id: str) -> bool:
    check_hotel_booked_up_query = f"SELECT HotelID FROM RoomsLeft WHERE HotelID='{hotel_id}' AND smallroomsleft=0 AND mediumroomsleft=0 AND largeroomsleft=0 AND apartmentroomsleft=0 AND studioroomsleft=0;"

    pg_client = PostgreSQLClient()
    hotel_booked_up_query = pg_client.execute_query_for_database(query=check_hotel_booked_up_query)

    result = True if hotel_booked_up_query else False
    return result


def get_number_of_rooms_left(hotel_id: str, room_type: str) -> int:
    number_of_rooms_left_query = f"SELECT {room_type}roomsleft FROM RoomsLeft WHERE HotelID='{hotel_id}';"

    pg_client = PostgreSQLClient()
    rooms_left_query = pg_client.execute_query_for_database(query=number_of_rooms_left_query)

    return rooms_left_query[0][0]
