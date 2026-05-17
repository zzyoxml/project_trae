<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学习语言" prop="learningLanguage">
        <el-select v-model="queryParams.learningLanguage" placeholder="请选择语言" clearable size="small">
          <el-option label="英语" value="en" />
          <el-option label="日语" value="ja" />
          <el-option label="汉语" value="zh" />
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="头像" align="center" prop="avatar" width="80">
        <template slot-scope="scope">
          <el-avatar :size="40" :src="getAvatarUrl(scope.row)">
            {{ scope.row.nickName ? scope.row.nickName.charAt(0).toUpperCase() : 'U' }}
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="用户名" align="center" prop="username" width="120" />
      <el-table-column label="密码" align="center" prop="password" width="100">
        <template slot-scope="scope">
          <span>******</span>
        </template>
      </el-table-column>
      <el-table-column label="昵称" align="center" prop="nickName" width="120" />
      <el-table-column label="邮箱" align="center" prop="email" width="160" />
      <el-table-column label="手机号" align="center" prop="phonenumber" width="120" />
      <el-table-column label="学习语言" align="center" prop="learningLanguages" width="120">
        <template slot-scope="scope">
          <el-tag v-for="lang in getLearningLanguages(scope.row.learningLanguages)" :key="lang" :type="getLanguageType(lang)" size="small" style="margin-right: 4px;">
            {{ getLanguageLabel(lang) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="学习时长(分)" align="center" prop="totalStudyTime" width="100" />
      <el-table-column label="连续学习" align="center" prop="currentStreak" width="100">
        <template slot-scope="scope">
          <el-tag type="danger" size="small" v-if="scope.row.currentStreak > 0">🔥 {{ scope.row.currentStreak }}天</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="总积分" align="center" prop="totalPoints" width="80" />
      <el-table-column label="等级" align="center" prop="level" width="80">
        <template slot-scope="scope">
          <el-tag type="warning" size="small">Lv.{{ scope.row.level || 1 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            :icon="scope.row.status === '0' ? 'el-icon-circle-close' : 'el-icon-check'"
            :type="scope.row.status === '0' ? 'warning' : 'success'"
            @click="handleChangeStatus(scope.row)"
          >{{ scope.row.status === '0' ? '停用' : '启用' }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            style="color: #f56c6c"
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

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :http-request="handleAvatarUpload"
            :disabled="avatarUploading"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar-img" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" :disabled="!!form.userId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" :prop="form.userId ? '' : 'password'">
              <el-input v-model="form.password" :type="showPassword ? 'text' : 'password'" :placeholder="form.userId ? '留空不修改' : '请输入密码'">
                <i slot="suffix" :class="showPassword ? 'el-icon-view' : 'el-icon-view el-input__icon'" @click="showPassword = !showPassword"></i>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="母语" prop="nativeLanguage">
              <el-select v-model="form.nativeLanguage" placeholder="请选择母语">
                <el-option label="中文" value="zh" />
                <el-option label="英语" value="en" />
                <el-option label="日语" value="ja" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学习语言" prop="learningLanguages">
              <el-select v-model="form.learningLanguages" multiple placeholder="请选择学习语言">
                <el-option label="英语" value="en" />
                <el-option label="日语" value="ja" />
                <el-option label="汉语" value="zh" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="个人简介" prop="bio">
          <el-input v-model="form.bio" type="textarea" :rows="3" placeholder="请输入个人简介" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="学员详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <div v-if="currentUser" style="text-align: center; margin-bottom: 20px;">
        <el-avatar :size="100" :src="getAvatarUrl(currentUser)">
          {{ currentUser.nickName ? currentUser.nickName.charAt(0).toUpperCase() : 'U' }}
        </el-avatar>
      </div>
      <el-descriptions :column="2" border v-if="currentUser">
        <el-descriptions-item label="用户ID">{{ currentUser.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentUser.nickName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phonenumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentUser.status === '0' ? 'success' : 'danger'" size="small">
            {{ currentUser.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="母语">{{ getLanguageLabel(currentUser.nativeLanguage) }}</el-descriptions-item>
        <el-descriptions-item label="学习语言">{{ getLearningLanguagesText(currentUser.learningLanguages) }}</el-descriptions-item>
        <el-descriptions-item label="当前等级">
          <el-tag type="warning" size="small">Lv.{{ currentUser.level || 1 }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总积分">{{ currentUser.totalPoints || 0 }}</el-descriptions-item>
        <el-descriptions-item label="经验值">{{ currentUser.experiencePoints || 0 }}</el-descriptions-item>
        <el-descriptions-item label="学习时长">{{ currentUser.totalStudyTime || 0 }} 分钟</el-descriptions-item>
        <el-descriptions-item label="连续学习">{{ currentUser.currentStreak || 0 }} 天</el-descriptions-item>
        <el-descriptions-item label="最长连续">{{ currentUser.longestStreak || 0 }} 天</el-descriptions-item>
        <el-descriptions-item label="注册时间" :span="2">{{ parseTime(currentUser.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="个人简介" :span="2">{{ currentUser.bio || '暂无简介' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, delUser, changeUserStatus } from '@/api/edu/user'
import { uploadAvatar } from '@/api/system/user'

export default {
  name: 'EduUser',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      userList: [],
      title: '',
      open: false,
      detailOpen: false,
      showPassword: false,
      avatarUploading: false,
      currentUser: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        email: null,
        learningLanguage: null
      },
      form: {},
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        nativeLanguage: [
          { required: true, message: '请选择母语', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows || []
        this.total = response.total || 0
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
      this.ids = selection.map(item => item.userId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增学员'
    },
    handleUpdate(row) {
      this.reset()
      const userId = row.userId || this.ids[0]
      getUser(userId).then(response => {
        const data = response.data || response
        this.form = {
          userId: data.userId,
          username: data.username,
          password: '',
          nickName: data.nickName,
          email: data.email,
          phonenumber: data.phonenumber,
          nativeLanguage: data.nativeLanguage,
          learningLanguages: data.learningLanguages ? data.learningLanguages.split(',') : [],
          status: data.status,
          bio: data.bio
        }
        this.open = true
        this.title = '修改学员'
      })
    },
    handleDetail(row) {
      this.currentUser = row
      this.detailOpen = true
    },
    handleChangeStatus(row) {
      const newStatus = row.status === '0' ? '1' : '0'
      const message = newStatus === '1' ? '是否确认停用该学员？' : '是否确认启用该学员？'
      this.$modal.confirm(message).then(() => {
        return changeUserStatus(row.userId, newStatus)
      }).then(() => {
        this.$modal.msgSuccess('操作成功')
        this.getList()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const submitData = {
            ...this.form,
            learningLanguages: Array.isArray(this.form.learningLanguages) 
              ? this.form.learningLanguages.join(',') 
              : this.form.learningLanguages
          }
          if (this.form.userId != null) {
            updateUser(submitData).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addUser(submitData).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        userId: null,
        username: null,
        password: null,
        nickName: null,
        email: null,
        phonenumber: null,
        nativeLanguage: 'zh',
        learningLanguages: [],
        status: '0',
        bio: null
      }
      this.showPassword = false
      this.resetForm('form')
    },
    handleDelete(row) {
      const userIds = row.userId || this.ids
      this.$modal.confirm('是否确认删除学员编号为"' + userIds + '"的数据项？').then(() => {
        return delUser(userIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    getLearningLanguages(languages) {
      if (!languages) return []
      return languages.split(',')
    },
    getLearningLanguagesText(languages) {
      if (!languages) return '-'
      return languages.split(',').map(l => this.getLanguageLabel(l)).join('、')
    },
    getLanguageType(language) {
      const types = { 'en': 'primary', 'ja': 'success', 'zh': 'warning' }
      return types[language] || 'info'
    },
    getLanguageLabel(language) {
      const labels = { 'en': '英语', 'ja': '日语', 'zh': '汉语' }
      return labels[language] || language || '-'
    },
    getAvatarUrl(row) {
      const avatar = row.avatar || row.avatarUrl
      if (!avatar) return ''
      // 如果已经是完整URL则直接返回
      if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
        return avatar
      }
      // 拼接基础URL
      return process.env.VUE_APP_BASE_API + avatar
    },
    handleAvatarUpload(options) {
      const file = options.file
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$modal.msgError('只能上传图片文件')
        return false
      }
      if (!isLt2M) {
        this.$modal.msgError('图片大小不能超过 2MB')
        return false
      }

      this.avatarUploading = true
      const formData = new FormData()
      formData.append('avatarfile', file)

      uploadAvatar(formData).then(response => {
        this.form.avatar = response.imgUrl
        this.avatarUploading = false
        this.$modal.msgSuccess('头像上传成功')
      }).catch(() => {
        this.avatarUploading = false
        this.$modal.msgError('头像上传失败')
      })

      return false
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar-img {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
}
</style>
