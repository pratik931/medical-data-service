import com.example.medicaldataservice.MedicalDataServiceApplication
import com.example.medicaldataservice.security.ApplicationUser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.util.*

@SpringBootTest(
    classes = [MedicalDataServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class MedicalDataServiceIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            val testUsers = listOf(
                ApplicationUser(username = "user1", password = "password1", roles = listOf("USER"))
            )
            val usersJson = jacksonObjectMapper().writeValueAsString(testUsers)
            System.setProperty("APP_USERS", usersJson)
        }
    }

    @Test
    fun `test endpoint to create medical data`() {
        val request = mapOf("patientId" to "patient-123", "heartbeatRate" to 80)

        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .postForEntity("/api/v1/medical-data", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun `test endpoint to get all medical data for patient`() {
        val patientId = "patient-123"

        val request = mapOf("patientId" to patientId, "heartbeatRate" to 80)
        restTemplate
            .withBasicAuth("user1", "password1")
            .postForEntity("/api/v1/medical-data", request, String::class.java)

        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .getForEntity("/api/v1/medical-data/patient/$patientId", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `test endpoint to get medical data by ID`() {
        val request = mapOf("patientId" to "patient-123", "heartbeatRate" to 80)
        val createResponse = restTemplate
            .withBasicAuth("user1", "password1")
            .postForEntity("/api/v1/medical-data", request, String::class.java)

        val id = jacksonObjectMapper().readTree(createResponse.body).get("id").asText()
        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .getForEntity("/api/v1/medical-data/$id", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `test endpoint to get medical data by ID should return 404 for non-existent ID`() {
        val id = UUID.randomUUID().toString()
        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .getForEntity("/api/v1/medical-data/$id", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `test endpoint to create medical data with bad request for blood pressure`() {
        val request = mapOf(
            "patientId" to "patient-123",
            "heartbeatRate" to 80,
            "bloodPressure" to mapOf("systolic" to 120)
        )

        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .postForEntity("/api/v1/medical-data", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `test endpoint to create medical data with bad request for heartbeat rate`() {
        val request = mapOf(
            "patientId" to "patient-123",
            "heartbeatRate" to "-89",
            )

        val response = restTemplate
            .withBasicAuth("user1", "password1")
            .postForEntity("/api/v1/medical-data", request, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}