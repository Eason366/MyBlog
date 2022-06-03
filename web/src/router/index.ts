import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

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
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-blog.vue')
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-category.vue')
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-doc.vue')
  },
  {
    path: '/doc',
    name: 'Doc',
    component: () => import(/* webpackChunkName: "about" */ '../views/doc.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: () => import(/* webpackChunkName: "about" */ '../views/admin/admin-user.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
