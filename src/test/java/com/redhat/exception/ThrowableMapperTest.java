package com.redhat.exception;

import com.redhat.customer.CustomerService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ThrowableMapperTest {

    @InjectMock
    CustomerService customerService;

    @Test
    public void throwUnexpectedRuntimeException() {
        Mockito.when(customerService.findAll()).thenThrow(new RuntimeException("Completely Unexpected"));
        ErrorResponse errorResponse = given()
                .when()
                .get("/customers")
                .then()
                .statusCode(500)
                .extract().as(ErrorResponse.class);
        assertThat(errorResponse.getErrorId()).isNotNull();
        assertThat(errorResponse.getErrors())
                .isNotNull()
                .hasSize(1)
                .contains(new ErrorResponse.ErrorMessage(ResourceBundle.getBundle("ValidationMessages").getString("System.error")));
    }

}
