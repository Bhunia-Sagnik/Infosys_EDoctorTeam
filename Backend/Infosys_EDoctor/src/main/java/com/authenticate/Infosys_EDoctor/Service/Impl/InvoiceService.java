package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {

    @Autowired
    AppointmentService appointmentService;

    private Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    public ByteArrayOutputStream generateInvoice(Long appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        logger.info("Started creating invoice for appointment ID: {}", appointmentId);

        // Sample Data (Replace this with actual fetched details)
        String title = "Appointment Invoice";
        String website = "E-Doctor Healthcare Services";
        String doctorName = "Dr. " + appointment.getDoctor().getName();
        String patientName = appointment.getPatient().getName();
        String appointmentDate = appointment.getAppointmentDateTime().toLocalDate().toString();
        String appointmentTime = appointment.getAppointmentDateTime().toLocalTime().toString();
        String hospitalName = appointment.getDoctor().getHospitalName();
        String location = appointment.getDoctor().getLocation();
        double consultationFee = appointment.getDoctor().getChargedPerVisit();
        double convenienceFee = consultationFee * 0.1;
        double totalAmount = consultationFee + convenienceFee;
        String paymentId = appointment.getPaymentId();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);

            // Hospital Details
            Font websiteFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph websiteDetails = new Paragraph(website + "\nYour one stop outpatient appointment service", websiteFont);
            websiteDetails.setAlignment(Element.ALIGN_CENTER);
            websiteDetails.setSpacingAfter(20);
            document.add(websiteDetails);

            // Appointment Details Section
            Font sectionHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph appointmentHeader = new Paragraph("Appointment Details", sectionHeaderFont);
            appointmentHeader.setSpacingAfter(10);
            document.add(appointmentHeader);

            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph appointmentDetails = new Paragraph(
                    "Appointment ID: " + appointmentId + "\n" +
                            "Doctor: " + doctorName + "\n" +
                            "Patient: " + patientName + "\n" +
                            "Date: " + appointmentDate + "\n" +
                            "Time: " + appointmentTime + "\n" +
                            "Hospital: " + hospitalName + "\n" +
                            "Location: " + location + "\n",
                    contentFont
            );
            appointmentDetails.setSpacingAfter(20);
            document.add(appointmentDetails);

            // Fee Details Section with Table
            Paragraph feeHeader = new Paragraph("Fee Details", sectionHeaderFont);
            feeHeader.setSpacingAfter(10);
            document.add(feeHeader);

            // Create Fee Table
            PdfPTable feeTable = new PdfPTable(2); // Two columns
            feeTable.setWidthPercentage(100);
            feeTable.setSpacingBefore(10);

            // Table Headers
            PdfPCell header1 = new PdfPCell(new Phrase("Description", contentFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Amount (Rs.)", contentFont));
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            feeTable.addCell(header1);
            feeTable.addCell(header2);

            // Table Rows
            feeTable.addCell(new PdfPCell(new Phrase("Consultation Fee", contentFont)));
            PdfPCell consultationFeeCell = new PdfPCell(new Phrase(String.valueOf(consultationFee), contentFont));
            consultationFeeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            feeTable.addCell(consultationFeeCell);

            feeTable.addCell(new PdfPCell(new Phrase("Convenience Fee (10%)", contentFont)));
            PdfPCell convenienceFeeCell = new PdfPCell(new Phrase(String.valueOf(convenienceFee), contentFont));
            convenienceFeeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            feeTable.addCell(convenienceFeeCell);

            feeTable.addCell(new PdfPCell(new Phrase("Total Amount", contentFont)));
            PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.valueOf(totalAmount), contentFont));
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            feeTable.addCell(totalAmountCell);

            // Payment ID Row
            PdfPCell paymentIdCell1 = new PdfPCell(new Phrase("Payment ID: " + paymentId, contentFont));
            paymentIdCell1.setColspan(2); // Align across the first column
            feeTable.addCell(paymentIdCell1);

            // Add Fee Table to Document
            document.add(feeTable);


            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));


            // Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
            // Footer Section at Bottom of Page
            Paragraph footer = new Paragraph("Thank you for choosing E-Doctor. Stay healthy! " +
                    "\n For enquiries contact us at support@gmail.com", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);

            document.add(footer);

        } catch (Exception e) {
            logger.error("Error while generating invoice", e);
        } finally {
            document.close();
        }

        logger.info("Invoice created successfully for appointment ID: {}", appointmentId);
        return out;
    }
}
