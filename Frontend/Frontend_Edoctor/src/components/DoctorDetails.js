import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "../services/api";
import "../CSS/DoctorDetails.css";  // Add appropriate CSS file

function DoctorDetails() {
  const { doctorId } = useParams(); // Fetch doctorId from URL
  const [doctor, setDoctor] = useState(null);
  const [availableDates, setAvailableDates] = useState([]);
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [reason, setReason] = useState(""); // New state for the reason
  const [isBooking, setIsBooking] = useState(false); // New state for loading
  const [avgRating, setAvgRating] = useState(null); // New state for average rating
  const [errors, setErrors] = useState({}); // New state for form errors
  const username = localStorage.getItem("username");

  // Form validation logic
  const validateForm = () => {
    const newErrors = {};
    const currentDateTime = new Date();

    // 1. Validate Appointment Date (Check if date is entered)
    if (!appointmentDate) {
      newErrors.appointmentDate = "Appointment date is required.";
    } else {
      // 2. Validate Appointment Time (Check if time is entered)
      if (!appointmentTime) {
        newErrors.appointmentTime = "Appointment time is required.";
      } else {
        // 3. Check if Appointment Date is greater than or equal to current date
        const appointmentDateTime = new Date(appointmentDate);
        
        if (appointmentDateTime <= currentDateTime) {
          newErrors.appointmentDate = "Appointment date must be in the future.";
        }
      }
    }

    // Validate Reason
    if (!reason.trim()) {
      newErrors.reason = "Reason for appointment is required.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Fetch doctor details
  const fetchDoctorDetails = async () => {
    try {
      const response = await axios.get(`/${username}/patient/getDoctorById?doctorId=${doctorId}`);
      setDoctor(response.data);
    } catch (error) {
      console.error("Error fetching doctor details:", error);
      alert("Failed to fetch doctor details.");
    }
  };

  // Fetch doctor availability
  const fetchDoctorAvailability = async () => {
    try {
      const response = await axios.get(`/${username}/patient/doctorAvailableDates?doctorId=${doctorId}`);
      setAvailableDates(response.data);
    } catch (error) {
      console.error("Error fetching available dates:", error);
      alert("Failed to fetch doctor availability.");
    }
  };

  // Fetch doctor's average rating
  const fetchDoctorRating = async () => {
    try {
      const response = await axios.get(`/${username}/feedback/doctorAvg/${doctorId}`);
      setAvgRating(response.data);
    } catch (error) {
      console.error("Error fetching doctor rating:", error);
      alert("Failed to fetch doctor rating.");
    }
  };

  // Handle appointment submission
  const handleAppointmentSubmit = async () => {
    if (!validateForm()) return; // Validate the form before proceeding

    const appointmentDateTime = `${appointmentDate}T${appointmentTime}`;

    // Appointment payload
    const appointment = {
      doctorId,
      appointmentDateTime,
      reason,
    };

    // Start booking
    setIsBooking(true);

    try {
      const response = await axios.post(`/${username}/patient/makeAppointment`, appointment);
      alert("Appointment scheduled successfully!");
      // Reset form fields after successful appointment
      setAppointmentDate("");
      setAppointmentTime("");
      setReason("");
    } catch (error) {
      console.error("Error scheduling appointment:", error);
      alert("Failed to schedule appointment. Please try again.");
    } finally {
      // End booking
      setIsBooking(false);
    }
  };

  // Render stars based on the rating
  const renderStars = (rating) => {
    if (rating === 0) {
      return "No feedback available";
    }
  
    const fullStars = Math.floor(rating);
    const halfStars = rating % 1 !== 0;
    const emptyStars = 5 - Math.ceil(rating);
  
    let stars = "";
    for (let i = 0; i < fullStars; i++) {
      stars += "★";
    }
    if (halfStars) {
      stars += "✩"; // Half star
    }
    for (let i = 0; i < emptyStars; i++) {
      stars += "☆";
    }
  
    return stars;
  };

  useEffect(() => {
    fetchDoctorDetails();
    fetchDoctorAvailability();
    fetchDoctorRating();
  }, [doctorId]);

  return (
    <body className="doctor-details">
      <div className="doctor-details-page">
        {doctor && (
          <>
            <div className="doctor-details-section">
              <h2>Dr. {doctor.name}</h2>
              <p><strong>Specialization:</strong> {doctor.specialization}</p>
              <p><strong>Location:</strong> {doctor.location}</p>
              <p><strong>Hospital:</strong> {doctor.hospitalName}</p>
              <p><strong>Charge Per Visit:</strong> ₹{doctor.chargedPerVisit}</p>
              <p className="feedback-stars">{avgRating !== 0 ? renderStars(avgRating) : "Loading rating..."}</p>

              <h3>Available Dates: </h3>
              {availableDates.length > 0 ? (
                <ul>
                  {availableDates.map((date) => (
                    <li key={date.availabilityId}>
                      {new Date(date.fromDate).toLocaleDateString()} to {new Date(date.endDate).toLocaleDateString()}
                    </li>
                  ))}
                </ul>
              ) : (
                <p>No available dates.</p>
              )}
            </div>

            <div className="book-appointment">
              <h3>Schedule Appointment</h3>
              <input
                type="date"
                value={appointmentDate}
                onChange={(e) => setAppointmentDate(e.target.value)}
                disabled={isBooking} // Disable input during booking
                className={errors.appointmentDate ? "validation-error" : ""}
              />
              {errors.appointmentDate && <span className="error">{errors.appointmentDate}</span>}
              
              <input
                type="time"
                value={appointmentTime}
                onChange={(e) => setAppointmentTime(e.target.value)}
                disabled={isBooking} // Disable input during booking
                className={errors.appointmentTime ? "validation-error" : ""}
              />
              {errors.appointmentTime && <span className="error">{errors.appointmentTime}</span>}

              <textarea
                placeholder="Enter the reason for the appointment"
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                disabled={isBooking} // Disable input during booking
                className={errors.reason ? "validation-error" : ""}
              />
              {errors.reason && <span className="error">{errors.reason}</span>}

              <button
                onClick={handleAppointmentSubmit}
                disabled={isBooking} // Disable button during booking
              >
                {isBooking ? "Booking..." : "Book Appointment"}
              </button>
            </div>
          </>
        )}
      </div>
    </body>
  );
}

export default DoctorDetails;
