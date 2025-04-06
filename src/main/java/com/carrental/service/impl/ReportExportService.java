package com.carrental.service.impl;

import com.carrental.dto.CarReportDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Service
public class ReportExportService {

    public void exportToPDF(List<CarReportDTO> cars, HttpServletResponse response) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
        } catch (DocumentException e) {
            throw new IOException("Error creating PDF document", e);
        }

        document.open();

        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD);
        Paragraph title = new Paragraph("Car Rental Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.addCell("ID");
        table.addCell("Make");
        table.addCell("Model");
        table.addCell("Year");
        table.addCell("Available");

        for (CarReportDTO car : cars) {
            table.addCell(String.valueOf(car.getCarId()));
            table.addCell(car.getMake());
            table.addCell(car.getModel());
            table.addCell(String.valueOf(car.getYear()));
            table.addCell(car.isAvailable() ? "Yes" : "No");
        }

        document.add(table);
        document.close();
    }

    public void exportToExcel(List<CarReportDTO> cars, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cars");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Make");
        header.createCell(2).setCellValue("Model");
        header.createCell(3).setCellValue("Year");
        header.createCell(4).setCellValue("Available");

        int rowNum = 1;
        for (CarReportDTO car : cars) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(car.getCarId());
            row.createCell(1).setCellValue(car.getMake());
            row.createCell(2).setCellValue(car.getModel());
            row.createCell(3).setCellValue(car.getYear());
            row.createCell(4).setCellValue(car.isAvailable() ? "Yes" : "No");
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
