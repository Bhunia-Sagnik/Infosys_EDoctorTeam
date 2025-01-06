import React, { useState, useEffect } from "react";
import axios from "../services/api";
import "../CSS/AvailabilityPage.css";

function AvailabilityPage() {
  const [availabilities, setAvailabilities] = useState([]);
  const [formData, setFormData] = useState({
    fromDate: "",
    endDate: "",
  });
  const [errors, setErrors] = useState({});
  const [editAvailabilityId, setEditAvailabilityId] = useState(null);
  const username = localStorage.getItem("username");

  useEffect(() => {
    const fetchInitialAvailabilities = async () => {
      if (username) {
        try {
          const response = await axios.get(`/doctor/${username}/availability/viewAvailability`);
          if (response.data.length !== 0) {
            setAvailabilities(response.data);
          }
        } catch (error) {
          console.error("Error fetching availabilities:", error);
          alert("Failed to fetch availabilities. Please check your login session.");
        }
      } else {
        alert("Login to fetch availabilities.");
      }
    };

    fetchInitialAvailabilities();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    // Remove validation error for the field
    setErrors({ ...errors, [name]: "" });
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.fromDate) newErrors.fromDate = "From Date is required.";
    if (!formData.endDate) newErrors.endDate = "End Date is required.";
    else if (new Date(formData.endDate) < new Date(formData.fromDate))
      newErrors.endDate = "End Date cannot be before From Date.";
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formErrors = validateForm();
    if (Object.keys(formErrors).length > 0) {
      setErrors(formErrors);
      return;
    }

    try {
      if (editAvailabilityId) {
        await axios.put(
          `/doctor/${username}/availability/updateAvailability?id=${editAvailabilityId}`,
          formData
        );
        alert("Availability updated successfully!");
        setEditAvailabilityId(null);
      } else {
        await axios.post(
          `/doctor/${username}/availability/addAvailability`,
          formData
        );
        alert("Availability added successfully!");
      }
      setFormData({ fromDate: "", endDate: "" });
      fetchAvailabilities();
    } catch (error) {
      console.error("Error saving availability:", error);
      alert("Failed to save availability. Please try again.");
    }
  };

  const fetchAvailabilities = async () => {
    try {
      const response = await axios.get(`/doctor/${username}/availability/viewAvailability`);
      setAvailabilities(response.data);
    } catch (error) {
      console.error("Error fetching availabilities:", error);
    }
  };

  const handleEdit = (availability) => {
    setEditAvailabilityId(availability.availabilityId);
    setFormData({
      fromDate: availability.fromDate,
      endDate: availability.endDate,
    });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this availability?")) {
      try {
        await axios.delete(`/doctor/${username}/availability/deleteAvailability?id=${id}`);
        alert("Availability deleted successfully!");
        fetchAvailabilities();
      } catch (error) {
        console.error("Error deleting availability:", error);
        alert("Failed to delete availability. Please try again.");
      }
    }
  };

  return (
    <body className="availability-page">
      <div className="availability-page-container">
        <h2>Doctor Availability</h2>

        {availabilities.length <= 0?(
          <p>No availabilities found</p>
        ): (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>From Date</th>
                <th>End Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
                {
                  availabilities.map((availability) => (
                    <tr key={availability.availabilityId}>
                      <td>{availability.availabilityId}</td>
                      <td>{availability.fromDate}</td>
                      <td>{availability.endDate}</td>
                      <td>
                        <button onClick={() => handleEdit(availability)}>Update</button>
                        <button onClick={() => handleDelete(availability.availabilityId)}>Delete</button>
                      </td>
                    </tr>
                  ))
                }
            </tbody>
          </table>
        )}

        

        <div className="availability-form">
          <h3>{editAvailabilityId ? "Update Availability" : "Add Availability"}</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>From Date:</label>
              <input
                type="date"
                name="fromDate"
                value={formData.fromDate}
                onChange={handleInputChange}
              />
              {errors.fromDate && <div className="error">{errors.fromDate}</div>}
            </div>
            <div className="form-group">
              <label>End Date:</label>
              <input
                type="date"
                name="endDate"
                value={formData.endDate}
                onChange={handleInputChange}
              />
              {errors.endDate && <div className="error">{errors.endDate}</div>}
            </div>
            <button type="submit">{editAvailabilityId ? "Update" : "Add"} Availability</button>
          </form>
        </div>
      </div>
    </body>
  );
}

export default AvailabilityPage;
