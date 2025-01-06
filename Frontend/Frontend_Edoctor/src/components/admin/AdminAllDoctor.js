import React, { useEffect, useState } from "react";
import api from "../../services/api";
import "../../CSS/admin/AdminAllDoctor.css";

function AllDoctors() {
  const [doctors, setDoctors] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    api
      .get(`${localStorage.getItem("username")}/admin/doctors`)
      .then((response) => {
        setDoctors(response.data);
      })
      .catch((error) => {
        console.error("Error fetching doctors:", error);
      })
      .finally(() => setIsLoading(false));
  }, []);

  return (
    <body className="admin-all-doctors">
      <div className="admin-all-doctors-page">
        <h1>All Doctors</h1>
        {isLoading ? (
          <p>Loading doctors...</p>
        ) : (
          <table className="all-doctor-table">
            <thead>
              <tr>
                <th>Doctor ID</th>
                <th>Name</th>
                <th>Specialization</th>
                <th>Location</th>
                <th>Hospital Name</th>
                <th>Mobile Number</th>
                <th>Charge per Visit</th>
              </tr>
            </thead>
            <tbody>
              {doctors.map((doctor) => (
                <tr key={doctor.doctorId}>
                  <td>{doctor.doctorId}</td>
                  <td>{doctor.name}</td>
                  <td>{doctor.specialization}</td>
                  <td>{doctor.location}</td>
                  <td>{doctor.hospitalName}</td>
                  <td>{doctor.mobileNo}</td>
                  <td>{doctor.chargedPerVisit}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </body>
  );
}

export default AllDoctors;
