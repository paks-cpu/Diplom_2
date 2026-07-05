package myhome.steps;

import io.qameta.allure.Step;

import static myhome.constants.ApiEndpoint.*;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import myhome.pojo.OrderCreateRequest;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private String accessToken;

    public void setAccessToken(String accessToken){
                this.accessToken = accessToken;
    }

    public static RequestSpecification getBaseSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    @Step("Создание заказа под авторизованным пользователем")
    public ValidatableResponse createNewOrderAuthUser(OrderCreateRequest orderCreateRequest) {
    return getBaseSpec()
                        .headers("Authorization", accessToken)
                        .body(orderCreateRequest)
                        .post(POST_ORDERS)
                        .then();
    }

    @Step("Создание заказа не под авторизованным пользователем")
    public ValidatableResponse createNewOrderNotAuthUser(OrderCreateRequest orderCreateRequest) {
                return  getBaseSpec()
                        .body(orderCreateRequest)
                        .post(POST_ORDERS)
                        .then();
    }

}
