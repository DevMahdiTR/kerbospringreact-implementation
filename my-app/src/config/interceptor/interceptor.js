import axios from "axios";
import { notification } from 'antd';
import Cookies from 'js-cookie';

// Define your base URLs
const ASBaseUrl = 'http://localhost:8081/api/v1';
const TgsBaseUrl = 'http://localhost:8082/api/v1';
const ServiceEBaseUrl = 'http://localhost:8083/api/v1';

// Create separate instances of Axios for each base URL
const ASInterceptor = axios.create({
    baseURL: ASBaseUrl,
    timeout: 60000
});

const TgsInterceptor = axios.create({
    baseURL: TgsBaseUrl,
    timeout: 60000
});

const ServiceEInterceptor = axios.create({
    baseURL: ServiceEBaseUrl,
    timeout: 60000
});

const statusMessages = {
    400: "Bad Request",
    401: "Unauthorized",
    404: "Not Found",
    405: "Method Not Allowed",
    403: 'Token expired',
    408: "Request Timeout",
    409: "Conflict",
    415: "Unsupported Media Type",
    422: "Unprocessable Entity",
    429: "Too Many Requests",
    500: "Internal Server Error",
    501: "Not Implemented",
    502: "Bad Gateway",
    503: "Service Unavailable",
    504: "Gateway Timeout",
    505: "HTTP Version Not Supported",
    506: "Variant Also Negotiates",
    507: "Insufficient Storage",
    508: "Loop Detected",
    509: "Bandwidth Limit Exceeded",
    510: "Not Extended",
    511: "Network Authentication Required",
    499: "Client Closed Request",
};

// Define a list of URLs that don't need authorization
const noAuthUrls = ['login', 'register', 'validate'];

// Function to check if URL needs authorization
const requiresAuth = (url) => !noAuthUrls.some(endpoint => url.includes(endpoint));

// Function to get the appropriate interceptor based on the URL
const getRequestInterceptor = (url) => {
    if (url.startsWith(ASBaseUrl)) {
        return requiresAuth(url) ? ASInterceptor : axios;
    } else if (url.startsWith(TgsBaseUrl)) {
        return requiresAuth(url) ? TgsInterceptor : axios;
    } else if (url.startsWith(ServiceEBaseUrl)) {
        return requiresAuth(url) ? ServiceEInterceptor : axios;
    } else {
        // Default interceptor if URL doesn't match any base URL
        return axios;
    }
};
// Set up request interceptors
axios.interceptors.request.use(
    (config) => {
        const jwtToken = Cookies.get('token');
        if (jwtToken) {
            config.headers['Authorization'] = `Bearer ${jwtToken}`;
        }
        return config;
    },
    (error) => {
        notification.error({
            message: "Error",
            description: error.message
        });
        return Promise.reject(error);
    }
);

// Set up response interceptors
axios.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        let notificationParam = {
            message: "",
        };
        if (error.code === 'ECONNREFUSED') {
            notificationParam.message = "Connection Refused";
            notificationParam.description = "Unable to connect to the server. Please check your internet connection or try again later.";
        } else {
            const status = error.response ? error.response.status : 500;
            notificationParam.message = statusMessages[status] || "Unknown Error";
            notificationParam.description = error.response?.data?.errors;
        }
        notification.error(notificationParam);
        console.log("error", notificationParam);
        return Promise.reject(error);
    }
);

// Export the default interceptor
export default axios;

