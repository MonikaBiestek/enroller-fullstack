<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
        methods: {
  
            addNewMeeting(meeting) {
				this.$http.post("meetings", meeting)
					.then((response) => {      
						meeting.id=response.data.id
						this.meetings.push(meeting);
					})
					.catch(response => alert('Błąd przy dodawaniu spotkania. Kod odpowiedzi: ' + response.status))
			},

            addMeetingParticipant(meeting) {
				this.$http.post("meetings/" + meeting.id + "/participants/" + this.username)
					.then(response => meeting.participants.push(response.data.login))
					.catch(response => alert('Błąd przy dodawaniu uczestnika. Kod odpowiedzi: ' + response.status))
				},            
              
            removeMeetingParticipant(meeting) {           
				this.$http.delete("meetings/" + meeting.id + "/participants/" + this.username)
					.then( () => meeting.participants.splice(meeting.participants.indexOf(this.username), 1))
					.catch(response => alert('Błąd przy usuwaniu uczestnika. Kod odpowiedzi: ' + response.status))
				},
           

            deleteMeeting(meeting) {
				this.$http.delete("meetings/" + meeting.id)
					.then(() =>this.meetings.splice(this.meetings.indexOf(meeting), 1))
					.catch(response => alert('Błąd przy usuwaniu spotkania. Kod odpowiedzi: ' + response.status))
				},
                    
        },
        
        mounted() {   
            
        this.$http.get('meetings')
			.then(response => {
				response.data.forEach(meeting=>{
				meeting.participants = [];
				this.meetings.push(meeting)
        
				this.$http.get('meetings/' + meeting.id + '/participants')
					.then(response=>{
						response.data.forEach((participant=>{
						meeting.participants.push(participant.login);
						}))})       
				})
				.catch(response => alert('Błąd przy pobieraniu listy spotkań. Kod odpowiedzi: ' + response.status))
			});
		}
	}
</script>
