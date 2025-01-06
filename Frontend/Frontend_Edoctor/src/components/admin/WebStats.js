import React, { useState, useEffect } from "react";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/WebStats.css";

function WebStats() {
  const [webStats, setWebStats] = useState(null); // Single object for stats
  const [selectedPeriod, setSelectedPeriod] = useState("today"); // Default period
  const [loading, setLoading] = useState(false);
  const username = localStorage.getItem("username"); // Retrieve username from localStorage

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
        if (dayOfWeek === 0) {
          startDate = new Date(today);
        } else {
          startDate.setDate(today.getDate() - dayOfWeek);
        }
        if (dayOfWeek === 6) {
          endDate = new Date(today);
        } else {
          endDate.setDate(today.getDate() + (6 - dayOfWeek));
        }
        break;
      case "last-month":
        startDate = new Date(today.getFullYear(), today.getMonth() - 1, 1);
        endDate = new Date(today.getFullYear(), today.getMonth(), 0); 
        break;
      case "this-month":
        startDate = new Date(today.getFullYear(), today.getMonth(), 1);
        break;
      default:
        startDate = endDate = today;
    }

    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(23, 59, 59, 999);

    const formatToLocalDateTime = (date) => {
      const offset = date.getTimezoneOffset() * 60000;
      const localDate = new Date(date.getTime() - offset);
      return localDate.toISOString().slice(0, 19);
    };

    return {
      startDate: formatToLocalDateTime(startDate),
      endDate: formatToLocalDateTime(endDate),
    };
  };

  const fetchWebStats = async (period) => {
    setLoading(true);
    try {
      const { startDate, endDate } = calculateDateRange(period);
      const response = await api.get(`/${username}/admin/webStatsBetween?startDate=${startDate}&endDate=${endDate}`);
      setWebStats(response.data);
    } catch (error) {
      console.error("Error fetching web stats:", error);
      alert("Error fetching data. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWebStats(selectedPeriod);
  }, [selectedPeriod]);

  const handlePeriodChange = (event) => {
    setSelectedPeriod(event.target.value);
  };

  const formatDate = (dateString) => {
    return dateString ? new Date(dateString).toISOString().slice(0, 10) : "";
  };

  return (
    <body className="web-stats">
      <div className="webstats-container">
        <h2>Web Statistics</h2>

        <div className="webstats-actions">
          <label htmlFor="period">Select Time Period: </label>
          <select
            id="period"
            value={selectedPeriod}
            onChange={handlePeriodChange}
            className="period-dropdown"
          >
            <option value="today">Today</option>
            <option value="last-week">Last Week</option>
            <option value="this-week">This Week</option>
            <option value="last-month">Last Month</option>
            <option value="this-month">This Month</option>
          </select>
        </div>

        {loading ? (
          <p className="loading-text">Loading stats...</p>
        ) : webStats ? (
          <div className="webstats-details">
            <h3>Statistics for {selectedPeriod.replace("-", " ")}</h3>
            <div className="stats-card">
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
                    <td className="pending">{webStats.pendingAppointments}</td>
                  </tr>
                  <tr>
                    <th>Confirmed Appointments</th>
                    <td className="confirmed">{webStats.confirmedAppointments}</td>
                  </tr>
                  <tr>
                    <th>Cancelled Appointments</th>
                    <td className="cancelled">{webStats.cancelledAppointments}</td>
                  </tr>
                  <tr>
                    <th>Paid Confirmed Appointments</th>
                    <td className="paid">{webStats.paidConfirmedAppointments}</td>
                  </tr>
                  <tr>
                    <th>Unpaid Confirmed Appointments</th>
                    <td className="unpaid">{webStats.unpaidConfirmedAppointments}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        ) : (
          <p className="no-data-text">No data available for the selected period.</p>
        )}
      </div>
    </body>
  );
}

export default WebStats;
