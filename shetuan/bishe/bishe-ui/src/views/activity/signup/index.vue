<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="活动ID" prop="activityId">
        <el-input v-model="queryParams.activityId" placeholder="请输入活动ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="报名状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 180px">
          <el-option v-for="dict in activity_signup_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="签到状态" prop="checkinStatus">
        <el-select v-model="queryParams.checkinStatus" placeholder="请选择签到状态" clearable style="width: 180px">
          <el-option v-for="dict in activity_checkin_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="CircleCheck" :disabled="single" @click="handleApprove" v-hasPermi="['activity:signup:approve']">审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['activity:signup:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['activity:signup:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="signupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报名ID" align="center" prop="signupId" width="90" />
      <el-table-column label="活动ID" align="center" prop="activityId" width="90" />
      <el-table-column label="活动标题" align="center" prop="activityTitle" :show-overflow-tooltip="true" />
      <el-table-column label="用户ID" align="center" prop="userId" width="90" />
      <el-table-column label="报名状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="activity_signup_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="签到状态" align="center" prop="checkinStatus">
        <template #default="scope">
          <dict-tag :options="activity_checkin_status" :value="scope.row.checkinStatus" />
        </template>
      </el-table-column>
      <el-table-column label="报名时间" align="center" prop="signupTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.signupTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签到时间" align="center" prop="checkinTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.checkinTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" align="center" prop="auditTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.auditTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button link type="primary" icon="CircleCheck" @click="handleApprove(scope.row)" v-hasPermi="['activity:signup:approve']">审核</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['activity:signup:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['activity:signup:remove']">删除</el-button>
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
        <el-form-item label="报名ID">
          <el-input v-model="approveForm.signupId" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="approveForm.status">
            <el-radio v-for="dict in activity_signup_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
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

    <el-dialog :title="title" v-model="open" width="560px" append-to-body>
      <el-form ref="signupRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="报名状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择报名状态">
            <el-option v-for="dict in activity_signup_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="签到状态" prop="checkinStatus">
          <el-select v-model="form.checkinStatus" placeholder="请选择签到状态">
            <el-option v-for="dict in activity_checkin_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="签到时间" prop="checkinTime">
          <el-date-picker v-model="form.checkinTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择签到时间" />
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

<script setup name="ActivitySignup">
import { listActivitySignup, getActivitySignup, updateActivitySignup, delActivitySignup, approveActivitySignup } from "@/api/activity/signup"

const { proxy } = getCurrentInstance()
const { activity_signup_status, activity_checkin_status } = proxy.useDict("activity_signup_status", "activity_checkin_status")

const signupList = ref([])
const open = ref(false)
const approveOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const approveTitle = ref("报名审核")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    activityId: undefined,
    userId: undefined,
    status: undefined,
    checkinStatus: undefined
  },
  rules: {
    status: [{ required: true, message: "请选择报名状态", trigger: "change" }]
  },
  approveForm: {
    signupId: undefined,
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
  listActivitySignup(queryParams.value).then(response => {
    signupList.value = response.rows
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
    signupId: undefined,
    status: undefined,
    checkinStatus: undefined,
    checkinTime: undefined,
    remark: undefined
  }
  proxy.resetForm("signupRef")
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
  ids.value = selection.map(item => item.signupId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleApprove(row) {
  const target = row || signupList.value.find(item => item.signupId === ids.value[0])
  if (!target) return
  approveForm.value = {
    signupId: target.signupId,
    status: target.status,
    auditRemark: target.auditRemark
  }
  approveOpen.value = true
}

function submitApprove() {
  proxy.$refs["approveRef"].validate(valid => {
    if (valid) {
      approveActivitySignup(approveForm.value).then(() => {
        proxy.$modal.msgSuccess("审核成功")
        approveOpen.value = false
        getList()
      })
    }
  })
}

function handleUpdate(row) {
  reset()
  const signupId = row.signupId || ids.value
  getActivitySignup(signupId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改报名信息"
  })
}

function submitForm() {
  proxy.$refs["signupRef"].validate(valid => {
    if (valid) {
      updateActivitySignup(form.value).then(() => {
        proxy.$modal.msgSuccess("修改成功")
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  const signupIds = row.signupId || ids.value
  proxy.$modal.confirm('是否确认删除报名编号为"' + signupIds + '"的数据项？').then(function() {
    return delActivitySignup(signupIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
</script>
