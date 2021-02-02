const config = {
  urls: {
    base: process.env.VUE_APP_BASE_URL,
    backend: {
      base: process.env.VUE_APP_BACKEND_URL,
      auth: `${process.env.VUE_APP_BACKEND_URL}/auth`,
    },
  },
  headers: {
    'Content-Type': 'application/json',
  },
  userTypes: {
    USER: 'USER',
    FM: 'FACILITY_MANAGER',
    OW: 'OFFICE_WORKER',
    ADMIN: 'ADMIN',
  },
  sensorTypes: {
    light: {
      mcuid: 1001,
      sensorType: 'light',
    },
  },
};

export default config;
