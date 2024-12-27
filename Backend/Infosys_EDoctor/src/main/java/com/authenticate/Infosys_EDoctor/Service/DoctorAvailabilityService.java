package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;

import java.util.List;

public interface DoctorAvailabilityService {
    public DoctorAvailability addAvailability(DoctorAvailability availability);
    public List<DoctorAvailability> getAvailabilityByDoctorId(String doctorId);
    public DoctorAvailability updateAvailability(int id, DoctorAvailability updatedAvailability);
    public boolean deleteAvailability(int id);
}
