package com.brianperin.squaredirectory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.brianperin.squaredirectory.model.response.Employee
import com.brianperin.squaredirectory.model.response.EmployeeType
import com.brianperin.squaredirectory.model.response.Employees
import com.brianperin.squaredirectory.network.ApiClient
import com.brianperin.squaredirectory.network.Result
import com.brianperin.squaredirectory.util.validate
import com.brianperin.squaredirectory.viewmodel.EmployeesViewModel
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.valiktor.ConstraintViolationException
import java.net.HttpURLConnection
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EmployeeTest() {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        mockWebServer = MockWebServer()
        mockWebServer.start()

    }

    @Test
    fun `read ok json`() {
        val reader = MockResponseFileReader("ok.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `read malformed json`() {
        val reader = MockResponseFileReader("malformed.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `read empty json`() {
        val reader = MockResponseFileReader("empty.json")
        assertNotNull(reader.content)
    }

    /**
     * Making sure our backend is returning 200
     */
    @Test
    fun `fetch details and check response Code 200 returned`() = runBlocking {

        val actualResult = ApiClient.apiService.getEmployees()

        assertEquals(200, actualResult.code())
    }

    @Test(expected = ConstraintViolationException::class)
    fun `test our invalid json for employees`() {

        val malformedJsonBody = MockResponseFileReader("malformed.json").content

        val employees: Employees = Gson().fromJson(malformedJsonBody, Employees::class.java)

        employees.validate()
    }

    @Test
    fun `test our valid json for employees`() {

        val malformedJsonBody = MockResponseFileReader("ok.json").content

        val employees: Employees = Gson().fromJson(malformedJsonBody, Employees::class.java)

        employees.validate()
    }

    @Test
    fun `test our empty json for employees`() {

        val malformedJsonBody = MockResponseFileReader("empty.json").content

        val employees: Employees = Gson().fromJson(malformedJsonBody, Employees::class.java)

        assert(employees.employees.isEmpty())
    }

    @Test(expected = ConstraintViolationException::class)
    fun `test email valid null`() {
        val employee = Employee(UUID.randomUUID(), "brian", "408xyz", "@@@", null, null, null, "Android", EmployeeType.PART_TIME)
        employee.validate()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}