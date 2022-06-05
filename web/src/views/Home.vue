<template>
  <a-layout style="padding: 0.24rem 0; background: #fff">
    <a-layout-sider width="240" style="background: #fff"
                    :style="{margin: '0.64rem 0 0', overflow: 'auto', position: 'fixed'}">

      <a-input-search
          placeholder="input search text"
          style="width: 2.30rem"
          @search="onSearch"
          :style="{margin: '0rem 0.25rem', overflow: 'auto', position: 'fixed'}"
      />

      <a-tree
          :tree-data="CategoryParentLevel"
          v-model:selectedKeys="selectedKeys"
          :style="{margin: '0.50rem 0.10rem', overflow: 'auto', position: 'fixed'}"
          :replace-fields="{title: 'name', key:'id'}"
      >
      </a-tree>


    </a-layout-sider>
    <a-layout-content :style="{ minHeight: '7.80rem', margin: '0.24rem 0.16rem 0', overflow: 'initial' }">

      <a-list item-layout="vertical" size="large" :pagination="pagination"
              :data-source="blogs"
              :style="{margin: '0.30rem 2.40rem 0 5.00rem'}">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span >
                <component :is= UserOutlined style="margin-right: 0.08rem" />
                {{ item.user}}
              </span>
              <span >
                <component :is= EyeOutlined style="margin-right: 0.08rem" />
                {{ item.viewCount}}
              </span>
              <span >
                <component :is= LikeOutlined style="margin-right: 0.08rem" />
                {{ item.voteCount}}
              </span>
            </template>
            <template #extra>
              <img
                  :width="350"
                  :height="220"
                  :src= "item.cover"
              />
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?blogId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
            </a-list-item-meta>
            {{ item.content }}
          </a-list-item>
        </template>
      </a-list>

    </a-layout-content>

  </a-layout>
</template>


<script lang="ts">
import { AppstoreOutlined,EyeOutlined,UserOutlined,LikeOutlined} from '@ant-design/icons-vue';
import {defineComponent, ref, onMounted, watch} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'Home',
  components: {
    AppstoreOutlined,
    EyeOutlined,
    UserOutlined,
    LikeOutlined,
  },
  setup() {
    console.log("setup");
    const blogs = ref()
    const pageSize = 10
    let categorys: any;
    const CategoryParentLevel = ref();


    const pagination = ref({
      onChange: (page: number) => {
        blogQuery({
          page:page,
          size:pageSize,
        })
      },
      current: 1,
      pageSize:pageSize,
      total: 0
    });

    let categories: Array<string> = [];

    const getAllCategories = (treeSelectData: any, id: any) => {
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          categories.push(id);

          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getAllCategories(children, children[j].id)
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getAllCategories(children, id);
          }
        }
      }
    };


    const selectedKeys = ref<string[]>([]);
    watch(selectedKeys, () => {
      console.log('selectedKeys', selectedKeys.value);
      categories = [];
      getAllCategories(categorys,selectedKeys.value[0]);
      axios.get("/blog/list", {
        params: {
          page:pagination.value.current,
          size:pagination.value.pageSize,
          categories:categories.join(","),
        }
      }).then((response) => {

        const data = response.data;
        console.log(data)
        if (data.success){
          blogs.value = data.content.list;
          // reload pagination
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }
      });
    });


    //========================  Search ========================
    const onSearch = (searchValue: string) => {
      axios.get("/blog/list", {
        params: {
          page:pagination.value.current,
          size:pagination.value.pageSize,
          name: searchValue
        }
      }).then((response) => {

        const data = response.data;
        console.log(data)
        if (data.success){
          blogs.value = data.content.list;
          // reload pagination
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }
      });
    };

    const blogQuery = (params: { page:number,size:number }) => {
      axios.get("/blog/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        const data = response.data;
        if (data.success){
          blogs.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else {
          message.error(data.message)
        }
      });
    };

    const categoryQuery = () => {
      axios.get("/category/all").then((response) => {

        const data = response.data;
        if (data.success){
          categorys = data.content;
          CategoryParentLevel.value = [];
          CategoryParentLevel.value = Tool.array2Tree(categorys,0);
          console.log("Tree：", CategoryParentLevel.value);


        } else {
          message.error(data.message)
        }

      });
    };

    onMounted(()=>{
      blogQuery({
        page:pagination.value.current,
        size:pageSize,
      }),
      categoryQuery()
    });

    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      openKeys: ref<string[]>(['sub1']),
      EyeOutlined,
      UserOutlined,
      LikeOutlined,
      pagination,
      blogs,
      CategoryParentLevel,
      onSearch,
      selectedKeys,
    };
  },
});
</script>

<style>
.title {
  font-size: 0.24rem;
}
.User {
  width: 1.20rem;
  height: 0.31rem;
  float: left;
  color: black;
  font-size: 0.18rem;
}
</style>