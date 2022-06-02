<template>
  <a-layout-header class="header" :style="{height:'0.64rem', position: 'fixed', zIndex: 1, width: '100%'}">
    <div class="logo" :style="{}"> Eason's Blog </div>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '0.64rem' }"
    >
      <a-menu-item key="/">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/blog">
        <router-link to="/admin/blog">Admin Blog</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">Admin Category</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">About</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'the-header'
});
</script>

<style>
.logo {
  width: 1.50rem;
  height: 0.64rem;
  float: left;
  color: white;
  font-size: 0.2rem;
  line-height: 0.64rem;
}

</style>