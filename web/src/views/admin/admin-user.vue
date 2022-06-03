<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '0.24rem', margin: 0, minHeight: '2.80rem' }"
        style="padding: 0.78rem 1.00rem"
    >



      <a-space size="large">
        <a-button type="primary" @click="add" size="large">
          Add User
        </a-button>

        <a-input-search
            placeholder="Input Name to Search"
            style="width: 5.00rem"
            size="large"
            enter-button="Search"
            @search="onSearch"
        />
      </a-space>

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
                @confirm="onDelete(record.id)"
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

        <a-form :model="record_user" layout="vertical" :rules="rules" ref="formRef">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="User Name" name="loginName">
                <a-input v-model:value="record_user.loginName"
                         placeholder="Please enter Your User Name"
                         :disabled="true"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Nick Name" name="name">
                <a-input v-model:value="record_user.name" placeholder="Please enter Your NickName"/>
              </a-form-item>
            </a-col>
          </a-row>

        </a-form>
      </a-modal>

      <a-modal
          title="New User"
          v-model:visible="new_visible"
          ok-text="Submit"
          cancel-text="Cancel"
          @ok="new_Submit"
      >

        <a-form :model="record_user" layout="vertical" :rules="rules" ref="formRef">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="User Name" name="loginName">
                <a-input v-model:value="record_user.loginName"
                         placeholder="Please enter Your User Name"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Nick Name" name="name">
                <a-input v-model:value="record_user.name" placeholder="Please enter Your NickName"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password" name="password">
                <a-input-password v-model:value="record_user.password"
                                  show-count :maxlength="20"
                                  placeholder="Please enter Your Password"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password Confirm" name="confirm">
                <a-input-password v-model:value="record_user.confirm"
                                  show-count :maxlength="20"
                                  placeholder="Please enter Your Password again"/>
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
          @ok="edit_Submit"
      >

        <a-form :model="record_user" layout="vertical" :rules="rules" ref="formRef">

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password" name="password">
                <a-input-password v-model:value="record_user.password"
                                  show-count :maxlength="20"
                                  placeholder="Please enter New Password"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password Confirm" name="confirm">
                <a-input-password v-model:value="record_user.confirm"
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
import { defineComponent, onMounted, ref, UnwrapRef, reactive} from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { RuleObject, ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
declare let hexMd5: any;
declare let KEY: any;

interface FormState {
  loginName: string;
  name: string;
  password: string;
  confirm:string;
  id:number|undefined;
}

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const loading = ref(false);

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

    const record_user : UnwrapRef<FormState> = reactive({
      loginName: '',
      name: '',
      password: '',
      confirm:'',
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
      if (value !== record_user.password) {
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

    const onDelete = (id:number) => {
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
    };


    //========================  Model  ========================
    const edit_visible = ref<boolean>(false);
    const new_visible = ref<boolean>(false);
    const reset_visible = ref<boolean>(false);


    const edit = (record:any) => {
      console.log('record',record)

      record_user.loginName = record.loginName
      record_user.name = record.name
      record_user.id = record.id
      record_user.password = record.password
      record_user.confirm = record.password

      edit_visible.value = true;
      console.log('record_user',record_user)
    };

    const reset = (record:any) => {
      console.log('record',record)

      record_user.loginName = record.loginName
      record_user.name = record.name
      record_user.id = record.id
      record_user.password = ''
      record_user.confirm = ''

      reset_visible.value = true;
      console.log('record_user',record_user)

    };


    const edit_Submit = () => {
      formRef.value
          .validate().then(() => {
        console.log('record_user',record_user)
        record_user.password=hexMd5(record_user.password + KEY);
        axios.post("/user/save", record_user).then((response) => {
          const data = response.data;
          console.log(record_user)
          if (data.success) {
            edit_visible.value = false;
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
          .catch((error: ValidateErrorEntity<FormState>) => {
            console.log('error', error);
          });


    };
    const add = () => {
      record_user.loginName = ''
      record_user.name = ''
      record_user.password = ''
      record_user.confirm = ''

      new_visible.value = true;
    };

    const new_Submit = () => {
      formRef.value
          .validate().then(() => {
        record_user.password= hexMd5(record_user.password + KEY);
        axios.post("/user/save", record_user).then((response) => {
          const data = response.data;
          console.log(record_user)
          if (data.success) {
            new_visible.value = false;

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
          .catch((error: ValidateErrorEntity<FormState>) => {
            console.log('error', error);
          });


    };
    
    //========================  Query ========================
    const userQuery = (params: { page:number,size:number }) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {

        const data = response.data;
        console.log('data',data)
        if (data.success){
          users.value = data.content.list;
          console.log(users)
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
      userQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })

    });

    return {
      formRef,
      users,
      pagination,
      columns,
      loading,
      edit_visible,
      new_visible,
      reset_visible,
      rules,
      add,
      onSearch,
      onDelete,
      edit_Submit,
      new_Submit,
      edit,
      reset,
      record_user,
    }
  }
});
</script>

