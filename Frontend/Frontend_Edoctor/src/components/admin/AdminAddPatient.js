import React, { useState, useEffect } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminAddPatient.css";

function AdminAddPatient() {
  const [patientUsername, setPatientUsername] = useState(""); // Holds the username entered by admin
  const [patientProfile, setPatientProfile] = useState(null); // Holds the patient profile fetched from backend
  const [formData, setFormData] = useState({
    name: "",
    mobileNo: "",
    bloodGroup: "",
    gender: "MALE",
    age: "",
    address: "",
  });
  const [errors, setErrors] = useState({}); // Holds validation errors
  const [loading, setLoading] = useState(false);
  const [isProfileChecked, setIsProfileChecked] = useState(false); // Track if the profile has been checked
  const [profileError, setProfileError] = useState(false); // Track error during profile check

  const username = localStorage.getItem("username"); // Admin's username

  // Validation helper functions
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

    if (!data.address || data.address.length < 10) {
      validationErrors.address = "Address must be at least 10 characters long.";
    }

    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0;
  };

  // Check if patient profile exists
  const checkProfile = async () => {
    if (!patientUsername) {
      alert("Enter a patient username to check.");
      return;
    }

    try {
      setLoading(true);
      setProfileError(null); // Reset any previous error
      const response = await axios.get(
        `/${username}/admin/patientProfile?patientUsername=${patientUsername}`
      );

      if (response.data === "Create profile") {
        setPatientProfile(null); // No profile exists
      } else {
        setPatientProfile(response.data); // Existing profile
      }
    } catch (error) {
      alert("Error fetching profile. Check username");
      console.error("Error checking profile:", error);
      setPatientUsername("");
      setProfileError(true);
    } finally {
      setLoading(false);
      setIsProfileChecked(true); // Set profile check status to true
    }
  };

  // Handle form input changes and clear errors
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrors({ ...errors, [name]: "" }); // Clear error when user starts typing
  };

  // Add a new patient profile
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm(formData)) {
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
      setIsProfileChecked(false); // Reset the profile check status
      setPatientUsername(""); // Clear the username after submission
    } catch (error) {
      console.error("Error adding profile:", error);
      alert("Error adding profile. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // Reset the form and states when the component is loaded or refreshed
    setPatientUsername("");
    setPatientProfile(null);
    setIsProfileChecked(false);
    setProfileError(null); // Clear any error when component is reloaded
  }, []);

  return (
    <body className="admin-add-patient">
      <div className="admin-add-patient-container">
        <h2>Admin Add Patient</h2>

        {/* Section to check if a profile exists */}
        {(!isProfileChecked || profileError) && (
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
            {profileError && <p className="error">{profileError}</p>}
          </div>
        )}

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
            <button
              onClick={() => {
                setPatientProfile(null);
                setPatientUsername("");
                setIsProfileChecked(false); // Allow user to check another profile
              }}
            >
              Check Another Username
            </button>
          </div>
        )}

        {/* If no profile exists, show the form to add a new profile */}
        {isProfileChecked && !patientProfile && !profileError && (
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
                  className={errors.name ? "validation-error" : ""}
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
                  className={errors.mobileNo ? "validation-error" : ""}
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
                  className={errors.bloodGroup ? "validation-error" : ""}
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
                  className={errors.age ? "validation-error" : ""}
                />
                {errors.age && <p className="error">{errors.age}</p>}
              </div>
              <div>
                <label>Address:</label>
                <textarea
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                  className={errors.address ? "validation-error" : ""}
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
    </body>
  );
}

export default AdminAddPatient;
