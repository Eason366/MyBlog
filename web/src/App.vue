<template>
  <a-layout>
    <a-layout-header class="header" :style="{height:'64px', position: 'fixed', zIndex: 1, width: '100%'}">
      <div class="logo"> Eason's Blog </div>
      <a-menu
          v-model:selectedKeys="selectedKeys1"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1">nav 1</a-menu-item>
        <a-menu-item key="2">nav 2</a-menu-item>
        <a-menu-item key="3">nav 3</a-menu-item>
      </a-menu>
    </a-layout-header>
    <router-view/>
    <a-layout-footer style="text-align: center">
      Blog ©2022 Created by Eason
    </a-layout-footer>
  </a-layout>
</template>


<style>
.logo {
  width: 120px;
  height: 31px;
  float: left;
  color: white;
  font-size: 18px;
}

</style>
