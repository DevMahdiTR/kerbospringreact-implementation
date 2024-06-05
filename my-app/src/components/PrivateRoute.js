import React, { useState } from 'react';
import Cookies from 'js-cookie';
import { Navigate } from 'react-router-dom';
import { ValidateServiceToken } from '../service/auth/AuthService';
const PrivateRoute = ({ children }) => {
    const [isValidToken, setIsValidToken] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const serviceToken = Cookies.get('serviceToken');

    if (serviceToken) {
        ValidateServiceToken(serviceToken)
            .then(() => {
                setIsValidToken(true);
            })
            .catch(() => {
                setIsValidToken(false);
            })
            .finally(() => {
                setIsLoading(false);
            });
    } else {
        setIsLoading(false);
    }

    if (isLoading) { return <></>; }
    return isValidToken ? children : <Navigate to='/' />;
};

export default PrivateRoute;
