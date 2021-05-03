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
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.valiktor.ConstraintViolationException
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EmployeeTest() {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val viewModel = EmployeesViewModel()

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<Result<Employees>>

    @Mock
    private lateinit var observer: Observer<Result<Employees>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel.employeesResult.observeForever(observer)
    }

    @Test
    fun `test api is successful and body is ok`() = runBlocking {

        val response = ApiClient.apiService.getEmployees()
        val success = response.isSuccessful
        val body = response.body()
        body?.validate()

        assertEquals(success, true)
    }

    @Test(expected = ConstraintViolationException::class)
    fun `test api is successful but validation throws error`() = runBlocking {

        val response = ApiClient.apiService.getMalformedEmployees()
        val success = response.isSuccessful
        val body = response.body()
        body?.validate()

        assertEquals(success, true)
    }

    @Test
    fun `verify view model has observers`() {
        assertTrue(viewModel.employeesResult.hasObservers())
    }

    @Test
    fun `is live data emitting correct status updates`() {

        viewModel.employeesResult.value = Result<Employees>(Result.Status.LOADING, null, null)

        assertEquals(viewModel.employeesResult.value!!.status, Result.Status.LOADING)

        viewModel.employeesResult.value = Result<Employees>(Result.Status.SUCCESS, null, null)

        assertEquals(viewModel.employeesResult.value!!.status, Result.Status.SUCCESS)

        viewModel.employeesResult.value = Result<Employees>(Result.Status.EMPTY, null, null)

        assertEquals(viewModel.employeesResult.value!!.status, Result.Status.EMPTY)

        viewModel.employeesResult.value = Result<Employees>(Result.Status.ERROR, null, null)

        assertEquals(viewModel.employeesResult.value!!.status, Result.Status.ERROR)
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

        val jsonBody = MockResponseFileReader("ok.json").content

        val employees: Employees = Gson().fromJson(jsonBody, Employees::class.java)

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
        viewModel.employeesResult.removeObserver(observer)
    }
}