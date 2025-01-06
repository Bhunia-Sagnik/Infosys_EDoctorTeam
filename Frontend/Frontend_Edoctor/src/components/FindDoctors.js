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

  // Fetch feedback for a single doctor
  const fetchFeedback = async (doctorId) => {
    try {
      const response = await axios.get(`/${username}/feedback/doctorAvg/${doctorId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching feedback:", error);
      return null;
    }
  };

  // Fetch all doctors and their feedback
  const fetchAllDoctors = async () => {
    setErrors({});
    try {
      const response = await axios.get(`/${username}/patient/findDoctors`);
      const doctorsData = response.data;

      // Fetch feedback for each doctor
      const feedbackPromises = doctorsData.map(async (doctor) => {
        const avgFeedback = await fetchFeedback(doctor.doctorId);
        return { ...doctor, avgFeedback };
      });

      const doctorsWithFeedback = await Promise.all(feedbackPromises);
      setDoctors(doctorsWithFeedback);
      setFilteredDoctors(doctorsWithFeedback);
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
      const doctorsData = response.data;

      // Fetch feedback for each doctor
      const feedbackPromises = doctorsData.map(async (doctor) => {
        const avgFeedback = await fetchFeedback(doctor.doctorId);
        return { ...doctor, avgFeedback };
      });

      const doctorsWithFeedback = await Promise.all(feedbackPromises);
      setFilteredDoctors(doctorsWithFeedback);
      setShowSearchOptions(false);
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
      const doctorsData = response.data;

      // Fetch feedback for each doctor
      const feedbackPromises = doctorsData.map(async (doctor) => {
        const avgFeedback = await fetchFeedback(doctor.doctorId);
        return { ...doctor, avgFeedback };
      });

      const doctorsWithFeedback = await Promise.all(feedbackPromises);
      setFilteredDoctors(doctorsWithFeedback);
      setShowSearchOptions(false);
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

  const renderStars = (rating) => {
    // If the rating is 0, return null to display "No feedback available"
    if (rating === 0) {
      return null;
    }
  
    const fullStars = Math.floor(rating);
    const halfStars = rating % 1 !== 0;
    const emptyStars = 5 - Math.ceil(rating);
  
    let stars = "";
    for (let i = 0; i < fullStars; i++) {
      stars += "★";
    }
    if (halfStars) {
      stars += "✩"; // Half star
    }
    for (let i = 0; i < emptyStars; i++) {
      stars += "☆";
    }
  
    return stars;
  };
  

  return (
    <body className="find-doctors">
      <div className="find-doctors-page">
        <h2>Find Doctors</h2>

        {showSearchOptions && (
          <div className="search-container">
            {/* View All Doctors Button */}
            <div className="view-all-section">
              <button onClick={fetchAllDoctors}>View All Doctors</button>
            </div>

            {/* Search By Name */}
            <div className="search-by-name-section">
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

            {/* Search By Specialization */}
            <div className="search-by-specialization-section">
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
                <div>
                  {doctor.avgFeedback !== 0 ? (
                    <p className="feedback-stars">{renderStars(doctor.avgFeedback)}</p>
                  ) : (
                    <p>No feedback available</p>
                  )}
                </div>
                <Link to={`/doctor-details/${doctor.doctorId}`}>
                  <button className="get-details">Get Details</button>
                </Link>
              </div>
            ))
          ) : (
            null
          )}
        </div>
      </div>
    </body>
  );
}

export default FindDoctors;
