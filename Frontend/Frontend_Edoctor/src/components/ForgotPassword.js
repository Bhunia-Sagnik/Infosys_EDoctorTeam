import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api"; // Ensure this is correctly configured for Axios
import "../CSS/ForgotPassword.css";
import validator from "validator";

function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const validateForm = () => {
    const newErrors = {};

    if (validator.isEmpty(email)) {
      newErrors.email = "Email is required";
    } else if (!validator.isEmail(email)) {
      newErrors.email = "Enter a valid email";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleForgotPassword = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    try {
      setLoading(true);

      // Sending POST request using Axios
      const response = await api.post("/auth/reset-password", null, {
        params: { email },
      });

      // If successful, set a success message
      setMessage(response.data || "Reset password link sent successfully!");

      // Redirect after 2 seconds
      setTimeout(() => {
        navigate("/reset-password", { state: { email } });
      }, 2000);
    } catch (error) {
      // Handle error response, ensuring we avoid rendering objects directly
      const errorMessage =
        error.response?.data?.error || "Failed to send reset password token";
      setMessage({ error: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  return (
    <body className="forgot-password-page">
      <div className="forgot-password-background">
        <div className="forgot-password-container">
          <h1>Forgot Password</h1>
          <form onSubmit={handleForgotPassword}>
            <div className="form-group">
              <label>Email:</label>
              <input
                type="email"
                className={`input-field ${errors.email ? "input-error" : ""}`}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email"
              />
              {errors.email && <p className="error-text">{errors.email}</p>}
            </div>
            <button className="btn-primary" type="submit" disabled={loading}>
              {loading ? "OTP is being sent..." : "Send OTP"}
            </button>
          </form>

          {message && (
            <p className="message">
              {typeof message === "string"
                ? message
                : `Error: ${message.error || "Unknown Error"}`}
            </p>
          )}
        </div>
      </div>
    </body>
  );
}

export default ForgotPassword;
