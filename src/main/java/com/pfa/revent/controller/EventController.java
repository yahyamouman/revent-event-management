package com.pfa.revent.controller;
import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.Participation;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.EventRepository;
import com.pfa.revent.repository.UserRepository;
import com.pfa.revent.security.Guard;
import com.pfa.revent.security.entity.MyUserDetails;
import com.pfa.revent.service.EventService;
import com.pfa.revent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class EventController {

    @Autowired
    private Guard guard;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/browse")
    public String browseEvents(Model model,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "keyword" , required = false, defaultValue = "") String keyword,
                               @RequestParam(name = "type" , required = false, defaultValue = "all") String type) {
        if (!type.equals("all")) {
            Page<Event> events = eventRepository.findEventByType(type, PageRequest.of(page - 1, (int) eventRepository.findAll(PageRequest.of(page - 1, 3)).getTotalElements()));
            model.addAttribute("events", events);
            model.addAttribute("size", events.getTotalElements());
        }
        else if (!keyword.equals("")) {
            Page<Event> events = eventRepository.findEventByEventTitleOrUsername(keyword, PageRequest.of(page - 1, (int) eventRepository.findAll(PageRequest.of(page - 1, 3)).getTotalElements()));
            model.addAttribute("events", events);
            model.addAttribute("size", events.getTotalElements());
        }
        else {
            Page<Event> events = eventRepository.findAll(PageRequest.of(page - 1, 3));
            model.addAttribute("events", eventRepository.findAll(PageRequest.of(page - 1, 3)));
            model.addAttribute("size", eventRepository.findAll(PageRequest.of(page - 1, 3)).getTotalElements());
        }
        model.addAttribute("all", eventRepository.findAll(PageRequest.of(0, 4)));
        return "event/browse-all-events";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','EDITOR')")
    @GetMapping("/events")
    public String manageEvents(Principal principal, Authentication authentication, Model model,
                               @RequestParam(defaultValue = "1") int page) {
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        String role = userService.userType(authentication);

        model.addAttribute("events", eventService.findAllByRole(principal,authentication,page));

        return "event/all-events";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','EDITOR')")
    @GetMapping("/create")
    public String createEvent(Event event) {
        return "redirect:/events";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/eventform")
    public String showSignUpForm(Event event) {
        return "event/add-event";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','EDITOR')")
    @PostMapping("/addevent")
    //upload file button
    public String addEvent(Authentication authentication, Principal currentprincipal, @Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "event/add-event";
        }
        //inject organizer
        MyUserDetails principal = (MyUserDetails) authentication.getPrincipal();
        event.setOrganizer(userRepository.findByUsername(principal.getUsername()).orElse(null));

        //default thumbnail
        event.setThumbnailName("default-thumbnail.png");

        //deal with tags
        //Take (string) tag -> tagService.exists(tag) ? eventTag.save((Tag) tag, (Event) event); tag.save(tag) &&
        //deal with media
        //Description too short
        eventRepository.save(event);

        model.addAttribute("events", eventService.findAllByRole(currentprincipal,authentication,1));
        return "event/all-events";
    }


    //////////////////////////////USER ACTIONS//////////////////////////////////////////////////
    @GetMapping("event/{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") long eventId, Model model) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event eventId:" + eventId));
        model.addAttribute("event", event);
        return "event/update-event";
    }

    @PostMapping("event/{eventId}/update")
    public String adminUpdateEvent(Authentication authentication, Principal principal,
                                   @PathVariable("eventId") long eventId, @Valid Event event, BindingResult result, Model model) {
        if (result.hasErrors()) {
            event.setEventId(eventId);
            return "event/update-event";
        }

        eventService.update(eventId, event);

        model.addAttribute("events", eventService.findAllByRole(principal,authentication,1));
        return "event/all-events";
    }

    // ALERT : DELETES USERS ?? From the Cascade Property
    @GetMapping("event/{eventId}/delete")
    public String adminDeleteEvent(Authentication authentication, Principal principal, @PathVariable("eventId") long eventId, Model model) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event eventId:" + eventId));
        eventRepository.delete(event);
        model.addAttribute("events", eventService.findAllByRole(principal,authentication,1));
        return "event/all-events";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////


    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/event/{eventId}/page")
    public String userPage(@PathVariable("eventId") long eventId, Model model){
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid user eventId:" + eventId));
        model.addAttribute("event", event);
        return "event/event-page";
    }

}