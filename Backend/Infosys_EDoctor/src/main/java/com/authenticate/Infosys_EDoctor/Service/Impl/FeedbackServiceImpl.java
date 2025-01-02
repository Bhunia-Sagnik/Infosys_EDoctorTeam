package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Feedback;
import com.authenticate.Infosys_EDoctor.Repository.FeedbackRepository;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    DoctorService doctorService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Feedback submitFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getFeedbackByDoctor(String doctorId) {
        return feedbackRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Feedback> getFeedbackByPatient(String patientId) {
        return feedbackRepository.findByPatientId(patientId);
    }

    @Override
    public List<String> getDoctorsWithFeedbackByPatient(String patientId) {
        return feedbackRepository.findDoctorIdsByPatientId(patientId);
    }


    @Override
    public Double getAvgFeedbackByDoctor(String doctorId) {
        return feedbackRepository.findAverageRatingByDoctorId(doctorId);
    }

}

