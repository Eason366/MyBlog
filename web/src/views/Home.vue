<template>
  <a-layout style="padding: 24px 0; background: #fff">
    <a-layout-sider width="240" style="background: #fff"
                    :style="{margin: '64px 0 0', overflow: 'auto', position: 'fixed'}">
      <a-avatar :size="200" style="background-color: #87d068"
                :style="{margin: '20px 25px', overflow: 'auto', position: 'fixed'}">
        <template #icon>
          <UserOutlined />
        </template>
      </a-avatar>
      <div class="User"
           :style="{margin: '220px 105px', overflow: 'auto', position: 'fixed'}">
        Eason
      </div>

      <a-menu style="width: 240px"
              :style="{margin: '260px 0px', overflow: 'auto', position: 'fixed'}">

        <a href="https://github.com/Eason366">
          <a-menu-item key="GithubOutlined" >
            <template #icon>
              <GithubFilled />
            </template>
            Github
          </a-menu-item>
        </a>
        <a href="https://www.linkedin.com/in/eason-wang366/">
          <a-menu-item key="LinkedinOutlined">
            <template #icon>
              <LinkedinFilled />
            </template>
            LinkedIn
          </a-menu-item>
        </a>
        <a href="mailto:wangy366@miamioh.edu">
          <a-menu-item key="MailOutlined">
            <template #icon>
              <MailFilled />
            </template>
            Email
          </a-menu-item>
        </a>

      </a-menu>

    </a-layout-sider>
    <a-layout-content :style="{ minHeight: '780px', margin: '24px 16px 0', overflow: 'initial' }">

      <a-list item-layout="vertical" size="large" :pagination="pagination"
              :data-source="blogs"
              :style="{margin: '30px 240px 0 500px'}">
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
                  :width="350"
                  :height="220"
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
import { AppstoreOutlined,EyeOutlined,UserOutlined,GithubFilled,LinkedinFilled,MailFilled} from '@ant-design/icons-vue';
import { defineComponent, ref ,onMounted} from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'Home',
  components: {
    AppstoreOutlined,
    EyeOutlined,
    UserOutlined,
    GithubFilled,
    LinkedinFilled,
    MailFilled,
  },
  setup() {
    console.log("setup");
    const blogs = ref()
    const pageSize = 10


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

    const blogQuery = (params: { page:number,size:number }) => {
      axios.get("/blog/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        const data = response.data;
        blogs.value = data.content.list;

        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };


    onMounted(()=>{
      blogQuery({
        page:pagination.value.current,
        size:pageSize,
      })
    });

    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      openKeys: ref<string[]>(['sub1']),
      EyeOutlined,
      UserOutlined,
      pagination,
      blogs,
      GithubFilled,
      LinkedinFilled,
      MailFilled,
    };
  },
});
</script>

<style>
.title {
  font-size: 24px;
}
.User {
  width: 120px;
  height: 31px;
  float: left;
  color: black;
  font-size: 18px;
}
</style>