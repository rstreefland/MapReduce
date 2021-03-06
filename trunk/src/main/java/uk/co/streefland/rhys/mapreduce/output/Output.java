package uk.co.streefland.rhys.mapreduce.output;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parent output class that manages common dependencies and provides abstraction for each output class
 * @author Rhys Streefland
 * @version 1.0
 */
public abstract class Output {

    /**
     * An array of predefined fonts for the PDF output
     */
    static final Font[] FONT = new Font[4];
    static {
        FONT[0] = new Font(Font.FontFamily.HELVETICA, 24);
        FONT[1] = new Font(Font.FontFamily.HELVETICA, 18);
        FONT[2] = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        FONT[3] = new Font(Font.FontFamily.HELVETICA, 10);
    }

    /**
     * Generates the main title for the PDF reports
     * @return The title as a Paragraph object
     */
    Paragraph getPDFTitle() {
        Paragraph mainTitle = new Paragraph("PassengerAirlineFlights Report", FONT[0]);
        mainTitle.setSpacingAfter(15);
        return mainTitle;
    }

    /**
     * Generates a footer for the PDF reports
     * @return The footer as a paragraph object
     */
    Paragraph getPDFFooter() {
        Paragraph footer = new Paragraph("Report generated by PassengerAirlineFlights v1.0 on " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()), FONT[3]);
        footer.setSpacingAfter(15);
        return footer;
    }

    /**
     * Prints the job output to console
     */
    public abstract void toConsole();

    /**
     * Writes the job output to a text file
     * @throws FileNotFoundException
     */
    public abstract void toTextFile() throws FileNotFoundException;

    /**
     * Generates a PDF report using the job output
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public abstract void toPDFFile() throws DocumentException, FileNotFoundException;
}
