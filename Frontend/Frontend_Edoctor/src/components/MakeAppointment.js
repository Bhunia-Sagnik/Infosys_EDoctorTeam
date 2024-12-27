import React, { useState } from "react";
import axios from "../services/api";
import "../CSS/MakeAppointments.css";

function MakeAppointment() {
  const [doctorId, setDoctorId] = useState("");
  const [appointmentDateTime, setAppointmentDateTime] = useState("");
  const [reason, setReason] = useState("");
  const [errors, setErrors] = useState({});

  // Validate form inputs
  const validateForm = () => {
    const newErrors = {};

    if (!doctorId.trim()) {
      newErrors.doctorId = "Doctor ID is required.";
    }

    if (!appointmentDateTime) {
      newErrors.appointmentDateTime = "Appointment date and time are required.";
    }

    if (!reason.trim()) {
      newErrors.reason = "Reason is required.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle appointment submission
  const handleAppointmentSubmission = async () => {
    if (!validateForm()) {
      alert("Please fill out all required fields.");
      return;
    }

    const data = { doctorId, appointmentDateTime, reason };

    try {
      await axios.post("/patient/book-appointment", data);
      alert("Appointment booked successfully!");

      // Clear form fields
      setDoctorId("");
      setAppointmentDateTime("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error booking appointment:", error);
      alert("Failed to book appointment. Please try again.");
    }
  };

  return (
    <div className="make-appointment">
      <h2>Make Appointment</h2>
      <form className="appointment-form">
        <label>Doctor ID:</label>
        <input
          type="text"
          value={doctorId}
          onChange={(e) => setDoctorId(e.target.value)}
          placeholder="Enter Doctor ID"
        />
        {errors.doctorId && <p className="error">{errors.doctorId}</p>}

        <label>Appointment Date & Time:</label>
        <input
          type="datetime-local"
          value={appointmentDateTime}
          onChange={(e) => setAppointmentDateTime(e.target.value)}
        />
        {errors.appointmentDateTime && (
          <p className="error">{errors.appointmentDateTime}</p>
        )}

        <label>Reason:</label>
        <textarea
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          placeholder="Enter reason"
        ></textarea>
        {errors.reason && <p className="error">{errors.reason}</p>}

        <button type="button" onClick={handleAppointmentSubmission}>
          Book Appointment
        </button>
      </form>
    </div>
  );
}

export default MakeAppointment;
