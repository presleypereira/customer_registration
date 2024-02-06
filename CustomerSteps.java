package com.shellenergy.functiontest.storysteps;

import static com.shellenergy.functiontest.helper.TestUtils.getEnvOrDefault;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.ResourceUtils.getFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.shellenergy.functiontest.helper.JdbcHelper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomerSteps {

	private static final String SERVICE_URL =
	        getEnvOrDefault("SERVICE_URL", "localhost:8080/esg__customer_registration");
	private static final String CREATE_CUSTOMER = "http://" + SERVICE_URL + "/v1/customers";
	private static final String GET_CUSTOMER_BY_REF = "http://" + SERVICE_URL + "/v1/customers?customerRef=%s";
	private static final String CUSTOMER_REF = "CUSTOMER_REF55";
	    
	private final JdbcHelper jdbcHelper;
	private final RestTemplate restTemplate;
	
	private HttpStatus statusCode;
    private String body;
	
	public CustomerSteps() {
		jdbcHelper = new JdbcHelper();
		restTemplate = new RestTemplate();
	}
	
	@Given("a customer record")
    public void givenAcustomerRecord() {
    }
	
	@Given("a customer is persisted")
	public void givenACustomerIsPersisted() {
		jdbcHelper.createCustomer(CUSTOMER_REF, "CUSTOMER_NAME55", "ADDRESS_LINE155", "ADDRESS_LINE255", "TOWN55", 
				"COUNTY55", "COUNTRY55", "POSTCODE55");
	}

	@When("the api is called to create customer")
    public void whenTheApiIsCalledToCreateCustomer() throws FileNotFoundException, IOException {
		String customerJson = readFileContent("src/test/resources/payload/create_customer.json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(customerJson, buildHeader());
		
		try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(CREATE_CUSTOMER, POST, httpEntity, String.class);

            statusCode = responseEntity.getStatusCode();
            body = responseEntity.getBody();

		} catch (final Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            body = e.getMessage();
        }
	}
	
	@When("the api is called to customers by customer ref")
	public void whenTheApiIsCalledToCustomersByCustomerRef() {
		HttpEntity<String> httpEntity = new HttpEntity<>("", buildHeader());
		
		try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(format(GET_CUSTOMER_BY_REF, CUSTOMER_REF), GET, httpEntity, String.class);

            statusCode = responseEntity.getStatusCode();
            body = responseEntity.getBody();

		} catch (final Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
            body = e.getMessage();
        }
	}

	@Then("the customer is created successfully")
    public void thenTheCustomerIsCreatedSuccessfully() throws FileNotFoundException, IOException, JSONException {
		String customerJson = readFileContent("src/test/resources/payload/saved_customer.json");
		
		assertThat(statusCode).isEqualTo(CREATED);

		JSONAssert.assertEquals(customerJson, body,
                new CustomComparator(JSONCompareMode.LENIENT, new Customization("id", (o1, o2) -> true)));
	}
	
	@Then("the customers are retrieved successfully")
	public void the_customers_are_retrieved_successfully() throws FileNotFoundException, IOException, JSONException {
		String customerJson = readFileContent("src/test/resources/payload/get_customer.json");
		
		assertThat(statusCode).isEqualTo(OK);
		
		JSONAssert.assertEquals(customerJson, body,
                new CustomComparator(JSONCompareMode.LENIENT, new Customization("id", (o1, o2) -> true)));
	}
	
	private HttpHeaders buildHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}
	
	private String readFileContent(final String fileName) throws FileNotFoundException, IOException {
        File file = getFile(fileName);
        return new String(readAllBytes(file.toPath()));
    }
}
