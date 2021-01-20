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
};

export default config;
