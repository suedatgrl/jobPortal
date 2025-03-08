package com.example.jobportal.controller;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.RecruiterJobsDto;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.services.JobPostActivityService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;
import com.example.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    private final UsersService usersService;

    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
    }

    //BURADA KALDIM
    @GetMapping("/dashboard/")//Dashboard for both recruiter and jobseeker
    public String searchJobs(Model model, @RequestParam(value = "job",required = false) String job,
                                          @RequestParam(value = "location",required = false) String location,
                                          @RequestParam(value = "partTime",required = false) String partTime,
                             @RequestParam(value = "fullTime",required = false) String fullTime,
                             @RequestParam(value = "freelance",required = false) String freelance,
                             @RequestParam(value = "remoteOnly",required = false) String remoteOnly,
                             @RequestParam(value = "officeOnly",required = false) String officeOnly,
                             @RequestParam(value = "partialRemote",required = false) String partialRemote,
                             @RequestParam(value = "today",required = false) boolean today,
                             @RequestParam(value = "days7",required = false) boolean days7,
                             @RequestParam(value = "days30",required = false) boolean days30
                             ) {

        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30){
            searchDate = LocalDate.now().minusDays(30);

        }else if (days7){
            searchDate = LocalDate.now().minusDays(7);
        }else if (today){
            searchDate = LocalDate.now();
        }else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance ==null){
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote =false;
        }

        if(officeOnly==null && remoteOnly == null && partialRemote == null){
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        //ın here request param things and their statements are checked and if they are true then the jobPost list is updated
        //example we request param in full time and then added model attribute about fulltime and added some condition about it. all these filters are checked and if they are true then the jobPost list is updated


        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername =authentication.getName();
            model.addAttribute("username",currentUsername);

            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {

                List<RecruiterJobsDto> recruiterJobs= jobPostActivityService.getRecruiterJobs(((RecruiterProfile)
                        currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            }
        }
        model.addAttribute("user",currentUserProfile);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")//Post new job button in recruiter dashboard profile
    public String addJobs(Model model) {

        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")//Post new job button in recruiter dashboard profile
    public String addNew(JobPostActivity jobPostActivity, Model model) {

        Users user = usersService.getCurrentUser();
        if(user !=null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity",jobPostActivity);
        JobPostActivity save =jobPostActivityService.addNew(jobPostActivity); //Transection hatası save den doalyı
        return "redirect:/dashboard/";
    }

    @PostMapping("/dashboard/edit/{id}")//Edit job button in recruiter dashboard profile
    public String editJob(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity",jobPostActivity);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }
}
