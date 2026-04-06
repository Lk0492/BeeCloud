<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="申请单状态" prop="formStatus">
        <el-select v-model="queryParams.formStatus" placeholder="全部状态" clearable>
          <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="historyList">
      <el-table-column label="申请单编号" align="center" prop="formNo" width="180" />
      <el-table-column label="状态" align="center" prop="formStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.formStatus)">{{ getStatusLabel(scope.row.formStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="版本" align="center" prop="versionNum" width="80">
        <template slot-scope="scope">第 {{ scope.row.versionNum }} 版</template>
      </el-table-column>
      <el-table-column label="附件数" align="center" prop="totalAttachments" width="80" />
      <el-table-column label="提交时间" align="center" prop="submitTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.submitTime) }}</span></template>
      </el-table-column>
      <el-table-column label="审核员" align="center" prop="reviewerName" width="100" />
      <el-table-column label="审核时间" align="center" prop="reviewTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.reviewTime) }}</span></template>
      </el-table-column>
      <el-table-column label="驳回原因" align="center" prop="rejectReason" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" fixed="right" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 查看详情 -->
    <el-dialog title="提交详情" :visible.sync="detailVisible" width="800px" append-to-body>
      <el-descriptions :column="2" border v-if="currentForm">
        <el-descriptions-item label="申请单编号">{{ currentForm.formNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentForm.formStatus)">{{ getStatusLabel(currentForm.formStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ parseTime(currentForm.submitTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ parseTime(currentForm.reviewTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核员">{{ currentForm.reviewerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="驳回原因" :span="2" v-if="currentForm.rejectReason">
          <span class="text-danger">{{ currentForm.rejectReason }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-button type="primary" size="small" v-if="currentForm && ['4','5'].includes(currentForm.formStatus)" @click="$router.push('/review/submit')">
        前往补交
      </el-button>
    </el-dialog>
  </div>
</template>

<script>
import { getHistory } from "@/api/review/submit"
import { getAuditInfo } from "@/api/review/audit"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "BcRecord",
  data() {
    return {
      loading: false,
      historyList: [],
      showSearch: true,
      queryParams: { formStatus: '' },
      statusOptions: [
        { value: '0', label: '未提交' },
        { value: '1', label: '已提交' },
        { value: '2', label: '审核中' },
        { value: '3', label: '已通过' },
        { value: '4', label: '已驳回' },
        { value: '5', label: '补交中' },
        { value: '6', label: '再审核' }
      ],
      detailVisible: false,
      currentForm: null
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
        const res = await getHistory()
        let data = res.data || []
        if (this.queryParams.formStatus) {
          data = data.filter(item => item.formStatus === this.queryParams.formStatus)
        }
        this.historyList = data
      } catch (e) {
        this.$modal.msgError("加载失败")
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.getList() },
    resetQuery() { this.queryParams = { formStatus: '' }; this.getList() },
    getStatusType(status) {
      const map = { '0': 'info', '1': 'primary', '2': 'warning', '3': 'success', '4': 'danger', '5': 'warning', '6': 'warning' }
      return map[status] || 'info'
    },
    getStatusLabel(status) {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回', '5': '补交中', '6': '再审核' }
      return map[status] || status
    },
    async handleView(row) {
      try {
        const res = await getAuditInfo(row.formId)
        this.currentForm = res.data
        this.detailVisible = true
      } catch (e) {
        this.$modal.msgError("加载详情失败")
      }
    }
  }
}
</script>

<style scoped>
.text-danger { color: #f56c6c; }
</style>
