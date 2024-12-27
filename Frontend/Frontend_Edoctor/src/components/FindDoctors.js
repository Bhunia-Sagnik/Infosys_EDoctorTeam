import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "../services/api";
import "../CSS/FindDoctors.css";

function FindDoctors() {
  const [doctors, setDoctors] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [searchSpecialization, setSearchSpecialization] = useState("");
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [errors, setErrors] = useState({});
  const [showSearchOptions, setShowSearchOptions] = useState(true);
  const username = localStorage.getItem("username");

  // Fetch all doctors
  const fetchAllDoctors = async () => {
    setErrors({});
    try {
      const response = await axios.get(`/${username}/patient/findDoctors`);
      setDoctors(response.data);
      setFilteredDoctors(response.data);
      setShowSearchOptions(false);
    } catch (error) {
      console.error("Error fetching all doctors:", error);
      alert("Failed to fetch doctors. Please try again later.");
    }
  };

  // Fetch doctors by Name
  const fetchDoctorsByName = async () => {
    if (!searchName.trim()) {
      setErrors({ searchName: "Doctor name is required." });
      return;
    }
    setErrors({});
    setFilteredDoctors([]);
    setSearchSpecialization("");

    try {
      const response = await axios.get(
        `/${username}/patient/findDoctorsByName?doctorName=${searchName}`
      );
      if (response.data && response.data.length > 0) {
        setFilteredDoctors(response.data);
        setShowSearchOptions(false);
      } else {
        alert("No doctors found for the given name.");
      }
    } catch (error) {
      console.error("Error fetching doctor by name:", error);
      alert("Failed to fetch doctor details. Please try again later.");
    }
  };

  // Fetch doctors by Specialization
  const fetchDoctorsBySpecialization = async () => {
    if (!searchSpecialization.trim()) {
      setErrors({ searchSpecialization: "Specialization is required." });
      return;
    }
    setErrors({});
    setFilteredDoctors([]);
    setSearchName("");

    try {
      const response = await axios.get(
        `/${username}/patient/findDoctorsBySpecialization?specialization=${searchSpecialization}`
      );
      if (response.data && response.data.length > 0) {
        setFilteredDoctors(response.data);
        setShowSearchOptions(false);
      } else {
        alert("No doctors found for the selected specialization.");
      }
    } catch (error) {
      console.error("Error fetching doctors by specialization:", error);
      alert("Failed to fetch doctors. Please try again later.");
    }
  };

  // Reset search options
  const resetSearch = () => {
    setShowSearchOptions(true);
    setSearchName("");
    setSearchSpecialization("");
    setFilteredDoctors([]);
  };

  return (
    <div className="find-doctors-page">
      <h2>Find Doctors</h2>

      {showSearchOptions && (
        <div className="fetch-options">
          <button onClick={fetchAllDoctors}>View All Doctors</button>

          <div className="search-section">
            <label>Enter Name:</label>
            <input
              type="text"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
              placeholder="Enter Name"
            />
            {errors.searchName && <p className="error">{errors.searchName}</p>}
            <button onClick={fetchDoctorsByName}>Search Doctor by Name</button>
          </div>

          <div className="search-section">
            <label>Enter Specialization:</label>
            <input
              type="text"
              value={searchSpecialization}
              onChange={(e) => setSearchSpecialization(e.target.value)}
              placeholder="Enter Specialization"
            />
            {errors.searchSpecialization && (
              <p className="error">{errors.searchSpecialization}</p>
            )}
            <button onClick={fetchDoctorsBySpecialization}>
              Search Doctor by Specialization
            </button>
          </div>
        </div>
      )}

      {!showSearchOptions && (
        <button className="reset-search" onClick={resetSearch}>
          Back to Search
        </button>
      )}

      <div className="doctor-cards-container">
        {filteredDoctors.length > 0 ? (
          filteredDoctors.map((doctor) => (
            <div className="doctor-card" key={doctor.doctorId}>
              <h3>Dr. {doctor.name}</h3>
              <p>
                <strong>Specialization:</strong> {doctor.specialization}
              </p>
              <p>
                <strong>Location:</strong> {doctor.location}
              </p>
              <p>
                <strong>Hospital:</strong> {doctor.hospitalName}
              </p>
              <p>
                <strong>Charge Per Visit:</strong> ₹{doctor.chargedPerVisit}
              </p>
              <Link to={`/doctor-details/${doctor.doctorId}`}>
                <button>Get Details</button>
              </Link>
            </div>
          ))
        ) : null}
      </div>
    </div>
  );
}

export default FindDoctors;
