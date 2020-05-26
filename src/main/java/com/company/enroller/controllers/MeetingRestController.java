package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST) 
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting=meetingService.findById(meeting.getId());
    if(foundMeeting !=null) {
        return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exists.", HttpStatus.CONFLICT);
    }
    meetingService.add(meeting);
    return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }
    
    
    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id){
	Meeting meeting = meetingService.findById(id);
	if (meeting == null) {
	return new ResponseEntity("Meeting doesn't exist.", HttpStatus.NOT_FOUND);
	}
	return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@PathVariable("id") long id, @PathVariable("login") String login){
	Meeting meeting = meetingService.findById(id);
	ParticipantService participantService = new ParticipantService();
	Participant participant = participantService.findByLogin(login);
	Collection<Participant> foundParticipants=meeting.getParticipants();
	for(Participant p: foundParticipants) {	
	if (p.getLogin().equals(login)) {
		return new ResponseEntity("Unable to add. A participant with login " + login + " already registered to this meeting.", HttpStatus.CONFLICT);
	}}

	meetingService.addParticipant(participant, meeting);
	return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}
    
    
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant1(@PathVariable("id") long id, @RequestBody Participant participant){
	Meeting meeting = meetingService.findById(id);

	Collection<Participant> foundParticipants=meeting.getParticipants();
	for(Participant p: foundParticipants) {	
	if (p.getLogin().equals(participant.getLogin())) {
		return new ResponseEntity("Unable to add. A participant with login " + participant.getLogin() + " already registered to this meeting.", HttpStatus.CONFLICT);
	}}
	meetingService.addParticipant(participant, meeting);
	return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	if (meeting == null) { 
	return new ResponseEntity("Meeting doesn't exist.",HttpStatus.NOT_FOUND);
	} 
	meetingService.delete(meeting);
	return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}
	
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") long id, @PathVariable("login") String login) {
	    Meeting meeting = meetingService.findById(id);
	    Collection<Participant> participants=meeting.getParticipants();
	    for(Participant p: participants) {
	    if ((p.getLogin()).equals(login)) {	    			    	
		meetingService.removeParticipant(p, meeting);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    	}}
	    return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
	}

    
}
