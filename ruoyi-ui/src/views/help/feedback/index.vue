<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb8">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#409eff">
            <i class="el-icon-question"></i>
          </div>
          <div class="stat-info">
            <div class="stat-num">{{ pendingCount }}</div>
            <div class="stat-label">待回复</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background:#67c23a">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-info">
            <div class="stat-num">{{ repliedCount }}</div>
            <div class="stat-label">已回复</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="用户姓名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="问题内容" prop="question">
        <el-input v-model="queryParams.question" placeholder="请输入问题内容" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="回复状态" prop="replyStatus">
        <el-select v-model="queryParams.replyStatus" placeholder="回复状态" clearable>
          <el-option label="待回复" value="0" />
          <el-option label="已回复" value="1" />
          <el-option label="已关闭" value="2" />
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
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['help:feedback:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['help:feedback:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="feedbackList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户姓名" align="center" prop="userName" width="120" />
      <el-table-column label="问题内容" align="left" prop="question" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="搜索关键词" align="left" prop="searchKeywords" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="回复状态" align="center" prop="replyStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.replyStatus)">{{ getStatusLabel(scope.row.replyStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="回复内容" align="left" prop="answer" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="回复人" align="center" prop="replyBy" width="100" />
      <el-table-column label="提交时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column label="回复时间" align="center" prop="replyTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.replyTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button v-if="scope.row.replyStatus === '0'" size="mini" type="primary" icon="el-icon-s-comment" @click="handleReply(scope.row)" v-hasPermi="['help:feedback:reply']">回复</el-button>
          <el-button v-else size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)" v-hasPermi="['help:feedback:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 回复对话框 -->
    <el-dialog title="回复问题" :visible.sync="replyOpen" width="600px" append-to-body>
      <el-form ref="replyForm" :model="replyForm" :rules="replyRules" label-width="100px">
        <el-form-item label="用户姓名">
          <el-input v-model="replyForm.userName" disabled />
        </el-form-item>
        <el-form-item label="问题描述">
          <div class="question-box">{{ replyForm.question }}</div>
        </el-form-item>
        <el-form-item label="回复内容" prop="answer">
          <el-input v-model="replyForm.answer" type="textarea" :rows="6" placeholder="请输入回复内容" maxlength="1000" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReply" :loading="replyLoading">提交回复</el-button>
        <el-button @click="replyOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="查看详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions v-if="viewData" :column="1" border>
        <el-descriptions-item label="用户姓名">{{ viewData.userName }}</el-descriptions-item>
        <el-descriptions-item label="问题描述">{{ viewData.question }}</el-descriptions-item>
        <el-descriptions-item label="搜索关键词">{{ viewData.searchKeywords || '-' }}</el-descriptions-item>
        <el-descriptions-item label="回复状态">
          <el-tag :type="getStatusType(viewData.replyStatus)">{{ getStatusLabel(viewData.replyStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="回复内容">{{ viewData.answer || '-' }}</el-descriptions-item>
        <el-descriptions-item label="回复人">{{ viewData.replyBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ parseTime(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="回复时间">{{ parseTime(viewData.replyTime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFeedback, getFeedback, replyFeedback, delFeedback } from "@/api/help/feedback"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "BcHelpFeedback",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      feedbackList: [],
      pendingCount: 0,
      repliedCount: 0,
      replyOpen: false,
      viewOpen: false,
      viewData: null,
      replyLoading: false,
      queryParams: { pageNum: 1, pageSize: 10, userName: '', question: '', replyStatus: '' },
      replyForm: {},
      replyRules: {
        answer: [{ required: true, message: "回复内容不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadStats()
  },
  methods: {
    parseTime,
    async getList() {
      this.loading = true
      try {
        const res = await listFeedback(this.queryParams)
        this.feedbackList = res.rows
        this.total = res.total
        this.updateStats()
      } catch (e) { this.$modal.msgError("加载失败") }
      finally { this.loading = false }
    },
    async loadStats() {
      try {
        const res = await this.$request({ url: '/help/feedback/pendingCount', method: 'get' })
        this.pendingCount = res.data || 0
      } catch (e) {}
    },
    updateStats() {
      this.pendingCount = this.feedbackList.filter(f => f.replyStatus === '0').length + (this.total - this.feedbackList.length > 0 ? this.pendingCount : 0)
    },
    getStatusType(status) {
      const map = { '0': 'warning', '1': 'success', '2': 'info' }
      return map[status] || 'info'
    },
    getStatusLabel(status) {
      const map = { '0': '待回复', '1': '已回复', '2': '已关闭' }
      return map[status] || status
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.feedbackId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleReply(row) {
      this.replyForm = { feedbackId: row.feedbackId, userName: row.userName, question: row.question, answer: '' }
      this.replyOpen = true
    },
    handleView(row) {
      getFeedback(row.feedbackId).then(res => {
        this.viewData = res.data
        this.viewOpen = true
      })
    },
    submitReply() {
      this.$refs.replyForm.validate(valid => {
        if (!valid) return
        this.replyLoading = true
        replyFeedback(this.replyForm).then(() => {
          this.$modal.msgSuccess("回复成功")
          this.replyOpen = false
          this.getList()
        }).finally(() => { this.replyLoading = false })
      })
    },
    handleDelete(row) {
      const feedbackIds = row.feedbackId || this.ids
      this.$modal.confirm('是否确认删除选中的数据项？').then(() => delFeedback(feedbackIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('/help/feedback/export', { ...this.queryParams }, `问题反馈_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 16px; }
.stat-card { display: flex; align-items: center; padding: 10px; }
.stat-icon {
  width: 50px; height: 50px; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: #fff; margin-right: 16px;
}
.stat-info { flex: 1; }
.stat-num { font-size: 24px; font-weight: 600; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.question-box {
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  line-height: 1.6;
  color: #606266;
}
</style>
