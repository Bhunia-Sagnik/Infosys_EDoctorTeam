import React, { useState, useEffect } from "react";
import api from "../services/api"; // Assuming your axios instance is here
import "../CSS/DoctorFeedback.css";

function DoctorFeedback() {
  const [avgRating, setAvgRating] = useState(null);
  const [feedbacks, setFeedbacks] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const username = localStorage.getItem("username");

  const fetchFeedbackData = () => {
    setIsLoading(true);
    setError(null);

    // Adjust API calls to use the passed username
    Promise.all([
      api.get(`/${username}/feedback/doctorAvg`),
      api.get(`/${username}/feedback/doctor`),
    ])
      .then(([avgRatingResponse, feedbackResponse]) => {
        setAvgRating(avgRatingResponse.data); // Expecting average rating data
        setFeedbacks(feedbackResponse.data); // Expecting feedback list
      })
      .catch((err) => {
        console.error("Error fetching feedback data:", err);
        setError("Failed to fetch feedback data. Please try again.");
      })
      .finally(() => setIsLoading(false));
  };

  useEffect(() => {
    if (username) {
      fetchFeedbackData();
    }
  }, [username]);

  // Function to render stars based on rating
  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const halfStars = rating % 1 !== 0;
    const emptyStars = 5 - Math.ceil(rating);

    let stars = "";
    for (let i = 0; i < fullStars; i++) {
      stars += "★";
    }
    if (halfStars) {
      stars += "✩"; // Half star
    }
    for (let i = 0; i < emptyStars; i++) {
      stars += "☆";
    }

    return stars;
  };

  return (
    <div className="feedback-container">
      <h2>Feedback</h2>
      {isLoading ? (
        <p>Loading feedback...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : feedbacks.length <= 0 ? (
        <p>No feedback available</p>
      ) : (
        <>
          <div className="avg-rating">
            <strong>Average Rating:</strong> {renderStars(avgRating) || "Not available"}
          </div>
          <table className="feedback-table">
            <thead>
              <tr>
                <th>Patient ID</th>
                <th>Rating</th>
                <th>Feedback</th>
              </tr>
            </thead>
            <tbody>
              {feedbacks.map((feedback) => (
                <tr key={feedback.id}>
                  <td>{feedback.patient.patientId}</td>
                  <td>{renderStars(feedback.rating)}</td>
                  <td>{feedback.feedbackText}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}

export default DoctorFeedback;
