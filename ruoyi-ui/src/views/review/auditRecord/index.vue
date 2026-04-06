<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作类型" prop="actionType">
        <el-select v-model="queryParams.actionType" placeholder="请选择" clearable>
          <el-option v-for="s in actionOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="recordList">
      <el-table-column label="申请单编号" align="center" prop="formId" width="120">
        <template slot-scope="scope">{{ scope.row.formId }}</template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center" prop="studentName" width="120" />
      <el-table-column label="审核员" align="center" prop="reviewerName" width="120" />
      <el-table-column label="操作类型" align="center" prop="actionType" width="100">
        <template slot-scope="scope">
          <el-tag :type="getActionType(scope.row.actionType)">{{ getActionLabel(scope.row.actionType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态变更" align="center" width="180">
        <template slot-scope="scope">
          <span class="text-muted">{{ getStatusLabel(scope.row.formStatusBefore) }}</span>
          <i class="el-icon-right"></i>
          <span class="text-primary">{{ getStatusLabel(scope.row.formStatusAfter) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="actionRemark" :show-overflow-tooltip="true" />
      <el-table-column label="操作时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listRecord, getRecordsByFormId } from "@/api/review/auditRecord"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "BcAuditRecord",
  data() {
    return {
      loading: false,
      recordList: [],
      total: 0,
      showSearch: true,
      queryParams: { pageNum: 1, pageSize: 10, studentName: '', actionType: '' },
      actionOptions: [
        { value: 'submit', label: '提交' },
        { value: 'accept', label: '通过' },
        { value: 'reject', label: '驳回' },
        { value: 'resubmit', label: '补交' },
        { value: 'review', label: '认领' },
        { value: 'assign', label: '分配' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    async getList() {
      this.loading = true
      try {
        const res = await listRecord(this.queryParams)
        this.recordList = res.rows
        this.total = res.total
      } catch (e) {
        this.$modal.msgError("加载失败")
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    getActionType(type) {
      const map = { 'submit': 'primary', 'accept': 'success', 'reject': 'danger', 'resubmit': 'warning', 'review': 'info', 'assign': 'info' }
      return map[type] || 'info'
    },
    getActionLabel(type) {
      const map = { 'submit': '提交', 'accept': '通过', 'reject': '驳回', 'resubmit': '补交', 'review': '认领', 'assign': '分配' }
      return map[type] || type
    },
    getStatusLabel(status) {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回', '5': '补交中', '6': '再审核' }
      return map[status] || status || '-'
    }
  }
}
</script>

<style scoped>
.text-muted { color: #909399; }
.text-primary { color: #409EFF; }
.mb8 { margin-bottom: 16px; }
</style>
