package com.pfa.revent.service;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.EventRepository;
import com.pfa.revent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Service
public class EventService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EventRepository eventRepository;

    public Page<Event> findEventsByOrganizer(Principal principal, Pageable pageable) {
        User principalUser = userRepository.findByUsername(principal.getName()).orElse(null);
        if(principalUser != null) {
            long organizerId = principalUser.getUserId();
            return eventRepository.findEventsByOrganizerId(organizerId, pageable);
        }
        return null;
    }

    public Page<Event> findSubEvents(Principal principal, Pageable pageable) {
        User principalUser = userRepository.findByUsername(principal.getName()).orElse(null);
        long moderatorId = principalUser.getUserId();
        return eventRepository.findSubEventsOfId(moderatorId, pageable);
    }

    public void update(long eventId, Event updateEvent){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event!=null){
            event.setEventTitle(updateEvent.getEventTitle());
            event.setCity(updateEvent.getCity());
            event.setRegion(updateEvent.getRegion());
            event.setZipCode(updateEvent.getZipCode());
            event.setStreet(updateEvent.getStreet());
            event.setOtherAddressInformation(updateEvent.getOtherAddressInformation());
            event.setStartDate(updateEvent.getStartDate());
            event.setEndDate(updateEvent.getEndDate());
            event.setEventType(updateEvent.getEventType());
            event.setDescription(updateEvent.getDescription());
            event.setThumbnailMediaId(updateEvent.getThumbnailMediaId());
            eventRepository.save(event);
        }
        else {
            System.out.println("Couldn't find or update event :" + eventId);
        }
    }

    public Page<Event> findAllByRole(Principal principal, Authentication authentication, int page){
        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        String role = userService.userType(authentication);
        if (role == "ADMIN") {
            return eventRepository.findAll(PageRequest.of(page - 1, 3));
        } else if (role == "MODERATOR") {
            //sub or my events
            return findSubEvents(principal, PageRequest.of(page - 1, 3));
        } else if (role == "EDITOR") {
            return findEventsByOrganizer(principal, PageRequest.of(page - 1, 3));
        }
        else {
            return null;
        }
    }

    public void saveImage(MultipartFile imageFile, long eventId) throws IOException {
        String folder = "./src/main/resources/static/img/event/";
        if(imageFile != null){
            byte[] bytes = imageFile.getBytes();
            Event event = eventRepository.getOne(eventId);

            String ext = imageFile.getContentType();
            event.setThumbnailName(eventId + "." + ext.split("/")[1]);
            eventRepository.save(event);
            Path path = Paths.get(folder + eventId + "." + ext.split("/")[1]);
            Files.write(path,bytes);
        }
        else{
            throw new IOException();
        }
    }
}
