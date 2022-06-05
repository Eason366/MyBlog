<template>
  <a-layout-header class="header" :style="{height:'0.64rem', position: 'fixed', zIndex: 1, width: '100%'}">
    <div class="logo" :style="{}"> Eason's Blog </div>

    <a class="login-sign" v-show="!user.id" @click="showLoginModal">
      <span>Login</span>
    </a>

    <a-popconfirm
        title="Are you sure Logout?"
        ok-text="Yes"
        cancel-text="No"
        @confirm="logout"
    >
      <a class="login-sign" v-show="user.id">
        <span>Logout</span>
      </a>
    </a-popconfirm>

    <div class="login-sign" v-show="user.id">
      <span>Welcome: {{user.name}}</span>
    </div>


    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '0.64rem' }"
    >
      <a-menu-item key="/">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">About</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/user">Admin User</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/blog" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/blog">Admin Blog</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category">Admin Category</router-link>
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
import {defineComponent, ref, computed} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'the-header',
  setup () {
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const signModalVisible = ref<boolean>(false);
    const user = computed(() => store.state.user);

    //========================  Login  ========================

    const loginUser = ref({
      loginName: "",
      password: ""
    });

    const showLoginModal = () => {
      loginUser.value.loginName='';
      loginUser.value.password='';
      loginModalVisible.value = true;
    };

    const login = () => {
      loginModalLoading.value = true;
      const tempUser = ref({
        password:hexMd5(loginUser.value.password + KEY),
        loginName:loginUser.value.loginName,
      })
      console.log(loginUser.value)
      axios.post('/user/Login', tempUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("Login Successful！");
          store.commit("setUser", data.content);
          loginUser.value.loginName='';
          loginUser.value.password='';
        } else {
          message.error(data.message);
          loginUser.value.password='';
        }
      });
    };

    //========================  Logout  ========================
    const logout = () => {
      console.log("Logout begin");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("Logout Successful！");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };


    return {
      signModalVisible,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      logout,
      user,
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
.login-sign{
  color: white;
  padding-left: 1rem;
  float: right;
  height: 0.64rem;
  font-size: 0.2rem;
  line-height: 0.64rem;
}

</style>