import React, { useState } from "react";
import axios from "../../services/api"; // Ensure the correct path to your API service
import "../../CSS/admin/AdminAddAppint.css";

function AdminAddAppointment() {
  const [patientId, setPatientId] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [appointmentDateTime, setAppointmentDateTime] = useState("");
  const [reason, setReason] = useState("");
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};
    if (!patientId.trim()) {
      newErrors.patientId = "Patient ID is required.";
    }
    if (!doctorId.trim()) {
      newErrors.doctorId = "Doctor ID is required.";
    }
    if (!appointmentDateTime) {
      newErrors.appointmentDateTime = "Appointment date and time are required.";
    }
    if (!reason.trim()) {
      newErrors.reason = "Reason for appointment is required.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleAppointmentSubmission = async () => {
    if (!validateForm()) return;

    try {
      const data = {
        patientId,
        doctorId,
        appointmentDateTime,
        reason,
      };

      await axios.post(
        `${localStorage.getItem("username")}/admin/appointmentAdd`,
        data
      );

      alert("Appointment added successfully!");
      setPatientId("");
      setDoctorId("");
      setAppointmentDateTime("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error adding appointment:", error);
      alert("Failed to add appointment. Please try again.");
    }
  };

  return (
    <div className="add-appointment">
      <h2>Add Appointment (Admin)</h2>
      <form className="appointment-form" onSubmit={(e) => e.preventDefault()}>
        <label>Patient ID:</label>
        <input
          type="text"
          value={patientId}
          onChange={(e) => setPatientId(e.target.value)}
          placeholder="Enter Patient ID"
        />
        {errors.patientId && <span className="error">{errors.patientId}</span>}

        <label>Doctor ID:</label>
        <input
          type="text"
          value={doctorId}
          onChange={(e) => setDoctorId(e.target.value)}
          placeholder="Enter Doctor ID"
        />
        {errors.doctorId && <span className="error">{errors.doctorId}</span>}

        <label>Appointment Date & Time:</label>
        <input
          type="datetime-local"
          value={appointmentDateTime}
          onChange={(e) => setAppointmentDateTime(e.target.value)}
        />
        {errors.appointmentDateTime && (
          <span className="error">{errors.appointmentDateTime}</span>
        )}

        <label>Reason:</label>
        <textarea
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          placeholder="Enter reason for appointment"
        ></textarea>
        {errors.reason && <span className="error">{errors.reason}</span>}

        <button type="button" onClick={handleAppointmentSubmission}>
          Add Appointment
        </button>
      </form>
    </div>
  );
}

export default AdminAddAppointment;
