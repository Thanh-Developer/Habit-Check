package com.demo.habitcheck.data.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.demo.habitcheck.data.local.TaskDatabase
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.utils.RemindType
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class TaskRepositoryImpTest {

    private lateinit var taskDatabase: TaskDatabase

    private lateinit var taskRepository: TaskRepository

    private val tasksTest = arrayOf(
        Task(
            0,
            "Title test 1",
            "Descriptio test",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        ),
        Task(
            1,
            "Title test 2",
            "Descriptio test",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        ),
    )

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        taskDatabase = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()

        taskRepository = TaskRepositoryImp(taskDatabase.taskDao())
    }

    @Test
    fun checkInsertTaskSuccess() = runBlocking {
        val newTask = Task(
            0,
            "Title test",
            "Descriptio test",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        )

        taskRepository.insertTask(newTask)

        // WHEN  - Task retrieved by ID
        val result = newTask.id?.let { taskRepository.getTaskById(it) }

        // THEN - Same task is returned
        assertThat(result?.title, `is`("Title test"))
        assertThat(result?.description, `is`("Descriptio test"))
        assertThat(result?.frequency, `is`(RemindType.ONE_TIME))
    }

    @Test
    fun checkDeleteTaskSuccess() = runBlocking {
        for (task in tasksTest) {
            taskRepository.insertTask(task)
        }

        val deleteTask = Task(
            0,
            "Title test 1",
            "Descriptio test",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        )

        // WHEN  - Delete task
        taskRepository.deleteTask(deleteTask)

        // THEN - Same task is returned
        var notes: List<Task>? = taskRepository.getAllTask()
        TestCase.assertEquals(1, notes?.size)
    }

    @After
    fun tearDown() {
        taskDatabase.close()
    }
}
