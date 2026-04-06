<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="status-cards mb8">
      <el-col :span="3">
        <div class="stat-card total">
          <div class="stat-num">{{ stats.totalForms || 0 }}</div>
          <div class="stat-label">总申请单</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card submitted">
          <div class="stat-num">{{ stats.submittedCount || 0 }}</div>
          <div class="stat-label">已提交</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card reviewing">
          <div class="stat-num">{{ stats.reviewingCount || 0 }}</div>
          <div class="stat-label">审核中</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card passed">
          <div class="stat-num">{{ stats.passedCount || 0 }}</div>
          <div class="stat-label">已通过</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card rejected">
          <div class="stat-num">{{ stats.rejectedCount || 0 }}</div>
          <div class="stat-label">已驳回</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card supply">
          <div class="stat-num">{{ stats.supplyCount || 0 }}</div>
          <div class="stat-label">补交中</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card rate">
          <div class="stat-num">{{ stats.passRate || 0 }}%</div>
          <div class="stat-label">通过率</div>
        </div>
      </el-col>
      <el-col :span="3">
        <div class="stat-card rate">
          <div class="stat-num">{{ stats.rejectRate || 0 }}%</div>
          <div class="stat-label">驳回率</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="申请单状态" prop="formStatus">
        <el-select v-model="queryParams.formStatus" placeholder="请选择" clearable>
          <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-check" size="mini" :disabled="multiple" @click="handleBatchAccept" v-hasPermi="['review:audit:batchAccept']">批量通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-close" size="mini" :disabled="multiple" @click="handleBatchReject" v-hasPermi="['review:audit:reject']">批量驳回</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-refresh" size="mini" :disabled="multiple" @click="handleBatchSupply" v-hasPermi="['review:audit:reject']">要求补交</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="formList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="申请单编号" align="center" prop="formNo" width="160" />
      <el-table-column label="学生姓名" align="center" prop="studentName" width="100" />
      <el-table-column label="学号" align="center" prop="studentNo" width="120" />
      <el-table-column label="组织" align="center" prop="deptName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="入学年份" align="center" prop="admissionYear" width="100" />
      <el-table-column label="状态" align="center" prop="formStatus" width="90">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.formStatus)">{{ getStatusLabel(scope.row.formStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="附件数" align="center" prop="totalAttachments" width="80" />
      <el-table-column label="审核员" align="center" prop="reviewerName" width="100" />
      <el-table-column label="提交时间" align="center" prop="submitTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="驳回原因" align="center" prop="rejectReason" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button v-if="scope.row.formStatus === '1' || scope.row.formStatus === '2' || scope.row.formStatus === '6'" size="mini" type="text" icon="el-icon-check" style="color:#67c23a" @click="handleAccept(scope.row)" v-hasPermi="['review:audit:accept']">通过</el-button>
          <el-button v-if="scope.row.formStatus === '1' || scope.row.formStatus === '2' || scope.row.formStatus === '6'" size="mini" type="text" icon="el-icon-close" style="color:#f56c6c" @click="handleReject(scope.row)" v-hasPermi="['review:audit:reject']">驳回</el-button>
          <el-button v-if="scope.row.formStatus === '1' || scope.row.formStatus === '2' || scope.row.formStatus === '6'" size="mini" type="text" icon="el-icon-warning" style="color:#e6a23c" @click="handleSupply(scope.row)" v-hasPermi="['review:audit:reject']">补交</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 查看详情弹窗 -->
    <el-dialog title="申请单详情" :visible.sync="detailVisible" width="900px" append-to-body>
      <el-descriptions :column="2" border v-if="currentForm">
        <el-descriptions-item label="申请单编号">{{ currentForm.formNo }}</el-descriptions-item>
        <el-descriptions-item label="学生姓名">{{ currentForm.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ currentForm.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="身份证">{{ currentForm.idCard }}</el-descriptions-item>
        <el-descriptions-item label="组织">{{ currentForm.deptName }}</el-descriptions-item>
        <el-descriptions-item label="入学年份">{{ currentForm.admissionYear }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentForm.formStatus)">{{ getStatusLabel(currentForm.formStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核员">{{ currentForm.reviewerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ parseTime(currentForm.submitTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ parseTime(currentForm.reviewTime) }}</el-descriptions-item>
        <el-descriptions-item label="驳回原因" :span="2" v-if="currentForm.rejectReason">
          <span class="text-danger">{{ currentForm.rejectReason }}</span>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">附件列表</el-divider>
      <div v-if="formAttachments.length > 0">
        <el-table :data="formAttachments" size="small" border>
          <el-table-column label="资料类型" prop="fileCategoryName" width="120" />
          <el-table-column label="文件名" prop="fileName" min-width="200" :show-overflow-tooltip="true" />
          <el-table-column label="大小" width="100">
            <template slot-scope="scope">{{ formatFileSize(scope.row.fileSize) }}</template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag size="mini" :type="getAttachStatusType(scope.row.attachStatus)">{{ getAttachStatusLabel(scope.row.attachStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-view" @click="previewFile(scope.row)">预览</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-empty v-else description="暂无附件" />

      <!-- 审核操作 -->
      <el-divider content-position="left">审核操作</el-divider>
      <div class="audit-actions">
        <el-input v-model="auditRemark" type="textarea" :rows="3" placeholder="请输入审核意见（驳回或补交时必填）" />
        <div class="audit-btn-group mt10">
          <el-button type="success" icon="el-icon-check" @click="doAccept" v-hasPermi="['review:audit:accept']">审核通过</el-button>
          <el-button type="danger" plain icon="el-icon-close" @click="doReject" v-hasPermi="['review:audit:reject']">审核驳回</el-button>
          <el-button type="warning" plain icon="el-icon-warning" @click="doSupply" v-hasPermi="['review:audit:reject']">要求补交</el-button>
          <el-button @click="detailVisible = false">关闭</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAudit, getAuditInfo, acceptForm, rejectForm, requestSupply, batchAccept, batchReject, getStatistics } from "@/api/review/audit"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "BcAudit",
  data() {
    return {
      loading: false,
      formList: [],
      total: 0,
      showSearch: true,
      ids: [],
      multiple: true,
      queryParams: { pageNum: 1, pageSize: 10, studentName: '', studentNo: '', formStatus: '' },
      stats: {},
      detailVisible: false,
      currentForm: null,
      formAttachments: [],
      auditRemark: '',
      statusOptions: [
        { value: '0', label: '未提交' },
        { value: '1', label: '已提交' },
        { value: '2', label: '审核中' },
        { value: '3', label: '已通过' },
        { value: '4', label: '已驳回' },
        { value: '5', label: '补交中' },
        { value: '6', label: '再审核' }
      ]
    }
  },
  created() {
    this.getList()
    this.loadStats()
  },
  methods: {
    parseTime,
    formatFileSize(size) {
      if (!size) return '-'
      if (size < 1024) return size + ' B'
      if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
      if (size < 1024 * 1024 * 1024) return (size / 1024 / 1024).toFixed(1) + ' MB'
      return (size / 1024 / 1024 / 1024).toFixed(1) + ' GB'
    },
    async getList() {
      this.loading = true
      try {
        const res = await listAudit(this.queryParams)
        this.formList = res.rows
        this.total = res.total
      } catch (e) {
        this.$modal.msgError("加载失败")
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const res = await getStatistics()
        this.stats = res.data || {}
      } catch (e) {}
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.formId)
      this.multiple = !selection.length
    },
    getStatusType(status) {
      const map = { '0': 'info', '1': 'primary', '2': 'warning', '3': 'success', '4': 'danger', '5': 'warning', '6': 'warning' }
      return map[status] || 'info'
    },
    getStatusLabel(status) {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回', '5': '补交中', '6': '再审核' }
      return map[status] || status
    },
    getAttachStatusType(status) {
      const map = { '0': 'info', '1': 'primary', '2': 'warning', '3': 'success', '4': 'danger' }
      return map[status] || 'info'
    },
    getAttachStatusLabel(status) {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回' }
      return map[status] || status
    },
    async handleView(row) {
      try {
        const res = await getAuditInfo(row.formId)
        this.currentForm = res.data
        this.formAttachments = this.currentForm?.attachmentList || []
        this.auditRemark = ''
        this.detailVisible = true
      } catch (e) {
        this.$modal.msgError("加载详情失败")
      }
    },
    async handleAccept(row) {
      await this.$modal.confirm("确认审核通过该申请单？")
      try {
        await acceptForm(row.formId, { rejectReason: '' })
        this.$modal.msgSuccess("审核通过")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async handleReject(row) {
      const { value } = await this.$prompt('请输入驳回原因', '审核驳回', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '驳回原因不能为空'
      })
      try {
        await rejectForm(row.formId, { rejectReason: value })
        this.$modal.msgSuccess("已驳回")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async handleSupply(row) {
      const { value } = await this.$prompt('请输入补交说明', '要求补交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '补交说明不能为空'
      })
      try {
        await requestSupply(row.formId, { rejectReason: value })
        this.$modal.msgSuccess("已发送补交通知")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async handleBatchAccept() {
      await this.$modal.confirm(`确认批量通过 ${this.ids.length} 条申请单？`)
      try {
        await batchAccept({ ids: this.ids, rejectReason: '' })
        this.$modal.msgSuccess("批量通过成功")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async handleBatchReject() {
      const { value } = await this.$prompt('请输入驳回原因', '批量驳回', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '驳回原因不能为空'
      })
      try {
        await batchReject({ ids: this.ids, rejectReason: value })
        this.$modal.msgSuccess("批量驳回成功")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async handleBatchSupply() {
      const { value } = await this.$prompt('请输入补交说明', '批量要求补交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '补交说明不能为空'
      })
      try {
        for (const id of this.ids) {
          await requestSupply(id, { rejectReason: value })
        }
        this.$modal.msgSuccess("已发送补交通知")
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async doAccept() {
      try {
        await acceptForm(this.currentForm.formId, { rejectReason: this.auditRemark })
        this.$modal.msgSuccess("审核通过")
        this.detailVisible = false
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async doReject() {
      if (!this.auditRemark || !this.auditRemark.trim()) {
        this.$modal.msgWarning("驳回原因不能为空")
        return
      }
      try {
        await rejectForm(this.currentForm.formId, { rejectReason: this.auditRemark })
        this.$modal.msgSuccess("已驳回")
        this.detailVisible = false
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    async doSupply() {
      if (!this.auditRemark || !this.auditRemark.trim()) {
        this.$modal.msgWarning("补交说明不能为空")
        return
      }
      try {
        await requestSupply(this.currentForm.formId, { rejectReason: this.auditRemark })
        this.$modal.msgSuccess("已发送补交通知")
        this.detailVisible = false
        this.getList()
        this.loadStats()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "操作失败")
      }
    },
    previewFile(row) {
      if (row.filePath) {
        window.open(row.filePath, '_blank')
      }
    }
  }
}
</script>

<style scoped>
.status-cards { margin-bottom: 16px; }
.stat-card {
  border-radius: 8px;
  padding: 16px 12px;
  text-align: center;
  color: #fff;
  min-height: 80px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.stat-card.total { background: linear-gradient(135deg, #409EFF, #337ecc); }
.stat-card.submitted { background: linear-gradient(135deg, #909399, #606266); }
.stat-card.reviewing { background: linear-gradient(135deg, #e6a23c, #b8860b); }
.stat-card.passed { background: linear-gradient(135deg, #67c23a, #4a8a2b); }
.stat-card.rejected { background: linear-gradient(135deg, #f56c6c, #b83030); }
.stat-card.supply { background: linear-gradient(135deg, #e6a23c, #b8860b); }
.stat-card.rate { background: linear-gradient(135deg, #9c27b0, #7b1fa2); }
.stat-num { font-size: 26px; font-weight: bold; }
.stat-label { font-size: 12px; margin-top: 4px; opacity: 0.9; }

.mb8 { margin-bottom: 16px; }
.mt10 { margin-top: 10px; }
.text-danger { color: #f56c6c; }

.audit-actions { padding: 8px 0; }
.audit-btn-group { display: flex; gap: 8px; }
</style>
