package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Repository.AppointmentRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.razorpay.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private InvoiceService invoiceService;

    private final RazorpayClient razorpayClient;

    public PaymentService() throws RazorpayException {
        this.razorpayClient = new RazorpayClient("rzp_test_A3WY0WOJLejK3e", "i7MaH2mMDECRFBTQ8LANbNmT");
    }

    public Map<String, Object> initiatePayment(Long appointmentId) throws Exception {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isEmpty()) {
            throw new IllegalArgumentException("Invalid appointment ID: " + appointmentId);
        }

        Appointment appointment = optionalAppointment.get();
        if (appointment.getStatus() != Appointment.Status.Confirmed) {
            throw new IllegalArgumentException("Payment can only be made for confirmed appointments");
        }

        int doctorFees = appointment.getDoctor().getChargedPerVisit();
        int amountInPaise = doctorFees * 100 + doctorFees * 10;

        // Initiating the payment
        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise); // Amount in paise
        options.put("currency", "INR");
        options.put("receipt", "appt_" + appointmentId);

        // Create the Razorpay order
        Order order = razorpayClient.Orders.create(options);
        String orderId = order.get("id");
        System.out.println("Order ID: " + orderId); // Log the order ID for debugging

        // Set the payment ID in the appointment and save
        appointment.setPaymentId(orderId);
        appointmentRepository.save(appointment);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("orderId", orderId);
        responseMap.put("amount", amountInPaise);

        return responseMap;
    }

    public String verifyPayment(Long appointmentId, String paymentId) throws Exception {
        // Fetch the appointment using appointmentId
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        // Fetch the payment details using Razorpay API
        Payment payment = razorpayClient.Payments.fetch(paymentId);

        // Check if the payment was captured successfully
        if ("captured".equals(payment.get("status").toString())) {
            appointment.setPaid(true);

            ByteArrayOutputStream pdfStream = invoiceService.generateInvoice(appointmentId);

            appointment.setInvoicePdf(pdfStream.toByteArray());

            appointmentRepository.save(appointment);

            return "Payment captured and verified successfully for appointment ID: " + appointmentId
                    + "\nGo to Payments page to download invoice";
        } else {
            throw new IllegalArgumentException("Payment not captured or failed for payment ID: " + paymentId);
        }
    }

    public ByteArrayInputStream getInvoice(Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        byte[] pdfByte = appointment.getInvoicePdf();

        return new ByteArrayInputStream(pdfByte);
    }
}
