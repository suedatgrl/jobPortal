package com.example.jobportal.services;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.RecruiterProfileRepository;
import com.example.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional; // this is used to avoid null pointer exception and to check if the object is null or not

@Service
public class UsersService {

    private  final UsersRepository usersRepository; //final bcs we dont want to change the value of this variable and we want to initialize it in the constructor

    private  final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    private  final PasswordEncoder passwordEncoder;
    @Autowired
    public UsersService(UsersRepository usersRepository,
                        JobSeekerProfileRepository jobSeekerProfileRepository,
                        RecruiterProfileRepository recruiterProfileRepository,
                        PasswordEncoder passwordEncoder) { //constructor injection
        this.usersRepository = usersRepository;

        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder =passwordEncoder;
    }

    public Users addNew(Users users){ // this method will save the user and profile
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));

        Users savedUser= usersRepository.save(users); //first save the user bcs we need the user id to save the profile

        users.setPassword(passwordEncoder.encode(users.getPassword()));

        int userTypeId = users.getUserTypeId().getUserTypeId();
        if(userTypeId == 1){ //if user is recruiter
            recruiterProfileRepository.save(new RecruiterProfile(savedUser)); // save the recruiter profile
        }else { //if user is job seeker
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser)); // save the job seeker profile
        }
        return savedUser; //finally return users after saving the profile
    }

    public Optional<Users> GetUserByEmail(String email){ //this method will return the user by email

        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {

        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found" + "user"));

            int userId = users.getUserId();

            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId).orElse(new  RecruiterProfile());
                return recruiterProfile;
            }else {
                JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
                return jobSeekerProfile;
            }

        }
        return null;
    }
}
