package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    DoctorRepository doctorRepository;

    public DoctorAvailability addAvailability(DoctorAvailability availability) {
        Optional<Doctor> doctor = doctorRepository.findByDoctorId(availability.getDoctorId());
        if(doctor.isEmpty()) {
            throw new RuntimeException("Add doctor first");
        }
        return availabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getAvailabilityByDoctorId(String doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    public DoctorAvailability updateAvailability(int id, DoctorAvailability updatedAvailability) {
        DoctorAvailability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        existingAvailability.setFromDate(updatedAvailability.getFromDate());
        existingAvailability.setEndDate(updatedAvailability.getEndDate());
        return availabilityRepository.save(existingAvailability);
    }

    public boolean deleteAvailability(int id) {
        DoctorAvailability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        availabilityRepository.deleteById(id);
        return true;
    }
}
