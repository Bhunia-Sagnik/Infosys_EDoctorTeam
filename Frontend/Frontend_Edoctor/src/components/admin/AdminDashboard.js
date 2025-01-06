import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../services/api";
import "../../CSS/admin/AdminDashboard.css";

function AdminDashboard() {
  const [hoveredDropdown, setHoveredDropdown] = useState(null);
  const [adminName, setAdminName] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const username = localStorage.getItem("username");
    if (username) {
      api
        .get(`/${username}/admin/getProfile`)
        .then((response) => {
          setAdminName(response.data.name);
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

  const handleLogout = () => {
    localStorage.removeItem("username");
    navigate("/login");
  };

  return (
    <div className="admin-dashboard-container">
      <header className="dashboard-header">
        <h1>Welcome{adminName ? `, ${adminName}` : " Admin"}!</h1>
      </header>

      {isLoading ? (
        <p>Loading profile...</p>
      ) : (
        <nav className="dashboard-navbar">
          <ul>
            {/* Patients Dropdown */}
            <li
              onMouseEnter={() => setHoveredDropdown("patients")}
              onMouseLeave={() => setHoveredDropdown(null)}
            >
              <button>Patients</button>
              {hoveredDropdown === "patients" && (
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
                    <Link to="/admin-all-patient">All Patients</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Doctors Dropdown */}
            <li
              onMouseEnter={() => setHoveredDropdown("doctors")}
              onMouseLeave={() => setHoveredDropdown(null)}
            >
              <button>Doctors</button>
              {hoveredDropdown === "doctors" && (
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
                    <Link to="/admin-all-Doctor">All Doctors</Link>
                  </li>
                </ul>
              )}
            </li>

            {/* Appointments Dropdown */}
            <li
              onMouseEnter={() => setHoveredDropdown("appointments")}
              onMouseLeave={() => setHoveredDropdown(null)}
            >
              <button>Appointments</button>
              {hoveredDropdown === "appointments" && (
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

            {/* Stats Dropdown */}
            <li
              onMouseEnter={() => setHoveredDropdown("stats")}
              onMouseLeave={() => setHoveredDropdown(null)}
            >
              <button>Stats</button>
              {hoveredDropdown === "stats" && (
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
              <button className="logout" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </ul>
        </nav>
      )}

      <div className="dashboard-content">
        <h2>Welcome to the Admin Dashboard</h2>
        <div>Manage patients, view doctor details, track status, and much more</div>
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
