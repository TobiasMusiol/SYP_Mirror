import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';

import { alert } from './alert.module';
import { user } from './user.module';

Vue.use(Vuex);

const userState = createPersistedState({
  paths: ['user'],
});

const store = new Vuex.Store({
  modules: {
    alert: alert,
    user: user,
  },
  plugins: [userState],
});

export default store;
