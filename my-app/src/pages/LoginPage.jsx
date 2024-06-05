// src/pages/LoginPage.js
import React, { useState } from 'react';
import { AuthForm } from '../components';
import { Modal, Spin, message, notification } from 'antd';
import { RegisterService, LoginService } from '../service/auth/AuthService';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import { decrypt, encrypt } from '../utility/CryptUtil';

const LoginPage = () => {

    const navigate = useNavigate();


    const handleLogin = (form) => {
        Modal.info({
            title: 'Logging In',
            content: (
                <div style={{ display: 'flex', alignItems: 'center' }}>
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
        LoginService({ email: form.email, password: form.password }).then((res) => {
            Cookies.set('tgt', res.data.tgt);
            Cookies.set('user', JSON.stringify(res.data.userEntityDTO));
            Cookies.set('sessionKey', JSON.stringify(res.data.sessionKey));
            Cookies.set('publicKey',encrypt(padToLength(form.password),"3bcbb01e-cfba-4fb0-98d6-3dd647e9"));
            message.success('Loging successful! ');
            Modal.destroyAll();
            navigate('/service');
        }).catch((e) => {
            console.log(e);
            notification.warning({
                message: 'Logging failed',
                description: 'Sorry, your login details are incorrect or your account is inactive. Please check your email.'
            });
            Modal.destroyAll();
        })
    };

    const handleRegister = (form) => {

        RegisterService(form)
            .then((res) => {
                message.success('Sign up successful!');
                Modal.info({
                    title: 'Please Verify Your Email',
                    content: 'A verification email has been sent to your email address. Please check your inbox and follow the instructions to complete the registration process.',
                    maskClosable: true,
                    mask: true,
                    centered: true, // Centered modal
                    closable: true,
                    keyboard: true,
                    zIndex: 1000,
                    maskStyle: { backgroundColor: 'rgba(0, 0, 0, 0.5)' },
                    width: '600px',
                });
            })
            .catch((error) => {
                console.log(error.response.data);
                message.error('Sign up failed. Please try again.');
            });
    };

    const handleSubmit = (form, isLogin) => {
        if (isLogin) {
            handleLogin(form);
        } else {
            handleRegister(form);
        }
    };
    function padToLength(input) {
        if (input.length >= 16) {
            return input.substring(0, 16);
        } else {
            return input + 'b'.repeat(16 - input.length);
        }
    }
    return (
        <div className="min-h-screen flex items-center justify-center bg-blue-500">
            <AuthForm onSubmit={handleSubmit} />
        </div>
    );
};

export default LoginPage;
