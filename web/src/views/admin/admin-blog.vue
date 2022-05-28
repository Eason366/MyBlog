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
          @change="handleTableChange"
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
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
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
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: {page:number,size:number}) => {
      loading.value = true;
      axios.get("/blog/list").then((response) => {
        loading.value = false;
        const data = response.data;
        blogs.value = data.content;

        // 重置分页按钮
        pagination.value.current = params.page;
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination:{current:number,pageSize:number}) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
      console.log(pagination.current)
      console.log(pagination.pageSize)
    };

    onMounted(() => {
      handleQuery({page:pagination.value.current,
                          size:pagination.value.pageSize});
    });

    return {
      blogs,
      pagination,
      columns,
      loading,
      handleTableChange
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
