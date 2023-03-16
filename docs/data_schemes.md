# Schematy baz danych

## MAIN TOUR DB
# Hotels
| ID | Name    | LocationID |
|----|---------|------------|
| 1  | Hotel A | 4          |
| 2  | Hotel B | 8          |
| 3  | Hotel C | 2          |

# Room Types
| ID | Name      | Capacity | BaseCost |
|----|-----------|----------|----------|
| 1  | small     | 2        | 80       |
| 2  | medium    | 4        | 100      |
| 3  | large     | 6        | 120      |
| 4  | apartment | 4        | 110      |
| 5  | studio    | 1        | 70       |

# Rooms
| ID | HotelID | RoomTypeID |
|----|---------|------------|
| 1  | 1       | 1          |
| 2  | 1       | 2          |
| 3  | 2       | 3          |
| 4  | 2       | 1          |
| 5  | 3       | 4          |

# Bookings Table
| ID | ReservationID | RoomID | CheckInDate | CheckOutDate |
|----|---------------|--------|-------------|--------------|
| 1  | 3             | 1      | 2023-03-16  | 2023-03-20   |
| 2  | 3             | 3      | 2023-03-16  | 2023-03-20   |
| 3  | 2             | 2      | 2023-03-25  | 2023-03-30   |
| 4  | 5             | 2      | 2023-03-24  | 2023-03-26   |
| 5  | 9             | 3      | 2023-03-21  | 2023-03-27   |

# Transport
| ID | From     | To       | Kind  | Capacity | Cost |
|----|----------|----------|-------|----------|------|
| 1  | Gdańsk   | Warszawa | Train | 120      | 40   |
| 2  | Chicago  | New York | Train | 200      | 80   |
| 3  | Berlin   | New York | Plane | 80       | 5000 |
| 4  | Rome     | London   | Plane | 80       | 2000 |
| 5  | Helsinki | London   | Plane | 60       | 1500 |


