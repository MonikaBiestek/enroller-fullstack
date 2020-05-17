<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
	  <button @click="registering = false"
	          :class="registering ? 'button-outline' : ''">Loguję się</button>
	  <button @click="registering = true"
	          :class="!registering ? 'button-outline' : ''">Rejestruję się</button>
      
	  <div v-if="error" class="error-alert">{{ error }}</div>
	  
	  <login-form @login="login($event)" 
	              v-if="registering == false"></login-form>
	  <login-form @login="register($event)"
	        button-label="Zarejestruj się"
			v-else></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
				registering: false,
				error: ''
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
			register(user) {
			 this.error = '';
			 this.$http.post('participants', user)
				 .then(response => {
					 // udało się
				 })
				 .catch(response => {
					 this.error = 'Taki użytkownik juz istnieje';     
				 });
			},
            logout() {
                this.authenticatedUsername = '';
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }
  
  .error-alert {
     border: 2px dotted red;
	 background: pink;
	 padding: 10px;
	 text-align: center;
	 border-radius: 50%;
  }
</style>

