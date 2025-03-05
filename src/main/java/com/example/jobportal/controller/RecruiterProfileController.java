package com.example.jobportal.controller;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.Users;
import com.example.jobportal.repository.UsersRepository;
import com.example.jobportal.services.RecruiterProfileService;
import com.example.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {



    private final UsersRepository usersRepository;

    private final RecruiterProfileService recruiterProfileService;


    public RecruiterProfileController( UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {

        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/")//Recruiter profile page
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            Users users = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new UsernameNotFoundException("Could not " + "find user"));

            Optional<RecruiterProfile> recruiterProfile =recruiterProfileService.getOne(users.getUserId()); //different from getcurrentuser method in userservice.
            // this method came from recruiterprofileservice

            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile",recruiterProfile.get());
               // model.addAttribute("user", users);
            }
        }
        return "recruiter_profile"; //It should match the name of the html file.
    }



    @PostMapping("/addNew") //Editing page. (first name last name,..., profile phtoto, save buuton)
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {
        Authentication  authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUserName = authentication.getName();
            Users users =
                    usersRepository.findByEmail(currentUserName).orElseThrow(() -> new
                            UsernameNotFoundException("Could not find user"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }

        model.addAttribute("profile",recruiterProfile);
        String fileName = "";
        if(!multipartFile.getOriginalFilename().equals("")){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            if (fileName.length() > 255) {
                fileName = fileName.substring(0, 255);
            }
            recruiterProfile.setProfilePhoto(fileName);
        }
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);

        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try{
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }catch (Exception ex){
            ex.printStackTrace();

        }
        return "redirect:/dashboard/";
    }
}
