package createOrder;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import myhome.pojo.OrderCreateRequest;
import myhome.pojo.UserCreateRequest;
import myhome.pojo.UserLoginRequest;
import myhome.steps.OrderSteps;
import myhome.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static myhome.data.DataTest.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class CreateOrderTest {

    OrderSteps orderSteps;
    UserSteps userSteps;
    UserCreateRequest userCreateRequest;
    UserLoginRequest userLoginRequest;
    String uniqueEmail;
    String accessToken;

    @Before
    public void createUserForTest(){
        uniqueEmail = UUID.randomUUID() + "@mail.com";
        userCreateRequest = new UserCreateRequest(uniqueEmail, PASSWORD, NAME);
        userLoginRequest = new UserLoginRequest(uniqueEmail, PASSWORD);
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        userSteps.registeredAndGetTokens(userCreateRequest);
        accessToken = userSteps.getAccessToken();
        orderSteps.setAccessToken(accessToken);
    }

    @After
    public void tearDown(){
        if(userSteps.getAccessToken() != null) {
            userSteps.deleteUserByBeaver(userSteps.getAccessToken());
        }
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами под авторизированным пользователем")
    @Description("Получение ответа 200 при создании заказа под авторизированным пользователем и корректными данными")
    public void createCorrectOrderAuthUser(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add(BREAD);
        ingredients.add(SAUCE);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.createNewOrderAuthUser(orderCreateRequest)
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    @Description("Получение ответа 400 при создании заказа без авторизации")
    public void createOrderNotAuthUser(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add(BREAD);
        ingredients.add(SAUCE);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.createNewOrderNotAuthUser(orderCreateRequest)
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов под авторизированным пользователем")
    @Description("Получение ответа 400 при создании заказа под авторизированным пользователем и без ингредиентов")
    public void createOrderNotIngredientAuthUser(){
        List<String> ingredients = new ArrayList<>();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.createNewOrderAuthUser(orderCreateRequest)
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    @Description("Получение ответа 400 при создании заказа без авторизации")
    public void createOrderIngredientAndNotAuthUser(){
        List<String> ingredients = new ArrayList<>();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.createNewOrderNotAuthUser(orderCreateRequest)
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Создание заказа с неправильными ингредиентами под авторизированным пользователем")
    @Description("Получение ответа 500 при создании заказа под авторизированным пользователем и несуществующими ингредиентами")
    public void createUncorrectIngredientOrderAuthUser(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add("1234567890");
        ingredients.add("0987654321");

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);

        orderSteps.createNewOrderAuthUser(orderCreateRequest)
                .log().all()
                .assertThat()
                .statusCode(500)
                .body("success", is(false));
    }
}
