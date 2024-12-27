import React, { useState, useEffect } from "react";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/DoctorStats.css"; // Assuming you already have the CSS file for styling

function DoctorStats() {
  const [doctors, setDoctors] = useState([]); // State to store fetched doctor data
  const [loading, setLoading] = useState(true); // State to handle loading state
  const username = localStorage.getItem("username");

  // Fetch doctor stats from the backend
  useEffect(() => {
    const fetchDoctorStats = async () => {
      try {
        setLoading(true);
        const response = await api.get(`/${username}/admin/allDoctorStats`); // Your backend API endpoint for doctors
        setDoctors(response.data); // Set the response data to the state
      } catch (error) {
        console.error("Error fetching doctor stats:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchDoctorStats();
  }, []);

  if (loading) {
    return <div>Loading...</div>; // Simple loading indicator
  }

  return (
    <div className="doctor-stats-container">
      <h2>Doctor Statistics</h2>

      {/* Table for displaying doctor stats */}
      <table className="doctor-stats-table">
        <thead>
          <tr>
            <th rowSpan="2">Doctor ID</th>
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
          {doctors.length > 0 ? (
            doctors.map((doctor) => (
              <tr key={doctor.doctorId}>
                <td>{doctor.doctorId}</td>
                <td>{doctor.name}</td>
                <td>{doctor.email}</td>
                <td>{doctor.mobileNo}</td>
                <td>{doctor.specialization}</td>
                <td>{doctor.totalAppointments}</td>
                <td className="pending">{doctor.pendingAppointments}</td>
                <td className="paid">{doctor.paidAppointments || 0}</td>
                <td className="unpaid">{doctor.unpaidAppointments || 0}</td>
                <td className="cancelled">{doctor.cancelledApointments}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="no-data">
                No data available
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default DoctorStats;
