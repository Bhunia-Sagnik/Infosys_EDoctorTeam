import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../CSS/VerifyEmail.css';
import axios from "../services/api";

function VerifyEmail() {
  const location = useLocation();
  const navigate = useNavigate();
  const [verificationCode, setVerificationCode] = useState('');
  const username = location.state?.username || '';

  const handleVerify = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`/auth/verify-email?code=${verificationCode}&username=${username}` );

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

  return (
    <div className="form-container">
      <h2>Verify Email</h2>
      <form onSubmit={handleVerify}>
        <input
          type="text"
          placeholder="Verification Code"
          value={verificationCode}
          onChange={(e) => setVerificationCode(e.target.value)}
          required
        />
        <button type="submit" className="btn">Verify</button>
      </form>
    </div>
  );
}

export default VerifyEmail;
