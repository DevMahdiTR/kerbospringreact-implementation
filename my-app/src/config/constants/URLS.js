export const ASUrl = 'http://localhost:8081/api/v1';
export const TgsUrl = 'http://localhost:8082/api/v1';
export const ServiceEUrl = 'http://localhost:8083/api/v1';



export const APIS = {

    AUTH: {
        login: '/auth/login',
        logout : '/auth/logout',
        register: '/auth/register',
        renewToken : (token, userId) => `/auth/renew?token=${token}&userId=${userId}`,
        
    },
    TGT: {
        generateServiceTicket  : (tgt,serviceId,encryptedAuthenticator) => `/tgt/generate-service-ticket?tgt=${tgt}&serviceId=${serviceId}&authenticator=${encryptedAuthenticator}`
    },
    ServiceEUrl: {
        login: (encryptedTgt , encryptedAuthenticator) => `/auth/login?encryptedTgt=${encryptedTgt}&encryptedAuthenticator=${encryptedAuthenticator}`,
        validateToken : (token) => `/auth/validate-token?token=${token}`
    }
}