package uk.co.streefland.rhys.mapreduce.output;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.streefland.rhys.mapreduce.data.Flight;
import uk.co.streefland.rhys.mapreduce.framework.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Prints out the inventory and passenger list of each flight
 * @author Rhys Streefland
 * @version 1.0
 */
public class FlightInventory extends Output {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Pair> flights;

    public FlightInventory(List<Pair> flights) {
        this.flights = flights;
    }

    public void toConsole() {
        System.out.println("\nList of Flights");
        System.out.println("---------------");

        for (Pair pair : flights) {
            String flightId = (String) pair.getKey();
            Flight flight = (Flight) pair.getValue();

            System.out.println("Flight ID: " + flightId);
            System.out.println("Source airport IATA/FAA code: " + flight.getSourceAirportCode());
            System.out.println("Destination airport IATA/FAA code: " + flight.getDestinationAirportCode());
            System.out.println("Departure time: " + flight.getDepartureTime());
            System.out.println("Arrival time: " + flight.getArrivalTime());
            System.out.println("Flight Duration: " + flight.getDuration());

            System.out.println("Passenger list:");

            // Print each passenger
            for (String passenger : flight.getPassengers()) {
                System.out.println("  " + passenger);
            }

            System.out.print("\n\n");
        }
    }

    public void toTextFile() throws FileNotFoundException {

        String fileName = "output/flightslist.txt";
        logger.info("Writing list of flights to file: " + fileName);
        PrintWriter out = new PrintWriter(fileName);

        out.println("\nList of Flights");
        out.println("---------------");

        for (Pair pair : flights) {
            String flightId = (String) pair.getKey();
            Flight flight = (Flight) pair.getValue();

            out.println("Flight ID: " + flightId);
            out.println("Source airport IATA/FAA code: " + flight.getSourceAirportCode());
            out.println("Destination airport IATA/FAA code: " + flight.getDestinationAirportCode());
            out.println("Departure time: " + flight.getDepartureTime());
            out.println("Arrival time: " + flight.getArrivalTime());
            out.println("Flight Duration: " + flight.getDuration());

            out.println("Passenger list:");

            // Print each passenger
            for (String passenger : flight.getPassengers()) {
                out.println("  " + passenger);
            }

            out.println("");
        }

        out.close();
    }

    public void toPDFFile() throws DocumentException, FileNotFoundException {

        String fileName = "output/flightslist.pdf";
        logger.info("Writing list of flights to file: " + fileName);

        // Create and open the document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        document.add(getPDFTitle());

        Paragraph title1 = new Paragraph("List of Flights", FONT[1]);
        title1.setSpacingAfter(15);
        document.add(title1);

        // Each flight is represented by a separate table
        for (Pair pair : flights) {
            PdfPTable table = new PdfPTable(2);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setSpacingAfter(15);

            String flightId = (String) pair.getKey();
            Flight flight = (Flight) pair.getValue();

            table.addCell(new Phrase("Flight ID", FONT[2]));
            table.addCell(new Phrase(flightId, FONT[2]));
            table.addCell(new Phrase("Source airport IATA/FAA code"));
            table.addCell(new Phrase(flight.getSourceAirportCode()));
            table.addCell(new Phrase("Destination airport IATA/FAA code"));
            table.addCell(new Phrase(flight.getDestinationAirportCode()));
            table.addCell(new Phrase("Departure time"));
            table.addCell(new Phrase(flight.getDepartureTime()));
            table.addCell(new Phrase("Arrival time"));
            table.addCell(new Phrase(flight.getArrivalTime()));
            table.addCell(new Phrase("Flight duration"));
            table.addCell(new Phrase(flight.getDuration()));

            table.addCell(new Phrase("Passenger list"));

            // Sub-table for passengers of current flight
            PdfPTable passengerTable = new PdfPTable(2);
            passengerTable.getDefaultCell().setBorder(0);

            // Write each passenger
            for (String passenger : flight.getPassengers()) {
                passengerTable.addCell(new Phrase(passenger));
            }
            table.addCell(passengerTable);

            document.add(table);
        }

        document.add(getPDFFooter());
        document.close();
    }
}
