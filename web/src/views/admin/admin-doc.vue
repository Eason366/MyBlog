<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '0.24rem', margin: 0, minHeight: '2.80rem' }"
        style="padding: 0.78rem 1.00rem"

    >


      <a-row>
        <a-col :span="21"/>
        <a-col :span="3">
          <a-space size="large">
            <a-button size="large" style="margin-right: 0.10rem" @click="Total_onClose">Cancel</a-button>

            <a-button size="large" type="primary" @click="Total_Submit">Submit</a-button>
          </a-space>
        </a-col>
      </a-row>

      <a-row>
        <a-col :span="24">
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
                      :dropdown-style="{ maxHeight: '4.00rem', overflow: 'auto'}"
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
                <a-form-item label="Description" >
                  <a-textarea
                      v-model:value="record_blog.description"
                      :rows="4"
                      placeholder="Please enter blog Description"
                  />
                </a-form-item>
              </a-col>
            </a-row>

            <a-row :gutter="16">
              <a-col :span="24">
                <a-form-item label="Content" >
                  <div id="editor">
                    <mavon-editor language="en"
                                  placeholder="Please start writing your blog..."
                                  v-model="mdContent"
                                  ref=md @change="getMdHtml" />
                  </div>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
      </a-row>

    </a-layout-content>
  </a-layout>

</template>



<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import store from "@/store";


export default defineComponent({
  name: 'AdminDoc',
  data() {
    return {
      mdContent: this.mdContentQuery(),
      htmlContent: ""
    }
  },
  methods: {
    //========================  Editor  ========================
    getMdHtml(mdContent: any, htmlContent: string) {
      this.htmlContent = htmlContent
      this.mdContent = mdContent
    },
    Total_Submit(){
      this.edit_Submit(this.mdContent,this.htmlContent)
      this.$router.push("/admin/blog");
    },
    Total_onClose(){
      this.edit_onClose()
      this.mdContent=this.mdContentQuery()
      this.$router.push("/admin/blog");
    },
    mdContentQuery (){
      axios.get("/blog/find-mdContent/"+this.$route.query.blogId).then((response) => {
        const data = response.data;
        if (data.success){
          this.mdContent=data.content
        } else {
          message.error(data.message)
        }
      });
    },
  },
  mounted() {
    this.$nextTick(function () {
      this.mdContentQuery()
    })
  },
  setup() {

    const route = useRoute()
    const blogs = ref();
    const record_blog = ref();
    record_blog.value={};
    const treeSelectData = ref();
    treeSelectData.value = [];
    const CategoryParentLevel = computed(() => store.state.tree);

//========================  Edit  ========================

    const edit_onClose = () => {
      blogQuery()
    };

    const edit_Submit = (mdContent:any,htmlContent:any) => {
      record_blog.value.mdcontent=mdContent
      record_blog.value.htmlcontent=htmlContent
      axios.post("/blog/save", record_blog.value).then((response) => {
        const data = response.data;
        if (data.success) {

          // reload
          message.success("Submitted successfully")
        }else {
          message.error(data.message)
        }
      });
    };


    //========================  Query ========================
    const blogQuery = () => {
      axios.get("/blog/list/"+route.query.blogId).then((response) => {
        const data = response.data;
        if (data.success){
          blogs.value = data.content;
          record_blog.value = Tool.copy(blogs.value)

        } else {
          message.error(data.message)
        }

      });
    };

    onMounted(() => {
      blogQuery()
      treeSelectData.value = Tool.copy(CategoryParentLevel.value);
      treeSelectData.value.unshift({id: 0, name: 'None'});
    });

    return {
      blogs,
      record_blog,
      treeSelectData,
      edit_Submit,
      edit_onClose,
    }

  },

});
</script>

<style>
#editor {
  width: 100%;
}
.v-note-wrapper{ z-index:0 !important; }
</style>