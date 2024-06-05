package org.kerberos.service.utility;

public final class APIRouters {


    private APIRouters() {}
    private static final String BASE_API_PATH = "api";
    private static final String CURRENT_VERSION = "v1";
    public static final String AUTH_ROUTER = BASE_API_PATH + "/" + CURRENT_VERSION + "/auth";
}
