import React from 'react'
import { LoginService, RenewToken, GenerateServiceTicket, LoginServiceE } from '../service/auth/AuthService'
import Cookies from 'js-cookie'
import { decrypt, encrypt } from '../utility/CryptUtil'
import { Modal, Spin } from 'antd'
import { useNavigate } from 'react-router-dom';
import { AuthenticationStepper } from '../components'
const ServicePage = () => {


    const navigate = useNavigate();



    const handleClick = async () => {


        Modal.info({
            title: 'Logging In Service 1',
            content: (
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <AuthenticationStepper />
                    <Spin size="large" />
                    <span style={{ marginLeft: '10px' }}>Logging in...</span>
                </div>
            ),
            mask: true,
            centered: true,
            zIndex: 1000,
            maskStyle: { backgroundColor: 'rgba(0, 0, 0, 0.5)' },
            width: 600,
            okButtonProps: { style: { display: 'none' } },
        });
        setTimeout(async () => {
            try {
                const userJson = Cookies.get('user');
                const userObject = JSON.parse(userJson);
                const tgt = Cookies.get('tgt').replace('"', '');

                await RenewToken(tgt, userObject.id);

                const encryptedPublicKey = Cookies.get('publicKey').replace('"', '');
                const decryptedPublicKey = decrypt(encryptedPublicKey, '3bcbb01e-cfba-4fb0-98d6-3dd647e9');

                const encryptedRedKey = Cookies.get('sessionKey').replace('"', '');
                const decryptedRedKey = decrypt(encryptedRedKey, decryptedPublicKey);

                const tgsAuthenticator = JSON.stringify({ "userId": userObject.id, "timestamp": Date.now() });
                const encryptedAuthenticator = encrypt(tgsAuthenticator, decryptedRedKey);

                const tgsResponse = await GenerateServiceTicket(tgt, 1, encryptedAuthenticator);
                const serviceTicket = tgsResponse.data.serviceTicket;
                const tgsSessionKey = tgsResponse.data.sessionKey;
                const decryptedServiceSessionKey = decrypt(tgsSessionKey, decryptedRedKey);

                const serviceAuthenticator = JSON.stringify({ "userId": userObject.id, "timestamp": Date.now() });
                const encryptedServiceAuthenticator = encrypt(serviceAuthenticator, decryptedServiceSessionKey);

                const serviceResponse = await LoginServiceE(serviceTicket, encryptedServiceAuthenticator);
                Cookies.set('serviceToken', serviceResponse.data.token);

                Modal.destroyAll();
                navigate('/service/dashboard');
            } catch (err) {
                console.log(err);
                Modal.destroyAll();
            }
        }, 6000);  

    }

    return (
        <div
            className="flex justify-center items-center h-screen bg-blue-500"

            onDragStart={(e) => e.preventDefault()}
            onMouseDown={(e) => e.preventDefault()}
            onDoubleClick={(e) => e.preventDefault()}
            onTouchStart={(e) => e.preventDefault()}
        >
            <div
                className="text-center w-24 h-24 bg-white rounded-full flex justify-center items-center cursor-pointer text-sm font-semibold shadow-md hover:shadow-lg"
                onClick={handleClick}
            >
                Connect to Service 1
            </div>
        </div>
    )
}

export default ServicePage
