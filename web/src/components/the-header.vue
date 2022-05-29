<template>
  <a-layout-header class="header" :style="{height:'64px', position: 'fixed', zIndex: 1, width: '100%'}">
    <div class="logo"> Eason's Blog </div>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
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
  width: 120px;
  height: 31px;
  float: left;
  color: white;
  font-size: 18px;
}

</style>