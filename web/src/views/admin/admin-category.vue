<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        style="padding: 78px 100px"
    >

      <a-space size="large">
        <a-button type="primary" @click="add" size="large">
          Add Category
        </a-button>
      </a-space>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="CategoryParentLevel"
          :loading="loading"
          :pagination="false"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>

            <a-popconfirm
                title="Are you sure delete this Category?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="onDelete(record.id)"
            >
              <a-button type="primary" danger>
                Delete
              </a-button>
            </a-popconfirm>


          </a-space>
        </template>
      </a-table>
      <a-drawer
          :width="500"
          title="Basic Drawer"
          placement="left"
          :visible="edit_visible"
          @close="edit_onClose"
      >

        <a-form :model="record_category" layout="vertical">
          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Name" name="Name">
                <a-input v-model:value="record_category.name" placeholder="Please enter category Name"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Parent" name="Parent">
                <a-tree-select
                    v-model:value="record_category.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    :tree-data="treeSelectData"
                    placeholder="Please enter Parent category "
                    tree-default-expand-all
                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                    :disabled="record_category.id === 0"
                >
                </a-tree-select>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item label="Sort" name="Sort">
                <a-input v-model:value="record_category.sort" placeholder="Please enter category Sort"/>
              </a-form-item>
            </a-col>
          </a-row>




        </a-form>
        <div
            :style="{
                  position: 'absolute',
                  right: 0,
                  bottom: 0,
                  width: '100%',
                  borderTop: '1px solid #e9e9e9',
                  padding: '10px 16px',
                  background: '#fff',
                  textAlign: 'right',
                  zIndex: 1,
                }"
        >
          <a-button style="margin-right: 8px" @click="edit_onClose">Cancel</a-button>
          <a-button type="primary" @click="edit_Submit">Submit</a-button>
        </div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import { Tool } from '@/util/tool';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const loading = ref(false);
    const treeSelectData = ref();
    treeSelectData.value = [];
    const columns = [
      {
        title: 'Name',
        dataIndex: 'name'
      },
      {
        title: 'Parent',
        dataIndex: 'parent'
      },
      {
        title: 'Sort',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    const categorys = ref();
    const CategoryParentLevel = ref();

    const onDelete = (id:number) => {
      axios.delete("/category/delete/"+id ).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success('Category Deleted Successfully');
          // reload
          categoryQuery()
        }else {
          message.error(data.message)
        }
      });
    };


    //========================  Drawer  ========================
    const edit_visible = ref<boolean>(false);
    const record_category = ref({});


    const edit = (record:any) => {
      edit_visible.value = true;
      record_category.value = Tool.copy(record)

      treeSelectData.value = Tool.copy(CategoryParentLevel.value);
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    const add = () => {
      edit_visible.value = true;
      record_category.value = {};

      treeSelectData.value = Tool.copy(CategoryParentLevel.value) || [];
      treeSelectData.value.unshift({id: 0, name: 'None'});
    };

    const edit_onClose = () => {
      edit_visible.value = false;
      categoryQuery()
    };

    const edit_Submit = () => {
      axios.post("/category/save", record_category.value).then((response) => {
        const data = response.data;
        if (data.success) {
          edit_visible.value = false;

          // reload
          categoryQuery()
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
    const categoryQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {

        const data = response.data;
        if (data.success){
          categorys.value = data.content;
          CategoryParentLevel.value = [];
          CategoryParentLevel.value = Tool.array2Tree(categorys.value,0);
          console.log("Tree：", CategoryParentLevel.value);

          loading.value = false;

        } else {
          message.error(data.message)
        }

      });
    };




    onMounted(() => {
      categoryQuery()
    });

    return {
      CategoryParentLevel,
      columns,
      loading,
      edit_visible,
      edit,
      add,
      treeSelectData,
      onDelete,
      edit_onClose,
      edit_Submit,
      record_category,
    }
  }
});
</script>

