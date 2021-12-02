package com.demo.habitcheck.ui.home

import com.demo.habitcheck.BaseTest
import com.demo.habitcheck.data.model.Task
import com.demo.habitcheck.data.repository.TaskRepository
import com.demo.habitcheck.utils.RemindType
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest : BaseTest() {

    @MockK
    private lateinit var taskRepository: TaskRepository

    private lateinit var homeViewModel: HomeViewModel

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
    )

    override fun setUp() {
        super.setUp()
        homeViewModel = spyk(HomeViewModel(taskRepository))
    }

    @Test
    fun checkInsertTaskSuccess() {
        mainCoroutineRule.runBlockingTest {
            val result = tasksTest
            coEvery {
                taskRepository.getAllTask()
            } returns result.toList()

            val notes: List<Task> = homeViewModel.getAllTask()
            assertTrue(notes.size == tasksTest.size)
            assertTrue(notes[0].frequency == RemindType.ONE_TIME)
        }
    }

}