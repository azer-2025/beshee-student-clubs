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
      <el-form-item label="社团状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择社团状态" clearable style="width: 180px">
          <el-option v-for="dict in club_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['club:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['club:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['club:info:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="clubList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="clubId" width="80" />
      <el-table-column label="社团名称" align="center" prop="clubName" :show-overflow-tooltip="true" />
      <el-table-column label="分类ID" align="center" prop="categoryId" width="90" />
      <el-table-column label="负责人ID" align="center" prop="leaderUserId" width="100" />
      <el-table-column label="审核状态" align="center" prop="applyStatus">
        <template #default="scope">
          <dict-tag :options="club_apply_status" :value="scope.row.applyStatus" />
        </template>
      </el-table-column>
      <el-table-column label="指导老师审核" align="center" prop="teacherAuditStatus">
        <template #default="scope">
          <dict-tag :options="club_apply_status" :value="scope.row.teacherAuditStatus" />
        </template>
      </el-table-column>
      <el-table-column label="社团状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="club_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="applyTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" min-width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['club:info:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['club:info:remove']">删除</el-button>
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

    <el-dialog :title="title" v-model="open" width="760px" append-to-body>
      <el-form ref="clubRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="社团名称" prop="clubName">
              <el-input v-model="form.clubName" placeholder="请输入社团名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
                <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人ID" prop="leaderUserId">
              <el-input v-model="form.leaderUserId" placeholder="请输入负责人用户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="社团状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in club_status" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="指导老师ID" prop="teacherUserId">
              <el-input v-model="form.teacherUserId" placeholder="请输入指导老师用户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="老师审核" prop="teacherAuditStatus">
              <el-select v-model="form.teacherAuditStatus" placeholder="请选择审核状态" clearable>
                <el-option v-for="dict in teacher_audit_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="社团Logo" prop="clubLogo">
          <image-upload v-model="form.clubLogo" :limit="1" />
        </el-form-item>
        <el-form-item label="社团简介" prop="clubIntro">
          <el-input v-model="form.clubIntro" type="textarea" :rows="4" placeholder="请输入社团简介" />
        </el-form-item>
        <el-form-item label="社团章程" prop="charterContent">
          <editor v-model="form.charterContent" :min-height="200" />
        </el-form-item>
        <el-form-item label="纳新海报" prop="recruitPoster">
          <image-upload v-model="form.recruitPoster" :limit="1" />
        </el-form-item>
        <el-form-item label="荣誉墙" prop="honorWall">
          <editor v-model="form.honorWall" :min-height="200" />
        </el-form-item>
        <el-form-item label="老师审核意见" prop="teacherAuditRemark">
          <el-input v-model="form.teacherAuditRemark" type="textarea" :rows="3" placeholder="请输入老师审核意见" />
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

<script setup name="ClubInfo">
import { listClubInfo, getClubInfo, addClubInfo, updateClubInfo, delClubInfo } from "@/api/club/info"
import { listClubCategory } from "@/api/club/category"

const { proxy } = getCurrentInstance()
const { club_apply_status, club_status, teacher_audit_status } = proxy.useDict("club_apply_status", "club_status", "teacher_audit_status")

const clubList = ref([])
const categoryOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    clubName: undefined,
    categoryId: undefined,
    applyStatus: undefined,
    teacherAuditStatus: undefined,
    status: undefined
  },
  rules: {
    clubName: [{ required: true, message: "社团名称不能为空", trigger: "blur" }],
    categoryId: [{ required: true, message: "请选择社团分类", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)
const categoryMap = computed(() => {
  const map = {}
  categoryOptions.value.forEach(item => {
    map[item.categoryId] = item.categoryName
  })
  return map
})

function getList() {
  loading.value = true
  listClubInfo(queryParams.value).then(response => {
    clubList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function getCategoryOptions() {
  listClubCategory({ pageNum: 1, pageSize: 1000 }).then(response => {
    categoryOptions.value = response.rows || []
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    clubId: undefined,
    categoryId: undefined,
    clubName: undefined,
    clubLogo: undefined,
    clubIntro: undefined,
    charterContent: undefined,
    recruitPoster: undefined,
    honorWall: undefined,
    leaderUserId: undefined,
    teacherUserId: undefined,
    teacherAuditStatus: undefined,
    teacherAuditRemark: undefined,
    status: "0"
  }
  proxy.resetForm("clubRef")
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
  ids.value = selection.map(item => item.clubId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "新增社团"
}

function handleUpdate(row) {
  reset()
  const clubId = row.clubId || ids.value
  getClubInfo(clubId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改社团"
  })
}

function submitForm() {
  proxy.$refs["clubRef"].validate(valid => {
    if (valid) {
      if (form.value.clubId != undefined) {
        updateClubInfo(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addClubInfo(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const clubIds = row.clubId || ids.value
  proxy.$modal.confirm('是否确认删除社团编号为"' + clubIds + '"的数据项？').then(function() {
    return delClubInfo(clubIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
getCategoryOptions()
</script>
