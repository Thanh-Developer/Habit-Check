package com.demo.habitcheck.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.utils.RemindType
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest : TestCase() {
    private lateinit var db: TaskDatabase
    private lateinit var noteDao: TaskDAO

    private val taskTest = Task(
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

    private val tasksTest = arrayOf(
        Task(
            0,
            "Title test",
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
            "Title test",
            "Descriptio test",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        ),
        Task(
            2,
            "Title test",
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
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java).build()
        noteDao = db.taskDao()
    }

    @Test
    fun checkInsertNotesSuccess() = runBlocking {
        for (task in tasksTest) {
            noteDao.insertTask(task)
        }

        var tasks: List<Task>? = noteDao.getAllTask()

        assertEquals(tasksTest.size, tasks?.size)
    }

    @Test
    fun checkUpdateNotesSuccess() = runBlocking {
        val newTask = Task(
            0,
            "Title new",
            "Descriptio new",
            1,
            RemindType.ONE_TIME,
            1635259500641,
            "1635259500641",
            "1635259500641",
            "1"
        )
        val newTitle = "Title new"

        noteDao.insertTask(taskTest)

        noteDao.updateTask(newTask)

        var notes: List<Task>? = noteDao.getAllTask()

        assertEquals(newTitle, notes?.get(0)?.title)
    }

    @Test
    fun checkGetAllNotesSuccess() = runBlocking {
        noteDao.insertTask(taskTest)

        var notes: List<Task>? = noteDao.getAllTask()

        assertEquals(1, notes?.size)
    }

    @Test
    fun checkDeleteNoteSuccess() {
        runBlocking {
            noteDao.insertTask(taskTest)

            noteDao.deleteTask(taskTest)

            var notes: List<Task>? = noteDao.getAllTask()

            notes?.let { assertTrue(it.isEmpty()) }
        }
    }

    @After
    fun closeDb() {
        db.close()
    }
}
