<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-sider width="240" style="background: #fff"
                    :style="{margin: '64px 0 0', overflow: 'auto', position: 'fixed'}">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          style="height: 100%"
      >
        <a-sub-menu key="sub1">
          <template #title>
                <span>
                  <appstore-outlined />
                  subnav 1
                </span>
          </template>
          <a-menu-item key="1">option1</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
                <span>
                  <appstore-outlined />
                  subnav 2
                </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
                <span>
                  <appstore-outlined />
                  subnav 3
                </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content :style="{ minHeight: '780px', margin: '24px 16px 0', overflow: 'initial' }">

      <a-list item-layout="vertical" size="large" :pagination="pagination"
              :data-source="blogs"
              :style="{margin: '24px 240px'}">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span >
            <component :is= EyeOutlined style="margin-right: 8px" />
            {{ item.viewCount}}
          </span>
            </template>
            <template #extra>
              <img
                  width="350"
                  alt="logo"
                  :src= "item.cover"
              />
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href" class="title">{{ item.name }}</a>
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
import { AppstoreOutlined,EyeOutlined} from '@ant-design/icons-vue';
import { defineComponent, ref ,onMounted} from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'Home',
  components: {
    AppstoreOutlined,
    EyeOutlined,
  },
  setup() {
    console.log("setup");
    const blogs = ref()


    onMounted(()=>{
      axios.get( "/blog/list").then((response) => {
        const data = response.data;
        blogs.value = data.content
      });
    });

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 10,
    };

    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      openKeys: ref<string[]>(['sub1']),
      EyeOutlined,
      pagination,
      blogs,
    };
  },
});
</script>

<style>
.title {
  font-size: 24px;
}
</style>