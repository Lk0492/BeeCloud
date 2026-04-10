<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="分类名称" prop="categoryName">
        <el-input v-model="queryParams.categoryName" placeholder="请输入分类名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['help:category:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['help:category:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['help:category:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 表格树 -->
    <el-table v-if="refreshTable" v-loading="loading" :data="categoryList" row-key="categoryId" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" default-expand-all>
      <el-table-column prop="categoryName" label="分类名称" width="250">
        <template slot-scope="scope">
          <span>{{ scope.row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="categoryCode" label="分类编码" width="180" />
      <el-table-column prop="icon" label="图标" width="100" align="center">
        <template slot-scope="scope">
          <i :class="'el-icon-' + (scope.row.icon || 'folder')" style="font-size: 18px; color: #409eff;"></i>
        </template>
      </el-table-column>
      <el-table-column prop="sortNum" label="排序" width="100" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)" v-hasPermi="['help:category:add']">新增</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['help:category:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)" v-hasPermi="['help:category:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级分类" prop="parentId">
              <treeselect v-model="form.parentId" :options="categoryOptions" :normalizer="normalizer" placeholder="选择上级分类" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="分类名称" prop="categoryName">
              <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类编码" prop="categoryCode">
              <el-input v-model="form.categoryCode" placeholder="请输入分类编码，如：freshman" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortNum">
              <el-input-number v-model="form.sortNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分类图标">
          <el-select v-model="form.icon" placeholder="请选择图标" clearable style="width:100%">
            <el-option v-for="icon in iconOptions" :key="icon" :label="icon" :value="icon">
              <span><i :class="'el-icon-' + icon"></i> {{ icon }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCategory, getCategory, addCategory, updateCategory, delCategory } from "@/api/help/category"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "BcHelpCategory",
  dicts: ['sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      categoryList: [],
      categoryOptions: [],
      title: "",
      open: false,
      refreshTable: true,
      queryParams: { categoryName: '', status: '' },
      form: {},
      rules: {
        categoryName: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
        categoryCode: [{ required: true, message: "分类编码不能为空", trigger: "blur" }],
        sortNum: [{ required: true, message: "排序号不能为空", trigger: "blur" }]
      },
      iconOptions: ['user', 'upload', 'download', 'checkbox', 'lock', 'question', 'folder', 'document', 'info', 'warning', 'success', 'star']
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await listCategory(this.queryParams)
        this.categoryList = this.handleTree(res.rows || [], "categoryId")
      } catch (e) { this.$modal.msgError("加载失败") }
      finally { this.loading = false }
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return { id: node.categoryId, label: node.categoryName, children: node.children }
    },
    handleQuery() { this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.categoryId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd(row) {
      this.reset()
      if (row != undefined) {
        this.form.parentId = row.categoryId
      }
      this.open = true
      this.title = "添加分类"
      listCategory().then(res => {
        this.categoryOptions = this.handleTree(res.rows || [], "categoryId")
      })
    },
    handleUpdate(row) {
      this.reset()
      getCategory(row.categoryId).then(res => {
        this.form = res.data
        this.open = true
        this.title = "修改分类"
        listCategory().then(resp => {
          this.categoryOptions = this.handleTree(resp.rows || [], "categoryId")
        })
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        if (this.form.categoryId) {
          updateCategory(this.form).then(() => {
            this.$modal.msgSuccess("修改成功")
            this.open = false
            this.getList()
          })
        } else {
          addCategory(this.form).then(() => {
            this.$modal.msgSuccess("新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { parentId: 0, sortNum: 0, status: '0', icon: 'folder' }
      this.resetForm("form")
    },
    handleDelete(row) {
      const categoryIds = row.categoryId || this.ids
      this.$modal.confirm('是否确认删除分类名称为"' + row.categoryName + '"的数据项？').then(() => delCategory(categoryIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 16px; }
</style>
