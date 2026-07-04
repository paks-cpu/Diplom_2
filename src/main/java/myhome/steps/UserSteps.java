package myhome.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import myhome.constants.ApiEndpoint;
import myhome.pojo.UserCreateRequest;
import myhome.pojo.UserLoginRequest;

import static io.restassured.RestAssured.*;
import static myhome.constants.ApiEndpoint.*;

public class UserSteps {

    private String accessToken;
    private String refreshToken;
    private String name;


    public static RequestSpecification getBaseSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(ApiEndpoint.BASE_URL);
    }

    @Step("Создание нового пользователя")
    public void registeredAndGetTokens(UserCreateRequest userCreateRequest) {
        Response response = getBaseSpec()
                .body(userCreateRequest)
                .post(POST_REGISTER_USER)
                .then()
                .extract()
                .response();

        this.accessToken = response.jsonPath().get("accessToken");
        this.refreshToken = response.jsonPath().get("refreshToken");
        this.name = response.jsonPath().get("user.name");
    }

    @Step("Регистрации пользователя с ожидаемой ошибкой")
    public ValidatableResponse registerUserExpectingError(UserCreateRequest userCreateRequest) {
        return getBaseSpec()
                .body(userCreateRequest)
                .post(POST_REGISTER_USER)
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUserByBeaver(String accessToken) {
        getBaseSpec()
                .header("Authorization", accessToken)
                .delete(DELETE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public Response authorizationUser(UserLoginRequest userLoginRequest) {
        Response authResponse = getBaseSpec()
                .body(userLoginRequest)
                .post(POST_AUTH_USER)
                .then()
                .extract()
                .response();

        this.accessToken = authResponse.jsonPath().get("accessToken");
        this.refreshToken = authResponse.jsonPath().get("refreshToken");
        return  authResponse;
    }

    public String getAccessToken(){
        return accessToken;
    }
    public String getRefreshToken(){
        return refreshToken;
    }
    public String getName(){
        return name;
    }
}