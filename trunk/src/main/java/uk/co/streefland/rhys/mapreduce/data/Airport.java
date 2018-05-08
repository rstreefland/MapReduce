package uk.co.streefland.rhys.mapreduce.data;

/**
 * Represents a single airport and stores its attributes
 * @author Rhys Streefland
 * @version 1.0
 */
public class Airport {

    private final String airportName;
    private final String airportCode;
    private final double latitude;
    private final double longitude;
    private final int flightCount;

    public Airport(String airportName, String airportCode, String latitude, String longitude, int flightCount) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
        this.flightCount = flightCount;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFlightCount() {
        return flightCount;
    }
}
