package com.carrental.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import com.carrental.entity.CarRental;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class PdfGeneratorUtil {

    public ByteArrayInputStream generateReturnStatement(CarRental rental) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Car Return Statement", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Rental ID: " + rental.getId()));
            document.add(new Paragraph("User: " + rental.getUser().getUsername()));
            document.add(new Paragraph("Car: " + rental.getCar().getModel()));
            document.add(new Paragraph("Rental Date: " + rental.getRentalDate()));
            document.add(new Paragraph("Return Date: " + rental.getReturnDate()));
            document.add(new Paragraph("Rental Amount: ₹" + rental.getRentalAmount()));
            document.add(new Paragraph("Late Fee: ₹" + rental.getLateFee()));
            document.add(new Paragraph("Total: ₹" + (rental.getRentalAmount() + rental.getLateFee())));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error while creating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

