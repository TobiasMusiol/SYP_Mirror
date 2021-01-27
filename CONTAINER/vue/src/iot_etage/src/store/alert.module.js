export const alert = {
  state: {
    displayAlert: false,
    alertMessage: '',
    alertType: '',
    alertTimer: undefined,
  },
  getters: {},

  mutations: {
    toggleAlert(
      state,
      { alertType = '', alertMessage = '', showAlert = false }
    ) {
      console.log(this);
      if (showAlert) {
        if (state.alertTimer) {
          clearTimeout(state.alertTimer);
        }
        state.alertType = alertType;
        state.alertMessage = alertMessage;
        state.alertTimer = this.dispatch('toggleAlert5000', {});
        state.displayAlert = true;
      } else {
        clearTimeout(state.alertTimer);
        state.alertTimer = null;
        state.alertType = '';
        state.alertMessage = '';
        state.displayAlert = false;
      }
    },
  },

  actions: {
    toggleAlert5000({ commit }) {
      return setTimeout(
        () =>
          commit('toggleAlert', {
            alertType: '',
            alertMessage: '',
            showAlert: false,
          }),
        5000
      );
    },
  },
};
