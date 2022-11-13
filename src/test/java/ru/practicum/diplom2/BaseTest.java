package ru.practicum.diplom2;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import ru.practicum.diplom2.pojos.SuccessSignInSignUpResponse;
import ru.practicum.diplom2.pojos.UserRequest;
import ru.practicum.diplom2.steps.UsersSteps;
import ru.practicum.diplom2.utils.UsersUtils;

public class BaseTest {
    UserRequest user;
    String accessToken;

    @Before
    public void init() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        user = UsersUtils.getUniqueUser();
        accessToken = UsersSteps
                .createUniqueUser(user)
                .as(SuccessSignInSignUpResponse.class)
                .getAccessToken();

        //Ожидаем 4 секунды, чтобы избежать 429
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void shutdown() {
        UsersSteps
                .deleteUser(accessToken)
                .then()
                .statusCode(202);
    }
}
