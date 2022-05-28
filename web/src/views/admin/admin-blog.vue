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
    const blogs = ref();


    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 10,
    };


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




    onMounted(() => {
      loading.value = true;
      axios.get("/blog/list").then((response) => {
        loading.value = false;
        const data = response.data;
        blogs.value = data.content;
      });
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

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
