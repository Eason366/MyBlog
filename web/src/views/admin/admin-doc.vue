<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        style="padding: 78px 100px"

    >


      <a-row>
        <a-col :span="21"/>
        <a-col :span="3">
          <a-space size="large">
            <a-button size="large" style="margin-right: 10px" @click="Total_onClose">Cancel</a-button>

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
                      :dropdown-style="{ maxHeight: '400px', overflow: 'auto'}"
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
import { defineComponent, onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";


export default defineComponent({
  name: 'AdminDoc',
  data() {
    return {
      mdContent: "",
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
      console.log('mdContent',this.mdContent)
      console.log('htmlContent',this.htmlContent)
      this.edit_Submit(this.mdContent,this.htmlContent)
    },
    Total_onClose(){
      this.edit_onClose()
      this.mdContent=""
    },
  },
  setup() {

    const route = useRoute()
    const blogs = ref();
    const record_blog = ref();
    record_blog.value={};
    const treeSelectData = ref();
    treeSelectData.value = [];
    const CategoryParentLevel = ref();
    let categorys: any;


//========================  Edit  ========================

    const edit_onClose = () => {
      blogQuery()
    };

    const edit_Submit = (mdContent:any,htmlContent:any) => {
      record_blog.value.mdcontent=mdContent
      record_blog.value.htmlcontent=htmlContent
      axios.post("/blog/save", record_blog.value).then((response) => {
        const data = response.data;
        console.log(record_blog.value)
        if (data.success) {

          // reload
          blogQuery()
          message.success("Submitted successfully")
        }else {
          message.error(data.message)
        }
      });
    };

//========================  setDisable ========================

    /**
     * Set a node and its descendants to disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // Traverse the array, that is, traverse a layer of nodes
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // if the current node is the target node
          console.log("disabled", node);
          // Set the target node to disabled
          node.disabled = true;

          // Traverse all child nodes and add disabled to all child nodes
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // If the current node is not the target node, go to its child nodes and look for it.
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    //========================  Query ========================
    const blogQuery = () => {
      axios.get("/blog/list/"+route.query.blogId).then((response) => {
        const data = response.data;
        if (data.success){
          blogs.value = data.content;
          console.log('Data:',blogs.value)
          record_blog.value = Tool.copy(blogs.value)
          console.log(record_blog)
          treeSelectData.value = Tool.copy(CategoryParentLevel.value);
          setDisable(treeSelectData.value, route.query.blogId);

          treeSelectData.value.unshift({id: 0, name: 'None'});


        } else {
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

          blogQuery()
        } else {
          message.error(data.message)
        }

      });
    };

    onMounted(() => {
      categoryQuery()

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