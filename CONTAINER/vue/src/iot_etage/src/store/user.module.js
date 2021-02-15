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
        // username und usertype setzen
        let [_, tokenBody, __] = user_token['user-token'].split('.');

        let encodedTokenInfo = JSON.parse(atob(tokenBody));

        state.username = encodedTokenInfo.sub;
        state.usertype = encodedTokenInfo.auth.authority;
        state.isAuthenticated = true;

        localStorage.setItem('user-token-exp', encodedTokenInfo.exp);

        // this.commit('getApps');
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
                // localStorage.setItem('apps', state.apps);
              });
            }
          })
          .catch((e) => {
            this.$store.commit('toggleAlert', {
              alertType: 'info',
              alertMessage: 'Login fehlgeschlagen',
              showAlert: true,
            });
          });
      } else {
        this.commit('toggleAlert', {
          alertType: 'info',
          alertMessage: 'Login fehlgeschlagen',
          showAlert: true,
        });
      }
    },

    isAuthenticated(state) {
      if (state.isAuthenticated) {
        let tokenExp = localStorage.getItem('user-token-exp');
        if (tokenExp) {
          let renewIn = calculateRenewToken(tokenExp);

          if (renewIn > FIVE_MINUTES) {
            return state.isAuthenticated;
          } else if (renewIn < FIVE_MINUTES && renewIn > 0) {
            fetch(`${config.urls.backend.auth}/renewtoken`, {
              method: 'post',
              headers: {
                ...config.headers,
                'Authorization': localStorage.getItem('user-token'),
              },
            }).then(async (response) => {
              if (response.status === 200) {
                const data = await response.json();
                this.commit('authenticateUser', {
                  ...data,
                });
              }
            });
          } else {
            this.commit('logoutUser');
          }
        }
      }
      return state.isAuthenticated;
    },

    getApps(state) {
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
              localStorage.setItem('apps', state.apps);
            });
          }
        })
        .catch((e) => {
          this.$store.commit('toggleAlert', {
            alertType: 'info',
            alertMessage: 'Login fehlgeschlagen',
            showAlert: true,
          });
        });
    },

    logoutUser(state) {
      localStorage.removeItem('user-token');
      localStorage.removeItem('user-token-exp');
      state.username = null;
      state.usertype = null;
      state.apps = null;
      state.isAuthenticated = false;
      localStorage.removeItem('vuex');
    },
  },
  actions: {},
};

const FIVE_MINUTES = 1000 * 60 * 5;

function calculateRenewToken(expireToken) {
  let now = Date.now();

  let diff = expireToken * 1000 - now;

  return diff;
}
