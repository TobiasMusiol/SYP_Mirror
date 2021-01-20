// import { VueRouter } from 'vue-router';

import Main from '../components/Main/Main.vue';
import Footer from '../components/Base/Footer.vue';
import NotFound404 from '../components/Main/NotFound404.vue';
import VueRouter from 'vue-router';

import store from '../store/store';

const routes = [
  {
    path: '*',
    components: {
      mainview: NotFound404,
    },
  },
  {
    path: '/',
    components: {
      mainview: Main,
      footerview: Footer,
    },
  },
  {
    path: '/login',
    components: {
      mainview: async () => await import('../components/Main/Login.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/sensors',
    components: {
      mainview: async () => await import('../components/Main/Sensor.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/events',
    components: {
      mainview: async () => await import('../components/Main/Events.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/beleuchtungssteuerung',
    components: {
      mainview: async () =>
        await import('../components/Main/Beleuchtungssteuerung.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/beleuchtungssteuerung/edit',
    components: {
      mainview: async () =>
        await import('../components/Main/BeleuchtungssteuerungEdit.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/markisolettensteuerung',
    components: {
      mainview: async () =>
        await import('../components/Main/Markisoletensteuerung.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/belueftungssteuerung',
    components: {
      mainview: async () =>
        await import('../components/Main/Belueftungssteuerung.vue'),
      footerview: Footer,
    },
  },
  {
    path: '/raumstatus',
    components: {
      mainview: async () => await import('../components/Main/RaumStatus.vue'),
      footerview: Footer,
    },
  },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

router.beforeEach((to, from, next) => {
  store.commit('isAuthenticated');
  console.log(store.state.user);
  console.log(store.state.user.isAuthenticated);
  if (store.state.user.isAuthenticated || to.path === '/login') {
    return next();
  }

  next('/login');
});

export default router;
