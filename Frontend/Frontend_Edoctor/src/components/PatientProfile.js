import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/PatientProfile.css";

function PatientProfile() {
  const [hasProfile, setHasProfile] = useState(false);
  const [patientProfile, setPatientProfile] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    mobileNo: "",
    bloodGroup: "",
    gender: "MALE",
    age: "",
    address: "",
  });
  const [loading, setLoading] = useState(false);
  const [showUpdateForm, setShowUpdateForm] = useState(false);
  const [errors, setErrors] = useState({
    name: "",
    mobileNo: "",
    bloodGroup: "",
    age: "",
    address: "",
  });

  const username = localStorage.getItem("username");

  const fetchProfile = async () => {
    if (username) {
      try {
        setLoading(true);
        const response = await axios.get(`/${username}/patient/viewProfile`);
        if (response.data) {
          setPatientProfile(response.data);
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

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (patientProfile && showUpdateForm) {
      setPatientProfile({ ...patientProfile, [name]: value });
    } else {
      setFormData({ ...formData, [name]: value });
    }
    setErrors({ ...errors, [name]: "" }); // Clear the error when user starts typing
  };

  const validateForm = (data) => {
    const validationErrors = {};

    if (!data.name || !/^[a-zA-Z\s]+$/.test(data.name)) {
      validationErrors.name = "Name must contain only letters and cannot be empty.";
    }

    if (!data.mobileNo || !/^\d{10}$/.test(data.mobileNo)) {
      validationErrors.mobileNo = "Mobile number must be exactly 10 digits.";
    }

    if (!data.bloodGroup || !/^(A|B|AB|O)[+-]$/.test(data.bloodGroup)) {
      validationErrors.bloodGroup = "Blood group must be in a valid format (e.g., A+).";
    }

    if (!data.age || data.age <= 0 || data.age > 120) {
      validationErrors.age = "Age must be a number between 1 and 120.";
    }

    if (!data.address || data.address.length < 5) {
      validationErrors.address = "Address must be at least 10 characters long.";
    }

    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataToValidate = hasProfile ? patientProfile : formData;

    // Only validate when data is available (for both add and update cases)
    if (!validateForm(dataToValidate)) return;

    try {
      setLoading(true);
      if (hasProfile && showUpdateForm) {
        const response = await axios.put(
          `/${username}/patient/updateProfile`,
          patientProfile
        );
        alert("Profile updated successfully!");
        setPatientProfile(response.data);
        setShowUpdateForm(false);
      } else {
        const response = await axios.post(
          `/${username}/patient/addProfile`,
          formData
        );
        alert("Profile added successfully!");
        setPatientProfile(response.data);
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
    <body className="patient-profile">
      <div className="patient-profile-container">
        {loading ? (
          <p>Loading...</p>
        ) : hasProfile ? (
          <>
            {!showUpdateForm ? (
              <>
                <h2>Patient Profile</h2>
                <div className="profile-details">
                  <p><strong>Patient ID:</strong> {patientProfile.patientId}</p>
                  <p><strong>Name:</strong> {patientProfile.name}</p>
                  <p><strong>Email:</strong> {patientProfile.email}</p>
                  <p><strong>Mobile Number:</strong> {patientProfile.mobileNo}</p>
                  <p><strong>Blood Group:</strong> {patientProfile.bloodGroup}</p>
                  <p><strong>Gender:</strong> {patientProfile.gender}</p>
                  <p><strong>Age:</strong> {patientProfile.age}</p>
                  <p><strong>Address:</strong> {patientProfile.address}</p>
                </div>
                <button onClick={() => setShowUpdateForm(true)} className="update-button">
                  Update Profile
                </button>
              </>
            ) : (
              <>
                <h2>Update Profile</h2>
                <form onSubmit={handleSubmit}>
                  <div>
                    <label>Name:</label>
                    <input
                      type="text"
                      name="name"
                      value={patientProfile.name || ""}
                      onChange={handleInputChange}
                      className={errors.name ? "validation-error" : ""}
                    />
                    {errors.name && <span className="error">{errors.name}</span>}
                  </div>
                  <div>
                    <label>Mobile Number:</label>
                    <input
                      type="tel"
                      name="mobileNo"
                      value={patientProfile.mobileNo || ""}
                      onChange={handleInputChange}
                      className={errors.mobileNo ? "validation-error" : ""}
                    />
                    {errors.mobileNo && <span className="error">{errors.mobileNo}</span>}
                  </div>
                  <div>
                    <label>Blood Group:</label>
                    <input
                      type="text"
                      name="bloodGroup"
                      value={patientProfile.bloodGroup || ""}
                      onChange={handleInputChange}
                      className={errors.bloodGroup ? "validation-error" : ""}
                    />
                    {errors.bloodGroup && <span className="error">{errors.bloodGroup}</span>}
                  </div>
                  <div>
                    <label>Gender:</label>
                    <select
                      name="gender"
                      value={patientProfile.gender || ""}
                      onChange={handleInputChange}
                      className={errors.gender ? "validation-error" : ""}
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
                      className={errors.age ? "validation-error" : ""}
                    />
                    {errors.age && <span className="error">{errors.age}</span>}
                  </div>
                  <div>
                    <label>Address:</label>
                    <textarea
                      name="address"
                      value={patientProfile.address || ""}
                      onChange={handleInputChange}
                      className={errors.address ? "validation-error" : ""}
                    />
                    {errors.address && <span className="error">{errors.address}</span>}
                  </div>
                  <button type="submit" disabled={loading} className="update-button">
                    {loading ? "Updating..." : "Update Profile"}
                  </button>
                </form>
              </>
            )}
          </>
        ) : (
          <>
            <h2>Add Profile</h2>
            <form onSubmit={handleSubmit}>
              <div>
                <label>Name:</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  className={errors.name ? "validation-error" : ""}
                />
                {errors.name && <span className="error">{errors.name}</span>}
              </div>
              <div>
                <label>Mobile Number:</label>
                <input
                  type="tel"
                  name="mobileNo"
                  value={formData.mobileNo}
                  onChange={handleInputChange}
                  className={errors.mobileNo ? "validation-error" : ""}
                />
                {errors.mobileNo && <span className="error">{errors.mobileNo}</span>}
              </div>
              <div>
                <label>Blood Group:</label>
                <input
                  type="text"
                  name="bloodGroup"
                  value={formData.bloodGroup}
                  onChange={handleInputChange}
                  className={errors.bloodGroup ? "validation-error" : ""}
                />
                {errors.bloodGroup && <span className="error">{errors.bloodGroup}</span>}
              </div>
              <div>
                <label>Gender:</label>
                <select
                  name="gender"
                  value={formData.gender}
                  onChange={handleInputChange}
                  className={errors.gender ? "validation-error" : ""}
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
                  value={formData.age}
                  onChange={handleInputChange}
                  className={errors.age ? "validation-error" : ""}
                />
                {errors.age && <span className="error">{errors.age}</span>}
              </div>
              <div>
                <label>Address:</label>
                <textarea
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                  className={errors.address ? "validation-error" : ""}
                />
                {errors.address && <span className="error">{errors.address}</span>}
              </div>
              <button type="submit" disabled={loading}>
                {loading ? "Adding Profile..." : "Add Profile"}
              </button>
            </form>
          </>
        )}
      </div>
    </body>
  );
}

export default PatientProfile;
