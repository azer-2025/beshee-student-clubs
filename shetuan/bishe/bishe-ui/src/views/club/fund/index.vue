<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="社团ID" prop="clubId">
        <el-input v-model="queryParams.clubId" placeholder="请输入社团ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="活动ID" prop="activityId">
        <el-input v-model="queryParams.activityId" placeholder="请输入活动ID" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 180px">
          <el-option v-for="dict in funding_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['club:fund:apply']">新增申请</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="CircleCheck" :disabled="single" @click="handleApprove" v-hasPermi="['club:fund:approve']">审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['club:fund:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['club:fund:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fundList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请ID" align="center" prop="fundId" width="80" />
      <el-table-column label="社团ID" align="center" prop="clubId" width="90" />
      <el-table-column label="活动ID" align="center" prop="activityId" width="90" />
      <el-table-column label="申请金额" align="center" prop="amount" width="100" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="funding_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="申请人ID" align="center" prop="applyUserId" />
      <el-table-column label="申请时间" align="center" prop="applyTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template #default="scope">
          <el-button link type="primary" icon="CircleCheck" @click="handleApprove(scope.row)" v-hasPermi="['club:fund:approve']">审核</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['club:fund:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['club:fund:remove']">删除</el-button>
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
        <el-form-item label="申请ID">
          <el-input v-model="approveForm.fundId" disabled />
        </el-form-item>
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="approveForm.status">
            <el-radio v-for="dict in funding_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
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
      <el-form ref="fundRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="社团ID" prop="clubId">
          <el-input v-model="form.clubId" placeholder="请输入社团ID" />
        </el-form-item>
        <el-form-item label="活动ID" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入活动ID" />
        </el-form-item>
        <el-form-item label="申请金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="申请理由" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入申请理由" />
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

<script setup name="ClubFund">
import { listClubFund, getClubFund, addClubFund, updateClubFund, delClubFund, approveClubFund } from "@/api/club/fund"

const { proxy } = getCurrentInstance()
const { funding_status } = proxy.useDict("funding_status")

const fundList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const approveOpen = ref(false)
const open = ref(false)
const approveTitle = ref("经费申请审核")
const title = ref("")

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    clubId: undefined,
    activityId: undefined,
    status: undefined
  },
  form: {},
  rules: {
    amount: [{ required: true, message: "请输入申请金额", trigger: "blur" }],
    reason: [{ required: true, message: "请输入申请理由", trigger: "blur" }]
  },
  approveForm: {
    fundId: undefined,
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
  listClubFund(queryParams.value).then(response => {
    fundList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function reset() {
  form.value = {
    fundId: undefined,
    clubId: undefined,
    activityId: undefined,
    amount: 0,
    reason: undefined
  }
  proxy.resetForm("fundRef")
}

function cancel() {
  open.value = false
  reset()
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "新增经费申请"
}

function handleUpdate(row) {
  reset()
  const fundId = row?.fundId || ids.value[0]
  if (!fundId) return
  getClubFund(fundId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改经费申请"
  })
}

function submitForm() {
  proxy.$refs["fundRef"].validate(valid => {
    if (valid) {
      if (form.value.fundId != undefined) {
        updateClubFund(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addClubFund(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
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

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.fundId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleDelete(row) {
  const fundIds = row?.fundId || ids.value
  if (!fundIds || (Array.isArray(fundIds) && fundIds.length === 0)) return
  proxy.$modal.confirm('是否确认删除经费申请编号为"' + fundIds + '"的数据项？').then(function() {
    return delClubFund(fundIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleApprove(row) {
  const target = row || fundList.value.find(item => item.fundId === ids.value[0])
  if (!target) return
  approveForm.value = {
    fundId: target.fundId,
    status: target.status,
    auditRemark: target.auditRemark
  }
  approveOpen.value = true
}

function submitApprove() {
  proxy.$refs["approveRef"].validate(valid => {
    if (valid) {
      approveClubFund(approveForm.value).then(() => {
        proxy.$modal.msgSuccess("审核成功")
        approveOpen.value = false
        getList()
      })
    }
  })
}

getList()
</script>
