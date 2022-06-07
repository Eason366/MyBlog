import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import store from "@/store";
import {Tool} from "@/util/tool";
import axios from "axios";
import {message} from "ant-design-vue";
import About from '../views/About.vue'
import AdminBlog from '../views/admin/admin-blog.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import AdminUser from '../views/admin/admin-user.vue'


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue')
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: About
  },
  {
    path: '/admin/blog',
    name: 'AdminBlog',
    component: AdminBlog,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: '',
    component: () => AdminDoc,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/doc',
    name: 'Doc',
    component: () => import(/* webpackChunkName: "about" */ '../views/doc.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: AdminUser,
    meta: {
      loginRequire: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    console.log('loginUser',loginUser)
    if (Tool.isEmpty(loginUser)) {
      message.error("Login Please！")
      next('/');
    } else {
      console.log(loginUser.token)
      axios.get("/redis/get/"+loginUser.token).then((response) => {
        if (response.data){
          next();
        }else {
          message.error("Login expired, please login again")
          console.log("Logout begin");
          axios.get('/user/logout/' + loginUser.token).then((response) => {
            const data = response.data;
            if (data.success) {
              store.commit("setUser", {});
            } else {
              message.error(data.message);
            }
          });
          next('/');
        }
      });

    }
  } else {
    const loginUser = store.state.user;
    console.log('loginUser',loginUser)
    if (Tool.isEmpty(loginUser)) {
      message.error("Login Please！")
      next();
    } else {
      console.log(loginUser.token)
      axios.get("/redis/get/"+loginUser.token).then((response) => {
        if (response.data){
          next();
        }else {
          message.error("Login expired, please login again")
          console.log("Logout begin");
          axios.get('/user/logout/' + loginUser.token).then((response) => {
            const data = response.data;
            if (data.success) {
              store.commit("setUser", {});
            } else {
              message.error(data.message);
            }
          });
          next();
        }
      });

    }
  }
});

export default router
