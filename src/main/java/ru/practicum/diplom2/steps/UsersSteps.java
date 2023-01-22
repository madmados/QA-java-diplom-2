package ru.practicum.diplom2.steps;

import io.restassured.RestAssured;
import ru.practicum.diplom2.constants.BaseConstants;
import ru.practicum.diplom2.constants.UserConstants;
import ru.practicum.diplom2.pojos.UserRequest;
import ru.practicum.diplom2.utils.UsersUtils;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.practicum.diplom2.pojos.SignInRequest;

import static io.restassured.RestAssured.given;

public class UsersSteps {
    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setBaseUri(BaseConstants.BASE_TEST_URL)
                    .setBasePath(UserConstants.BASE_AUTH_URL)
                    .setContentType(ContentType.JSON)
                    .build();

    @Step("Создание уникального юзера")
    public static Response createUniqueUser(UserRequest body) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(UserConstants.BASE_REGISTER_URL);
    }

    @Step("Создание дубликата юзера")
    public static Response createDuplicateUser() {
        UserRequest user = UsersUtils.getUniqueUser();
        RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(user)
                .when()
                .post(UserConstants.BASE_REGISTER_URL);

        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(user)
                .when()
                .post(UserConstants.BASE_REGISTER_URL);
    }

    @Step("Создание юзера без пароля")
    public static io.restassured.response.Response createUserWithoutPassword() {
        UserRequest user = UsersUtils.getUniqueUser();
        user.setPassword(null);

        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(user)
                .when()
                .post(UserConstants.BASE_REGISTER_URL);
    }

    @Step("Выполнение авторизации с помощью тела запроса на авторизацию")
    public static Response signInWithSignInRequest(SignInRequest signInRequest) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(signInRequest)
                .when()
                .post(UserConstants.BASE_LOGIN_URL);
    }

    @Step("Выполнение авторизации под несуществующими данными")
    public static Response signInWithInvalidData() {
        UserRequest user = UsersUtils.getUniqueUser();

        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(user)
                .when()
                .post(UserConstants.BASE_LOGIN_URL);
    }

    @Step("Авторизуемся и редактируем информацию о юзере")
    public static Response editUserDataWithAuth(String accessToken, UserRequest updatedUser) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch(UserConstants.BASE_USER_URL);
    }

    @Step("Редактируем информацию о юзере без авторизации")
    public static Response editUserDataWithoutAuth() {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(UsersUtils.getUniqueUser())
                .when()
                .patch(UserConstants.BASE_USER_URL);
    }

    @Step("Удаляем пользователя")
    public static Response deleteUser(String accessToken) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .delete(UserConstants.BASE_USER_URL);
    }
}
