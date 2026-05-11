<template>
  <div class="app-container">
    <el-card shadow="never">
      <template #header>
        <div class="header">
          <div class="title">代办提醒</div>
          <div class="legend">
            <span class="legend-item">
              <span class="legend-dot"></span>
              <span>当日有未到期代办</span>
            </span>
          </div>
        </div>
      </template>

      <el-skeleton :loading="loading" animated>
        <template #default>
          <el-calendar v-model="calendarDate">
            <template #date-cell="{ data }">
              <div class="cell" :class="{ 'has-task': hasTasks(data.day) }">
                <div class="cell-date">{{ formatDay(data.day) }}</div>
                <div v-if="hasTasks(data.day)" class="cell-tasks">
                  <div v-for="t in showTasks(data.day)" :key="t.id" class="task-line" :title="taskTitle(t)">
                    <span class="task-title">{{ t.title }}</span>
                    <span class="task-mail" :class="{ off: Number(t.remindEnabled) !== 1 }">
                      邮件:{{ Number(t.remindEnabled) === 1 ? "是" : "否" }}
                    </span>
                  </div>
                  <div v-if="moreCount(data.day) > 0" class="task-more">+{{ moreCount(data.day) }}</div>
                </div>
              </div>
            </template>
          </el-calendar>
        </template>
      </el-skeleton>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="TaskRemind">
import type { StuTask } from "@/types"
import { listTodoCalendarRemind } from "@/api/task/task"

const calendarDate = ref<Date>(new Date())
const loading = ref(false)
const tasks = ref<StuTask[]>([])
const taskMap = ref<Record<string, StuTask[]>>({})

function pad2(n: number) {
  return String(n).padStart(2, "0")
}

function formatMonth(d: Date) {
  return `${d.getFullYear()}-${pad2(d.getMonth() + 1)}`
}

function formatDay(day: string) {
  const parts = day.split("-")
  return `${parts[1]}-${parts[2]}`
}

function buildMap(list: StuTask[]) {
  const map: Record<string, StuTask[]> = {}
  list.forEach(item => {
    const due = (item as any).dueTime as string | undefined
    if (!due) return
    const day = due.slice(0, 10)
    if (!map[day]) map[day] = []
    map[day].push(item)
  })
  Object.keys(map).forEach(k => {
    map[k].sort((a: any, b: any) => String(a.dueTime || "").localeCompare(String(b.dueTime || "")))
  })
  return map
}

function fetchTasks() {
  loading.value = true
  listTodoCalendarRemind({ month: formatMonth(calendarDate.value) })
    .then(res => {
      tasks.value = res.rows || []
      taskMap.value = buildMap(tasks.value)
    })
    .finally(() => {
      loading.value = false
    })
}

function hasTasks(day: string) {
  return (taskMap.value[day] || []).length > 0
}

function showTasks(day: string) {
  return (taskMap.value[day] || []).slice(0, 2)
}

function moreCount(day: string) {
  const n = (taskMap.value[day] || []).length
  return n > 2 ? n - 2 : 0
}

function taskTitle(t: any) {
  const due = t.dueTime ? `（${t.dueTime}）` : ""
  const mail = Number(t.remindEnabled) === 1 ? " 邮件提醒：是" : " 邮件提醒：否"
  return `${t.title || ""}${due}${mail}`
}

watch(calendarDate, fetchTasks, { immediate: true })
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 16px;
  font-weight: 600;
}

.legend {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.legend-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  background: #2ec4b6;
}

.cell {
  height: 100%;
  padding: 6px;
  border-radius: 6px;
}

.cell.has-task {
  background: rgba(46, 196, 182, 0.18);
}

.cell-date {
  font-size: 12px;
  color: var(--el-text-color-primary);
  line-height: 18px;
  margin-bottom: 4px;
}

.cell-tasks {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.task-line {
  display: flex;
  align-items: center;
  gap: 6px;
  line-height: 16px;
  font-size: 12px;
  color: var(--el-text-color-regular);
}

.task-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.task-mail {
  flex: none;
  padding: 0 6px;
  border-radius: 10px;
  background: rgba(46, 196, 182, 0.25);
  color: #169c8f;
  font-size: 11px;
}

.task-mail.off {
  background: rgba(144, 147, 153, 0.18);
  color: var(--el-text-color-secondary);
}

.task-more {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  line-height: 16px;
}
</style>
