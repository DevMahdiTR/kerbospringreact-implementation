package org.example.authentication.utility;

public final class APIRouters {

    public static final String BASE_PATH = "http://localhost:8081";
    private static final String BASE_API_PATH = "api";
    private static final String CURRENT_VERSION = "v1";
    public static final String USER_ROUTER = BASE_API_PATH + "/" + CURRENT_VERSION + "/users";
    public static final String AUTH_ROUTER = BASE_API_PATH + "/" + CURRENT_VERSION + "/auth";
    public static final String RECIPE_ROUTER = BASE_API_PATH + "/" + CURRENT_VERSION + "/recipe";

    public static String getConfirmationURL(String confirmationToken) {
        return BASE_PATH + "/" + AUTH_ROUTER + "/confirm?token=" + confirmationToken;
    }

    private APIRouters() {}
}
