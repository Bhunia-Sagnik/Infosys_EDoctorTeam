import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../CSS/VerifyEmail.css';
import axios from "../services/api";
import validator from "validator";

function VerifyEmail() {
  const location = useLocation();
  const navigate = useNavigate();
  const [verificationCode, setVerificationCode] = useState('');
  const username = location.state?.username || '';
  const [errors, setErrors] = useState({});

  const handleVerify = async (e) => {
    e.preventDefault();

    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      await axios.post(
        `/auth/verify-email?code=${verificationCode}&username=${username}`
      );

      alert('Email verified successfully! Redirecting to Login...');
      navigate('/login');
    } catch (error) {
      if (error.response && error.response.status === 400) {
        alert(error.response.data || "Verification failed.");
      } else {
        alert("An unexpected error occurred during verification.");
      }
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (validator.isEmpty(verificationCode)) {
      newErrors.verificationCode = "Verification code is required.";
    } else if (!validator.isLength(verificationCode, { min: 4, max: 4 })) {
      newErrors.verificationCode = "Verification code must be 4 characters long.";
    } 

    return newErrors;
  };

  return (
    <body className="verify-email-page">
      <div className="verify-container">
        <h2>Verify Email</h2>
        <form onSubmit={handleVerify}>
          <input
            type="text"
            placeholder="Verification Code"
            value={verificationCode}
            onChange={(e) => {
              setVerificationCode(e.target.value);
              setErrors({ ...errors, verificationCode: '' });
            }}
            className={errors.verificationCode ? 'input-error' : ''}
          />
          {errors.verificationCode && (
            <span className="error-text">{errors.verificationCode}</span>
          )}
          <button type="submit" className="btn">Verify</button>
        </form>
      </div>
    </body>
  );
}

export default VerifyEmail;
