import React, { useState } from "react";
import axios from "../../services/api";
// import "../../CSS/AddDoctor.css";

function AdminUpdateDoctor() {
  const [username, setUsername] = useState("");
  const [doctorProfile, setDoctorProfile] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch doctor profile by username
  const fetchProfile = async () => {
    if (!username) {
      alert("Please enter a username.");
      return;
    }
    try {
      setLoading(true);
      const response = await axios.get(`doctor/${username}/viewProfile`);
      setDoctorProfile(response.data);
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
    setDoctorProfile({ ...doctorProfile, [name]: value });
  };

  // Handle form submission to update profile
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await axios.put(`/doctor/${username}/updateProfile`, doctorProfile);
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
      <h2>Admin Update Doctor Profile</h2>

      {/* Search Section */}
      {!doctorProfile && (
        <>
          <h3>Search Doctor</h3>
          <div className="search-container">
            <input
              type="text"
              placeholder="Enter Doctor Username"
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
      {doctorProfile && (
        <>
          <h3>Update Profile</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Doctor ID:</label>
              <input
                type="text"
                name="doctorId"
                value={doctorProfile.doctorId || ""}
                disabled
              />
            </div>
            <div>
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={doctorProfile.name || ""}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Specialization:</label>
              <input
                type="text"
                name="specialization"
                value={doctorProfile.specialization || ""}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Location:</label>
              <input
                type="text"
                name="location"
                value={doctorProfile.location || ""}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Hospital Name:</label>
              <input
                type="text"
                name="hospitalName"
                value={doctorProfile.hospitalName || ""}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Mobile Number:</label>
              <input
                type="tel"
                name="mobileNo"
                value={doctorProfile.mobileNo || ""}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>Charged per Visit:</label>
              <input
                type="number"
                name="chargedPerVisit"
                value={doctorProfile.chargedPerVisit || ""}
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

export default AdminUpdateDoctor;
