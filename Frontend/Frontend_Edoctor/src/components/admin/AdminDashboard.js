import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/AdminDashboard.css";

function AdminDashboard() {
  const [dropdownOpen, setDropdownOpen] = useState({}); 
  const [adminName, setAdminName] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  // Fetch doctor's profile based on username stored in localStorage
  useEffect(() => {
    const username = localStorage.getItem("username");
    if (username) {
      api
        .get(`/${username}/admin/getProfile`)
        .then((response) => {
          setAdminName(response.data.name); // Assuming 'name' is the doctor's name from the response
        })
        .catch((error) => {
          console.error("Error fetching profile:", error);
          setAdminName(null);
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

  // Toggle dropdown visibility
  const toggleDropdown = (key) => {
    setDropdownOpen((prevState) => ({
      ...prevState,
      [key]: !prevState[key],
    }));
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome{adminName ? `, ${adminName}` : " Admin"}!</h1>
      </header>

      {isLoading ? (
        <p>Loading profile...</p>
      ) : (
        <nav className="dashboard-navbar">
          <ul>
            {/* Patients Dropdown */}
            <li>
              <button onClick={() => toggleDropdown("patients")}>
                Patients
                <span className="arrow">
                  {dropdownOpen["patients"] ? "▲" : "▼"}
                </span>
              </button>
              {dropdownOpen["patients"] && (
                <ul className="dropdown-menu">
                  <li>
                    <Link to="/admin-addPatient">Add Patient</Link>
                  </li>
                  <li>
                    <Link to="/admin-updatePatient">Update Patient</Link>
                  </li>
                  <li>
                    <Link to="/admin-deletePatient">Delete Patient</Link>
                  </li>
                  <li>
                    <Link to="/all-patient">All Patients</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Doctors Dropdown */}
            <li>
              <button onClick={() => toggleDropdown("doctors")}>
                Doctors
                <span className="arrow">
                  {dropdownOpen["doctors"] ? "▲" : "▼"}
                </span>
              </button>
              {dropdownOpen["doctors"] && (
                <ul className="dropdown-menu">
                  <li>
                    <Link to="/admin-addDoctor">Add Doctor</Link>
                  </li>
                  <li>
                    <Link to="/admin-updateDoctor">Update Doctor</Link>
                  </li>
                  <li>
                    <Link to="/admin-deleteDoctor">Delete Doctor</Link>
                  </li>
                  <li>
                    <Link to="/all-Doctor">All Doctors</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Appointments Dropdown */}
            <li>
              <button onClick={() => toggleDropdown("appointments")}>
                Appointments
                <span className="arrow">
                  {dropdownOpen["appointments"] ? "▲" : "▼"}
                </span>
              </button>
              {dropdownOpen["appointments"] && (
                <ul className="dropdown-menu">
                  <li>
                    <Link to="/admin-addappointment">Add Appointment</Link>
                  </li>
                  <li>
                    <Link to="/admin-updateappointment">Update Appointment</Link>
                  </li>
                  <li>
                    <Link to="/admin-deleteappointment">Delete Appointment</Link>
                  </li>
                  <li>
                    <Link to="/admin-allappointments">All Appointments</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Stats */}
            <li>
              <button onClick={() => toggleDropdown("stats")}>
                Stats
                <span className="arrow">
                  {dropdownOpen["stats"] ? "▲" : "▼"}
                </span>
              </button>
              {dropdownOpen["stats"] && (
                <ul className="dropdown-menu">
                  <li>
                    <Link to="/patients-stats">Patients Stats</Link>
                  </li>
                  <li>
                    <Link to="/doctors-stats">Doctors Stats</Link>
                  </li>
                  <li>
                    <Link to="/web-stats">Website Stats</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Logout */}
            <li>
              <button onClick={handleLogout}>Logout</button>
            </li>
          </ul>
        </nav>
      )}

      {/* Background Section Below Navbar */}
      <div className="dashboard-content">
        <h2>Welcome to the Admin Dashboard</h2>
        <div>
          Manage patients, view doctor details, track status, and much more
        </div>
        <br />
        <p>
          You can navigate through the available sections in the navbar above to
          access patient information, doctor profiles, and system status.
        </p>
        <p>
          If you need any assistance, feel free to reach out to the support team
          or visit the Help section.
        </p>
      </div>
    </div>
  );
}

export default AdminDashboard;
