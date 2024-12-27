import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminDoctor.css";

function AdminAddDoctor() {
  const [doctorUsername, setDoctorUsername] = useState("");
  const [doctorProfile, setDoctorProfile] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    specialization: "",
    location: "",
    hospitalName: "",
    mobileNo: "",
    chargedPerVisit: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const username = localStorage.getItem("username"); // Admin's username

  // Validation helper function
  const validateForm = () => {
    const newErrors = {};

    if (!formData.name.trim()) {
      newErrors.name = "Name is required.";
    }

    if (!formData.specialization.trim()) {
      newErrors.specialization = "Specialization is required.";
    }

    if (!formData.mobileNo || !/^\d{10}$/.test(formData.mobileNo)) {
      newErrors.mobileNo = "Mobile number must be exactly 10 digits.";
    }

    if (!formData.chargedPerVisit || formData.chargedPerVisit <= 0) {
      newErrors.chargedPerVisit =
        "Charged per visit must be a positive number.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Check if doctor profile exists
  const checkProfile = async () => {
    if (!doctorUsername) {
      alert("Enter a doctor username to check.");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.get(
        `/${username}/admin/doctorProfile?doctorUsername=${doctorUsername}`
      );

      alert("Profile already exists. Cannot add a new profile.");
      setDoctorProfile(response.data);
    } catch (error) {
      console.error("Error checking profile:", error);
      alert("No profile with this username. You can proceed to add one.");
    } finally {
      setLoading(false);
    }
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Add a new doctor profile
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      alert("Please fill out all required fields correctly.");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.post(
        `/${username}/admin/doctorAdd?doctorUsername=${doctorUsername}`,
        formData
      );

      alert("Profile added successfully! Check the email for the doctor ID.");
      setFormData({
        name: "",
        specialization: "",
        location: "",
        hospitalName: "",
        mobileNo: "",
        chargedPerVisit: "",
      });
      setErrors({});
    } catch (error) {
      console.error("Error adding profile:", error);
      alert("Error adding profile. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-add-doctor-container">
      <h2>Admin Add Doctor</h2>

      {/* Section to check if a profile exists */}
      <div className="check-profile-section">
        <label>Enter Doctor Username:</label>
        <input
          type="text"
          value={doctorUsername}
          onChange={(e) => setDoctorUsername(e.target.value)}
        />
        <button onClick={checkProfile} disabled={loading}>
          {loading ? "Checking..." : "Check Profile"}
        </button>
      </div>

      {/* If profile exists, show profile details */}
      {doctorProfile && (
        <div className="profile-details">
          <h3>Profile Details</h3>
          <p>
            <strong>Name:</strong> {doctorProfile.name}
          </p>
          <p>
            <strong>Specialization:</strong> {doctorProfile.specialization}
          </p>
          <p>
            <strong>Location:</strong> {doctorProfile.location}
          </p>
          <p>
            <strong>Hospital Name:</strong> {doctorProfile.hospitalName}
          </p>
          <p>
            <strong>Mobile No:</strong> {doctorProfile.mobileNo}
          </p>
          <p>
            <strong>Charged per Visit:</strong> {doctorProfile.chargedPerVisit}
          </p>
        </div>
      )}

      {/* If no profile exists, show the form to add a profile */}
      {!doctorProfile && doctorUsername && (
        <div className="add-profile-section">
          <h3>Add New Doctor Profile</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
              />
              {errors.name && <p className="error">{errors.name}</p>}
            </div>
            <div>
              <label>Specialization:</label>
              <input
                type="text"
                name="specialization"
                value={formData.specialization}
                onChange={handleInputChange}
              />
              {errors.specialization && (
                <p className="error">{errors.specialization}</p>
              )}
            </div>
            <div>
              <label>Location:</label>
              <input
                type="text"
                name="location"
                value={formData.location}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Hospital Name:</label>
              <input
                type="text"
                name="hospitalName"
                value={formData.hospitalName}
                onChange={handleInputChange}
              />
            </div>
            <div>
              <label>Mobile Number:</label>
              <input
                type="tel"
                name="mobileNo"
                value={formData.mobileNo}
                onChange={handleInputChange}
              />
              {errors.mobileNo && <p className="error">{errors.mobileNo}</p>}
            </div>
            <div>
              <label>Charged per Visit:</label>
              <input
                type="number"
                name="chargedPerVisit"
                value={formData.chargedPerVisit}
                onChange={handleInputChange}
              />
              {errors.chargedPerVisit && (
                <p className="error">{errors.chargedPerVisit}</p>
              )}
            </div>
            <button type="submit" disabled={loading}>
              {loading ? "Adding Profile..." : "Add Profile"}
            </button>
          </form>
        </div>
      )}
    </div>
  );
}

export default AdminAddDoctor;
