<template>
  <div class="app-container">
    <!-- 状态卡片 -->
    <el-row :gutter="20" class="status-cards mb8">
      <el-col :span="4">
        <div class="status-card draft">
          <div class="status-num">{{ statusCount['0'] || 0 }}</div>
          <div class="status-label">未提交</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="status-card submitted">
          <div class="status-num">{{ statusCount['1'] || 0 }}</div>
          <div class="status-label">已提交</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="status-card reviewing">
          <div class="status-num">{{ statusCount['2'] || 0 }}</div>
          <div class="status-label">审核中</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="status-card passed">
          <div class="status-num">{{ statusCount['3'] || 0 }}</div>
          <div class="status-label">已通过</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="status-card rejected">
          <div class="status-num">{{ statusCount['4'] || 0 }}</div>
          <div class="status-label">已驳回</div>
        </div>
      </el-col>
      <el-col :span="4">
        <div class="status-card supply">
          <div class="status-num">{{ statusCount['5'] || 0 }}</div>
          <div class="status-label">补交中</div>
        </div>
      </el-col>
    </el-row>

    <!-- 申请单信息 -->
    <el-card class="mb8" shadow="never">
      <div slot="header">
        <span>申请单信息</span>
        <el-tag v-if="currentForm" class="fr" :type="statusType">
          {{ statusLabel }}
        </el-tag>
      </div>
      <el-descriptions :column="3" border v-if="currentForm">
        <el-descriptions-item label="申请单编号">{{ currentForm.formNo }}</el-descriptions-item>
        <el-descriptions-item label="学生姓名">{{ currentForm.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ currentForm.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="所属组织">{{ currentForm.deptName }}</el-descriptions-item>
        <el-descriptions-item label="入学年份">{{ currentForm.admissionYear }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ currentForm.submitTime ? parseTime(currentForm.submitTime) : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="版本号" v-if="currentForm.versionNum">
          第 {{ currentForm.versionNum }} 版
        </el-descriptions-item>
        <el-descriptions-item label="审核员" v-if="currentForm.reviewerName">
          {{ currentForm.reviewerName }}
        </el-descriptions-item>
        <el-descriptions-item label="驳回原因" v-if="currentForm.rejectReason" :span="3">
          <span class="text-danger">{{ currentForm.rejectReason }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="加载中..." />
    </el-card>

    <!-- 附件上传区域 -->
    <el-card shadow="never">
      <div slot="header">
        <span>上传资料</span>
        <span class="fr text-muted" style="font-size:12px">
          红色星号(*)为必填项，共 {{ currentForm && currentForm.totalAttachments || 0 }} 个附件
        </span>
      </div>

      <!-- 按资料类型分组展示 -->
      <div v-for="doc in docTypeList" :key="doc.typeId" class="doc-type-section">
        <div class="doc-type-header">
          <span class="doc-type-name">
            <span v-if="doc.isRequired === '1'" class="text-danger">*</span>
            {{ doc.typeName }}
          </span>
          <span class="doc-type-desc text-muted">{{ doc.typeDesc }}</span>
          <span class="doc-type-count fr text-muted">
            已上传: {{ getCategoryCount(doc.typeCode) }} / {{ doc.maxFileCount }}
            <span v-if="doc.allowMultiple === '1'">（支持多文件）</span>
          </span>
        </div>

        <!-- 已上传附件列表 -->
        <div class="file-list" v-if="getAttachments(doc.typeCode).length > 0">
          <div v-for="att in getAttachments(doc.typeCode)" :key="att.detailId" class="file-item">
            <i class="el-icon-document"></i>
            <span class="file-name" :title="att.fileName">{{ att.fileName }}</span>
            <span class="file-size text-muted">{{ formatFileSize(att.fileSize) }}</span>
            <el-tag size="mini" :type="getAttachStatusType(att.attachStatus)">
              {{ getAttachStatusLabel(att.attachStatus) }}
            </el-tag>
            <el-button
              v-if="canDelete(att)"
              size="mini" type="text" icon="el-icon-delete"
              class="fr" style="color:#f56c6c"
              @click="handleDeleteFile(att)">删除
            </el-button>
          </div>
        </div>

        <!-- 上传按钮 -->
        <div class="upload-area" v-if="canUpload(doc)">
          <el-upload
            ref="uploadRef"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :data="{ fileCategory: doc.typeCode, formId: currentForm ? currentForm.formId : 0 }"
            :before-upload="(file) => beforeUpload(file, doc)"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :on-remove="handleUploadRemove"
            :file-list="[]"
            :accept="getAccept(doc)"
            :disabled="uploading"
            multiple
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或 <em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">
              支持 {{ doc.allowedSuffix }}，单个文件不超过 {{ formatFileSize(doc.maxFileSize) }}
            </div>
          </el-upload>
        </div>
        <div v-else-if="!canUpload(doc)" class="upload-disabled text-muted">
          <span v-if="getCategoryCount(doc.typeCode) >= doc.maxFileCount">
            已达最大上传数量({{ doc.maxFileCount }})
          </span>
          <span v-else-if="!canEdit">
            当前状态不允许上传附件
          </span>
        </div>
      </div>

      <!-- 提交按钮 -->
      <div class="submit-area">
        <el-button
          v-if="canSubmit"
          type="primary" icon="el-icon-s-promotion"
          size="medium" :loading="submitting"
          @click="handleSubmit"
        >{{ submitButtonText }}</el-button>
        <el-button
          v-if="canResubmit"
          type="warning" icon="el-icon-refresh"
          size="medium" :loading="submitting"
          @click="handleResubmit"
        >重新提交</el-button>
        <el-button
          v-if="canDeleteForm"
          type="danger" plain icon="el-icon-delete"
          size="medium"
          @click="handleDeleteForm"
        >删除申请单</el-button>
        <el-button size="medium" @click="$router.go(-1)">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCurrentForm, submitForm, resubmitForm, getStatusCounts } from "@/api/review/submit"
import { getDocTypeOptions } from "@/api/review/docType"
import { listAttachment, delAttachment, addAttachment } from "@/api/review/attachment"
import { getToken } from "@/utils/auth"
import { parseTime, formatFileSize } from "@/utils/ruoyi"

export default {
  name: "BcSubmit",
  data() {
    return {
      currentForm: null,
      currentDocType: null, // 当前正在上传的资料类型
      attachmentList: [],
      docTypeList: [],
      statusCount: {},
      uploading: false,
      submitting: false,
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: { Authorization: "Bearer " + getToken() }
    }
  },
  computed: {
    statusType() {
      const map = { '0': 'info', '1': 'primary', '2': 'warning', '3': 'success', '4': 'danger', '5': 'warning', '6': 'warning' }
      return map[this.currentForm?.formStatus] || 'info'
    },
    statusLabel() {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回', '5': '补交中', '6': '再审核' }
      return map[this.currentForm?.formStatus] || ''
    },
    canEdit() {
      return this.currentForm && ['0', '4', '5'].includes(this.currentForm.formStatus)
    },
    canSubmit() {
      return this.currentForm && this.currentForm.formStatus === '0'
    },
    canResubmit() {
      return this.currentForm && ['4', '5'].includes(this.currentForm.formStatus)
    },
    canDeleteForm() {
      return this.currentForm && this.currentForm.formStatus === '0'
    },
    submitButtonText() {
      if (this.currentForm?.formStatus === '4') return '重新提交'
      if (this.currentForm?.formStatus === '5') return '补交并重新提交'
      return '提交审核'
    }
  },
  created() {
    this.loadData()
    this.loadDocTypes()
  },
  methods: {
    parseTime, formatFileSize,
    async loadData() {
      try {
        const [formRes, countRes] = await Promise.all([
          getCurrentForm(),
          getStatusCounts()
        ])
        this.currentForm = formRes.data
        this.statusCount = countRes.data || {}
        if (this.currentForm?.formId) {
          this.loadAttachments()
        }
      } catch (e) {
        this.$modal.msgError("加载申请单失败")
      }
    },
    async loadDocTypes() {
      try {
        const res = await getDocTypeOptions()
        this.docTypeList = res.data || []
      } catch (e) {
        this.$modal.msgError("加载资料类型失败")
      }
    },
    async loadAttachments() {
      try {
        const res = await listAttachment(this.currentForm.formId)
        this.attachmentList = res.data || []
      } catch (e) {
        this.$modal.msgError("加载附件失败")
      }
    },
    getAttachments(category) {
      return this.attachmentList.filter(a => a.fileCategory === category && a.delFlag !== '2')
    },
    getCategoryCount(category) {
      return this.getAttachments(category).length
    },
    canUpload(doc) {
      if (!this.canEdit) return false
      if (doc.allowMultiple !== '1' && this.getCategoryCount(doc.typeCode) >= 1) return false
      if (doc.allowMultiple === '1' && this.getCategoryCount(doc.typeCode) >= doc.maxFileCount) return false
      return true
    },
    canDelete(att) {
      return this.canEdit && att.attachStatus !== '3' && att.attachStatus !== '2'
    },
    getAccept(doc) {
      const suffix = doc.allowedSuffix || 'pdf,jpg,jpeg,png'
      return '.' + suffix.replace(/,/g, ',.')
    },
    beforeUpload(file, doc) {
      // 将当前资料类型信息暂存到 file 对象上，供 success 回调使用
      file._docType = doc
      const suffix = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
      const allowed = (doc.allowedSuffix || '').toLowerCase().split(',').map(s => s.trim())
      if (!allowed.includes(suffix)) {
        this.$modal.msgError(`不支持 ${suffix} 格式，仅支持：${doc.allowedSuffix}`)
        return false
      }
      if (file.size > doc.maxFileSize) {
        this.$modal.msgError(`文件 ${file.name} 超过大小限制（${this.formatFileSize(doc.maxFileSize)}}）`)
        return false
      }
      this.uploading = true
      return true
    },
    handleUploadSuccess(res, file) {
      this.uploading = false
      if (res.code === 200) {
        // 从 file._docType 取出上传时的资料类型
        const doc = file._docType
        const fileName = res.originalFilename || res.fileName || file.name
        const fileSize = file.size || res.size
        const filePath = res.url || res.fileName || ''
        const fileSuffix = fileName.split('.').pop() || ''
        // 调用审核模块接口保存附件元数据
        const attachment = {
          fileCategory: doc ? doc.typeCode : (res.fileCategory || ''),
          fileName: fileName,
          filePath: filePath,
          fileSize: fileSize,
          fileSuffix: fileSuffix
        }
        addAttachment(attachment).then(() => {
          this.loadAttachments()
          this.loadData()
          this.$modal.msgSuccess("上传成功")
        }).catch(err => {
          console.error("保存附件记录失败", err)
          this.$modal.msgError("文件已上传但保存记录失败，请刷新页面重试")
          this.loadAttachments()
          this.loadData()
        })
      } else {
        this.$modal.msgError(res.msg || "上传失败")
      }
    },
    handleUploadError(err, file, fileList) {
      this.uploading = false
      this.$modal.msgError("上传失败：" + err.message)
    },
    handleUploadRemove(file, fileList) {
      // 不做处理，附件通过删除按钮删除
    },
    async handleDeleteFile(att) {
      try {
        await this.$modal.confirm(`确定删除文件 "${att.fileName}" 吗？`)
        await delAttachment([att.detailId])
        this.$modal.msgSuccess("删除成功")
        this.loadAttachments()
        this.loadData()
      } catch (e) {}
    },
    async handleSubmit() {
      if (this.attachmentList.length === 0) {
        this.$modal.msgWarning("请先上传至少一个附件再提交")
        return
      }
      try {
        await this.$modal.confirm("确认提交资料审核吗？提交后将不能修改附件。")
        this.submitting = true
        await submitForm({ formId: this.currentForm.formId })
        this.$modal.msgSuccess("提交成功")
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "提交失败")
      } finally {
        this.submitting = false
      }
    },
    async handleResubmit() {
      try {
        await this.$modal.confirm("确认重新提交吗？")
        this.submitting = true
        await resubmitForm({ formId: this.currentForm.formId })
        this.$modal.msgSuccess("重新提交成功")
        this.loadData()
      } catch (e) {
        if (e !== 'cancel') this.$modal.msgError(e.message || "提交失败")
      } finally {
        this.submitting = false
      }
    },
    async handleDeleteForm() {
      this.$modal.msgWarning("删除功能请在提交记录页面操作")
    },
    getAttachStatusType(status) {
      const map = { '0': 'info', '1': 'primary', '2': 'warning', '3': 'success', '4': 'danger' }
      return map[status] || 'info'
    },
    getAttachStatusLabel(status) {
      const map = { '0': '未提交', '1': '已提交', '2': '审核中', '3': '已通过', '4': '已驳回' }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.status-cards { margin-bottom: 16px; }
.status-card {
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  color: #fff;
}
.status-card.draft { background: linear-gradient(135deg, #909399, #606266); }
.status-card.submitted { background: linear-gradient(135deg, #409EFF, #337ecc); }
.status-card.reviewing { background: linear-gradient(135deg, #e6a23c, #b8860b); }
.status-card.passed { background: linear-gradient(135deg, #67c23a, #4a8a2b); }
.status-card.rejected { background: linear-gradient(135deg, #f56c6c, #b83030); }
.status-card.supply { background: linear-gradient(135deg, #e6a23c, #b8860b); }
.status-num { font-size: 28px; font-weight: bold; }
.status-label { font-size: 13px; margin-top: 4px; opacity: 0.9; }

.mb8 { margin-bottom: 16px; }
.mr8 { margin-right: 8px; }
.fr { float: right; }
.text-danger { color: #f56c6c; }
.text-muted { color: #909399; font-size: 13px; }
.text-success { color: #67c23a; }

.doc-type-section {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 16px;
  overflow: hidden;
}
.doc-type-header {
  background: #f5f7fa;
  padding: 10px 16px;
  border-bottom: 1px solid #ebeef5;
}
.doc-type-name {
  font-weight: bold;
  font-size: 15px;
  margin-right: 8px;
}
.doc-type-count { font-size: 12px; }

.file-list { padding: 8px 16px; background: #fff; }
.file-item {
  display: flex;
  align-items: center;
  padding: 6px 0;
  border-bottom: 1px solid #f0f0f0;
}
.file-item:last-child { border-bottom: none; }
.file-name {
  flex: 1;
  margin: 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.file-size { font-size: 12px; margin-right: 8px; }

.upload-area { padding: 16px; background: #fff; }
.upload-disabled {
  padding: 16px;
  text-align: center;
  background: #fafafa;
  border: 1px dashed #dcdfe6;
  border-top: none;
  font-size: 13px;
}

.submit-area {
  text-align: center;
  padding: 24px;
  border-top: 1px solid #ebeef5;
  margin-top: 8px;
}
</style>
