<template>
  <a-layout-header class="header" :style="{height:'0.64rem', position: 'fixed', zIndex: 1, width: '100%'}">
    <div class="logo" :style="{}"> Eason's Blog </div>
    <a class="login-menu" @click="showLoginModal">
      <span>Login</span>
    </a>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '0.64rem' }"
    >
      <a-menu-item key="/">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">Admin User</router-link>
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

    <a-modal
        title="Login"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        ok-text="Login"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="User Name">
          <a-input v-model:value="loginUser.loginName"
                   placeholder="Please enter Your User Name"/>
        </a-form-item>
        <a-form-item label="Password">
          <a-input-password v-model:value="loginUser.password" type="password"
                            show-count :maxlength="20"
                            placeholder="Please enter Your Password"/>
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'the-header',
  setup () {
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);

    const loginUser = ref({
      loginName: "test",
      password: "test"
    });

    const showLoginModal = () => {
      loginModalVisible.value = true;
    };


    const login = () => {
      console.log("开始登录")
    };





    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login
    }
  }
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
.login-menu {
  float: right;
  color: white;
  width: 1.50rem;
  height: 0.64rem;
  font-size: 0.2rem;
  line-height: 0.64rem;
}

</style>