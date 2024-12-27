import React, { useEffect, useState } from "react";
import api from "../../services/api";
import "../../CSS/admin/AdminPatient.css";

function AllPatient() {
  const [patients, setPatients] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    api
      .get(`${localStorage.getItem("username")}/admin/patients`)
      .then((response) => {
        setPatients(response.data);
      })
      .catch((error) => {
        console.error("Error fetching patients:", error);
      })
      .finally(() => setIsLoading(false));
  }, []);

  return (
    <div className="patient-page">
      <h1>All Patients</h1>
      {isLoading ? (
        <p>Loading patients...</p>
      ) : (
        <table className="patient-table">
          <thead>
            <tr>
              <th>Patient ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Mobile Number</th>
              <th>Gender</th>
              <th>Blood Group</th>
              <th>Age</th>
            </tr>
          </thead>
          <tbody>
            {patients.map((patient) => (
              <tr key={patient.patientId}>
                <td>{patient.patientId}</td>
                <td>{patient.name}</td>
                <td>{patient.email}</td>
                <td>{patient.mobileNo}</td>
                <td>{patient.gender}</td>
                <td>{patient.bloodGroup}</td>
                <td>{patient.age}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default AllPatient;
