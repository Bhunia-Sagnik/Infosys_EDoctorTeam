import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminUpdatePatient.css";

function AdminUpdatePatient() {
  const [patientUsername, setPatientUsername] = useState(""); // Holds the username entered by admin
  const [patientProfile, setPatientProfile] = useState(null); // Holds the patient profile fetched from backend
  const [formData, setFormData] = useState({
    patientId: "",
    name: "",
    mobileNo: "",
    email: "",
    bloodGroup: "",
    gender: "",
    age: "",
    address: "",
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

    if (!formData.mobileNo || !/^\d{10}$/.test(formData.mobileNo)) {
      newErrors.mobileNo = "Mobile number must be exactly 10 digits.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Check if patient profile exists
  const checkProfile = async () => {
    if (!patientUsername) {
      alert("Enter a patient username to check.");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.get(
        `/${username}/admin/patientProfile?patientUsername=${patientUsername}`
      );

      if (response.data === "Create profile") {
        alert("Profile does not exist. Please create one first.");
        setPatientProfile(null);
      } else {
        setPatientProfile(response.data); // Existing profile
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
        `/${username}/admin/patientUpdate/${patientProfile.patientId}`,
        formData
      );

      alert("Profile updated successfully!");
      setPatientUsername(""); // Clear the username after update
      setPatientProfile(null); // Reset patient profile
      setFormData({
        patientId: "",
        name: "",
        mobileNo: "",
        email: "",
        bloodGroup: "",
        gender: "",
        age: "",
        address: "",
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
    <body className="admin-update-patient">
      <div className="admin-update-patient-container">
        <h1>Update Patient Profile</h1>

        {/* Section to check if a profile exists */}
        {!isProfileChecked && (
          <div className="check-profile-section">
            <label>Enter Patient Username:</label>
            <input
              type="text"
              value={patientUsername}
              onChange={(e) => setPatientUsername(e.target.value)}
              disabled={loading} // Disable input during backend request
            />
            <button onClick={checkProfile} disabled={loading}>
              {loading ? "Checking..." : "Check Profile"}
            </button>
          </div>
        )}

        {/* If profile exists, show the form to update profile */}
        {isProfileChecked && patientProfile && (
          <div className="update-profile-section">
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="patientId">Patient ID</label>
                <input
                  type="text"
                  id="patientId"
                  name="patientId"
                  value={formData.patientId || ""}
                  disabled
                />
              </div>
              <div className="form-group">
                <label htmlFor="name">Name</label>
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
                <label htmlFor="bloodGroup">Blood Group</label>
                <input
                  type="text"
                  id="bloodGroup"
                  name="bloodGroup"
                  value={formData.bloodGroup}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label htmlFor="gender">Gender</label>
                <input
                  type="text"
                  id="gender"
                  name="gender"
                  value={formData.name}
                  onChange={handleInputChange}
                  className={errors.gender ? "input-error" : ""}
                />
                {errors.gender && <p className="error">{errors.gender}</p>}
              </div>
              <div className="form-group">
                <label htmlFor="age">Age</label>
                <input
                  type="number"
                  id="age"
                  name="age"
                  value={formData.age}
                  onChange={handleInputChange}
                  className={errors.age ? "input-error" : ""}
                />
                {errors.age && <p className="error">{errors.age}</p>}
              </div>
              <div className="form-group">
                <label htmlFor="address">Address</label>
                <input
                  type="text"
                  id="address"
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <button type="submit" disabled={loading}>
                  {loading ? "Updating..." : "Update Patient Profile"}
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </body>
  );
}

export default AdminUpdatePatient;
