package com.pfa.revent.service;

import com.pfa.revent.entity.Participation;
import com.pfa.revent.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ParticipationService {

    @Autowired
    ParticipationRepository participationRepository;

    public int participationNumber(long eventId){
        return participationRepository.findParticipationByParticipatedEventEventId(eventId).size();
    }
}
