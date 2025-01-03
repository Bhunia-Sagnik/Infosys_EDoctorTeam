import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api"; // Axios instance
import "../CSS/Login.css";

function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};

    // Validate username
    if (!username) {
      newErrors.username = "Username is required.";
    } else if (username.length < 3) {
      newErrors.username = "Username must be at least 3 characters long.";
    } else if (!/^[a-zA-Z0-9]+$/.test(username)) {
      newErrors.username = "Username can only contain alphanumeric characters.";
    }

    // Validate password
    if (!password) newErrors.password = "Password is required";
    if (!/^.{8,16}$/.test(password) || !/[a-zA-Z]/.test(password) || !/\d/.test(password)) {
      newErrors.password = "Password must be 8-16 characters long and include both letters and digits";
    };

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!validateForm()) return; // Stop if form is invalid

    try {
      // Send POST request to backend
      const response = await api.post("auth/login", { username, password });

      // Store the username in localStorage
      localStorage.setItem("username", username);

      // Extract user data
      const user = response.data;

      setMessage(`Welcome ${user.name || "User"}! Redirecting to dashboard...`);

      // Redirect based on user role
      switch (user.role) {
        case "ADMIN":
          navigate("/admin-dashboard");
          break;
        case "DOCTOR":
          navigate(`/doctor-dashboard`); // Send username in the URL path
          break;
        case "PATIENT":
          navigate("/patient-dashboard");
          break;
        default:
          navigate("/home"); // Fallback for unknown roles
      }
    } catch (error) {
      // Handle error
      const errorMessage =
        error.response?.data?.error ||
        "Login failed. Please check your credentials.";
      setErrorMessage(errorMessage);
    }
  };

  return (
    <body className="login-page">
      <div className="login-container">
        <center>
          <h1>LOGIN</h1>
        </center>
        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label>Username:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              className={errors.username ? "input-error" : ""}
            />
            {errors.username && <p className="error-text">{errors.username}</p>}
          </div>
          <div className="form-group1">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              className={errors.password ? "input-error" : ""}
            />
            {errors.password && <p className="error-text">{errors.password}</p>}
          </div>
          <center>
            <button className="btn-primary" type="submit">
              Login
            </button>
          </center>
        </form>
        <div className="login-footer">
          <Link to="/forgot-password" className="forgot-password-link">
            Forgot Password?
          </Link>
          <Link to="/register" className="register-link">
            Don't have an account? Register
          </Link>
        </div>
        {message !== ""?(
          <p className="message">{message}</p>
        ): (
          <p className="error-message">{errorMessage}</p>
        )}
      </div>
    </body>
  );
}

export default Login;
