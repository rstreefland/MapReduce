package uk.co.streefland.rhys.mapreduce.output;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.streefland.rhys.mapreduce.data.Airport;
import uk.co.streefland.rhys.mapreduce.framework.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Prints out the number of flights from each airport
 * @author Rhys Streefland
 * @version 1.0
 */
public class AirportFlightCounter extends Output {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Pair> airports;

    public AirportFlightCounter(List<Pair> airports) {
        this.airports = airports;
    }

    public void toConsole() {
        List<String> unusedAirports = new ArrayList<>();

        System.out.println("\nFlights from each airport");
        System.out.println("-------------------------");

        for (Pair pair : airports) {
            Airport airport = (Airport) pair.getValue();

            if (airport.getFlightCount() == 0) {
                unusedAirports.add(airport.getAirportName());
            } else {
                System.out.println(airport.getAirportName() + ": " + airport.getFlightCount());
            }
        }

        System.out.println("\nUnused airports");
        System.out.println("---------------");

        for (String airport : unusedAirports) {
            System.out.println(airport);
        }
    }

    public void toTextFile() throws FileNotFoundException {

        String fileName = "output/airportflightcount.txt";
        logger.info("Writing airport flight count to file: " + fileName);
        PrintWriter out = new PrintWriter(fileName);

        List<String> unusedAirports = new ArrayList<>();

        out.println("\nFlights from each airport");
        out.println("-------------------------");

        for (Pair pair : airports) {
            Airport airport = (Airport) pair.getValue();

            if (airport.getFlightCount() == 0) {
                unusedAirports.add(airport.getAirportName());
            } else {
                out.println(airport.getAirportName() + ": " + airport.getFlightCount());
            }
        }

        out.println("");
        out.println("Unused airports");
        out.println("---------------");

        for (String airport : unusedAirports) {
            out.println(airport);
        }

        out.close();
    }

    public void toPDFFile() throws DocumentException, FileNotFoundException {
        List<String> unusedAirports = new ArrayList<>();

        String fileName = "output/airportflightcount.pdf";
        logger.info("Writing airport flight count to file: " + fileName);

        // Create and open the document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        document.add(getPDFTitle());

        Paragraph title1 = new Paragraph("Flights from each airport", FONT[1]);
        title1.setSpacingAfter(15);
        document.add(title1);

        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setSpacingAfter(15);

        // Write count corresponding to each airport
        for (Pair pair : airports) {
            Airport airport = (Airport) pair.getValue();

            if (airport.getFlightCount() == 0) {
                unusedAirports.add(airport.getAirportName());
            } else {
                table.addCell(new Phrase(airport.getAirportName()));
                table.addCell(new Phrase("" + airport.getFlightCount()));
            }
        }

        document.add(table);

        Paragraph title2 = new Paragraph("Unused airports", FONT[1]);
        title2.setSpacingAfter(15);
        document.add(title2);

        for (String airport : unusedAirports) {
            document.add(new Paragraph(airport));
        }

        document.add(Chunk.NEWLINE);
        document.add(getPDFFooter());
        document.close();
    }
}
