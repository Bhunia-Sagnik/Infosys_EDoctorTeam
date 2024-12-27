import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "../services/api";
import "../CSS/DoctorDetails.css";  // Add appropriate CSS file

function DoctorDetails() {
  const { doctorId } = useParams(); // Fetch doctorId from URL
  const [doctor, setDoctor] = useState(null);
  const [availableDates, setAvailableDates] = useState([]);
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [reason, setReason] = useState(""); // New state for the reason
  const [isBooking, setIsBooking] = useState(false); // New state for loading
  const username = localStorage.getItem("username");

  // Fetch doctor details
  const fetchDoctorDetails = async () => {
    try {
      const response = await axios.get(`/${username}/patient/getDoctorById?doctorId=${doctorId}`);
      setDoctor(response.data);
    } catch (error) {
      console.error("Error fetching doctor details:", error);
      alert("Failed to fetch doctor details.");
    }
  };

  // Fetch doctor availability
  const fetchDoctorAvailability = async () => {
    try {
      const response = await axios.get(`/${username}/patient/doctorAvailableDates?doctorId=${doctorId}`);
      setAvailableDates(response.data);
    } catch (error) {
      console.error("Error fetching available dates:", error);
      alert("Failed to fetch doctor availability.");
    }
  };

  const handleAppointmentSubmit = async () => {
    const appointmentDateTime = `${appointmentDate}T${appointmentTime}`;

    // Appointment payload
    const appointment = {
      doctorId,
      appointmentDateTime,
      reason,
    };

    // Start booking
    setIsBooking(true);

    try {
      const response = await axios.post(`/${username}/patient/makeAppointment`, appointment);
      alert("Appointment scheduled successfully!");
    } catch (error) {
      console.error("Error scheduling appointment:", error);
      alert("Failed to schedule appointment. Please try again.");
    } finally {
      // End booking
      setIsBooking(false);
    }
  };

  useEffect(() => {
    fetchDoctorDetails();
    fetchDoctorAvailability();
  }, [doctorId]);

  return (
    <div className="doctor-details-page">
      {doctor && (
        <>
          <h2>Doctor Details</h2>
          <div className="doctor-details-card">
            <h3>Dr. {doctor.name}</h3>
            <p><strong>Specialization:</strong> {doctor.specialization}</p>
            <p><strong>Location:</strong> {doctor.location}</p>
            <p><strong>Hospital:</strong> {doctor.hospitalName}</p>
            <p><strong>Charge Per Visit:</strong> ₹{doctor.chargedPerVisit}</p>
            <h4>Available Dates: </h4>
            {availableDates.length > 0 ? (
              <ul>
                {availableDates.map((date) => (
                  <li key={date.availabilityId}>
                    {new Date(date.fromDate).toLocaleDateString()} to {new Date(date.endDate).toLocaleDateString()}
                  </li>
                ))}
              </ul>
            ) : (
              <p>No available dates.</p>
            )}

            <h4>Schedule Appointment</h4>
            <input
              type="date"
              value={appointmentDate}
              onChange={(e) => setAppointmentDate(e.target.value)}
              disabled={isBooking} // Disable input during booking
            />
            <input
              type="time"
              value={appointmentTime}
              onChange={(e) => setAppointmentTime(e.target.value)}
              disabled={isBooking} // Disable input during booking
            />
            <textarea
              placeholder="Enter the reason for the appointment"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              disabled={isBooking} // Disable input during booking
            />
            <button
              onClick={handleAppointmentSubmit}
              disabled={isBooking} // Disable button during booking
            >
              {isBooking ? "Booking..." : "Book Appointment"}
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default DoctorDetails;
