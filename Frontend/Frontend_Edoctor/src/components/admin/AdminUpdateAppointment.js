import React, { useState } from "react";
import axios from "../../services/api"; // Ensure correct path for admin API service
import "../../CSS/admin/AdminUpdateAppoint.css";

function AdminUpdateAppointment() {
  const [appointmentId, setAppointmentId] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [reason, setReason] = useState("");
  const [error, setError] = useState(""); // To handle form validation errors
  const [errors, setErrors] = useState({
    appointmentId: "",
    appointmentDate: "",
    appointmentTime: "",
    reason: "",
  });
  const [isLoading, setIsLoading] = useState(false);

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
    
    // Validate Appointment ID
    if (!appointmentId) {
      newErrors.appointmentId = "Appointment ID is required.";
    } else if (isNaN(appointmentId)) {
      newErrors.appointmentId = "Appointment ID must be numeric.";
    }
    
    // Validate Reason
    if (!reason) {
      newErrors.reason = "Reason is required.";
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  
  

  const handleUpdate = async () => {
    const isValid = validateForm();
    if (!isValid) return;

    setIsLoading(true);

    const data = { appointmentDateTime: `${appointmentDate}T${appointmentTime}`, reason };

    try {
      await axios.put(
        `${localStorage.getItem("username")}/admin/appointmentUpdate/${appointmentId}`,
        data
      );
      alert("Appointment updated successfully!");
      setAppointmentId("");
      setAppointmentDate("");
      setAppointmentTime("");
      setReason("");
      setErrors({});
    } catch (error) {
      console.error("Error updating appointment:", error);
      alert("Failed to update appointment. Please try again.");
    }
    finally {
      setIsLoading(false);
    }
  };

  return (
    <body className="admin-update-appointment">
      <div className="admin-update-appointment-container">
        <h2>Admin Update Appointment</h2>
        <form className="appointment-form">
          {error && <div className="error-message">{error}</div>}

          {/* Appointment ID */}
          <label>Appointment ID:</label>
          <input
            type="text"
            value={appointmentId}
            onChange={(e) => setAppointmentId(e.target.value)}
            placeholder="Enter Appointment ID"
            className={errors.appointmentId ? "error" : ""}
          />
          {errors.appointmentId && <div className="error-message">{errors.appointmentId}</div>}

          {/* Appointment Date */}
          <label>Appointment Date:</label>
          <input
            type="date"
            value={appointmentDate}
            onChange={(e) => setAppointmentDate(e.target.value)}
            className={errors.appointmentDate ? "error" : ""}
          />
          {errors.appointmentDate && <div className="error-message">{errors.appointmentDate}</div>}

          {/* Appointment Time */}
          <label>Appointment Time:</label>
          <input
            type="time"
            value={appointmentTime}
            onChange={(e) => setAppointmentTime(e.target.value)}
            className={errors.appointmentTime ? "error" : ""}
          />
          {errors.appointmentTime && <div className="error-message">{errors.appointmentTime}</div>}

          {/* Reason */}
          <label>Reason:</label>
          <textarea
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Enter reason for the appointment"
            className={errors.reason ? "error" : ""}
          />
          {errors.reason && <div className="error-message">{errors.reason}</div>}

          <button
            className="update-btn"
            type="button"
            onClick={handleUpdate}
            disabled={isLoading} // Disable button if loading
          >
            {isLoading ? "Updating Appointment..." : "Update Appointment"} {/* Change button text based on loading */}
          </button>
        </form>
      </div>
    </body>
  );
}

export default AdminUpdateAppointment;
