import React, { useEffect, useState } from "react";
import axios from "../services/api";
import "../CSS/PendingPatientFeedback.css";

const PendingPatientFeedback = () => {
  const [pendingFeedbacks, setPendingFeedbacks] = useState([]);
  const [feedbackText, setFeedbackText] = useState({});
  const [rating, setRating] = useState({});
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

  return (
    <div className="pending-feedback">
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
                    onChange={(e) =>
                      setFeedbackText({ ...feedbackText, [doctor.doctorId]: e.target.value })
                    }
                  />
                </td>
                <td>
                  <input
                    type="number"
                    min="1"
                    max="5"
                    placeholder="Rating (1-5)"
                    value={rating[doctor.doctorId] || ""}
                    onChange={(e) =>
                      setRating({ ...rating, [doctor.doctorId]: e.target.value })
                    }
                  />
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
  );
};

export default PendingPatientFeedback;
