import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import store from "@/store";
import {Tool} from "@/util/tool";

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
      next();
    }
  } else {
    next();
  }
});

export default router
