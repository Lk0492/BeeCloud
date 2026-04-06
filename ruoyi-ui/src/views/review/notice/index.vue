<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb8">
      <el-col :span="1.5">
        <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
          <el-button size="small" icon="el-icon-bell">我的消息</el-button>
        </el-badge>
      </el-col>
      <el-col :span="1.5">
        <el-button size="small" icon="el-icon-check" @click="handleMarkAllRead" v-hasPermi="['review:notice:read']">全部已读</el-button>
      </el-col>
    </el-row>

    <!-- 搜索 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="通知类型" prop="noticeType">
        <el-select v-model="queryParams.noticeType" placeholder="请选择" clearable>
          <el-option v-for="s in noticeTypeOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否已读" prop="isRead">
        <el-select v-model="queryParams.isRead" placeholder="请选择" clearable>
          <el-option label="未读" value="0" />
          <el-option label="已读" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 消息列表 -->
    <el-table v-loading="loading" :data="noticeList">
      <el-table-column label="类型" align="center" prop="noticeType" width="120">
        <template slot-scope="scope">
          <el-tag :type="getNoticeType(scope.row.noticeType)">{{ getNoticeTypeLabel(scope.row.noticeType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="left" prop="title" min-width="200" />
      <el-table-column label="内容" align="left" prop="content" :show-overflow-tooltip="true" min-width="300" />
      <el-table-column label="是否已读" align="center" prop="isRead" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRead === '1' ? 'info' : 'warning'" size="small">
            {{ scope.row.isRead === '1' ? '已读' : '未读' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button v-if="scope.row.isRead !== '1'" size="mini" type="text" icon="el-icon-check" @click="handleMarkRead(scope.row)" v-hasPermi="['review:notice:read']">已读</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 消息详情 -->
    <el-dialog :title="currentNotice && currentNotice.title || '消息详情'" :visible.sync="noticeVisible" width="600px" append-to-body>
      <div class="notice-meta">
        <el-tag :type="getNoticeType(currentNotice && currentNotice.noticeType)">{{ getNoticeTypeLabel(currentNotice && currentNotice.noticeType) }}</el-tag>
        <span class="ml10 text-muted">{{ parseTime(currentNotice && currentNotice.createTime) }}</span>
      </div>
      <el-divider />
      <div class="notice-content">{{ currentNotice && currentNotice.content }}</div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="noticeVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotice, getUnreadCount, getNotice, markRead, markAllRead } from "@/api/review/notice"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "BcNotice",
  data() {
    return {
      loading: false,
      noticeList: [],
      total: 0,
      unreadCount: 0,
      showSearch: true,
      queryParams: { pageNum: 1, pageSize: 10, noticeType: '', isRead: '' },
      noticeVisible: false,
      currentNotice: null,
      noticeTypeOptions: [
        { value: 'review_pass', label: '审核通过' },
        { value: 'review_reject', label: '审核驳回' },
        { value: 'supply', label: '补交通知' },
        { value: 'system', label: '系统通知' }
      ]
    }
  },
  created() {
    this.getList()
    this.loadUnreadCount()
  },
  methods: {
    parseTime,
    async getList() {
      this.loading = true
      try {
        const res = await listNotice(this.queryParams)
        this.noticeList = res.rows
        this.total = res.total
      } catch (e) { this.$modal.msgError("加载失败") }
      finally { this.loading = false }
    },
    async loadUnreadCount() {
      try {
        const res = await getUnreadCount()
        this.unreadCount = res.data || 0
      } catch (e) {}
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    getNoticeType(type) {
      const map = { 'review_pass': 'success', 'review_reject': 'danger', 'supply': 'warning', 'system': 'primary' }
      return map[type] || 'info'
    },
    getNoticeTypeLabel(type) {
      const map = { 'review_pass': '审核通过', 'review_reject': '审核驳回', 'supply': '补交通知', 'system': '系统通知' }
      return map[type] || type
    },
    async handleView(row) {
      try {
        const res = await getNotice(row.noticeId)
        this.currentNotice = res.data
        this.noticeVisible = true
        if (row.isRead !== '1') {
          await markRead(row.noticeId)
          row.isRead = '1'
          this.loadUnreadCount()
        }
      } catch (e) {}
    },
    async handleMarkRead(row) {
      try {
        await markRead(row.noticeId)
        row.isRead = '1'
        this.loadUnreadCount()
        this.$modal.msgSuccess("已标记为已读")
      } catch (e) {}
    },
    async handleMarkAllRead() {
      try {
        await markAllRead()
        this.$modal.msgSuccess("全部已标记为已读")
        this.loadUnreadCount()
        this.getList()
      } catch (e) {}
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 16px; }
.ml10 { margin-left: 10px; }
.text-muted { color: #909399; font-size: 13px; }
.notice-meta { display: flex; align-items: center; }
.notice-content { line-height: 1.8; color: #303133; white-space: pre-wrap; }
</style>
