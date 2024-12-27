import React, { useState, useEffect } from "react";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/PatientStats.css"; // Assuming you already have the CSS file for styling

function PatientStats() {
  const [patients, setPatients] = useState([]); // State to store fetched patient data
  const [loading, setLoading] = useState(true); // State to handle loading state
  const username = localStorage.getItem("username");

  // Fetch patient stats from the backend
  useEffect(() => {
    const fetchPatientStats = async () => {
      try {
        setLoading(true);
        const response = await api.get(`/${username}/admin/allPatientStats`); // Your backend API endpoint
        setPatients(response.data); // Set the response data to the state
      } catch (error) {
        console.error("Error fetching patient stats:", error);
        alert("Error fetching data. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchPatientStats();
  }, []);

  if (loading) {
    return <div>Loading...</div>; // Simple loading indicator
  }

  return (
    <div className="patient-stats-container">
      <h2>Patient Statistics</h2>

      {/* Table for displaying patient stats */}
      <table className="patient-stats-table">
        <thead>
          <tr>
            <th rowSpan="2">Patient ID</th>
            <th rowSpan="2">Name</th>
            <th rowSpan="2">Mobile No</th>
            <th rowSpan="2">Email ID</th>
            <th rowSpan="2">No. of Appointments</th>
            <th rowSpan="2">Pending</th>
            <th colSpan="2">Confirmed</th>
            <th rowSpan="2">Cancelled</th>
          </tr>
          <tr>
            <th>Paid</th>
            <th>Unpaid</th>
          </tr>
        </thead>
        <tbody>
          {patients.length > 0 ? (
            patients.map((patient) => (
              <tr key={patient.patientId}>
                <td>{patient.patientId}</td>
                <td>{patient.name}</td>
                <td>{patient.mobileNo}</td>
                <td>{patient.email}</td>
                <td>{patient.totalAppointments}</td>
                <td className="pending">{patient.pendingAppointments}</td>
                <td className="paid">{patient.paidAppointments || 0}</td>
                <td className="unpaid">{patient.unpaidAppointments || 0}</td>
                <td className="cancelled">{patient.cancelledAppointments}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="no-data">
                No data available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default PatientStats;
