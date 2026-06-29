package loginUser;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import myhome.pojo.UserCreateRequest;
import myhome.pojo.UserLoginRequest;
import myhome.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static myhome.data.DataTest.NAME;
import static myhome.data.DataTest.PASSWORD;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

public class LoginUserTest {
    private UserSteps userSteps;
    private UserCreateRequest userCreateRequest;
    private String accessToken;

    @Before
    public void setUp(){
        String uniqueEmail = UUID.randomUUID() + "@mail.com";
        userCreateRequest = new UserCreateRequest(uniqueEmail, PASSWORD, NAME);
        userSteps = new UserSteps();
    }

    @After
    public void tearDown(){
        if(accessToken != null && !accessToken.isEmpty()){
            userSteps.deleteUserByBeaver(accessToken);
        }
    }

    @DisplayName("Регистрация и авторизация нового пользователя")
    @Description("Получение 201 ответа с accessToken и refreshToken при создании нового пользователя с последущей авторизацией и удалением пользователя")
    @Test
    public void newUserRegistrationSuccessAndDeleteUser() {
        userSteps.registeredAndGetTokens(userCreateRequest);
        accessToken = userSteps.getAccessToken();

        assertNotNull("accessToken не должен быть null", accessToken);
    }

    @DisplayName("Авторизация пользователя с неверными логином и паролем")
    @Description("Получение ошибки 401 при авторизация пользователя с неверными логином и паролем")
    @Test
    public void failedAuth() {
        String wrongEmail = "tutTochnoNeptavilnoVse@gmail.com";
        UserLoginRequest wrongLoginRequest = new UserLoginRequest(wrongEmail, "wrongPassword");

        userSteps.authorizationUser(wrongLoginRequest)
                .then()
                .log().all()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}
