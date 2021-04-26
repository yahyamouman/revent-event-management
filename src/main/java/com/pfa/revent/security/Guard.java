package com.pfa.revent.security;

import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.User;
import com.pfa.revent.repository.EventRepository;
import com.pfa.revent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Guard {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    public boolean checkUserId(Authentication authentication, long userId) {
        //admin allowed
        if (checkAdmin(authentication)){
            return true;
        }

        //user we want to access
        User user = userRepository.findById(userId).orElse(null);
        String name = authentication.getName();
        User current = userRepository.findByUsername(name).orElse(null);

        //self allowed
        if(current.getUserId() == userId){
            return true;
        }

        //parent moderator allowed
        if(checkModerator(authentication)){
            if(user.getParentModerator().getUserId() == current.getUserId()){
                return true;
            }
        }
        return false;
    }

    //ERROR WHEN CREATING EVENT : USER TYPE EDITOR
    public boolean checkEventId(Authentication authentication, long eventId) {

        Optional<Event> event = eventRepository.findById(eventId);

        User organizer = event.get().getOrganizer();
        long organizerId = organizer.getUserId();
        //admin allowed
        if (checkAdmin(authentication)){
            return true;
        }

        //User data we want to access
        String name = authentication.getName();
        User current = userRepository.findByUsername(name).orElse(null);

        //self allowed
        if(current.getUserId() == organizerId){
            return true;
        }

        //parent moderator allowed
        if(checkModerator(authentication)){
            if(organizer.getParentModerator().getUserId() == current.getUserId()){
                return true;
            }
        }
        return false;
    }

    public boolean checkAdmin(Authentication authentication){
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_ADMIN");
        if(authentication.getAuthorities().contains(role)){
            return true;
        }
        return false;
    }

    public boolean checkModerator(Authentication authentication){
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_MODERATOR");
        if(authentication.getAuthorities().contains(role)){
            return true;
        }
        return false;
    }

    public boolean checkEditor(Authentication authentication){
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_EDITOR");
        if(authentication.getAuthorities().contains(role)){
            return true;
        }
        return false;
    }

    public boolean checkViewer(Authentication authentication){
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_VIEWER");
        if(authentication.getAuthorities().contains(role)){
            return true;
        }
        return false;
    }

}