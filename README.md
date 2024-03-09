# FlyMile: Maximize Your Credit Card Points

## Overview
FlyMile is a robust Spring Boot application that empowers users to maximize their credit card points through efficient flight search options. The API interfaces with various airline databases/Scrapping to provide real-time, point-optimized flight data.

## Features
- **Flight Search**: Enables users to search for flights by origin, destination, Start date, end date and cabin preferences
- **FlyMile Range Search**: Flymile searches one way flights in given range from start date to end date.
- **Range Restrictions**:
-                         Start date MUST be from today to up-to 331 days in the future.
-                         End date must be up-to 7 days after start date , and should fall within 331 day range from today.
- 
- **Point Maximization**: Search Multiple airlines and Multiple days to help users find best buck for their credit card points.
- **Comprehensive Exception Handling**: Provides detailed handling of exceptions to ensure reliability and stability.

## Project Structure
- `src/main/java/flymile`: Contains the core application code.
  - `API`: Endpoints to access flight search and booking features.
  - `APIExceptions`: Defines custom exceptions for the API.
  - `ControllerAdvisor`: Manages global exception handling.
  - `DecompressorLogic`: Handles the decompression of API responses.
  - `Model`: Domain models for flights, pricing, etc.
  - `Controller`: Controllers for API request processing.
  - `Service`: Business logic services.
- `src/main/resources`: Configuration files and static resources.
- `src/test/java/flymile`: Tests for the application's functionality.

## Getting Started

### Prerequisites
- JDK 11+
- Maven

### Installation
1. Clone the repository:  git clone https://github.com/flymilesAU/FlyMile-API.git

2. Navigate to the project's root directory:  cd FlyMile-main
                                              cd backend
 
4. Build the application with Maven: mvn clean install
 
5. Execute the following Maven command: mvn spring-boot:run

The service will start on `http://localhost:8082`.

## Request Parameters

| Parameter       | Type    | Required | Description                                                                                             | Default |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------|---------|
| `departure`     | string  | Yes      | IATA code for departure airport. Must be among the 1627 supported airports.                             | N/A     |
| `arrival`       | string  | Yes      | IATA code for arrival airport. Must differ from `departure` and be among supported airports.            | N/A     |
| `startDate`     | date    | Yes      | Start date for flight search in `YYYY-MM-DD` format. Must be within 0-331 days from today.              | N/A     |
| `endDate`       | date    | Yes      | End date for flight  in `YYYY-MM-DD` format. be within 7 days post `startDate` and 331 days from today. | N/A     |
| `numPassengers` | integer | No       | Number of passengers, from 1 to 9.                                                                      | 1       |
| `upperCabin`    | boolean | No       | If true, only return Business and First-class flights                                                   | false   |

## Special Conditions

- When `upperCabin` is true but no Business or First-class flights are available, Economy and Premium Economy options will still be returned.

- `seatsRemaining` in `pricingDetail` with a value of 0 indicates undisclosed seat availability (There may be Seats). A positive integer represents the actual number of seats available.

- A `points` value of 0 in `pricingDetail` means there are no seats available for reward booking in that specific cabin class.


### Example Request

To search for flights from Montreal (YUL) to London Heathrow (LHR) for one passenger in economy class, departing [ June 14, 2024 , June 21, 2024], ( [ ] inclusive ) you would use the following URL:

http://localhost:8082/flights?departure=YUL&arrival=LHR&startDate=2024-06-14&endDate=2024-06-21&numPassengers=1&upperCabin=false

http://localhost:8082/flights?departure=YUL&arrival=LHR&startDate=2024-06-14&endDate=2024-06-21

Both URL's would return Same Response.
    

## Documentation
Additional documentation regarding the API endpoints and usage instructions can be found in the `API` directory of the source code.

### Key Validations Performed

- **Date Formats**: Validates that start and end dates are in the `YYYY-MM-DD` format.
- **Airport Codes**: Confirms that origin and destination airport codes are recognized and valid (1627 airports recognized currently).
- **Date Logic**: Ensures the start date is not in the past and is within 331 days from today,
                 and the end date is upTo 7 days ahead from start date and falls within 331 days from today.
- **Passenger Numbers**: Checks that the number of passengers is within an acceptable range (e.g., 1-9 passengers).

### Exception Handling

- `InvalidDateFormatException`: Thrown if either date does not match the expected format.
    {"status":"BAD_REQUEST","message":"Invalid date format, expected yyyy-MM-dd."}

  
- `OriginAirportInvalidException`: Thrown if the origin airport code is not in the list of known airports.
    {"status":"BAD_REQUEST","message":"The origin airport code is invalid or not supported."}

  
- `DestinationAirportInvalidException`: Thrown if the destination airport code is not in the list of known airports.
  {"status":"BAD_REQUEST","message":"The destination airport code is invalid or not supported."}

  
- `StartDateOutsideRangeException`: Thrown if the start date is not within the specified booking window.
  {"status":"BAD_REQUEST","message":"Start date must be from today to 331 days in the future"}

  
- `OriginDestinationRequiredException`: Thrown if either the origin or destination is not provided.
  {"status":"BAD_REQUEST","message":"Origin and destination must be provided."}

  
- `OriginDestinationSameException`: Thrown if the origin and destination are identical.
   {"status":"BAD_REQUEST","message":"Origin and destination must be different."}

  
- `PassengersNumberInvalidException`: Thrown if the number of passengers is outside the specified range.
  {"status":"BAD_REQUEST","message":"Number of passengers must be between 1 and 9."}

- `EndDateOutsideRangeException`: Thrown if the end date is not within a logical timeframe relative to the start date.
   {"status":"BAD_REQUEST","message":"End date must be up to 7 days after start date and fall within 331 days from today."}

  

### Usage

The `InputValidation` class is utilized internally by the FlyMile application to validate search parameters before processing flight searches. This ensures that all flight searches performed by the system are based on valid and logical input, reducing errors and improving user experience.
List<List<Flights>>
inner List id flights by each Date
  if no flights this date , this date is not included in the resulted List
For developers integrating with the FlyMile API, it is important to format and validate input data according to the rules described above to avoid exceptions and ensure a successful API request.

IF NO FLIGHT FOR ALL DATES BETWEEN START DATE AND END DATE :            RETURN []

### Sample Flight/Slice Object

```json
{
  // Total flight duration in minutes (sum of durations of all legs + sum of duration of all connection times)
  "duration": 1140,
  
  // Local Time (if in city A, that city's Time)
  "arrivalDateTime": "2024-06-14T20:25:00.000+01:00",
  
  // Local Time (if in city A, that city's Time)
  "departureDateTime": "2024-06-14T03:25:00.000+03:00",
  
  // If arrives same day then 0, if arrives next day then 1, if arrives next next day then 2 and so on.
  "arrivesNextDay": 0,
  
  // Details of pricing for different product types
  "pricingDetail": [
    {
      // Sould be displayed as 20k : if 47500 then 47.5K
      "points": 20000,
      
      // Prices in USD
      "cashPrice": 75.33,
      
      // COACH/ECONOMY/MAIN all means Economy
      "productType": "COACH",
      
      // Number of seats remaining
      "seatsRemaining": 2
    },
    {
      // If points = 0, means NO business class cabin on this particular flight.
      "points": 0,
      "cashPrice": 0,
      "productType": "BUSINESS",
      "seatsRemaining": 0
    },
    // You can see Premium Economy is Not displayed, WHY? because none of flights this DATE included Premium Economy.
    {
      "points": 47500,
      "cashPrice": 75.33,
      "productType": "FIRST",
      "seatsRemaining": 0 // Number of Seats not disclosed
    }
  ],
  
  // Details of each leg of the journey
  "legs": [
    {
      // Type of aircraft
      "aircraft": "Boeing 787-8",
      
      // Carrier code
      "carrierCode": "QR",
      
      // Flight number
      "flightNumber": "1183",
      
      // Local arrival time of this leg
      "arrivalDateTime": "2024-06-14T05:55:00.000+03:00",
      
      // Flight JED - DOH - LHR (555 minutes stop in DOHA)
      "connectionTimeInMinutes": 555,
      
      // Local departure time of this leg
      "departureDateTime": "2024-06-14T03:25:00.000+03:00",
      
      // Whether seats are fully convertible to bed
      "lieFlat": false,
      
      // Destination airport code
      "destination": "DOH",
      
      // Total flight time for this leg
      "durationInMinutes": 150,
      
      // Origin airport code
      "origin": "JED"
    },
    {
      // Type of aircraft
      "aircraft": "Boeing 777-300ER Passenger",
      
      // Carrier code
      "carrierCode": "QR",
      
      // Flight number
      "flightNumber": "15",
      
      // Local arrival time of this leg
      "arrivalDateTime": "2024-06-14T20:25:00.000+01:00",
      
      // AS this is the final leg, thus no connection
      "connectionTimeInMinutes": 0,
      
      // Local departure time of this leg
      "departureDateTime": "2024-06-14T15:10:00.000+03:00",
      
      // Whether seats are fully convertible to bed
      "lieFlat": false,
      
      // Destination airport code
      "destination": "LHR",
      
      // Total flight time for this leg
      "durationInMinutes": 435,
      
      // Origin airport code
      "origin": "DOH"
    }
  ]
}
```


## Contact
For queries or further assistance, reach out to the FlyMile team at flyMilesAu@gmail.com.

## Acknowledgments
- Thanks to all contributors and maintainers of this project.
- Appreciation for the external APIs and services that this project utilizes.

