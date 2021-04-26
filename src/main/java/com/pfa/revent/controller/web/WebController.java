package com.pfa.revent.controller.web;

import com.pfa.revent.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/")
    public String Home(Model model){
        model.addAttribute("events",eventRepository.findAll(PageRequest.of(0,3)));
        return "index";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','EDITOR','VIEWER')")
    @GetMapping(value = "/home")
    public String postHome(Model model){
        model.addAttribute("events",eventRepository.findAll(PageRequest.of(0,3)));
        return "post-index";
    }

    @RequestMapping(value = "/browse")
    public String browse(){
        return "browse";
    }


    @RequestMapping(value = "/contact")
    public String contact(){
        return "contact";
    }

}
