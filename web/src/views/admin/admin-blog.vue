<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '0.24rem', margin: 0, minHeight: '2.80rem' }"
        style="padding: 0.78rem 1.00rem"
    >



      <a-space size="large">
        <a-button type="primary" @click="add" size="large">
          Add Blog
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
          :data-source="blogs"
          :pagination="pagination"
          :loading="loading"
      >
        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category) }}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?blogId='+record.id">
              <a-button type="primary">
                Edit
              </a-button>
            </router-link>
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
          title="Add a new Blog"
          placement="left"
          :visible="add_visible"
          @close="edit_onClose"
      >

        <a-form :model="record_blog" layout="vertical">
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
              <a-form-item label="Category">
                <a-tree-select
                    v-model:value="record_blog.category"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '4.00rem', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="Please select blog Category"
                    tree-default-expand-all
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                    :disabled="record_blog.id === 0"
                >
                </a-tree-select>
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
import { defineComponent, onMounted, ref,computed } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import store from "@/store";
import { Tool } from '@/util/tool';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const loading = ref(false);
    const user = computed(() => store.state.user);

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
        title: 'Category',
        key: 'category',
        dataIndex: 'category',
        slots: { customRender: 'category' }
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

    const getCategoryName = (cid: number) => {
      let result = "";
      categorys.value.forEach((item: any) => {
        if (item.id === cid) {
          result = item.name;
        }
      });
      return result;
    };


    const treeSelectData = ref();
    treeSelectData.value = [];
    const blogs = ref();
    const pageSize = 10;
    const CategoryParentLevel = computed(() => store.state.tree);

    let categorys = computed(() => store.state.category);


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
    const add_visible = ref<boolean>(false);
    const record_blog = ref();
    record_blog.value = {}

    const add = () => {
      add_visible.value = true;
      record_blog.value = {};

    };

    const edit_onClose = () => {
      add_visible.value = false;
      blogQuery({
        page:pagination.value.current,
        size:pagination.value.pageSize,
      })
    };

    const edit_Submit = () => {
      record_blog.value.user = user.value.loginName
      record_blog.value.mdcontent = ''
      record_blog.value.htmlcontent = ''
      axios.post("/blog/save", record_blog.value).then((response) => {
        const data = response.data;
        console.log(record_blog.value)
        if (data.success) {
          add_visible.value = false;

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
          user: user.value.loginName,
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
      treeSelectData.value = Tool.copy(CategoryParentLevel.value) || [];
      treeSelectData.value.unshift({id: 0, name: 'None'});
    });

    return {
      blogs,
      pagination,
      columns,
      loading,
      add_visible,
      add,
      onSearch,
      onDelete,
      treeSelectData,
      edit_onClose,
      edit_Submit,
      record_blog,
      getCategoryName,
    }
  }
});
</script>

