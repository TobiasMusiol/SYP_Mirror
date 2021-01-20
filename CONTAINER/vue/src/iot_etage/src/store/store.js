import Vue from 'vue';
import Vuex from 'vuex';

import { alert } from './alert.module';
import { user } from './user.module';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    alert: alert,
    user: user,
  },
});

export default store;
