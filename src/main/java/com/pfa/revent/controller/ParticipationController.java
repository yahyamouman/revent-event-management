package com.pfa.revent.controller;


import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.Participation;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.EventRepository;
import com.pfa.revent.repository.ParticipationRepository;
import com.pfa.revent.repository.UserRepository;
import com.pfa.revent.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ParticipationController {

    @Autowired
    ParticipationRepository participationRepository;

    @Autowired
    ParticipationService participationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;


    /////////////////////////PARTICIPATING USER/////////////////////////////////////////////////////

    @GetMapping("/event/{eventId}/page/participation")
    public String participate(Authentication authentication, Principal principal, @PathVariable(name = "eventId") long eventId, @RequestParam(name = "type") String type){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        assert user != null;
        long userId = user.getUserId();

        Event event = eventRepository.findById(eventId).orElse(null);

        //check if participation already exists
        Optional<Participation> participation = participationRepository.getParticipationByUserAndEvent(userId,eventId);
        if(participation.isPresent()) {
            Participation currentParticipation = participation.get();
            //check if participation type has changed
            if(!currentParticipation.getParticipationType().equals(type)) {
                //update existing participation
                currentParticipation.setParticipationType(type);
                participationRepository.save(currentParticipation);
            }
            //do nothing
        }
        //save new participation
        else {
            Participation newParticipation = new Participation();
            newParticipation.setParticipant(user);
            newParticipation.setParticipatedEvent(event);
            newParticipation.setParticipationType(type);
            participationRepository.save(newParticipation);
        }
        return "redirect:/event/"+eventId+"/page";
    }
/*

@PostMapping("/event/{eventId}/page/participation")
    public String participate(Authentication authentication, Principal principal, @PathVariable(name = "eventId") long eventId, String participationType){

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        assert user != null;
        long userId = user.getUserId();

        Event event = eventRepository.findById(eventId).orElse(null);

        //check if participation already exists
        Optional<Participation> participation = participationRepository.getParticipationByUserAndEvent(userId,eventId);
        if(participation.isPresent()) {
            Participation currentParticipation = participation.get();
            //check if participation type has changed
            if(!currentParticipation.getParticipationType().equals(participationType)) {
                //update existing participation
                currentParticipation.setParticipationType(participationType);
                participationRepository.save(currentParticipation);
            }
            //do nothing
        }
        //save new participation
        else {
            Participation newParticipation = new Participation();
            newParticipation.setParticipant(user);
            newParticipation.setParticipatedEvent(event);
            newParticipation.setParticipationType(participationType);
            participationRepository.save(newParticipation);
        }
        return "redirect:/event/"+eventId+"/page";
    }
*/


    @GetMapping("/event/{eventId}/page/participation/cancel")
    public String cancelParticipation(Authentication authentication, Principal principal, @PathVariable("eventId") long eventId, Model model) {

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        long userId = user.getUserId();

        Event event = eventRepository.findById(eventId).orElse(null);

        Optional<Participation> participation = participationRepository.getParticipationByUserAndEvent(userId,eventId);
        participationRepository.delete(participation.get());

        return "redirect:/profile";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////EVENT OWNER/////////////////////////////////////////////////////////////

    @GetMapping("/event/participations")
    public String eventParticipations(@RequestParam(name = "eventId") long eventId, Model model/*, @RequestParam(defaultValue = "1") int page*/){

/*
        model.addAttribute("participations",participationRepository.getAllParticipationsByParticipatedEvent(eventId, PageRequest.of(page-1, 3)));
*/
        model.addAttribute("participations",participationRepository.getAllParticipationsByParticipatedEvent2(eventId));

        //additional stats here
        model.addAttribute("participationNumber",participationService.participationNumber(eventId));
        //////////////////////

        return "event/event-participations";
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}
