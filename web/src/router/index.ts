import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import store from "@/store";
import {Tool} from "@/util/tool";
import axios from "axios";
import {message} from "ant-design-vue";

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
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/admin/blog',
    name: 'AdminBlog',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-blog.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-category.vue'),
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-doc.vue'),
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
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-user.vue'),
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
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
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
    if (Tool.isEmpty(loginUser)) {
      console.log("用户未登录！");
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
