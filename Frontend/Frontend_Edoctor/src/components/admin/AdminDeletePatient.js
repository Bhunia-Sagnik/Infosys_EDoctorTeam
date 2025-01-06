import React, { useState } from "react";
import api from "../../services/api";
import "../../CSS/admin/AdminDeletePatient.css"

function AdminDeletePatient() {
  const [patientId, setPatientId] = useState("");

  const handleDelete = () => {
    if (window.confirm("Are you sure you want to delete this patient?")) {
      api
        .delete(`${localStorage.getItem("username")}/admin/patientDelete/${patientId}`)
        .then(() => {
          alert("Patient deleted successfully.");
          setPatientId("");
        })
        .catch((error) => {
          console.error("Error deleting patient:", error);
        });
    }
  };

  return (
    <body className="admin-delete-patient">
      <div className="admin-delete-patient-page">
        <h1>Delete Patient</h1>
        <input
          type="text"
          placeholder="Patient ID"
          value={patientId}
          onChange={(e) => setPatientId(e.target.value)}
        />
        <button onClick={handleDelete}>Delete Patient</button>
      </div>
    </body>
  );
}

export default AdminDeletePatient;