package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback submitFeedback(Feedback feedback);

    List<Feedback> getFeedbackByDoctor(String doctorId);

    List<Feedback> getFeedbackByPatient(String patientId);

    List<String> getDoctorsWithFeedbackByPatient(String patientId);

    Double getAvgFeedbackByDoctor(String doctorId);
}
