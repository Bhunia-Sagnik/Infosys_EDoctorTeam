import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/AddPatient.css";

function AdminUpdatePatient() {
  const [username, setUsername] = useState("");
  const [patientProfile, setPatientProfile] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch patient profile by username
  const fetchProfile = async () => {
    if (!username) {
      alert("Please enter a username.");
      return;
    }
    try {
      setLoading(true);
      const response = await axios.get(`/${username}/patient/viewProfile`);
      setPatientProfile(response.data);
    } catch (error) {
      alert("Error fetching profile. Please check the username.");
      console.error("Error fetching profile:", error);
    } finally {
      setLoading(false);
    }
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPatientProfile({ ...patientProfile, [name]: value });
  };

  // Handle form submission to update profile
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await axios.put(`/${username}/patient/updateProfile`, patientProfile);
      alert("Profile updated successfully.");
    } catch (error) {
      alert("Error updating profile. Please try again.");
      console.error("Error updating profile:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profile-container">
      <h2>Admin Update Patient Profile</h2>

      {/* Search Section */}
      {!patientProfile && (
        <>
          <h3>Search Patient</h3>
          <div className="search-container">
            <input
              type="text"
              placeholder="Enter Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <button onClick={fetchProfile} disabled={loading}>
              {loading ? "Fetching..." : "Search"}
            </button>
          </div>
        </>
      )}

      {/* Update Section */}
      {patientProfile && (
        <>
          <h3>Update Profile</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Patient ID:</label>
              <input
                type="text"
                name="patientId"
                value={patientProfile.patientId || ""}
                disabled
              />
            </div>
            <div>
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={patientProfile.name || ""}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Mobile Number:</label>
              <input
                type="tel"
                name="mobileNo"
                value={patientProfile.mobileNo || ""}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={patientProfile.email || ""}
                disabled
              />
            </div>
            <div>
              <label>Blood Group:</label>
              <input
                type="text"
                name="bloodGroup"
                value={patientProfile.bloodGroup || ""}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Gender:</label>
              <select
                name="gender"
                value={patientProfile.gender || ""}
                onChange={handleInputChange}
              >
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHERS">Others</option>
              </select>
            </div>
            <div>
              <label>Age:</label>
              <input
                type="number"
                name="age"
                value={patientProfile.age || ""}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Address:</label>
              <textarea
                name="address"
                value={patientProfile.address || ""}
                onChange={handleInputChange}
              />
            </div>
            <button type="submit" disabled={loading}>
              {loading ? "Updating..." : "Update Profile"}
            </button>
          </form>
        </>
      )}
    </div>
  );
}

export default AdminUpdatePatient;
