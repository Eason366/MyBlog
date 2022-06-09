<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '0.24rem', margin: 0, minHeight: '2.80rem' }"
        style="padding: 0.78rem 1.00rem"
    >


      <a-input-search
          placeholder="Input Name to Search"
          style="width: 5.00rem"
          size="large"
          enter-button="Search"
          @search="onSearch"
      />


      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button @click="reset(record)">
              Reset Password
            </a-button>

            <a-button type="primary" @click="edit(record)">
                Edit
              </a-button>
            <a-popconfirm
                title="Are you sure delete this Blog?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="onDelete(record.id,record.loginName)"
            >
              <a-button type="primary" danger>
                Delete
              </a-button>
            </a-popconfirm>


          </a-space>
        </template>
      </a-table>
      <a-modal
          title="Update Your Info"
          v-model:visible="edit_visible"
          ok-text="Submit"
          cancel-text="Cancel"
          @ok="edit_Submit"
      >

        <a-form :model="edit_user" layout="vertical" :rules="rules" ref="formRef">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="User Name" name="loginName">
                <a-input v-model:value="edit_user.loginName"
                         placeholder="Please enter Your User Name"
                         :disabled="true"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Nick Name" name="name">
                <a-input v-model:value="edit_user.name" placeholder="Please enter Your NickName"/>
              </a-form-item>
            </a-col>
          </a-row>

        </a-form>
      </a-modal>

      <a-modal
          title="Reset Your Password"
          v-model:visible="reset_visible"
          ok-text="Submit"
          cancel-text="Cancel"
          @ok="reset_Submit"
      >

        <a-form :model="reset_user" layout="vertical" :rules="rules" ref="formRef">

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password" name="password">
                <a-input-password v-model:value="reset_user.password"
                                  show-count :maxlength="20"
                                  placeholder="Please enter New Password"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password Confirm" name="confirm">
                <a-input-password v-model:value="reset_user.confirm"
                                  show-count :maxlength="20"
                                  placeholder="Please enter New Password again"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-modal>

    </a-layout-content>
  </a-layout>
</template>



<script lang="ts">
import {defineComponent, onMounted, ref, UnwrapRef, reactive, computed} from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { RuleObject, ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
import store from "@/store";
declare let hexMd5: any;
declare let KEY: any;

interface edit_FormState {
  loginName: string;
  name: string;
  id:number|undefined;
}
interface reset_FormState {

  password: string;
  confirm:string;
  id:number|undefined;
}

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const loading = ref(false);
    const user = computed(() => store.state.user);

    const columns = [
      {
        title: 'User Name',
        dataIndex: 'loginName'
      },
      {
        title: 'Nickname',
        dataIndex: 'name',
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];


    const pageSize = 10;
    const users=ref();
    //========================  Rule  ========================
    const formRef = ref();

    const edit_user : UnwrapRef<edit_FormState> = reactive({
      loginName: '',
      name: '',
      id:undefined,
    });
    const reset_user : UnwrapRef<reset_FormState> = reactive({
      password: '',
      confirm: '',
      id:undefined,
    });

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
      if (value !== reset_user.password) {
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

    const pagination = ref({
      onChange: (page: number) => {
        userQuery({
          page:page,
          size:pageSize,
        })
      },
      current: 1,
      pageSize: pageSize,
      total: 0
    });

    const onDelete = (id:number,loginName:string) => {
      if (loginName==user.value.loginName){
        axios.delete("/user/delete/"+id ).then((response) => {
          const data = response.data;
          if (data.success) {
            message.success('User Deleted Successfully');
            // reload
            userQuery({
              page:pagination.value.current,
              size:pagination.value.pageSize,
            })
          }else {
            message.error(data.message)
          }
        });
      }else {
        message.error("You do not have permission to delete")
      }
    };


    //========================  Reset Password  ========================
    const reset_visible = ref<boolean>(false);

    const reset = (record:any) => {
      if (record.loginName==user.value.loginName){
        reset_user.id = record.id
        reset_user.password = ''
        reset_user.confirm = ''

        reset_visible.value = true;
      }else {
        message.error("You do not have permission to reset your password")
      }
    };

    const reset_Submit = () => {
      formRef.value
          .validate().then(() => {
        const tempUser = ref({
          id:reset_user.id,
          password:hexMd5(reset_user.password + KEY),
        })
        axios.post("/user/save", tempUser.value).then((response) => {
          const data = response.data;
          if (data.success) {
            reset_visible.value = false;

            // reload
            userQuery({
              page:pagination.value.current,
              size:pagination.value.pageSize,
            })
          }else {
            message.error(data.message)
          }
        });
      })
          .catch((error: ValidateErrorEntity<reset_FormState>) => {
          });
    };

    //========================  Edit Information  ========================
    const edit_visible = ref<boolean>(false);

    const edit = (record:any) => {
      if (record.loginName==user.value.loginName){
        edit_user.loginName = record.loginName
        edit_user.name = record.name
        edit_user.id = record.id

        edit_visible.value = true;
      }else {
        message.error("You do not have permission to edit")
      }
    };

    const edit_Submit = () => {
      formRef.value
          .validate().then(() => {
        const tempUser = ref({
          id:edit_user.id,
          name:edit_user.name,
          loginName:edit_user.loginName,
        })
        axios.post("/user/save", tempUser.value).then((response) => {
          const data = response.data;
          if (data.success) {
            edit_visible.value = false;

            // reload
            userQuery({
              page:pagination.value.current,
              size:pagination.value.pageSize,
            })
          }else {
            message.error(data.message)
          }
        });
      })
          .catch((error: ValidateErrorEntity<edit_FormState>) => {
            console.log('error', error);
          });
    };

    //========================  Query ========================
    const userQuery = (params: { page:number,size:number }) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page:pagination.value.current,
          size:pagination.value.pageSize,
        }
      }).then((response) => {

        const data = response.data;
        if (data.success){
          users.value = data.content.list;
          loading.value = false;
          // reload pagination
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }

      });
    };

    //========================  Search ========================
    const onSearch = (searchValue: string) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page:pagination.value.current,
          size:pagination.value.pageSize,
          loginName: searchValue
        }
      }).then((response) => {

        const data = response.data;
        if (data.success){
          users.value = data.content.list;
          loading.value = false;
          // reload pagination
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }

      });
    };


    onMounted(() => {
      onSearch("")
    });

    return {
      formRef,
      users,
      pagination,
      columns,
      loading,
      edit_visible,
      reset_visible,
      rules,
      onSearch,
      onDelete,
      edit_Submit,
      reset_Submit,
      edit,
      reset,
      reset_user,
      edit_user,
    }
  }
});
</script>

