package com.redhat.customer;

import com.redhat.exception.ErrorResponse;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestHTTPEndpoint(CustomerResource.class)
public class CustomerResourceTest {

    @Test
    public void getAll() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void getById() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        Customer got = given()
                .when()
                .get("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(200)
                .extract().as(Customer.class);
        assertThat(saved).isEqualTo(got);
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/{customerId}", 987654321)
                .then()
                .statusCode(404);
    }

    @Test
    public void post() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        assertThat(saved.getCustomerId()).isNotNull();
    }

    @Test
    public void postFailNoFirstName() {
        Customer customer = createCustomer();
        customer.setFirstName(null);
        ErrorResponse errorResponse = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(400)
                .extract().as(ErrorResponse.class);
        assertThat(errorResponse.getErrorId()).isNull();
        assertThat(errorResponse.getErrors())
                .isNotNull()
                .hasSize(1)
                .contains(new ErrorResponse.ErrorMessage("post.customer.firstName", getErrorMessage("Customer.firstName.required")));
    }

    @Test
    public void postFailNoFirstNameNoLastNameAndBadEmail() {
        Customer customer = new Customer();
        customer.setEmail("NotGood");
        ErrorResponse errorResponse = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertThat(errorResponse.getErrorId()).isNull();
        assertThat(errorResponse.getErrors())
                .isNotNull()
                .hasSize(3)
                .contains(
                        new ErrorResponse.ErrorMessage("post.customer.firstName", getErrorMessage("Customer.firstName.required")),
                        new ErrorResponse.ErrorMessage("post.customer.lastName", getErrorMessage("Customer.lastName.required")),
                        new ErrorResponse.ErrorMessage("post.customer.email", getErrorMessage("Customer.email.invalid"))
                );
    }

    @Test
    public void put() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        saved.setFirstName("Updated");
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(204);
    }

    @Test
    public void putFailNoLastName() {
        Customer customer = createCustomer();
        Customer saved = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .post()
                .then()
                .statusCode(201)
                .extract().as(Customer.class);
        saved.setLastName(null);
        ErrorResponse errorResponse = given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{customerId}", saved.getCustomerId())
                .then()
                .statusCode(400)
                .extract().as(ErrorResponse.class);
        assertThat(errorResponse.getErrorId()).isNull();
        assertThat(errorResponse.getErrors())
                .isNotNull()
                .hasSize(1)
                .contains(new ErrorResponse.ErrorMessage("put.customer.lastName", getErrorMessage("Customer.lastName.required")));
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(RandomStringUtils.randomAlphabetic(10));
        customer.setMiddleName(RandomStringUtils.randomAlphabetic(10));
        customer.setLastName(RandomStringUtils.randomAlphabetic(10));
        customer.setEmail(RandomStringUtils.randomAlphabetic(10) + "@rhenergy.dev");
        customer.setPhone(RandomStringUtils.randomNumeric(10));
        return customer;
    }

    private String getErrorMessage(String key) {
        return ResourceBundle.getBundle("ValidationMessages").getString(key);
    }

}
