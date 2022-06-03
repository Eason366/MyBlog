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
      <a-drawer
          :width="500"
          title="Basic Drawer"
          placement="left"
          :visible="edit_visible"
          @close="edit_onClose"
      >

        <a-form :model="record_user" layout="vertical">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="User Name" name="Name">
                <a-input v-model:value="record_user.loginName"
                         placeholder="Please enter blog Name"
                         :disabled="!!record_user.id"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Nick Name">
                <a-input v-model:value="record_user.name" placeholder="Please enter blog Cover Url"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Password">
                <a-input v-model:value="record_user.password" placeholder="Please enter blog Cover Url"/>
              </a-form-item>
            </a-col>
          </a-row>


        </a-form>
        <div
            :style="{
                  position: 'absolute',
                  right: 0,
                  bottom: 0,
                  width: '100%',
                  borderTop: '0.01rem solid #e9e9e9',
                  padding: '0.10rem 0.16rem',
                  background: '#fff',
                  textAlign: 'right',
                  zIndex: 1,
                }"
        >
          <a-button style="margin-right: 0.08rem" @click="edit_onClose">Cancel</a-button>
          <a-button type="primary" @click="edit_Submit">Submit</a-button>
        </div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { Tool } from '@/util/tool';

export default defineComponent({
  name: 'AdminEbook',
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
        title: 'Password',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];
    


    const pageSize = 10;
    const users=ref();


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


    //========================  Drawer  ========================
    const edit_visible = ref<boolean>(false);
    const record_user = ref({});


    const edit = (record:any) => {
      console.log('record',record)
      edit_visible.value = true;
      record_user.value = Tool.copy(record)
      console.log('record_user',record_user)
      
    };

    const add = () => {
      edit_visible.value = true;
      record_user.value = {};
    };

    const edit_onClose = () => {
      edit_visible.value = false;
      userQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    };

    const edit_Submit = () => {
      axios.post("/user/save", record_user.value).then((response) => {
        const data = response.data;
        console.log(record_user.value)
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
      users,
      pagination,
      columns,
      loading,
      edit_visible,
      add,
      onSearch,
      onDelete,
      edit_onClose,
      edit_Submit,edit,
      record_user,
    }
  }
});
</script>

