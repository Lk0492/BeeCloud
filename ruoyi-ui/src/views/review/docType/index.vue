<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="类型编码" prop="typeCode">
        <el-input v-model="queryParams.typeCode" placeholder="请输入类型编码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="queryParams.typeName" placeholder="请输入类型名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="岗位状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['review:docType:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['review:docType:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['review:docType:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="docTypeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型编码" align="center" prop="typeCode" width="160" />
      <el-table-column label="类型名称" align="center" prop="typeName" width="150" />
      <el-table-column label="材料说明" align="center" prop="typeDesc" :show-overflow-tooltip="true" />
      <el-table-column label="是否必填" align="center" prop="isRequired" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRequired === '1' ? 'danger' : 'info'">{{ scope.row.isRequired === '1' ? '必填' : '选填' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="允许多文件" align="center" prop="allowMultiple" width="110">
        <template slot-scope="scope">
          <el-tag :type="scope.row.allowMultiple === '1' ? 'success' : 'info'">{{ scope.row.allowMultiple === '1' ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最大文件数" align="center" prop="maxFileCount" width="100" />
      <el-table-column label="允许后缀" align="center" prop="allowedSuffix" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="排序" align="center" prop="sortNum" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['review:docType:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)" v-hasPermi="['review:docType:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="类型编码" prop="typeCode">
          <el-input v-model="form.typeCode" placeholder="请输入类型编码，如：id_card" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入类型名称，如：身份证" />
        </el-form-item>
        <el-form-item label="材料说明" prop="typeDesc">
          <el-input v-model="form.typeDesc" type="textarea" placeholder="请输入材料说明" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否必填" prop="isRequired">
              <el-radio-group v-model="form.isRequired">
                <el-radio label="1">必填</el-radio>
                <el-radio label="0">选填</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="允许多文件" prop="allowMultiple">
              <el-radio-group v-model="form.allowMultiple">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大文件数" prop="maxFileCount">
              <el-input-number v-model="form.maxFileCount" :min="1" :max="99" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="允许后缀" prop="allowedSuffix">
          <el-input v-model="form.allowedSuffix" placeholder="如：pdf,jpg,jpeg,png" />
        </el-form-item>
        <el-form-item label="最大文件大小">
          <el-input-number v-model="formMaxSizeMB" :min="1" :max="100" controls-position="right" /> MB
        </el-form-item>
        <el-form-item label="排序" prop="sortNum">
          <el-input-number v-model="form.sortNum" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
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
import { listDocType, getDocType, addDocType, updateDocType, delDocType } from "@/api/review/docType"

export default {
  name: "BcDocType",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      docTypeList: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, typeCode: '', typeName: '', status: '' },
      form: {},
      rules: {
        typeCode: [{ required: true, message: "类型编码不能为空", trigger: "blur" }],
        typeName: [{ required: true, message: "类型名称不能为空", trigger: "blur" }]
      },
      formMaxSizeMB: 10
    }
  },
  created() { this.getList() },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await listDocType(this.queryParams)
        this.docTypeList = res.rows
        this.total = res.total
      } catch (e) { this.$modal.msgError("加载失败") }
      finally { this.loading = false }
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.typeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.formMaxSizeMB = 10
      this.open = true
      this.title = "添加资料类型"
    },
    handleUpdate(row) {
      const typeId = row.typeId || this.ids[0]
      getDocType(typeId).then(res => {
        this.form = res.data
        this.formMaxSizeMB = Math.round((this.form.maxFileSize || 10485760) / 1048576)
        this.open = true
        this.title = "修改资料类型"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.maxFileSize = this.formMaxSizeMB * 1024 * 1024
          if (this.form.typeId) {
            updateDocType(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDocType(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { isRequired: '1', allowMultiple: '0', maxFileCount: 1, maxFileSize: 10485760, sortNum: 0, status: '0' }
      this.resetForm("form")
    },
    handleDelete(row) {
      const typeIds = row.typeId || this.ids
      this.$modal.confirm('是否确认删除类型编号为"' + typeIds + '"的数据项？').then(() => delDocType(typeIds)).then(() => {
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
