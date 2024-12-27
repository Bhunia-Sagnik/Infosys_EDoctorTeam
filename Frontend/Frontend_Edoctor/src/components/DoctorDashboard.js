import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api"; // Assuming this is where your axios instance is stored
import "../CSS/DoctorDashboard.css";

function DoctorDashboard() {
  const [doctorName, setDoctorName] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  // Fetch doctor's profile based on username stored in localStorage
  useEffect(() => {
    const username = localStorage.getItem("username");
    if (username) {
      api
        .get(`/doctor/${username}/viewProfile`)
        .then((response) => {
          setDoctorName(response.data.name); // Assuming 'name' is the doctor's name from the response
        })
        .catch((error) => {
          console.error("Error fetching profile:", error);
          setDoctorName(null);
        })
        .finally(() => setIsLoading(false));
    } else {
      setIsLoading(false);
    }
  }, []);

  // Logout function to clear localStorage and redirect
  const handleLogout = () => {
    localStorage.removeItem("username");
    navigate("/login"); // Redirect to login page or home
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome{doctorName ? `, Dr. ${doctorName}` : " Doctor"}!</h1>
      </header>

      {isLoading ? (
        <p>Loading profile...</p>
      ) : (
        <nav className="dashboard-navbar">
          <ul>
            <li>
              <Link to="/doctor-profile">Profile</Link>
            </li>
            <li>
              <Link to="/availability">Availability</Link>
            </li>
            <li>
              <Link to="/appointments">Appointments</Link>
            </li>
            <li>
              <Link to="/doc-feedback">View Feedbacks</Link>
            </li>
            <li>
              <button onClick={handleLogout}>Logout</button>
            </li>
          </ul>
        </nav>
      )}

      {/* Background Section Below Navbar */}
      <div className="dashboard-content">
        <h2>Every click here leads to healthier lives. Let’s go!</h2>
        <pre>
          Manage your appointments update your availability view your profile
          all from this central hub
        </pre>
        <p>
          You can navigate through the available sections in the navbar above to
          access your schedule, patient details, and more.
        </p>
        <p>
          If you need any assistance, feel free to reach out to the support team
          or visit the Help section.
        </p>
      </div>
    </div>
  );
}

export default DoctorDashboard;
