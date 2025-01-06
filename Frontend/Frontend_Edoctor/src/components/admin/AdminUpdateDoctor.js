import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminUpdateDoctor.css";

function AdminUpdateDoctor() {
  const [doctorUsername, setDoctorUsername] = useState(""); // Holds the username entered by admin
  const [doctorProfile, setDoctorProfile] = useState(null); // Holds the doctor profile fetched from backend
  const [formData, setFormData] = useState({
    doctorId: "",
    name: "",
    specialization: "",
    location: "",
    hospitalName: "",
    mobileNo: "",
    email: "",
    chargedPerVisit: "",
  });
  const [errors, setErrors] = useState({}); // Holds form validation errors
  const [loading, setLoading] = useState(false); // Tracks loading state for API requests
  const [isProfileChecked, setIsProfileChecked] = useState(false); // Tracks if the profile has been checked

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
      newErrors.chargedPerVisit = "Charged per visit must be a positive number.";
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

      if (response.data === "Create profile") {
        alert("Profile does not exist. Please create one first.");
        setDoctorProfile(null);
      } else {
        setDoctorProfile(response.data); // Existing profile
        setFormData(response.data); // Set form data to existing profile
        setIsProfileChecked(true);
      }
    } catch (error) {
      alert("Error fetching profile. Please check the username.");
      console.error("Error checking profile:", error);
    } finally {
      setLoading(false);
    }
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Handle form submission to update profile
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      await axios.put(
        `/${username}/admin/doctorUpdate/${doctorProfile.doctorId}`,
        formData
      );

      alert("Profile updated successfully!");
      setDoctorUsername(""); // Clear the username after update
      setDoctorProfile(null); // Reset doctor profile
      setFormData({
        doctorId: "",
        name: "",
        specialization: "",
        location: "",
        hospitalName: "",
        mobileNo: "",
        email: "",
        chargedPerVisit: "",
      });
      setErrors({});
      setIsProfileChecked(false); // Reset profile check status
    } catch (error) {
      console.error("Error updating profile:", error);
      alert("Error updating profile. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <body className="admin-update-doctor">
      <div className="admin-update-doctor-container">
        <h1>Update Doctor Profile</h1>

        {/* Section to check if a profile exists */}
        {!isProfileChecked && (
          <div className="check-profile-section">
            <label>Enter Doctor Username:</label>
            <input
              type="text"
              value={doctorUsername}
              onChange={(e) => setDoctorUsername(e.target.value)}
              disabled={loading} // Disable input during backend request
            />
            <button onClick={checkProfile} disabled={loading}>
              {loading ? "Checking..." : "Check Profile"}
            </button>
          </div>
        )}

        {/* If profile exists, show the form to update profile */}
        {isProfileChecked && doctorProfile && (
          <div className="update-profile-section">
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="doctorId">Doctor ID</label>
                <input
                  type="text"
                  id="doctorId"
                  name="doctorId"
                  value={formData.doctorId || ""}
                  disabled
                />
              </div>
              <div className="form-group">
                <label htmlFor="name">Doctor's Name</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  className={errors.name ? "input-error" : ""}
                />
                {errors.name && <p className="error">{errors.name}</p>}
              </div>
              <div className="form-group">
                <label htmlFor="specialization">Specialization</label>
                <input
                  type="text"
                  id="specialization"
                  name="specialization"
                  value={formData.specialization}
                  onChange={handleInputChange}
                  className={errors.specialization ? "input-error" : ""}
                />
                {errors.specialization && (
                  <p className="error">{errors.specialization}</p>
                )}
              </div>
              <div className="form-group">
                <label htmlFor="location">Location</label>
                <input
                  type="text"
                  id="location"
                  name="location"
                  value={formData.location}
                  onChange={handleInputChange}
                  className={errors.location ? "input-error" : ""}
                />
                {errors.location && <p className="error">{errors.location}</p>}
              </div>
              <div className="form-group">
                <label htmlFor="hospitalName">Hospital Name</label>
                <input
                  type="text"
                  id="hospitalName"
                  name="hospitalName"
                  value={formData.hospitalName}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label htmlFor="mobileNo">Mobile No</label>
                <input
                  type="text"
                  id="mobileNo"
                  name="mobileNo"
                  value={formData.mobileNo}
                  onChange={handleInputChange}
                  className={errors.mobileNo ? "input-error" : ""}
                />
                {errors.mobileNo && <p className="error">{errors.mobileNo}</p>}
              </div>
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="text"
                  id="email"
                  name="email"
                  value={formData.email || ""}
                  disabled
                />
              </div>
              <div className="form-group">
                <label htmlFor="chargedPerVisit">Charged per Visit</label>
                <input
                  type="number"
                  id="chargedPerVisit"
                  name="chargedPerVisit"
                  value={formData.chargedPerVisit}
                  onChange={handleInputChange}
                  className={errors.chargedPerVisit ? "input-error" : ""}
                />
                {errors.chargedPerVisit && (
                  <p className="error">{errors.chargedPerVisit}</p>
                )}
              </div>
              <div className="form-group">
                <button type="submit" disabled={loading}>
                  {loading ? "Updating..." : "Update Doctor Profile"}
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </body>
  );
}

export default AdminUpdateDoctor;
