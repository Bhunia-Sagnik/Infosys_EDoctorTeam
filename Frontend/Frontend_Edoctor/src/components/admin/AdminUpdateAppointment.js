import React, { useState } from "react";
import axios from "../../services/api"; // Ensure correct path for admin API service
import "../../CSS/admin/AdminUpdateAppoint.css";

function AdminUpdateAppointment() {
  const [appointmentId, setAppointmentId] = useState("");
  const [appointmentDateTime, setAppointmentDateTime] = useState("");
  const [reason, setReason] = useState("");
  const [error, setError] = useState(""); // To handle form validation errors

  const validateForm = () => {
    // Check for empty or invalid fields
    if (!appointmentId || !appointmentDateTime || !reason) {
      return "All fields are required.";
    }
    if (isNaN(appointmentId)) {
      return "Appointment ID and Doctor ID must be numeric.";
    }
    return ""; // No errors
  };

  const handleUpdate = async () => {
    const validationError = validateForm();
    if (validationError) {
      setError(validationError); // Set the error message if validation fails
      return;
    }

    const data = {appointmentDateTime, reason };
    try {
      await axios.put(
        `${localStorage.getItem(
          "username"
        )}/admin/appointmentUpdate/${appointmentId}`,
        data
      );
      alert("Appointment updated successfully!");
      setAppointmentId("");
      setAppointmentDateTime("");
      setReason("");
      setError(""); // Clear any validation error on success
    } catch (error) {
      console.error("Error updating appointment:", error);
      alert("Failed to update appointment. Please try again.");
    }
  };

  return (
    <div className="admin-update-appointment">
      <h2>Admin Update Appointment</h2>
      <form className="appointment-form">
        {error && <div className="error-message">{error}</div>}{" "}
        {/* Show validation error message */}
        <label>Appointment ID:</label>
        <input
          type="text"
          value={appointmentId}
          onChange={(e) => setAppointmentId(e.target.value)}
          placeholder="Enter Appointment ID"
        />
        <label>Appointment Date & Time:</label>
        <input
          type="datetime-local"
          value={appointmentDateTime}
          onChange={(e) => setAppointmentDateTime(e.target.value)}
        />
        <label>Reason:</label>
        <textarea
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          placeholder="Enter reason for the appointment"
        ></textarea>
        <button type="button" onClick={handleUpdate}>
          Update Appointment
        </button>
      </form>
    </div>
  );
}

export default AdminUpdateAppointment;
