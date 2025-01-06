import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/Payments.css";

function Payments() {
  const [appointments, setAppointments] = useState([]);
  const [isFetching, setIsFetching] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false); // New state for payment success
  const username = localStorage.getItem("username");

  // Fetch all confirmed appointments
  const fetchAppointments = async () => {
    try {
      setIsFetching(true);
      const response = await axios.get(`/${username}/patient/viewConfirmedAppointments`);
      setAppointments(response.data.length === 0 ? [] : response.data);
    } catch (error) {
      console.error("Error fetching appointments:", error);
      alert("Failed to fetch appointments. Please try again.");
    } finally {
      setIsFetching(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  const handlePayNow = async (appointmentId) => {
    try {
      // Call the backend to initiate payment
      const response = await axios.post(`/${username}/payments/initiate/${appointmentId}`);
      
      // Fetch the order ID and amount from the response
      const { orderId, amount } = response.data;
  
      // Configure Razorpay payment options
      const options = {
        key: "rzp_test_A3WY0WOJLejK3e", // Razorpay test key
        amount: amount, // Amount in paise, replace with actual appointment fee
        currency: "INR",
        order_id: orderId,
        handler: async function (response) {
          const payment_id = response.razorpay_payment_id;
          // Verify payment after the user completes payment
          await verifyPayment(appointmentId, payment_id);
        },
        theme: {
          color: "#F37254",
        },
      };
  
      // Open Razorpay payment modal
      const razorpay = new window.Razorpay(options);
      razorpay.open();
    } catch (error) {
      console.error("Error initiating payment:", error);
      alert("Failed to initiate payment.");
    }
  };

  const verifyPayment = async (appointmentId, paymentId) => {
    try {
      // Call the backend to verify the payment
      const response = await axios.post(`/${username}/payments/verify/${appointmentId}?paymentId=${paymentId}`);
      
      setPaymentSuccess(true); // Show success message on successful payment verification
      setTimeout(() => {
        setPaymentSuccess(false); // Hide success message after a few seconds
      }, 3000);

      fetchAppointments(); // Reload appointments after payment verification
    } catch (error) {
      console.error("Error verifying payment:", error);
      alert("Payment verification failed.");
    }
  };

  const handleViewInvoice = async (appointmentId) => {
    try {
      const response = await axios.get(
        `/${username}/payments/generateInvoice/${appointmentId}`,
        { responseType: "arraybuffer" }
      );
      const pdfBlob = new Blob([response.data], { type: "application/pdf" });
      const pdfUrl = URL.createObjectURL(pdfBlob);
      
      // Open the PDF in a new tab
      window.open(pdfUrl, "_blank");
    } catch (error) {
      console.error("Error viewing invoice:", error);
      alert("Failed to generate invoice.");
    }
  };

  const formatDateTime = (dateTime) => {
    const [date, time] = dateTime.split("T");
    return { date, time };
  };

  return (
    <body className="payments-page">
      <div className="payments-page-container">
        <h2>Payments</h2>
        
        {/* Display Success Message */}
        {paymentSuccess && (
          <div className="success-message show">
            <div className="success-icon">✓</div>
            <p>Payment Successful</p>
          </div>
        )}
        
        {isFetching ? (
          <p>Loading appointments...</p>
        ) : appointments.length === 0 ? (
          <p>No confirmed appointments found</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Appointment ID</th>
                <th>Doctor Name</th>
                <th>Appointment Date</th>
                <th>Appointment Time</th>
                <th>Payment Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {appointments.map((appointment) => {
                const { date, time } = formatDateTime(appointment.appointmentDateTime);
                return (
                  <tr key={appointment.appointmentId}>
                    <td>{appointment.appointmentId}</td>
                    <td>Dr. {appointment.doctor.name}</td>
                    <td>{date}</td>
                    <td>{time}</td>
                    <td>{appointment.paid ? "Paid" : "Unpaid"}</td>
                    <td>
                      {appointment.paid ? (
                        <button className="invoice" onClick={() => handleViewInvoice(appointment.appointmentId)}>
                          View Invoice
                        </button>
                      ) : (
                        <button className="pay" onClick={() => handlePayNow(appointment.appointmentId)} disabled={isProcessing}>
                          Pay Now
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </body>
  );
}

export default Payments;
