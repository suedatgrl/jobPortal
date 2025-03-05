package com.example.jobportal.services;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.repository.RecruiterProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id) {
        return recruiterProfileRepository.findById(id);
    } //getOne method is used to get the recruiter profile of the current user. by passing the id of the current user.
    // optional<recruiterprofile> using optional make sure that the object is not null.
    // if recruiterprofile is null then it will return empty optional.
    // and recruiterprofile is the object of the recruiterprofile class.

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {

        return recruiterProfileRepository.save(recruiterProfile);
    }
}
