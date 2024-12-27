import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import api from "../../services/api"; // Assuming this is where your axios instance is stored
import "../../CSS/admin/AdminDoctor.css"; // CSS file for styling

function AdminDoctor() {
  const [doctors, setDoctors] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [editDoctor, setEditDoctor] = useState(null);

  useEffect(() => {
    api
      .get(`/${localStorage.getItem("username")}/admin/doctors`)
      .then((response) => {
        setDoctors(response.data);
      })
      .catch((error) => {
        console.error("Error fetching doctors:", error);
      })
      .finally(() => setIsLoading(false));
  }, []);

  const handleEdit = (doctor) => {
    setEditDoctor(doctor);
  };

  const handleDelete = (doctorId) => {
    if (window.confirm("Are you sure you want to delete this doctor?")) {
      api
        .delete(
          `/${localStorage.getItem("username")}/admin/doctorDelete/${doctorId}`
        )
        .then(() => {
          setDoctors((prevDoctors) =>
            prevDoctors.filter((doctor) => doctor.id !== doctorId)
          );
          alert("Doctor deleted successfully.");
        })
        .catch((error) => {
          console.error("Error deleting doctor:", error);
        });
    }
  };

  const handleSave = () => {
    if (editDoctor) {
      api
        .put(
          `/${localStorage.getItem("username")}/admin/doctorUpdate/${
            editDoctor.doctorId
          }`,
          editDoctor
        )
        .then((response) => {
          setDoctors((prevDoctors) =>
            prevDoctors.map((doctor) =>
              doctor.id === response.data.id ? response.data : doctor
            )
          );
          setEditDoctor(null);
          alert("Doctor updated successfully.");
        })
        .catch((error) => {
          console.error("Error updating doctor:", error);
        });
    }
  };

  return (
    <div className="doctor-page">
      <header className="doctor-page-header">
        <h1>Doctor Management</h1>
        <Link to="/admin-dashboard">Back to Dashboard</Link>
      </header>

      {isLoading ? (
        <p>Loading doctors...</p>
      ) : (
        <table className="doctor-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Mobile No</th>
              <th>Email</th>
              <th>Specialization</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {doctors.map((doctor) => (
              <tr key={doctor.doctorId}>
                <td>{doctor.doctorId}</td>
                <td>
                  {editDoctor && editDoctor.doctorId === doctor.doctorId ? (
                    <input
                      type="text"
                      value={editDoctor.name}
                      onChange={(e) =>
                        setEditDoctor({ ...editDoctor, name: e.target.value })
                      }
                    />
                  ) : (
                    doctor.name
                  )}
                </td>
                <td>
                  {editDoctor && editDoctor.doctorId === doctor.doctorId ? (
                    <input
                      type="text"
                      value={editDoctor.mobileNo}
                      onChange={(e) =>
                        setEditDoctor({
                          ...editDoctor,
                          mobileNo: e.target.value,
                        })
                      }
                    />
                  ) : (
                    doctor.mobileNo
                  )}
                </td>
                <td>
                  {editDoctor && editDoctor.doctorId === doctor.doctorId ? (
                    <input
                      type="email"
                      value={editDoctor.email}
                      onChange={(e) =>
                        setEditDoctor({
                          ...editDoctor,
                          email: e.target.value,
                        })
                      }
                    />
                  ) : (
                    doctor.email
                  )}
                </td>
                <td>
                  {editDoctor && editDoctor.doctorId === doctor.doctorId ? (
                    <input
                      type="text"
                      value={editDoctor.specialization}
                      onChange={(e) =>
                        setEditDoctor({
                          ...editDoctor,
                          specialization: e.target.value,
                        })
                      }
                    />
                  ) : (
                    doctor.specialization
                  )}
                </td>
                <td>
                  {editDoctor && editDoctor.doctorId === doctor.doctorId ? (
                    <button onClick={handleSave}>Save</button>
                  ) : (
                    <button onClick={() => handleEdit(doctor)}>Edit</button>
                  )}
                  <button onClick={() => handleDelete(doctor.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default AdminDoctor;
