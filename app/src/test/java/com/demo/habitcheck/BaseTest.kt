package com.demo.habitcheck

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.OngoingStubbing

@ExperimentalCoroutinesApi
open class BaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    open fun setUp() {
        // make annotation work
        MockitoAnnotations.initMocks(this)
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    /**
     *  Call this fun for Object class to avoid non-null error in kotlin
     */
    fun <T> any(type: Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    fun <T> uninitialized(): T = null as T

    fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

    @After
    open fun tearDown() {
        unmockkAll()
    }
}