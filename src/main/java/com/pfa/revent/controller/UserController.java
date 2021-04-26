package com.pfa.revent.controller;

import com.pfa.revent.entity.Participation;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.ParticipationRepository;
import com.pfa.revent.repository.UserRepository;
import com.pfa.revent.security.Guard;
import com.pfa.revent.security.entity.MyUserDetails;
import com.pfa.revent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Guard guard;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipationRepository participationRepository;

    ////////////////////////////////ANY NON USER REGISTER///////////////////////////////////////
    @PreAuthorize("permitAll()")
    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "user/add-user";
    }


    @PreAuthorize("permitAll()")
    @PostMapping("/adduser")
    public String addUser(Authentication authentication, Principal principal, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/add-user";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //before saving check parentId injection (done)
        if(authentication != null){
            if(guard.checkModerator(authentication)){
                user.setParentModerator(userRepository.findByUsername(principal.getName()).orElse(null));
            }
        }

        // "ROLE_MODERATOR,ROLE_EDITOR,ROLE_VIEWER"
        // ->["ROLE_MODERATOR","ROLE_EDITOR","ROLE_VIEWER"]
        // -> "ROLE_MODERATOR" -> ["ROLE","MODERATOR"]
        // -> "MODERATOR"
        user.setUserType(user.getRoles().split(",")[0].split("_")[1]);

        user.setAvatarName("default-avatar.png");

        userRepository.save(user);
        return "index";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////USER ACTIONS//////////////////////////////////////////////////
    @GetMapping("user/{userId}/edit")
    public String adminShowUpdateForm(@PathVariable("userId") long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user userId:" + userId));
        user.setUserId(userId);
        model.addAttribute("user", user);
        return "user/update-user";
    }

    @PostMapping("user/{userId}/update")
    public String updateUser(Authentication authentication, Principal principal,
                             @PathVariable("userId") long userId, @Valid User user,
                             BindingResult result, Model model) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User currentUser = userRepository.findByUsername(principal.getName()).get();

        if(!currentUser.getUsername().equals(user.getUsername())){
            userService.update(userId, user);
            return "redirect:/login";
        }

        String role = userService.userType(authentication);

        userService.update(userId, user);

        if (currentUser.getUserId() != userId){
            if(role == "ADMIN"){
                model.addAttribute("users", userRepository.findAll(PageRequest.of(0, 3)));
                return "user/all-users";
            }
            else if (role == "MODERATOR") {
                model.addAttribute("users", userService.findChildUsersByParent(principal, PageRequest.of(0, 3)));
                return "user/all-users";
            }
        }
        model.addAttribute("user",userRepository.findById(userId).orElse(null));
        return "redirect:/profile";
    }


    @GetMapping("user/{userId}/delete")
    public String adminDeleteUser(@PathVariable("userId") long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user userId:" + userId));
        if (userId == user.getUserId()) {
            userRepository.delete(user);
            return "redirect:/";
        }
        model.addAttribute("users", userRepository.findAll(PageRequest.of(0, 3)));
        return "post-index";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////



    //////////////////////////////////////ANY USER PROFILE//////////////////////////////////////
    @GetMapping("/profile")
    public String getProfile(Principal principal , Model model, @RequestParam(name = "page", defaultValue ="1") int page){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("user", user);

        Page<Participation> participations = participationRepository.getAllParticipationsByParticipant(user.getUserId(),PageRequest.of(page-1, 3));
        model.addAttribute("participations", participations);

        return "user/user-page";
    }

    ///EDITING//////////DONE////////////////

    //Awaiting Testing

/*  @GetMapping("/user/{userId}/profile")
    public String userPage(@PathVariable("userId") long userId, Model model){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user userId:" + userId));
        model.addAttribute("user", user);
        return "user/user-page";
    }*/
    /////////////////////////////////////////

    @GetMapping("/user/{userId}/profile/edit")
    public String userShowUpdateForm(@PathVariable("userId") long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user userId:" + userId));
        model.addAttribute("user", user);
        return "user/update-user";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////Manage Users////////////////////////////////////////////////
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @GetMapping("/manage")
    public String manageUsers(Principal principal , Authentication authentication, Model model, @RequestParam(defaultValue = "1") int page){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("parent", user);
        String role = userService.userType(authentication);
        if (role == "ADMIN"){
            model.addAttribute("users", userRepository.findAll(PageRequest.of(page-1, 3)));
            return "user/all-users";
        }
        if (role == "MODERATOR") {
            model.addAttribute("users", userService.findChildUsersByParent(principal, PageRequest.of(page - 1, 3)));
            return "user/all-users";
        }
        return "/profile";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/subusers")
    public @ResponseBody Collection<User> subUsers(Principal principal){
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        return user.getEditors();
    }
}