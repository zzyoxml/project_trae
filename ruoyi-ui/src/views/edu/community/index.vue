<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="帖子标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入帖子标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="language">
        <el-select v-model="queryParams.language" placeholder="请选择语言" clearable size="small">
          <el-option label="英语" value="en" />
          <el-option label="日语" value="ja" />
          <el-option label="汉语" value="zh" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="正常" value="published" />
          <el-option label="待审核" value="pending" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleBatchAudit"
        >批量审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleBatchDelete"
        >批量删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="postId" width="80" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="语言" align="center" prop="language" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLanguageType(scope.row.language)" size="small">
            {{ getLanguageName(scope.row.language) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="postType" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTypeType(scope.row.postType)" size="small">
            {{ getTypeName(scope.row.postType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="点赞数" align="center" prop="likeCount" width="80" />
      <el-table-column label="评论数" align="center" prop="commentCount" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >查看</el-button>
          <el-button
            v-if="scope.row.status === 'pending'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAudit(scope.row)"
          >审核</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="帖子详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="帖子ID">{{ detailData.postId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="语言">
          <el-tag :type="getLanguageType(detailData.language)" size="small">
            {{ getLanguageName(detailData.language) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getTypeType(detailData.postType)" size="small">
            {{ getTypeName(detailData.postType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ detailData.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ detailData.commentCount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>帖子内容</el-divider>
      <div class="post-content" v-html="detailData.content"></div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPost, getPost, delPost, auditPost, batchAuditPost } from '@/api/edu/post'

export default {
  name: 'EduCommunity',
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      postList: [],
      detailOpen: false,
      detailData: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        language: null,
        status: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listPost(this.queryParams).then(response => {
        this.postList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.multiple = !selection.length
    },
    handleDetail(row) {
      getPost(row.postId).then(response => {
        this.detailData = response.data
        this.detailOpen = true
      })
    },
    handleAudit(row) {
      this.$confirm(`是否审核通过帖子"${row.title}"?`, '审核', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        type: 'info'
      }).then(() => {
        const post = { ...row, status: 'published' }
        return auditPost(row.postId, 'published')
      }).then(() => {
        this.$message.success('审核成功')
        this.getList()
      }).catch(() => {
        this.$message.info('已取消')
      })
    },
    handleBatchAudit() {
      this.$confirm(`是否审核通过选中的${this.ids.length}个帖子?`, '批量审核', {
        confirmButtonText: '通过',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        return batchAuditPost(this.ids)
      }).then(() => {
        this.$message.success('批量审核成功')
        this.getList()
      }).catch(() => {})
    },
    handleDelete(row) {
      const postIds = row.postId || this.ids
      this.$confirm(`是否确认删除帖子ID为"${postIds}"的数据项?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delPost(postIds)
      }).then(() => {
        this.$message.success('删除成功')
        this.getList()
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$confirm(`是否确认删除选中的${this.ids.length}个帖子?`, '批量删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delPost(this.ids)
      }).then(() => {
        this.$message.success('批量删除成功')
        this.getList()
      }).catch(() => {})
    },
    getLanguageType(language) {
      const types = { 'en': 'primary', 'ja': 'success', 'zh': 'warning' }
      return types[language] || 'info'
    },
    getLanguageName(language) {
      const names = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
      return names[language] || language
    },
    getTypeType(type) {
      const types = {
        'discussion': 'primary',
        'question': 'warning',
        'tip': 'success',
        'share': 'info',
        'achievement': 'danger'
      }
      return types[type] || 'info'
    },
    getTypeName(type) {
      const names = {
        'discussion': '讨论',
        'question': '提问',
        'tip': '技巧',
        'share': '分享',
        'achievement': '成就'
      }
      return names[type] || type
    },
    getStatusType(status) {
      const types = {
        'published': 'success',
        'pending': 'warning',
        'rejected': 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        'published': '正常',
        'pending': '待审核',
        'rejected': '已拒绝'
      }
      return texts[status] || '未知'
    },
    parseTime(time) {
      return time || '-'
    }
  }
}
</script>

<style scoped lang="scss">
.mb8 {
  margin-bottom: 8px;
}

.post-content {
  padding: 20px;
  line-height: 1.8;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fafafa;
}
</style>