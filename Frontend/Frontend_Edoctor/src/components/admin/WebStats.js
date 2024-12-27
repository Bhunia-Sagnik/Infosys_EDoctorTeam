import React, { useState, useEffect } from "react";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/WebStats.css";

function WebStats() {
  const [webStats, setWebStats] = useState(null); // Single object for stats
  const [selectedPeriod, setSelectedPeriod] = useState("today"); // Default period
  const [loading, setLoading] = useState(false);
  const username = localStorage.getItem("username"); // Retrieve username from localStorage

  // Helper function to calculate startDate and endDate based on the selected period
  const calculateDateRange = (period) => {
    const today = new Date();
    let startDate = new Date();
    let endDate = new Date();

    switch (period) {
      case "today":
        startDate = endDate = today;
        break;
      case "last-week":
        startDate.setDate(today.getDate() - 7);
        endDate.setDate(today.getDate() - 1);
        break;
      case "this-week":
        const dayOfWeek = today.getDay(); // Sunday = 0, Monday = 1, ..., Saturday = 6

        // Calculate start date (last Sunday)
        if (dayOfWeek === 0) {
          // Today is Sunday, so start date is today
          startDate = new Date(today);
        } else {
          // Subtract the dayOfWeek to get the last Sunday
          startDate.setDate(today.getDate() - dayOfWeek);
        }

        // Calculate end date (coming Saturday)
        if (dayOfWeek === 6) {
          // Today is Saturday, so end date is today
          endDate = new Date(today);
        } else {
          // Add the remaining days to get to the coming Saturday
          endDate.setDate(today.getDate() + (6 - dayOfWeek));
        }
        break;
      case "last-month":
        startDate = new Date(today.getFullYear(), today.getMonth() - 1, 1);
        endDate = new Date(today.getFullYear(), today.getMonth(), 0); // Last day of the previous month
        break;
      case "this-month":
        startDate = new Date(today.getFullYear(), today.getMonth(), 1);
        break;
      default:
        startDate = endDate = today;
    }

    // Adjust startDate to 00:00:00
    startDate.setHours(0, 0, 0, 0);

    // Adjust endDate to 23:59:59
    endDate.setHours(23, 59, 59, 999);

    // Convert to LocalDateTime format: YYYY-MM-DDTHH:mm:ss
    const formatToLocalDateTime = (date) => {
      const offset = date.getTimezoneOffset() * 60000; // Convert to milliseconds
      const localDate = new Date(date.getTime() - offset); // Adjust to local time
      return localDate.toISOString().slice(0, 19); // Keep only YYYY-MM-DDTHH:mm:ss
    };

    return {
      startDate: formatToLocalDateTime(startDate),
      endDate: formatToLocalDateTime(endDate),
    };
  };

  // Fetch data based on the selected period
  const fetchWebStats = async (period) => {
    setLoading(true);
    try {
      const { startDate, endDate } = calculateDateRange(period);
      const response = await api.get(
        `/${username}/admin/webStatsBetween?startDate=${startDate}&endDate=${endDate}`
      );
      setWebStats(response.data);
    } catch (error) {
      console.error("Error fetching web stats:", error);
      alert("Error fetching data. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  // Effect to fetch data when the period changes
  useEffect(() => {
    fetchWebStats(selectedPeriod);
  }, [selectedPeriod]);

  // Handle the dropdown change
  const handlePeriodChange = (event) => {
    setSelectedPeriod(event.target.value);
  };

  const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toISOString().slice(0, 10) : "";
  };

  return (
    <div className="webstats-container">
      <h2>Web Statistics</h2>

      <div className="webstats-actions">
        <label htmlFor="period">Select Time Period: </label>
        <select
          id="period"
          value={selectedPeriod}
          onChange={handlePeriodChange}
        >
          <option value="today">Today</option>
          <option value="last-week">Last Week</option>
          <option value="this-week">This Week</option>
          <option value="last-month">Last Month</option>
          <option value="this-month">This Month</option>
        </select>
      </div>

      {loading ? (
        <p>Loading stats...</p>
      ) : webStats ? (
        <div className="webstats-details">
          <h3>Statistics for {selectedPeriod.replace("-", " ")}</h3>
          <table className="webstats-table">
            <tbody>
              <tr>
                <th>Start Date</th>
                <td>{formatDate(webStats.startDate)}</td>
              </tr>
              <tr>
                <th>End Date</th>
                <td>{formatDate(webStats.endDate)}</td>
              </tr>
              <tr>
                <th>Total Appointments</th>
                <td>{webStats.totalAppointments}</td>
              </tr>
              <tr>
                <th>Pending Appointments</th>
                <td>{webStats.pendingAppointments}</td>
              </tr>
              <tr>
                <th>Confirmed Appointments</th>
                <td>{webStats.confirmedAppointments}</td>
              </tr>
              <tr>
                <th>Cancelled Appointments</th>
                <td>{webStats.cancelledAppointments}</td>
              </tr>
              <tr>
                <th>Paid Confirmed Appointments</th>
                <td>{webStats.paidConfirmedAppointments}</td>
              </tr>
              <tr>
                <th>Unpaid Confirmed Appointments</th>
                <td>{webStats.unpaidConfirmedAppointments}</td>
              </tr>
            </tbody>
          </table>
        </div>
      ) : (
        <p>No data available for the selected period.</p>
      )}
    </div>
  );
}

export default WebStats;
