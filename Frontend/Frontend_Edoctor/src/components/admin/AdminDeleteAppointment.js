import React, { useEffect, useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminDeleteAppointment.css";

function AdminDeleteAppointment() {
  const [appointments, setAppointments] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  // Fetch all appointments when the component loads
  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        const response = await axios.get(
          `${localStorage.getItem("username")}/admin/appointments`
        );
        // Filter only pending appointments
        const pendingAppointments = response.data.filter(
          (appointment) => appointment.status === "Pending"
        );
        setAppointments(pendingAppointments);
      } catch (error) {
        console.error("Error fetching appointments:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAppointments();
  }, []);

  // Delete a specific appointment
  const handleDelete = async (appointmentId) => {
    if (window.confirm("Are you sure you want to delete this appointment?")) {
      try {
        await axios.delete(
          `${localStorage.getItem("username")}/admin/appointmentDelete/${appointmentId}`
        );
        alert("Appointment deleted successfully!");

        // Remove the deleted appointment from the local state
        setAppointments((prevAppointments) =>
          prevAppointments.filter(
            (appointment) => appointment.appointmentId !== appointmentId
          )
        );
      } catch (error) {
        console.error("Error deleting appointment:", error);
        alert("Failed to delete appointment. Please try again.");
      }
    }
  };

  return (
    <div className="admin-delete-appointment">
      <h2>Delete Appointments</h2>
      {isLoading ? (
        <p>Loading appointments...</p>
      ) : appointments.length === 0 ? (
        <p>No pending appointments available.</p>
      ) : (
        <table className="appointments-table">
          <thead>
            <tr>
              <th>Appointment ID</th>
              <th>Doctor Name</th>
              <th>Patient Name</th>
              <th>Appointment Date & Time</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => (
              <tr key={appointment.appointmentId}>
                <td>{appointment.appointmentId}</td>
                <td>Dr. {appointment.doctor.name}</td>
                <td>{appointment.patient.name}</td>
                <td>{appointment.appointmentDateTime}</td>
                <td>
                  <button
                    className="delete-btn"
                    onClick={() => handleDelete(appointment.appointmentId)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default AdminDeleteAppointment;
