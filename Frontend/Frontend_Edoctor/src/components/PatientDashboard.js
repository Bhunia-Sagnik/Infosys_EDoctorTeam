import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../CSS/PatientDashboard.css";
import api from "../services/api";

function PatientDashboard() {
  const [patientName, setPatientName] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);

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

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  // Logout function to clear localStorage and redirect
  const handleLogout = () => {
    localStorage.removeItem("username");
    navigate("/login"); // Redirect to login page or home
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome{patientName ? `, ${patientName}` : " Patient"}!</h1>
      </header>

      {isLoading ? (
        <p>Loading profile...</p>
      ) : (
        <nav className="dashboard-navbar">
          <ul>
            <li>
              <Link to="/add-patient">Profile</Link>
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
            <li>
              <button onClick={toggleDropdown}>
                Feedback
                <span className="arrow">{dropdownOpen ? "▲" : "▼"}</span>
              </button>
              {dropdownOpen && (
                <ul className="dropdown-menu">
                  <li>
                    <Link to="/all-patient-feedback">All Feedbacks</Link>
                  </li>
                  <li>
                    <Link to="/pending-patient-feedback">
                      Pending Feedbacks
                    </Link>
                  </li>
                </ul>
              )}
            </li>
            <li>
              <button onClick={handleLogout}>Logout</button>
            </li>
          </ul>
        </nav>
      )}
      {/* Background Section Below Navbar */}
      <div className="dashboard-content">
        <h2>Empowering your wellness, one step at a time!</h2>

        <pre>
          Book your appointments view your medical history update your profile
          all from this central hub
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
