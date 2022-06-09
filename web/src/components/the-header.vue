<template>
  <a-layout-header class="header" :style="{height:'0.64rem', position: 'fixed', zIndex: 1, width: '100%'}">
    <div class="logo" :style="{}"> Blog </div>

    <a class="login-sign" v-show="!user.id" @click="showLoginModal">
      <span>Login</span>
    </a>

    <a class="login-sign" v-show="!user.id" @click="showSignModal">
      <span>Sign in</span>
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
        title="New User"
        v-model:visible="signModalVisible"
        ok-text="Submit"
        cancel-text="Cancel"
        @ok="signSubmit"
    >

      <a-form :model="newUser" layout="vertical" :rules="rules" ref="formRef">
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="User Name" name="loginName">
              <a-input v-model:value="newUser.loginName"
                       placeholder="Please enter Your User Name"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="Nick Name" name="name">
              <a-input v-model:value="newUser.name" placeholder="Please enter Your NickName"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="Password" name="password">
              <a-input-password v-model:value="newUser.password"
                                show-count :maxlength="20"
                                placeholder="Please enter Your Password"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="Password Confirm" name="confirm">
              <a-input-password v-model:value="newUser.confirm"
                                show-count :maxlength="20"
                                placeholder="Please enter Your Password again"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>

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
import {defineComponent, reactive, ref, UnwrapRef, computed} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {RuleObject, ValidateErrorEntity} from "ant-design-vue/es/form/interface";
import store from "@/store";

interface FormState {
  loginName: string;
  name: string;
  password: string;
  confirm:string;
  id:number|undefined;
}

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'the-header',
  setup () {
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const signModalVisible = ref<boolean>(false);
    const user = computed(() => store.state.user);


    //========================  Sign in  ========================

    const newUser : UnwrapRef<FormState> = reactive({
      loginName: '',
      name: '',
      password: '',
      confirm:'',
      id:undefined,
    });

    const showSignModal = () => {
      newUser.loginName = ''
      newUser.name = ''
      newUser.password = ''
      newUser.confirm = ''

      signModalVisible.value = true;
    };

    const signSubmit = () => {
      formRef.value
          .validate().then(() => {
        newUser.password= hexMd5(newUser.password + KEY);
        axios.post("/user/save", newUser).then((response) => {
          const data = response.data;
          if (data.success) {
            signModalVisible.value = false;

          }else {
            message.error(data.message)
          }
        });
      })
          .catch((error: ValidateErrorEntity<FormState>) => {
            console.log('error', error);
          });


    };

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
    //========================  Rule  ========================
    const formRef = ref();

    let passwordValidator1 = async(rule: RuleObject, value: string) => {
      if (value && value.indexOf(' ') === -1) {
        return Promise.resolve()
      } else {
        return Promise.reject('Please enter characters without spaces')
      }
    }

    let passwordValidator2 = async(rule: RuleObject, value: string) => {
      let passwordreg = /(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,20}/
      if (!passwordreg.test(value)) {
        return Promise.reject('Password must be a combination of numbers, letters and special characters, please enter 6-20 digits')
      }else{
        return Promise.resolve()
      }
    }

    let confirmValidator = async(rule: RuleObject, value: string) => {
      if (value !== newUser.password) {
        return Promise.reject("Two inputs don't match!");
      } else {
        return Promise.resolve();
      }
    }


    const rules = {
      loginName:[ { required: true, message: 'Please enter Your User Name', trigger: ['change', 'blur'] }],
      name:[ { required: true, message: 'Please enter Your Nick Name', trigger: ['change', 'blur'] }],
      password: [
        { required: true, message: 'Please enter Your Password', trigger: ['change', 'blur'] },
        {
          type: 'string',
          trigger: ['change', 'blur'],
          validator: passwordValidator1,
        },
        {
          trigger: ['change', 'blur'],
          validator: passwordValidator2, }
      ],
      confirm:[
        { required: true, message: 'Please enter Your Password again', trigger: ['change', 'blur'] },
        {
          validator: confirmValidator,
          trigger: ['change', 'blur']
        }
      ],

    }


    return {
      formRef,
      rules,
      newUser,
      showSignModal,
      signSubmit,
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