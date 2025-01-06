import React, { useState, useEffect } from "react";
import axios from "../services/api"; // Axios instance with base URL
import "../CSS/AllPatientFeedback.css"; // Add your styling here

const PatientFeedback = () => {
  const username = localStorage.getItem("username");
  const [allFeedbacks, setAllFeedbacks] = useState([]);
  const [loading, setLoading] = useState(false);

  // Fetch all feedbacks
  const fetchAllFeedbacks = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${username}/feedback/patient`);
      setAllFeedbacks(response.data);
    } catch (error) {
      console.error("Error fetching feedbacks:", error);
      alert("Error fetching feedbacks. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllFeedbacks();
  }, []);

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
    <body className="patient-all-feedback">
      <div className="patient-all-feedback-page">
        <h1>All Feedbacks</h1>

        {loading && <p>Loading...</p>}

        {!loading && allFeedbacks.length > 0 ? (
          <table className="feedback-table">
            <thead>
              <tr>
                <th>Doctor</th>
                <th>Feedback</th>
                <th>Rating</th>
              </tr>
            </thead>
            <tbody>
              {allFeedbacks.map((feedback) => (
                <tr key={feedback.id}>
                  <td>{feedback.doctor.name}</td>
                  <td>{feedback.feedbackText || "No feedback provided"}</td>
                  <td className="rating-stars">{renderStars(feedback.rating)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          !loading && <p>No feedbacks found.</p>
        )}
      </div>
    </body>
  );
};

export default PatientFeedback;
