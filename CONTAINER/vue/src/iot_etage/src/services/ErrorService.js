import store from '../store/store';

export default class ErrorService {
  constructor() {
    // this.initHandler();
  }

  static onError(error) {
    const response = error.response;
    console.log(response);

    if (response && response.status === 401) {
      //Not Authorized => Logout User
      store.state.user.commit('logoutUser');
    } else if (response && response.status === 403) {
      store.state.alert.commit('toggleAlert', {
        alertType: 'info',
        alertMessage: 'Sie habe keine Rechte dafür',
        showAlert: true,
      });
    } else {
      console.log(error);
    }
    // Send Error to Log Engine e.g LogRocket
    //ErrorService.logRocketLogEngine(error);
  }

  static initHandler() {
    const scope = this;
    window.onerror = (message, url, lineNo, columnNo, error) => {
      console.log(error, 'test');
      if (error) {
        scope.onError(error);
        console.log(message, url, lineNo, columnNo, error);
      }
    };
  }
}
