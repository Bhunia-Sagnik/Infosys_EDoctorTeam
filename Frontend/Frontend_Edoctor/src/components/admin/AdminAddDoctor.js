import React, { useState } from "react";
import axios from "../../services/api";
import "../../CSS/admin/AdminAddDoctor.css";

function AdminAddDoctor() {
  const [doctorUsername, setDoctorUsername] = useState(""); // Holds the username entered by admin
  const [doctorProfile, setDoctorProfile] = useState(null); // Holds the doctor profile fetched from backend
  const [formData, setFormData] = useState({
    name: "",
    specialization: "",
    location: "",
    hospitalName: "",
    mobileNo: "",
    chargedPerVisit: "",
  });
  const [errors, setErrors] = useState({}); // Holds form validation errors
  const [loading, setLoading] = useState(false); // Tracks loading state for API requests
  const [isProfileChecked, setIsProfileChecked] = useState(false); // Tracks if the profile has been checked
  const [profileError, setProfileError] = useState(false); // Tracks error during profile check

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
      setProfileError(null); // Reset any previous error
      const response = await axios.get(
        `/${username}/admin/doctorProfile?doctorUsername=${doctorUsername}`
      );

      if (response.data === "Create profile") {
        setDoctorProfile(null); // No profile exists
      } else {
        setDoctorProfile(response.data); // Existing profile
      }
    } catch (error) {
      alert("Error fetching profile. Check username");
      console.error("Error checking profile:", error);
      setDoctorUsername("");
      setProfileError(true);
    } finally {
      setLoading(false);
      setIsProfileChecked(true); // Set profile check status to true
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
      setIsProfileChecked(false); // Reset the profile check status
      setDoctorUsername(""); // Clear the username after submission
    } catch (error) {
      console.error("Error adding profile:", error);
      alert("Error adding profile. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <body className="admin-add-doctor">
      <div className="admin-add-doctor-container">
        <h1>Add Doctor</h1>

        {/* Section to check if a profile exists */}
        {(!isProfileChecked || profileError) && (
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
            {profileError && <p className="error">{profileError}</p>}
          </div>
        )}

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
            <button
              onClick={() => {
                setDoctorProfile(null);
                setDoctorUsername("");
                setIsProfileChecked(false); // Allow user to check another profile
              }}
              className="check-another-username"
            >
              Check Another Username
            </button>
          </div>
        )}

        {/* If no profile exists, show the form to add a new profile */}
        {isProfileChecked && !doctorProfile && !profileError && (
          <div className="add-profile-section">
            <form onSubmit={handleSubmit}>
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
                {errors.location && (
                  <p className="error">{errors.location}</p>
                )}
              </div>
              <div className="form-group">
                <label htmlFor="hospitalName">Hospital Name</label>
                <input
                  type="text"
                  id="hospitalName"
                  name="hospitalName"
                  value={formData.hospitalName}
                  onChange={handleInputChange}
                  className={errors.hospitalName ? "input-error" : ""}
                />
                {errors.hospitalName && (
                  <p className="error">{errors.hospitalName}</p>
                )}
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
                <label htmlFor="chargedPerVisit">Charged per Visit</label>
                <input
                  type="number"
                  id="chargedPerVisit"
                  name="chargedPerVisit"
                  value={formData.chargedPerVisit}
                  onChange={handleInputChange}
                  className={errors.chargedPerVisit ? "input-error" : "chargedPerVisit"}
                />
                {errors.chargedPerVisit && (
                  <p className="error">{errors.chargedPerVisit}</p>
                )}
              </div>
              <div className="form-group">
                <button type="submit" disabled={loading}>
                  {loading ? "Adding..." : "Add Doctor Profile"}
                </button>
              </div>
            </form>
          </div>
        )}
      </div>
    </body>
  );
}

export default AdminAddDoctor;
