package createUser;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import myhome.pojo.UserCreateRequest;
import myhome.pojo.UserLoginRequest;
import myhome.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static myhome.data.DataTest.*;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

public class UserRegistrationTest {

    UserSteps userSteps;
    UserCreateRequest userCreateRequest;
    UserLoginRequest userLoginRequest;
    String uniqueEmail;

    @Before
    public void createUserForTest(){
        uniqueEmail = UUID.randomUUID() + "@mail.com";
        userCreateRequest = new UserCreateRequest(uniqueEmail, PASSWORD, NAME);
        userLoginRequest = new UserLoginRequest(uniqueEmail, PASSWORD);
        userSteps = new UserSteps();
    }

    @After
    public void tearDown(){
        if(userSteps.getAccessToken() != null) {
            userSteps.deleteUserByBeaver(userSteps.getAccessToken());
        }
    }

    @DisplayName("Создание нового пользователя")
    @Description("Получение 201 ответа с accessToken и refreshToken при создании нового пользователя")
    @Test
    public void newUserRegistrationSuccess(){
        userSteps.registeredAndGetTokens(userCreateRequest);

        assertNotNull(userSteps.getAccessToken());
        assertNotNull(userSteps.getRefreshToken());
    }

    @DisplayName("Создание двух идентичных пользователей")
    @Description("Получение 403 ответа при создании идентичных пользователей")
    @Test
    public void newIdenticalRegistrationUserFailed(){
        userSteps.registeredAndGetTokens(userCreateRequest);
        assertNotNull(userSteps.getAccessToken());
        assertNotNull(userSteps.getRefreshToken());

        userSteps.registerUserExpectingError(userCreateRequest)
                .statusCode(403)
                .body("message", org.hamcrest.Matchers.equalTo("User already exists"));
    }

    @DisplayName("Создание пользователя без одного из обязательных полей")
    @Description("Получение 403 ответа при создании пользователя без обязательного поля name")
    @Test
    public void newUserRegistrationNoFieldName(){
        userSteps.registerUserExpectingError(userCreateRequest)
        .statusCode(403)
        .body("message", org.hamcrest.Matchers.equalTo("Email, password and name are required fields"));
    }
}
