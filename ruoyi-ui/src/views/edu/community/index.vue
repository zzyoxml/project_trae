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
          <el-option label="英语" value="英语" />
          <el-option label="日语" value="日语" />
          <el-option label="汉语" value="汉语" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="正常" value="0" />
          <el-option label="待审核" value="1" />
          <el-option label="已删除" value="2" />
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
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="postId" width="80" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="作者" align="center" prop="authorName" width="120" />
      <el-table-column label="语言" align="center" prop="language" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLanguageType(scope.row.language)" size="small">
            {{ scope.row.language }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTypeType(scope.row.type)" size="small">
            {{ scope.row.type }}
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
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
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
            v-if="scope.row.status === '1'"
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
        <el-descriptions-item label="作者">{{ detailData.authorName }}</el-descriptions-item>
        <el-descriptions-item label="语言">
          <el-tag :type="getLanguageType(detailData.language)" size="small">
            {{ detailData.language }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getTypeType(detailData.type)" size="small">
            {{ detailData.type }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ detailData.likeCount }}</el-descriptions-item>
        <el-descriptions-item label="评论数">{{ detailData.commentCount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider>帖子内容</el-divider>
      <div class="post-content" v-html="detailData.content"></div>

      <el-divider>评论列表</el-divider>
      <el-table :data="detailData.comments || []" max-height="300">
        <el-table-column label="评论者" align="center" prop="commenterName" width="120" />
        <el-table-column label="评论内容" align="center" prop="content" :show-overflow-tooltip="true" />
        <el-table-column label="点赞数" align="center" prop="likeCount" width="80" />
        <el-table-column label="评论时间" align="center" prop="commentTime" width="180" />
        <el-table-column label="操作" align="center" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteComment(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
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
      setTimeout(() => {
        this.postList = [
          {
            postId: 1,
            title: '英语学习方法分享',
            authorName: '小明',
            language: '英语',
            type: '分享',
            likeCount: 45,
            commentCount: 12,
            status: '0',
            content: '<p>大家好，我来分享一下我的英语学习方法。</p><p>1. 每天早起背单词<br>2. 晚上看美剧练习听力<br>3. 周末做口语练习</p><p>希望对大家有帮助！</p>',
            createTime: '2024-01-15 10:00:00'
          },
          {
            postId: 2,
            title: '日语学习资源推荐',
            authorName: '太郎',
            language: '日语',
            type: '资源',
            likeCount: 38,
            commentCount: 8,
            status: '1',
            content: '<p>推荐一些我常用的日语学习资源：</p><ul><li>沪江日语</li><li>日语N3备考书</li><li>YouTube日语学习频道</li></ul>',
            createTime: '2024-01-14 15:30:00'
          },
          {
            postId: 3,
            title: '汉语拼音技巧',
            authorName: '小红',
            language: '汉语',
            type: '技巧',
            likeCount: 32,
            commentCount: 15,
            status: '0',
            content: '<p>学习汉语拼音的一些小技巧：</p><p>1. 注意声调<br>2. 多听多读<br>3. 练习四声</p>',
            createTime: '2024-01-13 09:20:00'
          },
          {
            postId: 4,
            title: '学习打卡第二天',
            authorName: 'John',
            language: '英语',
            type: '打卡',
            likeCount: 28,
            commentCount: 5,
            status: '0',
            content: '<p>第二天打卡！</p><p>今天学习了：</p><ul><li>Unit 1 词汇</li><li>基础语法</li></ul>',
            createTime: '2024-01-12 14:15:00'
          },
          {
            postId: 5,
            title: '每日一句日语',
            authorName: '田中',
            language: '日语',
            type: '分享',
            likeCount: 25,
            commentCount: 6,
            status: '1',
            content: '<p>今日の一言：</p><p>「努力は裏切らない」</p><p>（努力不会背叛）</p>',
            createTime: '2024-01-11 11:45:00'
          }
        ]
        this.total = this.postList.length
        this.loading = false
      }, 500)
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
      this.detailData = {
        ...row,
        comments: [
          { commenterName: '用户A', content: '很实用的方法！', likeCount: 5, commentTime: '2024-01-15 10:30:00' },
          { commenterName: '用户B', content: '谢谢分享', likeCount: 3, commentTime: '2024-01-15 11:00:00' }
        ]
      }
      this.detailOpen = true
    },
    handleAudit(row) {
      this.$confirm(`是否审核通过帖子"${row.title}"?`, '审核', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        type: 'info'
      }).then(() => {
        this.$message.success('审核通过')
        this.getList()
      }).catch(() => {
        this.$message.info('已拒绝')
        this.getList()
      })
    },
    handleBatchAudit() {
      this.$confirm(`是否审核通过选中的${this.ids.length}个帖子?`, '批量审核', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        type: 'info'
      }).then(() => {
        this.$message.success('批量审核成功')
        this.getList()
      })
    },
    handleDelete(row) {
      const postIds = row.postId || this.ids
      this.$confirm(`是否确认删除帖子ID为"${postIds}"的数据项?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getList()
      })
    },
    handleBatchDelete() {
      this.$confirm(`是否确认删除选中的${this.ids.length}个帖子?`, '批量删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量删除成功')
        this.getList()
      })
    },
    handleDeleteComment(row) {
      this.$confirm(`是否删除该评论?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.handleDetail(this.detailData)
      })
    },
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    getLanguageType(language) {
      const types = {
        '英语': 'primary',
        '日语': 'success',
        '汉语': 'warning'
      }
      return types[language] || 'info'
    },
    getTypeType(type) {
      const types = {
        '分享': 'primary',
        '资源': 'success',
        '技巧': 'warning',
        '打卡': 'info'
      }
      return types[type] || 'info'
    },
    getStatusType(status) {
      const types = {
        '0': 'success',
        '1': 'warning',
        '2': 'danger'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        '0': '正常',
        '1': '待审核',
        '2': '已删除'
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
