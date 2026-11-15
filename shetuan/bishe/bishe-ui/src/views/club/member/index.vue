<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="社团ID" prop="clubId">
        <el-input v-model="queryParams.clubId" placeholder="请输入社团ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="成员状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 180px">
          <el-option v-for="dict in club_member_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="CircleCheck" :disabled="single" @click="handleApprove" v-hasPermi="['club:member:approve']">审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['club:member:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['club:member:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="memberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="成员ID" align="center" prop="memberId" width="80" />
      <el-table-column label="社团ID" align="center" prop="clubId" width="90" />
      <el-table-column label="用户ID" align="center" prop="userId" width="90" />
      <el-table-column label="角色" align="center" prop="memberRole">
        <template #default="scope">
          <dict-tag :options="club_member_role" :value="scope.row.memberRole" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="club_member_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="职务" align="center" prop="positionName" :show-overflow-tooltip="true" />
      <el-table-column label="入社时间" align="center" prop="joinTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.joinTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" prop="auditTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.auditTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button link type="primary" icon="CircleCheck" @click="handleApprove(scope.row)" v-hasPermi="['club:member:approve']">审核</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['club:member:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['club:member:remove']">删除</el-button>
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

    <el-dialog :title="approveTitle" v-model="approveOpen" width="520px" append-to-body>
      <el-form ref="approveRef" :model="approveForm" :rules="approveRules" label-width="90px">
        <el-form-item label="成员ID">
          <el-input v-model="approveForm.memberId" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="approveForm.status">
            <el-radio v-for="dict in club_member_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditRemark">
          <el-input v-model="approveForm.auditRemark" type="textarea" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitApprove">确定</el-button>
          <el-button @click="approveOpen = false">取消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog :title="title" v-model="open" width="520px" append-to-body>
      <el-form ref="memberRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="成员角色" prop="memberRole">
          <el-select v-model="form.memberRole" placeholder="请选择角色">
            <el-option v-for="dict in club_member_role" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="成员状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option v-for="dict in club_member_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="入社时间" prop="joinTime">
          <el-date-picker v-model="form.joinTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择入社时间" />
        </el-form-item>
        <el-form-item label="担任职务" prop="positionName">
          <el-input v-model="form.positionName" placeholder="请输入担任职务" />
        </el-form-item>
        <el-form-item label="所获荣誉" prop="honorRecord">
          <el-input v-model="form.honorRecord" type="textarea" placeholder="请输入所获荣誉" />
        </el-form-item>
        <el-form-item label="退社时间" prop="leaveTime">
          <el-date-picker v-model="form.leaveTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择退社时间" />
        </el-form-item>
        <el-form-item label="退社原因" prop="leaveReason">
          <el-input v-model="form.leaveReason" type="textarea" placeholder="请输入退社原因" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ClubMember">
import { listClubMember, approveClubMember, updateClubMember, delClubMember } from "@/api/club/member"

const { proxy } = getCurrentInstance()
const { club_member_role, club_member_status } = proxy.useDict("club_member_role", "club_member_status")

const memberList = ref([])
const open = ref(false)
const approveOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const approveTitle = ref("成员审核")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    clubId: undefined,
    userId: undefined,
    status: undefined
  },
  rules: {
    memberRole: [{ required: true, message: "请选择成员角色", trigger: "change" }],
    status: [{ required: true, message: "请选择成员状态", trigger: "change" }]
  },
  approveForm: {
    memberId: undefined,
    status: undefined,
    auditRemark: undefined
  },
  approveRules: {
    status: [{ required: true, message: "请选择审核结果", trigger: "change" }]
  }
})

const { queryParams, form, rules, approveForm, approveRules } = toRefs(data)

function getList() {
  loading.value = true
  listClubMember(queryParams.value).then(response => {
    memberList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    memberId: undefined,
    memberRole: undefined,
    status: undefined,
    joinTime: undefined,
    positionName: undefined,
    honorRecord: undefined,
    leaveTime: undefined,
    leaveReason: undefined,
    remark: undefined
  }
  proxy.resetForm("memberRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.memberId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleApprove(row) {
  const target = row || memberList.value.find(item => item.memberId === ids.value[0])
  if (!target) return
  approveForm.value = {
    memberId: target.memberId,
    status: target.status,
    auditRemark: target.auditRemark
  }
  approveOpen.value = true
}

function submitApprove() {
  proxy.$refs["approveRef"].validate(valid => {
    if (valid) {
      approveClubMember(approveForm.value).then(() => {
        proxy.$modal.msgSuccess("审核成功")
        approveOpen.value = false
        getList()
      })
    }
  })
}

function handleUpdate(row) {
  reset()
  const target = row || memberList.value.find(item => item.memberId === ids.value[0])
  if (!target) return
  form.value = { ...target }
  open.value = true
  title.value = "修改成员"
}

function submitForm() {
  proxy.$refs["memberRef"].validate(valid => {
    if (valid) {
      updateClubMember(form.value).then(() => {
        proxy.$modal.msgSuccess("修改成功")
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  const memberIds = row.memberId || ids.value
  proxy.$modal.confirm('是否确认删除成员编号为"' + memberIds + '"的数据项？').then(function() {
    return delClubMember(memberIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
</script>
