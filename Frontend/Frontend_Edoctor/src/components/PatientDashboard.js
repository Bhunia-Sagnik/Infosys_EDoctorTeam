import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../CSS/PatientDashboard.css";
import api from "../services/api";

function PatientDashboard() {
  const [patientName, setPatientName] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const [isDropdownVisible, setIsDropdownVisible] = useState(false); // State to control dropdown visibility

  // Fetch patient's profile based on username stored in localStorage
  useEffect(() => {
    const username = localStorage.getItem("username");
    if (username) {
      api
        .get(`/${username}/patient/viewProfile`) // API endpoint for patient profile
        .then((response) => {
          setPatientName(response.data.name); // Assuming 'name' is the patient's name
        })
        .catch((error) => {
          console.error("Error fetching profile:", error);
          setPatientName(null);
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
    <div className="patient-dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome{patientName ? `, ${patientName}` : " Patient"}!</h1>
      </header>

      {isLoading ? (
        <p>Loading profile...</p>
      ) : (
        <nav className="dashboard-navbar">
          <ul>
            <li>
              <Link to="/patient-profile">Profile</Link>
            </li>
            <li>
              <Link to="/find-doctors">Doctors</Link>
            </li>
            <li>
              <Link to="/patient-appointments">Appointments</Link>
            </li>
            <li>
              <Link to="/payments">Payments</Link>
            </li>
            {/* Feedback Dropdown */}
            <li
              onMouseEnter={() => setIsDropdownVisible(true)}
              onMouseLeave={() => setIsDropdownVisible(false)}
            >
              <button className="feedback">
                Feedback
              </button>
              {isDropdownVisible && (
                <ul
                  className="dropdown-menu"
                  onMouseEnter={() => setIsDropdownVisible(true)} // Keep dropdown visible when hovering over it
                  onMouseLeave={() => setIsDropdownVisible(false)} // Hide dropdown when mouse leaves
                >
                  <li>
                    <Link to="/all-patient-feedback">All Feedbacks</Link>
                  </li>
                  <li>
                    <Link to="/pending-patient-feedback">Pending Feedbacks</Link>
                  </li>
                </ul>
              )}
            </li>
            <li>
              <button className="logout" onClick={handleLogout}>Logout</button>
            </li>
          </ul>
        </nav>
      )}
      
      {/* Background Section Below Navbar */}
      <div className="dashboard-content">
        <h2>Empowering your wellness, one step at a time!</h2>
        <pre>
          Book your appointments, view your medical history, and update your profile
          all from this central hub.
        </pre>
        <p>
          Use the navigation bar above to explore your appointments, health
          records, and more.
        </p>
        <p>
          If you need any help or guidance, feel free to contact the support
          team or check the Help section.
        </p>
      </div>
    </div>
  );
}

export default PatientDashboard;
