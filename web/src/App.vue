<template>
  <a-layout>
    <a-layout-header class="header" :style="{height:'64px', position: 'fixed', zIndex: 1, width: '100%'}">
      <div class="logo"> Eason's Blog </div>
      <a-menu
          v-model:selectedKeys="selectedKeys1"
          theme="dark"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1">nav 1</a-menu-item>
        <a-menu-item key="2">nav 2</a-menu-item>
        <a-menu-item key="3">nav 3</a-menu-item>
      </a-menu>
    </a-layout-header>
    <a-layout-content style="padding: 0 50px">
      <a-layout style="padding: 24px 0; background: #fff">
        <a-layout-sider width="200" style="background: #fff"
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
                  <user-outlined />
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
                  <laptop-outlined />
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
                  <notification-outlined />
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
                  :data-source="listData"
                  :style="{ padding:'0 100px', margin: '24px 200px'}">
            <template #renderItem="{ item }">
              <a-list-item key="item.title">
                <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
                </template>
                <template #extra>
                  <img
                      width="272"
                      alt="logo"
                      src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
                  />
                </template>
                <a-list-item-meta :description="item.description">
                  <template #title>
                    <a :href="item.href">{{ item.title }}</a>
                  </template>
                  <template #avatar><a-avatar :src="item.avatar" /></template>
                </a-list-item-meta>
                {{ item.content }}
              </a-list-item>
            </template>
          </a-list>

        </a-layout-content>
      </a-layout>
    </a-layout-content>
    <a-layout-footer style="text-align: center">
      Blog ©2022 Created by Eason
    </a-layout-footer>
  </a-layout>
</template>
<script lang="ts">
import { UserOutlined, LaptopOutlined, NotificationOutlined,StarOutlined, LikeOutlined, MessageOutlined  } from '@ant-design/icons-vue';
import { defineComponent, ref } from 'vue';
const listData: Record<string, string>[] = [];

for (let i = 0; i < 8; i++) {
  listData.push({
    href: 'https://www.antdv.com/',
    title: `ant design vue part ${i}`,
    avatar: 'https://joeschmoe.io/api/v1/random',
    description:
        'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
        'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}
export default defineComponent({
  components: {
    UserOutlined,
    LaptopOutlined,
    NotificationOutlined,
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 10,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];
    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      openKeys: ref<string[]>(['sub1']),
      listData,
      pagination,
      actions,
    };
  },
});
</script>
<style>
.logo {
  width: 120px;
  height: 31px;
  float: left;
  color: white;
  font-size: 18px;
}

</style>
