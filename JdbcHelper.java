package com.shellenergy.functiontest.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JdbcHelper {

	private final static String POSTGRES_DRIVER = TestUtils.getEnvOrDefault("DATASOURCE_CUSTOMER_REGISTRATION_DRIVER_CLASS_NAME","org.postgresql.Driver");
    private final static String POSTGRES_HOST_DB = TestUtils.getEnvOrDefault("DATASOURCE_CUSTOMER_REGISTRATION_URL","jdbc:postgresql://localhost:5433/reach");
    private final static String POSTGRES_USERNAME = TestUtils.getEnvOrDefault("DATASOURCE_CUSTOMER_REGISTRATION_USERNAME","regvalidationservice");
    private final static String POSTGRES_PASSWORD = TestUtils.getEnvOrDefault("DATASOURCE_CUSTOMER_REGISTRATION_PASSWORD","regvalidationservice");

    private final JdbcTemplate jdbcTemplate;

    public JdbcHelper() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRES_DRIVER);
        dataSource.setUrl(POSTGRES_HOST_DB);
        dataSource.setPassword(POSTGRES_PASSWORD);
        dataSource.setUsername(POSTGRES_USERNAME);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanUp() {
//        jdbcTemplate.execute(SQL_TRUNCATE_REGISTRATION_RESULT);
    }

    public List<CustomerDto> selectAllCustomers() {
        var sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CustomerDto.class));
    }

	public void createCustomer(String customer_ref, String customer_name, String addressLine1, String addressLine2, String town,
			String county, String country, String postcode) {
		var sql = "INSERT into customer(customer_ref,customer_name,address_line_1,address_line_2,town,county,country,postcode) "
				+ "values('" + customer_ref + "','" + customer_name + "','" + addressLine1 + "','" + addressLine2 + "','" 
				+ town + "','" + county + "','" + country + "','" + postcode + "')";
		jdbcTemplate.execute(sql);
		
		System.out.println("customer created successfully");
	}


}
