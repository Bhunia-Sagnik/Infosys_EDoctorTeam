import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/AppointmentsPage.css";
import "../CSS/Modal.css";

function AppointmentsPage() {
  const [appointments, setAppointments] = useState([]);
  const [cancelReason, setCancelReason] = useState("");
  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState("");
  const [actionType, setActionType] = useState("");
  const [isFetching, setIsFetching] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false); // Added for process tracking
  const username = localStorage.getItem("username");

  // Fetch all appointments for a doctor
  const fetchAppointments = async () => {
    try {
      setIsFetching(true);
      const response = await axios.get(`/doctor/${username}/viewAppointments`);
      if (response.data.length === 0) {
        setAppointments([]);
      } else {
        setAppointments(response.data);
      }
    } catch (error) {
      console.error("Error fetching appointments:", error);
      alert("Failed to fetch appointments. Please try again.");
    } finally {
      setIsFetching(false);
    }
  };

  // Automatically fetch appointments on page load
  useEffect(() => {
    fetchAppointments();
  }, []);

  const openConfirmationModal = (appointmentId, action) => {
    setSelectedAppointment(appointmentId);
    setActionType(action);
    setShowConfirmationModal(true);
  };

  const closeConfirmationModal = () => {
    setShowConfirmationModal(false);
    setSelectedAppointment(null);
    setActionType("");
  };

  const handleConfirmAppointment = async () => {
    setIsProcessing(true);
    try {
      await axios.put(`/doctor/confirm-appointment/${selectedAppointment}`);
      fetchAppointments();
      closeConfirmationModal();
    } catch (error) {
      console.error("Error confirming appointment:", error);
      alert("Failed to confirm appointment.");
      closeConfirmationModal();
    } finally {
      setIsProcessing(false);
    }
  };

  const handleCancelAppointment = async () => {
    if (!cancelReason) {
      alert("Please provide a reason for cancellation.");
      return;
    }
    setIsProcessing(true);
    try {
      await axios.put(`/doctor/cancel-appointment/${selectedAppointment}?reason=${cancelReason}`);
      setCancelReason("");
      fetchAppointments();
      closeConfirmationModal();
    } catch (error) {
      console.error("Error cancelling appointment:", error);
      alert("Failed to cancel appointment.");
      closeConfirmationModal();
    } finally {
      setIsProcessing(false);
    }
  };

  const formatDateTime = (dateTime) => {
    const [date, time] = dateTime.split("T");
    return { date, time };
  };

  return (
    <div className="appointments-page">
      <h2>My Appointments</h2>

      {isFetching ? (
        <p>Loading appointments...</p>
      ) : appointments.length === 0 ? (
        <p>No appointments found</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Appointment ID</th>
              <th>Patient Name</th>
              <th>Age</th>
              <th>Gender</th>
              <th>Reason</th>
              <th>Appointment Date</th>
              <th>Appointment Time</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => {
              const { date, time } = formatDateTime(appointment.appointmentDateTime);
              return (
                <tr key={appointment.appointmentId}>
                  <td>{appointment.appointmentId}</td>
                  <td>{appointment.patient.name}</td>
                  <td>{appointment.patient.age}</td>
                  <td>{appointment.patient.gender}</td>
                  <td>{appointment.reason}</td>
                  <td>{date}</td>
                  <td>{time}</td>
                  <td>{appointment.status}</td>
                  <td>
                    {appointment.status === "Pending" && (
                      <>
                        <button
                          onClick={() =>
                            openConfirmationModal(appointment.appointmentId, "confirm")
                          }
                          disabled={isProcessing}
                        >
                          Confirm
                        </button>
                        <button
                          onClick={() =>
                            openConfirmationModal(appointment.appointmentId, "cancel")
                          }
                          disabled={isProcessing}
                        >
                          Cancel
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {showConfirmationModal && (
        <div className="confirmation-modal">
          <div className="modal-content">
            <p>
              Are you sure you want to {actionType} this appointment?
            </p>
            {actionType === "cancel" && (
              <textarea
                value={cancelReason}
                onChange={(e) => setCancelReason(e.target.value)}
                placeholder="Provide reason for cancellation"
              />
            )}
            <div className="modal-actions">
              <button
                onClick={
                  actionType === "confirm"
                    ? handleConfirmAppointment
                    : handleCancelAppointment
                }
                disabled={isProcessing}
              >
                {isProcessing ? "Processing..." : "Yes"}
              </button>
              <button onClick={closeConfirmationModal} disabled={isProcessing}>
                No
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AppointmentsPage;
