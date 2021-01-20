import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';

import vuetify from './plugins/vuetify';

Vue.config.productionTip = false;

import router from './config/router';
import store from './store/store';

Vue.use(VueRouter);

// const router = new VueRouter({
//   mode: 'history',
//   routes,
// });

// console.log(store);

new Vue({
  router,
  vuetify,
  store: store,
  render: (h) => h(App),
}).$mount('#app');
