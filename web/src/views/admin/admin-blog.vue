<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        style="padding: 78px 100px"
    >



      <a-space size="large">
        <a-button type="primary" @click="add" size="large">
          Add Blog
        </a-button>

        <a-input-search
            placeholder="Input Name to Search"
            style="width: 500px"
            size="large"
            enter-button="Search"
            @search="onSearch"
        />
        </a-space>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="blogs"
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

        <a-form :model="record_blog" layout="vertical" :rules="rules">
          <a-row :gutter="16">
            <a-col :span="24">
          <a-form-item label="Name" name="Name">
            <a-input v-model:value="record_blog.name" placeholder="Please enter blog Name"/>
          </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Cover">
                <a-input v-model:value="record_blog.cover" placeholder="Please enter blog Cover Url"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Category 1">
                <a-input v-model:value="record_blog.category1Id" placeholder="Please enter blog First Category"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Category 2">
                <a-input v-model:value="record_blog.category2Id" placeholder="Please enter blog Second Category"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Description">
                <a-textarea
                    v-model:value="record_blog.description"
                    :rows="4"
                    placeholder="Please enter blog Description"
                />
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
        title: 'Cover',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: 'Category 1',
        key: 'category',
        dataIndex: 'category1Id'
      },
      {
        title: 'Category 2',
        key: 'category',
        dataIndex: 'category2Id'
      },
      {
        title: 'View Count',
        dataIndex: 'viewCount'
      },
      {
        title: 'Vote Count',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const blogs = ref();
    const pageSize = 10;


    const pagination = ref({
      onChange: (page: number) => {
        blogQuery({
          page:page,
          size:pageSize,
        })
      },
      current: 1,
      pageSize: pageSize,
      total: 0
    });

    const onDelete = (id:number) => {
      axios.delete("/blog/delete/"+id ).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success('Blog Deleted Successfully');
          // reload
          blogQuery({
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
    const record_blog = ref({});

    const rules = {
      Name:[{required: true,message: 'Please Enter blog Name', trigger: 'blur' }]
    }

    const edit = (record:JSON) => {
      edit_visible.value = true;
      record_blog.value = Tool.copy(record)
    };

    const add = () => {
      edit_visible.value = true;
      record_blog.value = {};
    };

    const edit_onClose = () => {
      edit_visible.value = false;
      blogQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    };

    const edit_Submit = () => {
      axios.post("/blog/save", record_blog.value).then((response) => {
        const data = response.data;
        if (data.success) {
          edit_visible.value = false;

          // reload
          blogQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          })
        }else {
          message.error(data.message)
        }
      });
    };


    //========================  Query ========================
    const blogQuery = (params: { page:number,size:number }) => {
      loading.value = true;
      axios.get("/blog/list", {
        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {

        const data = response.data;
        if (data.success){
          blogs.value = data.content.list;
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
      axios.get("/blog/list", {
        params: {
          page:pagination.value.current,
          size:pagination.value.pageSize,
          name: searchValue
        }
      }).then((response) => {

        const data = response.data;
        if (data.success){
          blogs.value = data.content.list;
          loading.value = false;
          // reload pagination
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }

      });
    };


    onMounted(() => {
      blogQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    });

    return {
      blogs,
      pagination,
      columns,
      loading,
      edit_visible,
      edit,
      add,
      rules,
      onSearch,
      onDelete,
      edit_onClose,
      edit_Submit,
      record_blog,
    }
  }
});
</script>

