import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/Doctor_Profile.css";

function Profile() {
  const [hasProfile, setHasProfile] = useState(false);
  const [doctorId, setDoctorId] = useState("");
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
  const username = localStorage.getItem("username");

  // Fetch Doctor Profile by ID
  const fetchProfile = async () => {
    if (username) {
      try {
        const response = await axios.get(`/doctor/${username}/viewProfile`);
        setDoctorProfile(response.data);
        setHasProfile(true); // Show profile section
      } catch (error) {
        alert("Profile not found. Please check the Doctor ID.");
        console.error("Error fetching profile:", error);
      }
    }
  };

  // Handle Form Input Changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    // Remove validation error for the field
    setErrors({ ...errors, [name]: "" });
  };

  // Form Validation
  const validateForm = () => {
    const newErrors = {};
    if (!formData.name) newErrors.name = "Name is required.";
    if (!formData.specialization) newErrors.specialization = "Specialization is required.";
    if (!formData.location) newErrors.location = "Location is required.";
    if (!formData.hospitalName) newErrors.hospitalName = "Hospital Name is required.";
    if (!formData.mobileNo) newErrors.mobileNo = "Mobile Number is required.";
    else if (!/^\d{10}$/.test(formData.mobileNo)) newErrors.mobileNo = "Invalid Mobile Number.";
    if (!formData.chargedPerVisit) newErrors.chargedPerVisit = "Charge per Visit is required.";
    else if (isNaN(formData.chargedPerVisit)) newErrors.chargedPerVisit = "Charge must be a number.";
    return newErrors;
  };

  // Add or Update Profile
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formErrors = validateForm();
    if (Object.keys(formErrors).length > 0) {
      setErrors(formErrors);
      return;
    }

    try {
      setLoading(true);

      if (doctorProfile) {
        // Update existing profile
        const response = await axios.put(`/doctor/${username}/updateProfile`, formData);
        alert("Profile updated successfully!");
        setDoctorProfile(response.data); // Update profile state
      } else {
        // Add new profile
        const response = await axios.post(`/doctor/${username}/addProfile`, formData);
        alert("Profile added successfully! Check mail for doctorId.");
        setDoctorProfile(response.data); // Set the new profile
        setHasProfile(true); // Switch to profile view
        setFormData({
          name: "",
          specialization: "",
          location: "",
          hospitalName: "",
          mobileNo: "",
          chargedPerVisit: "",
        });
        setDoctorId(response.data.doctorId);
      }
    } catch (error) {
      alert("Error saving profile. Please try again.");
      console.error("Error submitting profile:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (username) {
      fetchProfile();
    }
  }, [doctorId]);

  // Pre-fill form with existing doctorProfile if it exists
  useEffect(() => {
    if (doctorProfile) {
      setFormData({
        name: doctorProfile.name || "",
        specialization: doctorProfile.specialization || "",
        location: doctorProfile.location || "",
        hospitalName: doctorProfile.hospitalName || "",
        mobileNo: doctorProfile.mobileNo || "",
        chargedPerVisit: doctorProfile.chargedPerVisit || "",
      });
    }
  }, [doctorProfile]);

  return (
    <div className="profile-container">
      <h2>Doctor Profile</h2>

      {!doctorProfile && (
        <>
          <h3>Add Profile</h3>
          <form onSubmit={handleSubmit} noValidate>
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
              />
              {errors.name && <div className="error">{errors.name}</div>}
            </div>
            <div className="form-group">
              <label>Specialization:</label>
              <input
                type="text"
                name="specialization"
                value={formData.specialization}
                onChange={handleInputChange}
              />
              {errors.specialization && <div className="error">{errors.specialization}</div>}
            </div>
            <div className="form-group">
              <label>Location:</label>
              <input
                type="text"
                name="location"
                value={formData.location}
                onChange={handleInputChange}
              />
              {errors.location && <div className="error">{errors.location}</div>}
            </div>
            <div className="form-group">
              <label>Hospital Name:</label>
              <input
                type="text"
                name="hospitalName"
                value={formData.hospitalName}
                onChange={handleInputChange}
              />
              {errors.hospitalName && <div className="error">{errors.hospitalName}</div>}
            </div>
            <div className="form-group">
              <label>Mobile Number:</label>
              <input
                type="tel"
                name="mobileNo"
                value={formData.mobileNo}
                onChange={handleInputChange}
              />
              {errors.mobileNo && <div className="error">{errors.mobileNo}</div>}
            </div>
            <div className="form-group">
              <label>Charged per Visit:</label>
              <input
                type="text"
                name="chargedPerVisit"
                value={formData.chargedPerVisit}
                onChange={handleInputChange}
              />
              {errors.chargedPerVisit && <div className="error">{errors.chargedPerVisit}</div>}
            </div>
            <button type="submit" disabled={loading}>
              {loading ? "Adding Profile..." : "Add Profile"}
            </button>
          </form>
        </>
      )}

      {doctorProfile && (
        <>
          <h3>Edit Profile</h3>
          <form onSubmit={handleSubmit} noValidate>
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
              />
              {errors.name && <div className="error">{errors.name}</div>}
            </div>
            <div className="form-group">
              <label>Specialization:</label>
              <input
                type="text"
                name="specialization"
                value={formData.specialization}
                onChange={handleInputChange}
              />
              {errors.specialization && <div className="error">{errors.specialization}</div>}
            </div>
            <div className="form-group">
              <label>Location:</label>
              <input
                type="text"
                name="location"
                value={formData.location}
                onChange={handleInputChange}
              />
              {errors.location && <div className="error">{errors.location}</div>}
            </div>
            <div className="form-group">
              <label>Hospital Name:</label>
              <input
                type="text"
                name="hospitalName"
                value={formData.hospitalName}
                onChange={handleInputChange}
              />
              {errors.hospitalName && <div className="error">{errors.hospitalName}</div>}
            </div>
            <div className="form-group">
              <label>Mobile Number:</label>
              <input
                type="tel"
                name="mobileNo"
                value={formData.mobileNo}
                onChange={handleInputChange}
              />
              {errors.mobileNo && <div className="error">{errors.mobileNo}</div>}
            </div>
            <div className="form-group">
              <label>Charged per Visit:</label>
              <input
                type="text"
                name="chargedPerVisit"
                value={formData.chargedPerVisit}
                onChange={handleInputChange}
              />
              {errors.chargedPerVisit && <div className="error">{errors.chargedPerVisit}</div>}
            </div>
            <button type="submit" disabled={loading}>
              {loading ? "Updating Profile..." : "Update Profile"}
            </button>
          </form>
        </>
      )}
    </div>
  );
}

export default Profile;
