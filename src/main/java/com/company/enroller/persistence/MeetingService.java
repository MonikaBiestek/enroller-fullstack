package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		return session.createCriteria(Meeting.class).list();
	}

	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);

	}
	public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	
	
	public void  delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}

	public Meeting updateDescription(Meeting meeting, String description) {
		Transaction transaction = this.session.beginTransaction();
		meeting.setDescription(description);
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Meeting updateName(Meeting meeting, String name) {
		Transaction transaction = this.session.beginTransaction();
		meeting.setName(name);
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Meeting updateDate(Meeting meeting, String date) {
		Transaction transaction = this.session.beginTransaction();
		meeting.setDate(date);
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
	
	/*
	 * public Meeting updateParticipants(Meeting meeting, String login) {
	 * Transaction transaction = this.session.beginTransaction();
	 * meeting.setDate(date); session.update(meeting); transaction.commit(); return
	 * meeting; }
	 */
	

	public void addParticipant(Participant participant, Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		meeting.addParticipant(participant);
		transaction.commit();
		//return meeting;
	}
	
	public void removeParticipant(Participant participant, Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		meeting.removeParticipant(participant);
		transaction.commit();
		//return meeting;
	}

	
	
}
