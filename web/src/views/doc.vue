<template>
  <a-layout style="padding: 64px; background: #fff">
    <a-layout-content>
      <mavon-editor id="editor" class="markdown-body" innerHTML="" style="margin: 0px 260px 0px 260px">
        <!--      {{html}}-->
      </mavon-editor>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
import { defineComponent, onMounted,} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  data() {
    const html = document.createElement("markdown-body")
    return {
      html,
    }
  },
  methods: {
    //========================  Editor  ========================
    htmlContentQuery (){
      axios.get("/blog/find-htmlContent/"+this.$route.query.blogId).then((response) => {
        const data = response.data;
        if (data.success){
          this.html.innerHTML=data.content
          document.getElementById("editor")!.appendChild(this.html);
          console.log(this.html)
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
  unmounted(){
    console.log("unmounted 被调用")
    this.$nextTick(function () {
      document.getElementById("editor")!.removeChild(this.html);
    })
    console.log("unmounted 调用完成")

  },
  setup() {
    const route = useRoute();
    onMounted(() => {
      console.log(route.query)

    });

    return {
    }
  }
});
</script>

<style>
.v-note-wrapper{ z-index:0 !important; }
</style>
