import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Register from './components/Register';
import VerifyEmail from './components/VerifyEmail';
import Login from './components/Login';
import ForgotPassword from './components/ForgotPassword';
import ResetPassword from './components/ResetPassword';
import Doctor_Profile from './components/Doctor_Profile';
import DoctorDashboard from './components/DoctorDashboard';
import PatientDashboard from './components/PatientDashboard';
import AvailabilityPage from './components/AvailabilityPage';
import AppointmentsPage from './components/AppointmentsPage';
import PatientAppointmentsPage from './components/PatientAppointmentsPage';
import AddPatient from './components/AddPatient';
import MakeAppointment from './components/MakeAppointment';
import DoctorDetails from './components/DoctorDetails';
import FindDoctors from './components/FindDoctors';
import UpdateAppointment from "./components/UpdateAppointment";
import CancelAppointment from "./components/CancelAppointment";
import Payments from "./components/Payments";
import Chatbot from './components/Chatbot';
import DoctorFeedback from './components/DoctorFeedback';
import AllPatientFeedback from './components/AllPatientFeedback';
import PendingPatientFeedback from './components/PendingPatientFeedback';
import AdminDashboard from "./components/admin/AdminDashboard";
import AdminDoctor from "./components/admin/AdminDoctor";

import AdminAddPatient from "./components/admin/AdminAddPatient";
import AdminUpdatePatient from "./components/admin/AdminUpdatePatient";
import AdminDeletePatient from "./components/admin/AdminDeletePatient";
import AllPatient from "./components/admin/AllPatient";

import PatientStats from "./components/admin/PatientStats";
import DoctorStats from "./components/admin/DoctorStats";
import WebStats from "./components/admin/WebStats";
import AdminAppoint from "./components/admin/AdminAppoint";

import AdminAddDoctor from './components/admin/AdminAddDoctor';
import AdminUpdateDoctor from './components/admin/AdminUpdateDoctor';
import AdminDeleteDoctor from './components/admin/AdminDeleteDoctor';
import AllDoctor from "./components/admin/AllDoctor";
import AdminAddAppointment from './components/admin/AdminAddAppointmet';
import AdminUpdateAppointment from './components/admin/AdminUpdateAppointment';
import AdminDeleteAppointment from './components/admin/AdminDeleteAppointment';
import AdminAllAppointment from './components/admin/AdminAllAppointment';



function App() {
  return (
    <Router>
      <Chatbot /> {/* Chatbot component will be visible on every page */}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/verify-email" element={<VerifyEmail />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/doctor-dashboard" element={<DoctorDashboard />} />
        <Route path="/patient-dashboard" element={<PatientDashboard />} />
        <Route path="/availability" element={<AvailabilityPage/>}/>
        <Route path="/appointments" element={<AppointmentsPage />} />
        <Route path="/patient-appointments" element={<PatientAppointmentsPage />} />
        <Route path="/doctor-profile" element={<Doctor_Profile />} />
        <Route path="/add-patient" element={<AddPatient />} />
        <Route path="/doctor-details/:doctorId" element={<DoctorDetails />} />
        <Route path="/make-appointment" element={<MakeAppointment />} />
        <Route path="/find-doctors" element={<FindDoctors />} />
        <Route path="/update-appointment" element={<UpdateAppointment />} />
        <Route path="/cancel-appointment" element={<CancelAppointment />} />
        <Route path="/payments" element={<Payments />} />
        <Route path="/doc-feedback" element={<DoctorFeedback />} />
        <Route path="/all-patient-feedback" element={<AllPatientFeedback />} />
        <Route path="/pending-patient-feedback" element={<PendingPatientFeedback />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />

        <Route path="/admin-addPatient" element={<AdminAddPatient />} />
        <Route path="/admin-updatePatient" element={<AdminUpdatePatient />} />
        <Route path="/admin-deletePatient" element={<AdminDeletePatient />} />
        <Route path="/all-patient" element={<AllPatient />} />

        
        <Route path="/admin-addDoctor" element={<AdminAddDoctor />} />
        <Route path="/admin-updateDoctor" element={<AdminUpdateDoctor />} />
        <Route path="/admin-deleteDoctor" element={<AdminDeleteDoctor />} />
        <Route path="/all-doctor" element={<AllDoctor />} />

        <Route path="/admin-addappointment" element={<AdminAddAppointment />} />
        <Route path="/admin-updateappointment" element={<AdminUpdateAppointment />} />
        <Route path="/admin-deleteappointment" element={<AdminDeleteAppointment />} />
        <Route path="/admin-allappointments" element={<AdminAllAppointment />} />



        <Route path="/admin-doctor" element={<AdminDoctor />} />
        <Route path="/patients-stats" element={<PatientStats />} />
        <Route path="/doctors-stats" element={<DoctorStats />} />
        <Route path="/web-stats" element={<WebStats />} />
        <Route path="/admin-appointments" element={<AdminAppoint />} />
      </Routes>
    </Router>
  );
}

export default App;
