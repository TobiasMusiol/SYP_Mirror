import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';
// import 'vuetify/dist/vuetify.min.css';

import '@fortawesome/fontawesome-free/css/all.css';
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';

Vue.component('font-awesome-icon', FontAwesomeIcon); // Register component globally
library.add(fas);

Vue.use(Vuetify);

//const opts = {}

export default new Vuetify({
  icons: {
    iconfont: 'fa',
  },
});