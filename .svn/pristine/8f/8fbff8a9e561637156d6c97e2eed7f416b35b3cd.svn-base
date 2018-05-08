package uk.co.streefland.rhys.mapreduce.output;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.streefland.rhys.mapreduce.data.Airport;
import uk.co.streefland.rhys.mapreduce.data.Flight;
import uk.co.streefland.rhys.mapreduce.framework.Mapper;
import uk.co.streefland.rhys.mapreduce.framework.Pair;
import uk.co.streefland.rhys.mapreduce.framework.Reducer;
import uk.co.streefland.rhys.mapreduce.framework.Shuffler;
import uk.co.streefland.rhys.mapreduce.utility.DistanceCalculator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Calculates the distance travelled by each flight and each passenger in nautical miles. Prints out both lists
 * @author Rhys Streefland
 * @version 1.0
 */
public class MileageCounter extends Output {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Pair> airports;
    private final List<Pair> flightMileage;
    private final List<Pair> passengerMileage;

    public MileageCounter(List<Pair> flights, List<Pair> airports) {
        this.airports = airports;

        logger.debug("Mapping flight mileage");
        MileageMapper mileageMapper = new MileageMapper();
        flightMileage = mileageMapper.executeFlightMapper(flights);

        logger.debug("Mapping passenger mileage");
        mileageMapper = new MileageMapper();
        List<Pair> mappedPassengerMileage = mileageMapper.executePassengerMapper(flights);

        logger.debug("Shuffling passenger mileage");
        Shuffler passengerMileageShuffler = new Shuffler();
        List<Pair<String, List>> shuffledPassengerMileage = passengerMileageShuffler.execute(mappedPassengerMileage);

        logger.debug("Reducing passenger mileage");
        Reducer passengerMileageReducer = new PassengerMileageReducer();
        passengerMileage = passengerMileageReducer.execute(shuffledPassengerMileage);
    }

    /**
     * Prints the output to the console
     */
    public void toConsole() {
        System.out.println("\nNautical miles of each flight");
        System.out.println("-----------------------------");

        for (Pair pair : flightMileage) {
            String flightCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();

            System.out.println(flightCode + ": " + miles);
        }

        System.out.println("\nNautical miles travelled by each passenger");
        System.out.println("------------------------------------------");

        for (Pair pair : passengerMileage) {
            String passengerCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();
            System.out.println(passengerCode + ": " + miles);
        }
    }

    public void toTextFile() throws FileNotFoundException {

        String fileName = "output/mileage.txt";
        logger.info("Writing flight and passenger mileage to file:  " + fileName);
        PrintWriter out = new PrintWriter(fileName);

        out.println("\nNautical miles of each flight");
        out.println("-----------------------------");

        for (Pair pair : flightMileage) {
            String flightCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();

            out.println(flightCode + ": " + miles);
        }

        out.println("");
        out.println("Nautical miles travelled by each passenger");
        out.println("------------------------------------------");

        for (Pair pair : passengerMileage) {
            String passengerCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();
            out.println(passengerCode + ": " + miles);
        }

        out.close();
    }

    public void toPDFFile() throws DocumentException, FileNotFoundException {

        String fileName = "output/mileage.pdf";
        logger.info("Writing flight and passenger mileage to file: " + fileName);

        // Create and open the document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        document.add(getPDFTitle());

        Paragraph title1 = new Paragraph("Nautical miles of each flight", FONT[1]);
        title1.setSpacingAfter(15);
        document.add(title1);

        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setSpacingAfter(15);

        // Write flight mileage
        for (Pair pair : flightMileage) {
            String flightCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();

            table.addCell(new Phrase(flightCode));
            table.addCell(new Phrase("" + miles));
        }
        document.add(table);

        Paragraph title2 = new Paragraph("Nautical miles travelled by each passenger", FONT[1]);
        title2.setSpacingAfter(15);
        document.add(title2);

        PdfPTable table2 = new PdfPTable(2);
        table2.setHorizontalAlignment(Element.ALIGN_LEFT);

        // Write passenger mileage
        for (Pair pair : passengerMileage) {
            String passengerCode = (String) pair.getKey();
            Double miles = (Double) pair.getValue();

            table2.addCell(new Phrase(passengerCode));
            table2.addCell(new Phrase("" + miles));
        }

        document.add(table2);
        document.add(getPDFFooter());
        document.close();
    }

    /**
     * Maps the mileage for each flight or passenger.
     * Overrides the execute method of Mapper to provide a more specific implementation
     */
    private class MileageMapper extends Mapper {

        /**
         * Maps the mileage of each flight
         * @param input MapReduced list of flights
         * @return List of flightCode, distance pairs
         */
        private List<Pair> executeFlightMapper(List<Pair> input) {
            // Map for each flight
            for (Pair pair : input) {
                Future future = executorService.submit(() -> map(pair));
                futures.add(future);
            }

            collectResults(output);
            return output;
        }

        /**
         * Maps the mileage of each passenger
         * @param input MapReduced list of flights
         * @return List of passenger, distance pairs
         */
        private List<Pair> executePassengerMapper(List<Pair> input) {
            for (Pair pair : input) {

                Flight flight = (Flight) pair.getValue();

                // Map for each passenger of each flight
                for (String passenger : flight.getPassengers()) {
                    Future future = executorService.submit(() -> map(new Pair<>(passenger, flight)));
                    futures.add(future);
                }
            }

            collectResults(output);
            return output;
        }

        private Pair map(Pair pair) {

            Flight flight = (Flight) pair.getValue();

            Airport sourceAirport = null;
            Airport destinationAirport = null;

            // Match airportCode with corresponding airport in MapReduced airports list
            for (Pair a : airports) {

                Airport airport = (Airport) a.getValue();

                if (flight.getSourceAirportCode().equals(airport.getAirportCode())) {
                    sourceAirport = airport;
                }

                if (flight.getDestinationAirportCode().equals(airport.getAirportCode())) {
                    destinationAirport = airport;
                }
            }

            // Calculate the distance
            double distance = DistanceCalculator.calculate(sourceAirport.getLatitude(), sourceAirport.getLongitude(), destinationAirport.getLatitude(), destinationAirport.getLongitude());

            return new Pair<>(pair.getKey(), distance);
        }

        /**
         * Not used for this mapper
         */
        public Pair map(String input) {
            return null;
        }
    }

    /**
     * Reduces the miles travelled by each passenger on each flight into the total mileage travelled by each passenger
     */
    public class PassengerMileageReducer extends Reducer {

        public Pair reduce(Pair<String, List> input, List<Pair<String, List>> shuffled) {

            List distances = input.getValue();
            double totalDistance = 0;

            // sum up each distance into one total
            for (Object object : distances) {
                Double distance = (Double) object;
                totalDistance = totalDistance + distance;
            }

            return new Pair<>(input.getKey(), totalDistance);
        }
    }
}
