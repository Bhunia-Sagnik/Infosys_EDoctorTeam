import React, { useEffect, useState } from "react";
import axios from "../services/api";
import "../CSS/PendingPatientFeedback.css";

const PendingPatientFeedback = () => {
  const [pendingFeedbacks, setPendingFeedbacks] = useState([]);
  const [feedbackText, setFeedbackText] = useState({});
  const [rating, setRating] = useState({});
  const [errors, setErrors] = useState({});
  const username = localStorage.getItem("username");

  // Fetch pending feedbacks when component mounts
  useEffect(() => {
    const fetchPendingFeedbacks = async () => {
      try {
        const response = await axios.get(`/${username}/feedback/pending-feedback`);
        setPendingFeedbacks(response.data);
      } catch (error) {
        console.error("Error fetching pending feedbacks:", error);
        alert("Failed to fetch pending feedbacks. Please try again.");
      }
    };

    fetchPendingFeedbacks();
  }, [username]);

  // Handle feedback submission
  const handleAddFeedback = async (doctorId) => {
    const feedback = {
      feedbackText: feedbackText[doctorId] || "",
      rating: rating[doctorId] || 0,
    };

    // Basic validation
    let formErrors = {};
    if (!feedbackText[doctorId]) {
      formErrors.feedbackText = "Feedback cannot be empty!";
    }
    if (!rating[doctorId] || rating[doctorId] < 1 || rating[doctorId] > 5) {
      formErrors.rating = "Rating must be between 1 and 5!";
    }

    if (Object.keys(formErrors).length > 0) {
      setErrors(formErrors);
      return; // Do not proceed with feedback submission if there are errors
    }

    try {
      const response = await axios.post(`/${username}/feedback/submit/${doctorId}`, feedback);
      alert("Feedback submitted successfully!");
      // Refresh the pending feedback list after successful submission
      setPendingFeedbacks((prev) => prev.filter((doctor) => doctor.doctorId !== doctorId));
    } catch (error) {
      console.error("Error submitting feedback:", error);
      alert("Failed to submit feedback. Please try again.");
    }
  };

  // Handle change for feedback text and reset error when user starts typing
  const handleFeedbackChange = (e, doctorId) => {
    setFeedbackText({ ...feedbackText, [doctorId]: e.target.value });
    if (errors.feedbackText) {
      setErrors({ ...errors, feedbackText: null }); // Remove error when user starts typing
    }
  };

  // Handle change for rating and reset error when user starts typing
  const handleRatingChange = (e, doctorId) => {
    setRating({ ...rating, [doctorId]: e.target.value });
    if (errors.rating) {
      setErrors({ ...errors, rating: null }); // Remove error when user starts typing
    }
  };

  return (
    <body className="pending-patient-feedback">
      <div className="pending-patient-feedback-container">
        <h2>Pending Feedback</h2>
        {pendingFeedbacks.length === 0 ? (
          <p>No pending feedbacks to show!</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Doctor Name</th>
                <th>Specialization</th>
                <th>Feedback</th>
                <th>Rating</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {pendingFeedbacks.map((doctor) => (
                <tr key={doctor.doctorId}>
                  <td>{doctor.name}</td>
                  <td>{doctor.specialization}</td>
                  <td>
                    <input
                      type="text"
                      placeholder="Enter feedback"
                      value={feedbackText[doctor.doctorId] || ""}
                      onChange={(e) => handleFeedbackChange(e, doctor.doctorId)}
                      style={{
                        borderColor: errors.feedbackText ? "red" : "",
                      }}
                    />
                    {errors.feedbackText && (
                      <div className="error-message">{errors.feedbackText}</div>
                    )}
                  </td>
                  <td>
                    <input
                      type="number"
                      min="1"
                      max="5"
                      placeholder="Rating (1-5)"
                      value={rating[doctor.doctorId] || ""}
                      onChange={(e) => handleRatingChange(e, doctor.doctorId)}
                      style={{
                        borderColor: errors.rating ? "red" : "",
                      }}
                    />
                    {errors.rating && (
                      <div className="error-message">{errors.rating}</div>
                    )}
                  </td>
                  <td>
                    <button onClick={() => handleAddFeedback(doctor.doctorId)}>Add Feedback</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </body>
  );
};

export default PendingPatientFeedback;
