import React, { useState } from "react";
import axios from "../services/api";
import "../CSS/CancelAppointment.css";

function CancelAppointment() {
  const [appointmentId, setAppointmentId] = useState("");
  const [reason, setReason] = useState("");
  const [errors, setErrors] = useState({});

  // Validate form inputs
  const validateForm = () => {
    const newErrors = {};

    if (!appointmentId.trim()) {
      newErrors.appointmentId = "Appointment ID is required.";
    }

    if (!reason.trim()) {
      newErrors.reason = "Reason for cancellation is required.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle cancellation
  const handleCancel = async () => {
    if (!validateForm()) {
      alert("Please fill out all required fields.");
      return;
    }

    try {
      await axios.delete(`/patient/appointments/${appointmentId}`);
      alert("Appointment canceled successfully!");

      // Clear form fields
      setAppointmentId("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error canceling appointment:", error);
      alert("Failed to cancel appointment. Please try again.");
    }
  };

  return (
    <div className="cancel-appointment">
      <h2>Cancel Appointment</h2>
      <form className="cancel-form">
        <label>Appointment ID:</label>
        <input
          type="text"
          value={appointmentId}
          onChange={(e) => setAppointmentId(e.target.value)}
          placeholder="Enter Appointment ID"
        />
        {errors.appointmentId && (
          <p className="error">{errors.appointmentId}</p>
        )}

        <label>Reason:</label>
        <textarea
          placeholder="Reason for cancellation"
          value={reason}
          onChange={(e) => setReason(e.target.value)}
        />
        {errors.reason && <p className="error">{errors.reason}</p>}

        <button type="button" onClick={handleCancel}>
          Cancel Appointment
        </button>
      </form>
    </div>
  );
}

export default CancelAppointment;
