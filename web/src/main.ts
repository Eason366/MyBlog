import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import axios from 'axios';
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';
import 'highlight.js/styles/googlecode.css';
import './util/rem.js';


axios.defaults.baseURL = process.env.VUE_APP_API_HOST;


/**
 * axios interceptor
 */
axios.interceptors.request.use(function (config) {
    console.log('Parameters：', config);
    return config;
}, error => {
    console.log('Error：', error);
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('Result：', response);
    return response;
}, error => {
    console.log('Error：', error);
    return Promise.reject(error);
});

createApp(App).use(store).use(router).use(Antd).use(mavonEditor).mount('#app');
