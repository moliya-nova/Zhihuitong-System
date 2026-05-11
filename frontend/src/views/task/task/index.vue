<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学生ID" prop="studentId">
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入学生ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择任务类型" clearable>
          <el-option
            v-for="dict in task_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入任务标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['task:task:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['task:task:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['task:task:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['task:task:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="学生ID" align="center" prop="studentId" />
      <el-table-column label="任务类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :options="task_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="任务标题" align="center" prop="title" />
      <el-table-column label="任务内容/备注" align="center" prop="content" />
      <el-table-column label="学期标识" align="center" prop="term" />
      <el-table-column label="关联课程ID" align="center" prop="courseId" />
      <el-table-column label="课程名称快照" align="center" prop="courseNameSnapshot" />
      <el-table-column label="优先级" align="center" prop="priority">
        <template #default="scope">
          <dict-tag :options="priority" :value="scope.row.priority"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="state" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="截止时间/考试时间" align="center" prop="dueTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.dueTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否开启提醒" align="center" prop="remindEnabled" />
      <el-table-column label="提醒时间" align="center" prop="remindTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.remindTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      //这里修改了
      <el-table-column label="负责人邮箱" align="center" prop="assigneeEmail" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['task:task:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['task:task:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改任务管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="taskRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="学生ID" prop="studentId">
              <el-input v-model="form.studentId" placeholder="请输入学生ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择任务类型">
                <el-option
                  v-for="dict in task_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入任务标题" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="任务内容/备注">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="学期标识" prop="term">
              <el-input v-model="form.term" placeholder="请输入学期标识" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="关联课程ID" prop="courseId">
              <el-input v-model="form.courseId" placeholder="请输入关联课程ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="课程名称快照" prop="courseNameSnapshot">
              <el-input v-model="form.courseNameSnapshot" placeholder="请输入课程名称快照" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级">
                <el-option
                  v-for="dict in priority"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="dict in state"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="截止时间/考试时间" prop="dueTime">
              <el-date-picker clearable
                v-model="form.dueTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择截止时间/考试时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="是否开启提醒" prop="remindEnabled">
              <el-input v-model="form.remindEnabled" placeholder="请输入是否开启提醒" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="提醒时间" prop="remindTime">
              <el-date-picker clearable
                v-model="form.remindTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择提醒时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
      <el-col :span="24">
  <el-form-item label="负责人邮箱" prop="assigneeEmail">
    <el-input 
      v-model="form.assigneeEmail" 
      placeholder="请输入负责人邮箱，用于接收任务提醒"
      clearable
    />
    <div style="color: #909399; font-size: 12px; margin-top: 4px;">
      任务到期时会发送提醒邮件到此邮箱
    </div>
  </el-form-item>
</el-col>
<el-col :span="24">
  <el-form-item label="创建时间" prop="createdAt">
    <el-date-picker clearable
      v-model="form.createdAt"
      type="date"
      value-format="YYYY-MM-DD"
      placeholder="请选择创建时间">
    </el-date-picker>
  </el-form-item>
</el-col>
<el-col :span="24">
  <el-form-item label="更新时间" prop="updatedAt">
    <el-date-picker clearable
      v-model="form.updatedAt"
      type="date"
      value-format="YYYY-MM-DD"
      placeholder="请选择更新时间">
    </el-date-picker>
  </el-form-item>
</el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Task">
import type { StuTask, TaskQueryParams } from "@/types/api/task/task"
import { listTask, getTask, delTask, addTask, updateTask } from "@/api/task/task"

const { proxy } = getCurrentInstance()
const { priority, task_type, state } = useDict('priority', 'task_type', 'state')

const taskList = ref<StuTask[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const data = reactive({
  form: {} as StuTask,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    studentId: undefined,
    type: undefined,
    title: undefined,
    content: undefined,
  } as TaskQueryParams,
  rules: {
    studentId: [
      { required: true, message: "学生ID不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "任务类型不能为空", trigger: "change" }
    ],
    title: [
      { required: true, message: "任务标题不能为空", trigger: "blur" }
    ],
    dueTime: [
      { required: true, message: "截止时间/考试时间不能为空", trigger: "blur" }
    ],
    createdAt: [
      { required: true, message: "创建时间不能为空", trigger: "blur" }
    ],
    updatedAt: [
      { required: true, message: "更新时间不能为空", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询任务管理列表 */
function getList() {
  loading.value = true
  listTask(queryParams.value).then(response => {
    taskList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    studentId: null,
    type: null,
    title: null,
    content: null,
    term: null,
    courseId: null,
    courseNameSnapshot: null,
    priority: null,
    status: null,
    dueTime: null,
    remindEnabled: null,
    remindTime: null,
    createdAt: null,
    updatedAt: null
  }
  proxy.resetForm("taskRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection: StuTask[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加任务管理"
}

/** 修改按钮操作 */
function handleUpdate(row: StuTask) {
  reset()
  const _id = row.id || ids.value[0]
  getTask(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改任务管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["taskRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updateTask(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addTask(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: StuTask) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除任务管理编号为"' + _ids + '"的数据项？').then(function() {
    return delTask(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('task/task/export', {
    ...queryParams.value
  }, `task_${new Date().getTime()}.xlsx`)
}

getList()
</script>
