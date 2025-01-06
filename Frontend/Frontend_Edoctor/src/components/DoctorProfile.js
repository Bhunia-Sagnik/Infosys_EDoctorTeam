import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/DoctorProfile.css";

function DoctorProfile() {
  const [hasProfile, setHasProfile] = useState(false);
  const [doctorProfile, setDoctorProfile] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    specialization: "",
    location: "",
    hospitalName: "",
    mobileNo: "",
    chargedPerVisit: "",
  });
  const [loading, setLoading] = useState(false);
  const [showUpdateForm, setShowUpdateForm] = useState(false);
  const [errors, setErrors] = useState({});
  const username = localStorage.getItem("username");

  // Fetch Doctor Profile
  const fetchProfile = async () => {
    if (username) {
      try {
        setLoading(true);
        const response = await axios.get(`/doctor/${username}/viewProfile`);
        if (response.data) {
          setDoctorProfile(response.data);
          setHasProfile(true);
        }
      } catch (error) {
        console.error("Error fetching profile:", error);
      } finally {
        setLoading(false);
      }
    } else {
      alert("Login first to fetch profile.");
    }
  };

  // Handle Input Change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const targetData = showUpdateForm && hasProfile ? doctorProfile : formData;
    const updatedData = { ...targetData, [name]: value };

    if (showUpdateForm && hasProfile) {
      setDoctorProfile(updatedData);
    } else {
      setFormData(updatedData);
    }
    setErrors({ ...errors, [name]: "" }); // Clear error for the field
  };

  // Validate Form Data
  const validateForm = (data) => {
    const validationErrors = {};
    if (!data.name) validationErrors.name = "Name is required.";
    if (!data.specialization) validationErrors.specialization = "Specialization is required.";
    if (!data.location) validationErrors.location = "Location is required.";
    if (!data.hospitalName) validationErrors.hospitalName = "Hospital name is required.";
    if (!data.mobileNo || !/^\d{10}$/.test(data.mobileNo)) {
      validationErrors.mobileNo = "Mobile number must be a valid 10-digit number.";
    }
    if (!data.chargedPerVisit || isNaN(data.chargedPerVisit)) {
      validationErrors.chargedPerVisit = "Charge per visit must be a number.";
    }
    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0;
  };

  // Handle Form Submit
  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataToValidate = hasProfile && showUpdateForm ? doctorProfile : formData;

    if (!validateForm(dataToValidate)) return;

    try {
      setLoading(true);
      if (hasProfile && showUpdateForm) {
        const response = await axios.put(`/doctor/${username}/updateProfile`, doctorProfile);
        alert("Profile updated successfully!");
        setDoctorProfile(response.data);
        setShowUpdateForm(false);
      } else {
        const response = await axios.post(`/doctor/${username}/addProfile`, formData);
        alert("Profile added successfully!");
        setDoctorProfile(response.data);
        setHasProfile(true);
      }
    } catch (error) {
      alert("Error saving profile. Please try again.");
      console.error("Error submitting profile:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  return (
    <body className="doctor-profile">
      <div className="doctor-profile-container">
        {loading ? (
          <p>Loading...</p>
        ) : hasProfile ? (
          <>
            {!showUpdateForm ? (
              <>
                <h2>Doctor Profile</h2>
                <div className="profile-details">
                  <p><strong>Doctor ID:</strong> {doctorProfile.doctorId}</p>
                  <p><strong>Name:</strong> {doctorProfile.name}</p>
                  <p><strong>Specialization:</strong> {doctorProfile.specialization}</p>
                  <p><strong>Location:</strong> {doctorProfile.location}</p>
                  <p><strong>Hospital Name:</strong> {doctorProfile.hospitalName}</p>
                  <p><strong>Email:</strong> {doctorProfile.email}</p>
                  <p><strong>Mobile Number:</strong> {doctorProfile.mobileNo}</p>
                  <p><strong>Charged Per Visit:</strong> {doctorProfile.chargedPerVisit}</p>
                </div>
                <button onClick={() => setShowUpdateForm(true)}>Edit Profile</button>
              </>
            ) : (
              <form onSubmit={handleSubmit}>
                <h2>Update Profile</h2>
                {/* Form fields */}
                <div className="form-group">
                  <label>Name:</label>
                  <input
                    type="text"
                    name="name"
                    value={doctorProfile.name || ""}
                    onChange={handleInputChange}
                    className={errors.name ? "validation-error" : ""}
                  />
                  {errors.name && <div className="error">{errors.name}</div>}
                </div>
                <div className="form-group">
                  <label>Specialization:</label>
                  <input
                    type="text"
                    name="specialization"
                    value={doctorProfile.specialization || ""}
                    onChange={handleInputChange}
                    className={errors.specialization ? "validation-error" : ""}
                  />
                  {errors.specialization && <div className="error">{errors.specialization}</div>}
                </div>
                <div className="form-group">
                  <label>Location:</label>
                  <input
                    type="text"
                    name="location"
                    value={doctorProfile.location || ""}
                    onChange={handleInputChange}
                    className={errors.location ? "validation-error" : ""}
                  />
                  {errors.location && <div className="error">{errors.location}</div>}
                </div>
                <div className="form-group">
                  <label>Hospital Name:</label>
                  <input
                    type="text"
                    name="hospitalName"
                    value={doctorProfile.hospitalName || ""}
                    onChange={handleInputChange}
                    className={errors.hospitalName ? "validation-error" : ""}
                  />
                  {errors.hospitalName && <div className="error">{errors.hospitalName}</div>}
                </div>
                <div className="form-group">
                  <label>Mobile No:</label>
                  <input
                    type="text"
                    name="mobileNo"
                    value={doctorProfile.mobileNo || ""}
                    onChange={handleInputChange}
                    className={errors.mobileNo ? "validation-error" : ""}
                  />
                  {errors.mobileNo && <div className="error">{errors.mobileNo}</div>}
                </div>
                <div className="form-group">
                  <label>Charge Per Visit:</label>
                  <input
                    type="text"
                    name="chargedPerVisit"
                    value={doctorProfile.chargedPerVisit || ""}
                    onChange={handleInputChange}
                    className={errors.chargedPerVisit ? "validation-error" : ""}
                  />
                  {errors.chargedPerVisit && <div className="error">{errors.chargedPerVisit}</div>}
                </div>

                <button type="submit">Update Profile</button>
              </form>
            )}
          </>
        ) : (
          <form onSubmit={handleSubmit}>
            <h2>Add Profile</h2>
            {/* Form fields */}
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                className={errors.name ? "validation-error" : ""}
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
                className={errors.specialization ? "validation-error" : ""}
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
                className={errors.location ? "validation-error" : ""}
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
                className={errors.hospitalName ? "validation-error" : ""}
              />
              {errors.hospitalName && <div className="error">{errors.hospitalName}</div>}
            </div>
            <div className="form-group">
              <label>Mobile No:</label>
              <input
                type="text"
                name="mobileNo"
                value={formData.mobileNo}
                onChange={handleInputChange}
                className={errors.mobileNo ? "validation-error" : ""}
              />
              {errors.mobileNo && <div className="error">{errors.mobileNo}</div>}
            </div>
            <div className="form-group">
              <label>Charge Per Visit:</label>
              <input
                type="text"
                name="chargedPerVisit"
                value={formData.chargedPerVisit}
                onChange={handleInputChange}
                className={errors.chargedPerVisit ? "validation-error" : ""}
              />
              {errors.chargedPerVisit && <div className="error">{errors.chargedPerVisit}</div>}
            </div>
            <button type="submit">Add Profile</button>
          </form>
        )}
      </div>
    </body>
  );
}

export default DoctorProfile;
