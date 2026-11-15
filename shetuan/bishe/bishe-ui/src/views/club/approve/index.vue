<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="社团名称" prop="clubName">
        <el-input v-model="queryParams.clubName" placeholder="请输入社团名称" clearable style="width: 220px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 200px">
          <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="applyStatus">
        <el-select v-model="queryParams.applyStatus" placeholder="请选择审核状态" clearable style="width: 180px">
          <el-option v-for="dict in club_apply_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="老师审核" prop="teacherAuditStatus">
        <el-select v-model="queryParams.teacherAuditStatus" placeholder="请选择老师审核状态" clearable style="width: 180px">
          <el-option v-for="dict in teacher_audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="applyList">
      <el-table-column label="ID" align="center" prop="clubId" width="80" />
      <el-table-column label="社团名称" align="center" prop="clubName" :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="categoryId" width="140">
        <template #default="scope">
          <span>{{ categoryMap[scope.row.categoryId] || scope.row.categoryId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请人ID" align="center" prop="applyUserId" width="100" />
      <el-table-column label="负责人ID" align="center" prop="leaderUserId" width="100" />
      <el-table-column label="审核状态" align="center" prop="applyStatus" width="100">
        <template #default="scope">
          <dict-tag :options="club_apply_status" :value="scope.row.applyStatus" />
        </template>
      </el-table-column>
      <el-table-column label="老师审核" align="center" prop="teacherAuditStatus" width="110">
        <template #default="scope">
          <dict-tag :options="teacher_audit_status" :value="scope.row.teacherAuditStatus" />
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template #default="scope">
          <el-button link type="primary" icon="CircleCheck" @click="handleApprove(scope.row)" v-hasPermi="['club:apply:approve']">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="approveTitle" v-model="approveOpen" width="560px" append-to-body>
      <el-form ref="approveRef" :model="approveForm" :rules="approveRules" label-width="100px">
        <el-form-item label="社团ID">
          <el-input v-model="approveForm.clubId" disabled />
        </el-form-item>
        <el-form-item label="社团名称">
          <el-input v-model="approveForm.clubName" disabled />
        </el-form-item>
        <el-form-item label="负责人ID" prop="leaderUserId">
          <el-input v-model="approveForm.leaderUserId" placeholder="请输入负责人用户ID" />
        </el-form-item>
        <el-form-item label="指导老师ID" prop="teacherUserId">
          <el-input v-model="approveForm.teacherUserId" placeholder="请输入指导老师用户ID" />
        </el-form-item>
        <el-form-item label="老师审核" prop="teacherAuditStatus">
          <el-select v-model="approveForm.teacherAuditStatus" placeholder="请选择审核状态" clearable>
            <el-option v-for="dict in teacher_audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核结果" prop="applyStatus">
          <el-radio-group v-model="approveForm.applyStatus">
            <el-radio v-for="dict in club_apply_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditRemark">
          <el-input v-model="approveForm.auditRemark" type="textarea" placeholder="请输入审核意见" />
        </el-form-item>
        <el-form-item label="老师意见" prop="teacherAuditRemark">
          <el-input v-model="approveForm.teacherAuditRemark" type="textarea" placeholder="请输入老师审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApprove">确定</el-button>
          <el-button @click="approveOpen = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ClubApprove">
import { listClubApply, approveClub } from "@/api/club/info"
import { listClubCategory } from "@/api/club/category"

const { proxy } = getCurrentInstance()
const { club_apply_status, teacher_audit_status } = proxy.useDict("club_apply_status", "teacher_audit_status")

const applyList = ref([])
const categoryOptions = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const approveOpen = ref(false)
const approveTitle = ref("社团审核")

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    clubName: undefined,
    categoryId: undefined,
    applyStatus: undefined,
    teacherAuditStatus: undefined
  },
  approveForm: {
    clubId: undefined,
    clubName: undefined,
    leaderUserId: undefined,
    teacherUserId: undefined,
    teacherAuditStatus: undefined,
    teacherAuditRemark: undefined,
    applyStatus: undefined,
    auditRemark: undefined
  },
  approveRules: {
    applyStatus: [{ required: true, message: "请选择审核结果", trigger: "change" }]
  }
})

const { queryParams, approveForm, approveRules } = toRefs(data)
const categoryMap = computed(() => {
  const map = {}
  categoryOptions.value.forEach(item => {
    map[item.categoryId] = item.categoryName
  })
  return map
})

function getList() {
  loading.value = true
  listClubApply(queryParams.value).then(response => {
    applyList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function getCategoryOptions() {
  listClubCategory({ pageNum: 1, pageSize: 1000 }).then(response => {
    categoryOptions.value = response.rows || []
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleApprove(row) {
  approveForm.value = {
    clubId: row.clubId,
    clubName: row.clubName,
    leaderUserId: row.leaderUserId,
    teacherUserId: row.teacherUserId,
    teacherAuditStatus: row.teacherAuditStatus,
    teacherAuditRemark: row.teacherAuditRemark,
    applyStatus: row.applyStatus,
    auditRemark: row.auditRemark
  }
  approveOpen.value = true
}

function submitApprove() {
  proxy.$refs["approveRef"].validate(valid => {
    if (valid) {
      approveClub(approveForm.value).then(() => {
        proxy.$modal.msgSuccess("审核成功")
        approveOpen.value = false
        getList()
      })
    }
  })
}

getList()
getCategoryOptions()
</script>
