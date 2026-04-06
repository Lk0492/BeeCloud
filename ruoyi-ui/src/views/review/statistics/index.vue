<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="status-cards mb8">
      <el-col :span="4">
        <div class="stat-card total">
          <div class="stat-num">{{ stats.totalForms || 0 }}</div>
          <div class="stat-label">总申请单</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="stat-card submitted">
          <div class="stat-num">{{ stats.submittedCount || 0 }}</div>
          <div class="stat-label">已提交</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="stat-card reviewing">
          <div class="stat-num">{{ stats.reviewingCount || 0 }}</div>
          <div class="stat-label">审核中</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="stat-card passed">
          <div class="stat-num">{{ stats.passedCount || 0 }}</div>
          <div class="stat-label">已通过</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="stat-card rejected">
          <div class="stat-num">{{ stats.rejectedCount || 0 }}</div>
          <div class="stat-label">已驳回</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="stat-card supply">
          <div class="stat-num">{{ stats.supplyCount || 0 }}</div>
          <div class="stat-label">补交中</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb8">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">审核通过率</div>
          <div class="rate-display">
            <span class="rate-num success">{{ stats.passRate || 0 }}%</span>
            <div class="rate-bar">
              <div class="rate-fill success" :style="{ width: (stats.passRate || 0) + '%' }"></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">审核驳回率</div>
          <div class="rate-display">
            <span class="rate-num danger">{{ stats.rejectRate || 0 }}%</span>
            <div class="rate-bar">
              <div class="rate-fill danger" :style="{ width: (stats.rejectRate || 0) + '%' }"></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStatistics } from "@/api/review/audit"

export default {
  name: "BcStatistics",
  data() {
    return {
      stats: {}
    }
  },
  created() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const res = await getStatistics()
        this.stats = res.data || {}
      } catch (e) {}
    }
  }
}
</script>

<style scoped>
.status-cards { margin-bottom: 16px; }
.stat-card {
  border-radius: 8px;
  padding: 20px 12px;
  text-align: center;
  color: #fff;
  min-height: 90px;
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
.stat-num { font-size: 30px; font-weight: bold; }
.stat-label { font-size: 13px; margin-top: 4px; opacity: 0.9; }
.mb8 { margin-bottom: 16px; }

.rate-display { text-align: center; padding: 10px 0; }
.rate-num { font-size: 36px; font-weight: bold; display: block; }
.rate-num.success { color: #67c23a; }
.rate-num.danger { color: #f56c6c; }
.rate-bar { height: 12px; background: #f0f0f0; border-radius: 6px; margin-top: 12px; overflow: hidden; }
.rate-fill { height: 100%; border-radius: 6px; transition: width 1s ease; }
.rate-fill.success { background: linear-gradient(90deg, #67c23a, #8bc34a); }
.rate-fill.danger { background: linear-gradient(90deg, #f56c6c, #e53935); }
</style>
