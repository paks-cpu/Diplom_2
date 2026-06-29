package myhome.constants;

public class ApiEndpoint {
    public static final String BASE_URL = "https://stellarburgers.education-services.ru"; //сайт для тестирования
    public static final String GET_INGREDIENTS = "/api/ingredients"; //получения данных об ингридиентах
    public static final String POST_ORDERS = "/api/orders"; //создание заказа
    public static final String POST_PASSWORD_RESET = "/api/password-reset"; //восстановление и сброс пароля
    public static final String POST_REGISTER_USER = "/api/auth/register"; //создание пользователя
    public static final String POST_AUTH_USER = "/api/auth/login"; //авторизация пользователя
    public static final String GET_USER_INFO = "/api/auth/user"; //получениие и обновление информации о пользователе
    public static final String DELETE_USER = "/api/auth/user"; //удаление полььзователя
    public static final String GET_ALL_ORDERS = "/api/orders/all"; //получить все заказы
    public static final String GET_USER_ORDERS = "/api/orders"; //получить заказы конкретного пользоваля
}
