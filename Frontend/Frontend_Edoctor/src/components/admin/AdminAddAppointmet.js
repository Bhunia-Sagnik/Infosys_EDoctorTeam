import React, { useState } from "react";
import axios from "../../services/api"; // Ensure the correct path to your API service
import "../../CSS/admin/AdminAddAppointment.css";

function AdminAddAppointment() {
  const [patientId, setPatientId] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [reason, setReason] = useState("");
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false); // State to track loading status

  // Form validation logic
  const validateForm = () => {
    const newErrors = {};
    const currentDateTime = new Date();

    // Validate Patient ID
    if (!patientId.trim()) {
      newErrors.patientId = "Patient ID is required.";
    } else if (!/^PAT-[A-Z0-9]{8}$/.test(patientId)) {
      newErrors.patientId = "Patient ID must start with 'PAT-' followed by 8 characters.";
    }

    // Validate Doctor ID
    if (!doctorId.trim()) {
      newErrors.doctorId = "Doctor ID is required.";
    } else if (!/^DOC-[A-Z0-9]{8}$/.test(doctorId)) {
      newErrors.doctorId = "Doctor ID must start with 'DOC-' followed by 8 characters.";
    }

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

  const handleAppointmentSubmission = async () => {
    if (!validateForm()) return;

    // Combine Date and Time into one field
    const appointmentDateTime = `${appointmentDate}T${appointmentTime}`;

    // Start loading when request is being sent
    setIsLoading(true);

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
      setAppointmentDate("");
      setAppointmentTime("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error adding appointment:", error);
      alert("Failed to add appointment. Please try again.");
    } finally {
      // Stop loading after request completion (success or failure)
      setIsLoading(false);
    }
  };

  return (
    <body className="admin-add-appointment">
      <div className="admin-add-appointment-container">
        <h2>Add Appointment</h2>
        <form className="appointment-form" onSubmit={(e) => e.preventDefault()}>
          <label>Patient ID:</label>
          <input
            type="text"
            value={patientId}
            onChange={(e) => setPatientId(e.target.value)}
            placeholder="Enter Patient ID"
            className={errors.patientId ? "error" : ""}
          />
          {errors.patientId && <span className="error">{errors.patientId}</span>}

          <label>Doctor ID:</label>
          <input
            type="text"
            value={doctorId}
            onChange={(e) => setDoctorId(e.target.value)}
            placeholder="Enter Doctor ID"
            className={errors.doctorId ? "error" : ""}
          />
          {errors.doctorId && <span className="error">{errors.doctorId}</span>}

          <label>Appointment Date:</label>
          <input
            type="date"
            value={appointmentDate}
            onChange={(e) => setAppointmentDate(e.target.value)}
            className={errors.appointmentDate ? "error" : ""}
          />
          {errors.appointmentDate && (
            <span className="error">{errors.appointmentDate}</span>
          )}

          <label>Appointment Time:</label>
          <input
            type="time"
            value={appointmentTime}
            onChange={(e) => setAppointmentTime(e.target.value)}
            className={errors.appointmentTime ? "error" : ""}
          />
          {errors.appointmentTime && (
            <span className="error">{errors.appointmentTime}</span>
          )}

          <label>Reason:</label>
          <textarea
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Enter reason for appointment"
            className={errors.reason ? "error" : ""}
          ></textarea>
          {errors.reason && <span className="error">{errors.reason}</span>}

          <button
            className="add-btn"
            type="button"
            onClick={handleAppointmentSubmission}
            disabled={isLoading} // Disable button if loading
          >
            {isLoading ? "Adding Appointment..." : "Add Appointment"} {/* Change button text based on loading */}
          </button>
        </form>
      </div>
    </body>
  );
}

export default AdminAddAppointment;
