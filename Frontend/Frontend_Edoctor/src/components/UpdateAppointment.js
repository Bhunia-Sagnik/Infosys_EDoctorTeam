import React, { useState } from "react";
import axios from "../services/api";
import "../CSS/UpdateAppointment.css";

function UpdateAppointment() {
  const [appointmentId, setAppointmentId] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [appointmentDateTime, setAppointmentDateTime] = useState("");
  const [reason, setReason] = useState("");
  const [errors, setErrors] = useState({});

  // Validation function
  const validateForm = () => {
    const newErrors = {};

    if (!appointmentId.trim()) {
      newErrors.appointmentId = "Appointment ID is required.";
    }

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

  // Handle form submission
  const handleUpdate = async () => {
    if (!validateForm()) {
      alert("Please fill out all required fields correctly.");
      return;
    }

    const data = { doctorId, appointmentDateTime, reason };

    try {
      await axios.put(`/patient/appointments/${appointmentId}`, data);
      alert("Appointment updated successfully!");

      // Clear form fields after successful update
      setAppointmentId("");
      setDoctorId("");
      setAppointmentDateTime("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error updating appointment:", error);
      alert("Failed to update appointment. Please try again.");
    }
  };

  return (
    <div className="update-appointment">
      <h2>Update Appointment</h2>
      <form className="appointment-form">
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

        <button type="button" onClick={handleUpdate}>
          Update Appointment
        </button>
      </form>
    </div>
  );
}

export default UpdateAppointment;
