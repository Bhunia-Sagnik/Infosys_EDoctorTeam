import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import "../CSS/Register.css";
import validator from "validator";

function Register() {
  const [formData, setFormData] = useState({
    role: "PATIENT",
    username: "",
    password: "",
    email: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setErrors({ ...errors, [name]: "" });
  };

  const validateForm = () => {
    const newErrors = {};
  
    // Username validation
    if (validator.isEmpty(formData.username)) {
      newErrors.username = "Username is required";
    } else if (!validator.isLength(formData.username, { min: 3, max: 10 })) {
      newErrors.username = "Username must be 3-10 characters long";
    } else if (!validator.isAlphanumeric(formData.username)) {
      newErrors.username = "Username can only contain alphanumeric characters.";
    }
  
    // Email validation
    if (validator.isEmpty(formData.email)) {
      newErrors.email = "Email is required";
    } else if (!validator.isEmail(formData.email)) {
      newErrors.email = "Enter a valid email";
    }
  
    // Password validation
    if (validator.isEmpty(formData.password)) {
      newErrors.password = "Password is required";
    } else if (
      !validator.isLength(formData.password, { min: 8, max: 16 }) ||
      !/[a-zA-Z]/.test(formData.password) ||
      !/\d/.test(formData.password)
    ) {
      newErrors.password =
        "Password must be 8-16 characters long and include both letters and digits";
    }
  
    return newErrors;
  };
  

  const handleRegister = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
  
    setLoading(true);
  
    try {
      const response = await api.post("/auth/register", formData);
      alert("Registration successful! Redirecting to Verify Email...");
      navigate("/verify-email", { state: { username: formData.username } });
    } catch (error) {
      console.error("Error occurred during registration:", error);
      const errorMessage =
        error.response?.data?.message ||
        "An error occurred. Please try again later.";
      alert(`Registration failed: ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  };
  

  return (
    <body className="register-page">
      <div className="register-container">
        <h1>Register</h1>
        <form onSubmit={handleRegister}>
          <div className="form-group">
            <label htmlFor="username">Username:</label>
            <input
              id="username"
              type="text"
              name="username"
              placeholder="Enter your username"
              value={formData.username}
              onChange={handleInputChange}
              className={errors.username ? "validation-error" : ""}
            />
            {errors.username && (
              <span className="error-text">{errors.username}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              id="email"
              type="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleInputChange}
              className={errors.email ? "validation-error" : ""}
            />
            {errors.email && <span className="error-text">{errors.email}</span>}
          </div>
          <div className="form-group1">
            <label htmlFor="password">Password:</label>
            <input
              id="password"
              type="password"
              name="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleInputChange}
              className={errors.password ? "validation-error" : ""}
            />
            {errors.password && (
              <span className="error-text">{errors.password}</span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="role">Role:</label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleInputChange}
            >
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Doctor</option>
            </select>
          </div>
          <br></br>
          <button className="btn-primary" type="submit" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
        </form>

        <div className="regsiter-footer">
          <Link to="/login" className="login-link">
            Already have an account? Login
          </Link>
        </div>
      </div>
    </body>
  );
}

export default Register;
