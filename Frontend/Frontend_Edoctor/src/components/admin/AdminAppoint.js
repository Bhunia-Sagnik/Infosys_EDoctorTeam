import React, { useState, useEffect } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminAppoint.css";

function AdminAppoint() {
  const [appointments, setAppointments] = useState([]);
  const [cancelReason, setCancelReason] = useState("");
  const [updatedReason, setUpdatedReason] = useState("");
  const [updatedDateTime, setUpdatedDateTime] = useState("");
  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [actionType, setActionType] = useState("");
  const [isFetching, setIsFetching] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const username = localStorage.getItem("username");

  // Fetch all appointments for the patient
  const fetchAppointments = async () => {
    try {
      setIsFetching(true);
      const response = await axios.get(`/${username}/admin/appointments`);
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

  const openConfirmationModal = (appointmentId, action, appointment) => {
    setSelectedAppointment(appointmentId);
    setActionType(action);
    setShowConfirmationModal(true);

    if (action === "update") {
      setUpdatedReason(appointment.reason);
      setUpdatedDateTime(appointment.appointmentDateTime);
    }
  };

  const closeConfirmationModal = () => {
    setShowConfirmationModal(false);
    setSelectedAppointment(null);
    setActionType("");
    setUpdatedReason("");
    setUpdatedDateTime("");
  };

  const handleUpdateAppointment = async () => {
    setIsProcessing(true);
    try {
      const updatedData = {
        appointmentDateTime: updatedDateTime,
        reason: updatedReason,
      };
      await axios.put(
        `{/${username}/admin/appointmentUpdate/${selectedAppointment}}`
      );
      alert("Appointment updated successfully.");
      fetchAppointments();
      closeConfirmationModal();
    } catch (error) {
      console.error("Error updating appointment:", error);
      alert("Failed to update appointment.");
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
      await axios.put(
        `/${username}/admin/appointmentDelete/${selectedAppointment}`
      );
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
              <th>Doctor Name</th>
              <th>Reason</th>
              <th>Appointment Date</th>
              <th>Appointment Time</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => {
              const { date, time } = formatDateTime(
                appointment.appointmentDateTime
              );
              return (
                <tr key={appointment.appointmentId}>
                  <td>{appointment.appointmentId}</td>
                  <td>Dr. {appointment.doctor.name}</td>
                  <td>{appointment.reason}</td>
                  <td>{date}</td>
                  <td>{time}</td>
                  <td>{appointment.status}</td>
                  <td>
                    {appointment.status === "Pending" && (
                      <>
                        <button
                          onClick={() =>
                            openConfirmationModal(
                              appointment.appointmentId,
                              "update",
                              appointment
                            )
                          }
                          disabled={isProcessing}
                        >
                          Update
                        </button>
                        <button
                          onClick={() =>
                            openConfirmationModal(
                              appointment.appointmentId,
                              "cancel",
                              appointment
                            )
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
            <p>Are you sure you want to {actionType} this appointment?</p>

            {actionType === "cancel" && (
              <textarea
                value={cancelReason}
                onChange={(e) => setCancelReason(e.target.value)}
                placeholder="Provide reason for cancellation"
              />
            )}

            {actionType === "update" && (
              <div className="update-form">
                <label htmlFor="updatedReason">Reason:</label>
                <input
                  id="updatedReason"
                  type="text"
                  value={updatedReason}
                  onChange={(e) => setUpdatedReason(e.target.value)}
                  placeholder="Enter new reason"
                />
                <label htmlFor="updatedDateTime">
                  Appointment Date & Time:
                </label>
                <input
                  id="updatedDateTime"
                  type="datetime-local"
                  value={updatedDateTime}
                  onChange={(e) => setUpdatedDateTime(e.target.value)}
                />
              </div>
            )}

            <div className="modal-actions">
              <button
                onClick={
                  actionType === "update"
                    ? handleUpdateAppointment
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

export default AdminAppoint;
