<template>
  <a-layout style="padding: 0.64rem; background: #fff">
    <a-layout-content>

      <div class="title">
        <h2>{{doc.title}}</h2>
        <a-space size="large" >
          <span >
            <component :is= UserOutlined style="margin-right: 0.08rem" />
            {{doc.user}}
          </span>
          <span >
            <component :is= EyeOutlined style="margin-right: 0.08rem" />
            {{doc.viewCount}}
          </span>
          <span >
            <component :is= LikeOutlined style="margin-right: 0.08rem" />
            {{doc.voteCount}}
          </span>
        </a-space>
      </div>

      <a-divider style="height: 2px; background-color: #9999cc"/>

      <div class="editor" :innerHTML="html" style="margin: 0rem 2.60rem 0rem 2.60rem">
        <!--      {{html}}-->
      </div>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
import { defineComponent, onMounted,ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {EyeOutlined, LikeOutlined, UserOutlined} from '@ant-design/icons-vue';
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  components: {
    EyeOutlined,
    UserOutlined,
    LikeOutlined,
  },
  data() {
    const html = ref()
    const doc = ref()
    doc.value = {
      title:'',
      viewCount:0,
      voteCount:0,
      user:'',
    }
    return {
      html,
      doc,
    }
  },
  methods: {
    //========================  Editor  ========================
    htmlContentQuery (){
      axios.get("/blog/find-htmlContent/"+this.$route.query.blogId).then((response) => {
        const data = response.data;
        if (data.success){
          this.docQuery()
          this.html=data.content
        } else {
          message.error(data.message)
        }
      });
    },
    docQuery (){
      axios.get("/blog/list/"+this.$route.query.blogId).then((response) => {
        console.log("Eason",this.$route.query.blogId)
        const data = response.data;
        if (data.success){
          this.doc = {
            title:data.content.name,
            viewCount:data.content.viewCount,
            voteCount:data.content.voteCount,
            user:data.content.user,
          }
        } else {
          message.error(data.message)
        }

      });
    },
  },
  mounted() {
    this.$nextTick(function () {
      this.htmlContentQuery()

    })
  },
  setup() {
    const route = useRoute();
    onMounted(() => {
      console.log(route.query)

    });

    return {
      EyeOutlined,
      UserOutlined,
      LikeOutlined,
    }
  }
});
</script>

<style>
.v-note-wrapper{ z-index:0 !important; }

/* table 样式 */
.editor table {
  border-top: 0.01rem solid #ccc;
  border-left: 0.01rem solid #ccc;
}
.editor table td,
.editor table th {
  border-bottom: 0.01rem solid #ccc;
  border-right: 0.01rem solid #ccc;
  padding: 0.03rem 0.05rem;
}
.editor table th {
  border-bottom: 0.02rem solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.editor blockquote {
  display: block;
  border-left: 0.08rem solid #d0e5f2;
  padding: 0.05rem 0.10rem;
  margin: 0.10rem 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.editor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 0.03rem;
  padding: 0.03rem 0.05rem;
  margin: 0 0.03rem;
}
.editor pre code {
  display: block;
}

/* ul ol 样式 */
.editor ul, ol {
  margin: 0.10rem 0 0.10rem 0.20rem;
}

/* 和antdv p冲突，覆盖掉 */
.editor blockquote p {
  font-family:"YouYuan";
  margin: 0.20rem 0.10rem !important;
  font-size: 0.16rem !important;
  font-weight:600;
}

.editor h1 {
  font-size: 0.36rem;
  line-height: 0.40rem;
}
.editor img{
  max-width:5rem;
  :width:e-xpression(this.width>5?"5rem":this.width);
}
.title{
  text-align: center;
}
.title h2{
  font-size: 0.6rem;
}
.title span{
  font-size: 0.3rem;
}

</style>
