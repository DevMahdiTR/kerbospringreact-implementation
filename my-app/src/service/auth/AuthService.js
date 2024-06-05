import { APIS,ASUrl,TgsUrl,ServiceEUrl } from "../../config/constants/URLS";
import fetch from '../../config/interceptor/interceptor';


export const LoginService = (data) => {
    return fetch({
        method : 'post',
        url : ASUrl + APIS.AUTH.login,
        data,
    })
}

export const logOutService = () => {
    return fetch({
        method : 'get',
        url : ASUrl + APIS.AUTH.logout,
    })
}

export const RegisterService = (data)=>{
    return fetch({
        method: "post",
        url: ASUrl+ APIS.AUTH.register,
        data
    })
}

export const RenewToken = (token , userId) => {
    return fetch({
        method : 'get',
        url : ASUrl + APIS.AUTH.renewToken(token,userId),
    })
}

export const GenerateServiceTicket = (tgt,serviceId,encryptedAuthenticator) => {
    return fetch({
        method : 'get',
        url : TgsUrl + APIS.TGT.generateServiceTicket(tgt,serviceId,encryptedAuthenticator),
    })
}

export const LoginServiceE = (encryptedTgt , encryptedAuthenticator) => {
    return fetch({
        method : 'get',
        url : ServiceEUrl + APIS.ServiceEUrl.login(encryptedTgt,encryptedAuthenticator),
    })
}

export const ValidateServiceToken = (token) => {
    return fetch({
        method : 'get',
        url : ServiceEUrl + APIS.ServiceEUrl.validateToken(token),
    })
}