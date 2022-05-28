<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="blogs"
          :pagination="pagination"
          :loading="loading"
          style="padding: 64px 100px"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary">
              编辑
            </a-button>
            <a-button type="primary" danger>
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '分类',
        key: 'category',
        dataIndex: 'category'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
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

    const blogQuery = (params: { page:number,size:number }) => {
      loading.value = true;
      axios.get("/blog/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        blogs.value = data.content.list;

        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };



    onMounted(() => {
      blogQuery({
        page:1,
        size:pagination.value.pageSize,
      })
    });

    return {
      blogs,
      pagination,
      columns,
      loading,
    }
  }
});
</script>

