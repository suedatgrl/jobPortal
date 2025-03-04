package com.example.jobportal.controller;
import com.example.jobportal.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
//import ch.qos.logback.core.model.Model; Bu importta addAttribute çalışmıyordu.
import com.example.jobportal.entity.Users;
import com.example.jobportal.entity.UsersType;
import com.example.jobportal.services.UsersTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersTypeService usersTypeService, UsersService usersService){ //Constructor Injection   //Setter Injection
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes= usersTypeService.getAll();
        model.addAttribute("user", new Users());
        model.addAttribute("getAllTypes", usersTypes);
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model){

        Optional<Users> optionalUsers = usersService.GetUserByEmail(users.getEmail());
        if(optionalUsers.isPresent()){
            model.addAttribute("error", "User email already exists, register with another email");
            List<UsersType> usersTypes= usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersTypes); //BURALAR BENDE FARKLI
            model.addAttribute("user", new Users());
            return "register";
        }

        usersService.addNew(users);
        return "redirect:/dashboard/";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        }
        return "redirect:/dashboard/";
    }
}


