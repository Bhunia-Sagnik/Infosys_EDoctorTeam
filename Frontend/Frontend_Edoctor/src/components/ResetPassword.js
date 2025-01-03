import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../services/api"; // Ensure this is configured correctly
import "../CSS/ResetPassword.css";
import validator from "validator";


function ResetPassword() {
  const location = useLocation();
  const navigate = useNavigate();
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [errors, setErrors] = useState({});
  const email = location.state?.email || "";

  // Helper function to validate form inputs
  const validateForm = () => {
    const newErrors = {};

    if (validator.isEmpty(otp)) {
      newErrors.otp = "OTP is required.";
    } else if (!validator.isLength(otp, { min: 4, max: 4 })) {
      newErrors.otp = "OTP must be 4 characters long.";
    }

    if (validator.isEmpty(newPassword)) {
      newErrors.newPassword = "Password is required";
    } else if (
      !validator.isLength(newPassword, { min: 8, max: 16 }) ||
      !/[a-zA-Z]/.test(newPassword) ||
      !/\d/.test(newPassword)
    ) {
      newErrors.newPassword =
        "Password must be 8-16 characters long and include both letters and digits";
    }

    if (newPassword !== confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0; // Valid if no errors
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      setMessage("Please fix the errors above.");
      setMessageType("error");
      return;
    }

    try {
      const response = await api.post("/auth/reset-password/confirm", null, {
        params: { email, token: otp, newPassword },
      });

      setMessage("Password reset successfully! Redirecting to login...");
      setMessageType("success");

      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (error) {
      const errorMessage =
        error.response?.data?.error || "Password reset failed.";
      setMessage(errorMessage);
      setMessageType("error");
    }
  };

  return (
    <body className="reset-password-page">
      <div className="reset-password-container">
        <h1>Reset Password</h1>
        <form onSubmit={handleResetPassword}>
          <div className="form-group">
            <label>OTP:</label>
            <input
              type="text"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              placeholder="Enter OTP"
              required
            />
            {errors.otp && <p className="error">{errors.otp}</p>}
          </div>
          <div className="form-group">
            <label>New Password:</label>
            <input
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="Enter new password"
              required
            />
            {errors.newPassword && <p className="error">{errors.newPassword}</p>}
          </div>
          <div className="form-group">
            <label>Confirm Password:</label>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="Confirm new password"
              required
            />
            {errors.confirmPassword && (
              <p className="error">{errors.confirmPassword}</p>
            )}
          </div>
          <button className="btn-primary" type="submit">
            Reset Password
          </button>
        </form>
        {message && <p className={`message ${messageType}`}>{message}</p>}
      </div>
    </body>
  );
}

export default ResetPassword;
