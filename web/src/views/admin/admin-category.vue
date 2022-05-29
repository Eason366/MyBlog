<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        style="padding: 78px 100px"
    >



      <a-space size="large">
        <a-button type="primary" @click="add" size="large">
          Add Category
        </a-button>
      </a-space>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categorys"
          :pagination="pagination"
          :loading="loading"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>

            <a-popconfirm
                title="Are you sure delete this Category?"
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

        <a-form :model="record_category" layout="vertical" :rules="rules">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Name" name="Name">
                <a-input v-model:value="record_category.name" placeholder="Please enter category Name"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Parent" name="Parent">
                <a-input v-model:value="record_category.parent" placeholder="Please enter category Name"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Sort" name="Sort">
                <a-input v-model:value="record_category.sort" placeholder="Please enter category Name"/>
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
                  borderTop: '1px solid #e9e9e9',
                  padding: '10px 16px',
                  background: '#fff',
                  textAlign: 'right',
                  zIndex: 1,
                }"
        >
          <a-button style="margin-right: 8px" @click="edit_onClose">Cancel</a-button>
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
        title: 'Name',
        dataIndex: 'name'
      },
      {
        title: 'Parent',
        dataIndex: 'parent'
      },
      {
        title: 'Sort',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const categorys = ref();
    const pageSize = 10;


    const pagination = ref({
      onChange: (page: number) => {
        categoryQuery({
          page:page,
          size:pageSize,
        })
      },
      current: 1,
      pageSize: pageSize,
      total: 0
    });

    const onDelete = (id:number) => {
      axios.delete("/category/delete/"+id ).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success('Category Deleted Successfully');
          // reload
          categoryQuery({
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
    const record_category = ref({});

    const rules = {
      Name:[{required: true,message: 'Please Enter category Name', trigger: 'blur' }]
    }

    const edit = (record:JSON) => {
      edit_visible.value = true;
      record_category.value = Tool.copy(record)
    };

    const add = () => {
      edit_visible.value = true;
      record_category.value = {};
    };

    const edit_onClose = () => {
      edit_visible.value = false;
      categoryQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    };

    const edit_Submit = () => {
      axios.post("/category/save", record_category.value).then((response) => {
        const data = response.data;
        if (data.success) {
          edit_visible.value = false;

          // reload
          categoryQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          })
        }else {
          message.error(data.message)
        }
      });
    };


    //========================  Query ========================
    const categoryQuery = (params: { page:number,size:number }) => {
      loading.value = true;
      axios.get("/category/list", {
        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {

        const data = response.data;
        if (data.success){
          categorys.value = data.content.list;
          loading.value = false;
          // reload pagination
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }

      });
    };




    onMounted(() => {
      categoryQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    });

    return {
      categorys,
      pagination,
      columns,
      loading,
      edit_visible,
      edit,
      add,
      rules,
      onDelete,
      edit_onClose,
      edit_Submit,
      record_category,
    }
  }
});
</script>

