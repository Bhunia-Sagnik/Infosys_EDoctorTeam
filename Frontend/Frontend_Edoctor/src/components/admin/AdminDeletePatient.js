import React, { useState } from "react";
import api from "../../services/api";

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
    <div className="patient-page">
      <h1>Delete Patient</h1>
      <input
        type="text"
        placeholder="Patient ID"
        value={patientId}
        onChange={(e) => setPatientId(e.target.value)}
      />
      <button onClick={handleDelete}>Delete Patient</button>
    </div>
  );
}

export default AdminDeletePatient;