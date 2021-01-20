import config from '../config/config';

export const user = {
  state: {
    username: null,
    usertype: null,
    isAuthenticated: false,
    apps: null,
  },
  getters: {},
  mutations: {
    authenticateUser(state, user_token) {
      if (user_token.status === 201) {
        localStorage.setItem('user-token', user_token['user-token']);
        let [_, tokenBody, __] = user_token['user-token'].split('.');

        let encodedTokenInfo = JSON.parse(atob(tokenBody));
        console.log(encodedTokenInfo);
        state.username = encodedTokenInfo.sub;
        state.usertype = encodedTokenInfo.auth.authority;
        this.commit('getApps');
      } else {
        console.log(this.$store);
        this.$store.commit('toggleAlert', {
          alertType: 'info',
          alertMessage: 'Login fehlgeschlagen',
          showAlert: true,
        });
      }
    },

    isAuthenticated(state) {
      let userToken = localStorage.getItem('user-token');
      if (userToken) {
        // username und usertype setzen
        let [_, tokenBody, __] = userToken.split('.');

        let encodedTokenInfo = JSON.parse(atob(tokenBody));
        console.log(encodedTokenInfo);
        state.username = encodedTokenInfo.sub;
        state.usertype = encodedTokenInfo.auth.authority;

        //Ablauf des Tokens checken und wenn weniger als 5 Minuten neuen holen (momentan nicht implementiert)

        // Apps holen
        this.commit('getApps');
        state.isAuthenticated = true;
      }

      return this.isAuthenticated;
    },

    getApps(state) {
      let my_header = new Headers({
        ...config.headers,
        'Authorization': localStorage.getItem('user-token'),
      });
      console.log(my_header.get('Authorization'));
      console.log(my_header);
      fetch(`${config.urls.backend.auth}/apps`, {
        headers: {
          ...config.headers,
          'Authorization': localStorage.getItem('user-token'),
        },
      })
        .then((response) => {
          if (response.status === 200) {
            response.json().then((data) => {
              console.log(data);
              state.apps = data;
            });
          }
        })
        .catch((e) => {
          console.log(this.$store);
          this.$store.commit('toggleAlert', {
            alertType: 'info',
            alertMessage: 'Login fehlgeschlagen',
            showAlert: true,
          });
        });
    },

    logoutUser(state) {
      localStorage.removeItem('user-token');
      state.username = null;
      state.usertype = null;
      this.isAuthenticated = false;
    },
  },
  actions: {},
};
