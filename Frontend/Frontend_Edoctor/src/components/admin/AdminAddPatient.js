import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminPatient.css";

function AdminAddPatient() {
  const [patientUsername, setPatientUsername] = useState("");
  const [patientProfile, setPatientProfile] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    mobileNo: "",
    bloodGroup: "",
    gender: "MALE",
    age: "",
    address: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const username = localStorage.getItem("username"); // Admin's username

  // Validation helper functions
  const validateForm = () => {
    const newErrors = {};

    if (!formData.name || !/^[a-zA-Z\s]+$/.test(formData.name)) {
      newErrors.name = "Name must contain only letters and cannot be empty.";
    }

    if (!formData.mobileNo || !/^\d{10}$/.test(formData.mobileNo)) {
      newErrors.mobileNo = "Mobile number must be exactly 10 digits.";
    }

    if (!formData.bloodGroup || !/^(A|B|AB|O)[+-]$/.test(formData.bloodGroup)) {
      newErrors.bloodGroup =
        "Blood group must be in a valid format (e.g., A+).";
    }

    if (!formData.age || formData.age <= 0 || formData.age > 120) {
      newErrors.age = "Age must be a number between 1 and 120.";
    }

    if (!formData.address || formData.address.length < 10) {
      newErrors.address = "Address must be at least 10 characters long.";
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
        alert("No profile exists. Proceed to create one.");
        setPatientProfile(null); // No profile exists
      } else {
        alert("Profile already exists. Cannot add a new profile.");
        setPatientProfile(response.data); // Existing profile
      }
    } catch (error) {
      console.error("Error checking profile:", error);
      alert("Error fetching profile. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Add a new patient profile
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      alert("Please fill out all fields correctly before submitting.");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.post(
        `/${username}/admin/patientAdd?patientUsername=${patientUsername}`,
        formData
      );

      alert("Profile added successfully! Check the email for the patient ID.");
      setFormData({
        name: "",
        mobileNo: "",
        bloodGroup: "",
        gender: "MALE",
        age: "",
        address: "",
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
    <div className="admin-add-patient-container">
      <h2>Admin Add Patient</h2>

      {/* Section to check if a profile exists */}
      <div className="check-profile-section">
        <label>Enter Patient Username:</label>
        <input
          type="text"
          value={patientUsername}
          onChange={(e) => setPatientUsername(e.target.value)}
        />
        <button onClick={checkProfile} disabled={loading}>
          {loading ? "Checking..." : "Check Profile"}
        </button>
      </div>

      {/* If profile exists, show profile details */}
      {patientProfile && (
        <div className="profile-details">
          <h3>Profile Details</h3>
          <p>
            <strong>Name:</strong> {patientProfile.name}
          </p>
          <p>
            <strong>Mobile No:</strong> {patientProfile.mobileNo}
          </p>
          <p>
            <strong>Blood Group:</strong> {patientProfile.bloodGroup}
          </p>
          <p>
            <strong>Gender:</strong> {patientProfile.gender}
          </p>
          <p>
            <strong>Age:</strong> {patientProfile.age}
          </p>
          <p>
            <strong>Address:</strong> {patientProfile.address}
          </p>
        </div>
      )}

      {/* If no profile exists, show the form to add a profile */}
      {!patientProfile && patientUsername && (
        <div className="add-profile-section">
          <h3>Add New Patient Profile</h3>
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
              <label>Blood Group:</label>
              <input
                type="text"
                name="bloodGroup"
                value={formData.bloodGroup}
                onChange={handleInputChange}
              />
              {errors.bloodGroup && (
                <p className="error">{errors.bloodGroup}</p>
              )}
            </div>
            <div>
              <label>Gender:</label>
              <select
                name="gender"
                value={formData.gender}
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
                value={formData.age}
                onChange={handleInputChange}
              />
              {errors.age && <p className="error">{errors.age}</p>}
            </div>
            <div>
              <label>Address:</label>
              <textarea
                name="address"
                value={formData.address}
                onChange={handleInputChange}
              />
              {errors.address && <p className="error">{errors.address}</p>}
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

export default AdminAddPatient;
