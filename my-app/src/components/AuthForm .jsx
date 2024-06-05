// src/components/AuthForm.js
import React, { useEffect, useState } from 'react';

const AuthForm = ({ onSubmit }) => {
    const [isLogin, setIsLogin] = useState(true);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    const toggleMode = () => {
        setIsLogin(!isLogin);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        const formData = {
            firstName,
            lastName,
            email: event.target.email.value,
            password: event.target.password.value
        };
        onSubmit(formData,isLogin);
    };



  return (
    <form className="w-1/2 max-w-md mx-auto bg-white p-8 shadow-md rounded-lg border-t-4 border-blue-500" onSubmit={handleSubmit} >
      <h2 className="text-2xl font-bold mb-6 text-gray-800">{isLogin ? 'Login' : 'Register'}</h2>
      {!isLogin && (
        <>
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="firstName">First Name</label>
            <input className="text-sm w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500 " type="text" id="firstName" value={firstName} onChange={(e) => setFirstName(e.target.value)} required />
          </div>
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="lastName">Last Name</label>
            <input className=" text-sm w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500" type="text" id="lastName" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
          </div>
        </>
      )}
      <div className="mb-4">
        <label className="block text-gray-700 mb-2" htmlFor="email">Email</label>
        <input className=" text-sm w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500" type="email" id="email" required />
      </div>
      <div className="mb-4">
        <label className="block text-gray-700 mb-2" htmlFor="password">Password</label>
        <input className=" text-sm w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:border-blue-500" type="password" id="password" required />
      </div>
      <button className=" text-sm w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600 focus:outline-none" type='submit'>
        {isLogin ? 'Login' : 'Register'}
      </button>
      <p className="mt-4 text-gray-700">{isLogin ? "Don't have an account?" : "Already have an account?"} <a  className="text-blue-500  cursor-pointer" onClick={toggleMode}>{isLogin ? 'Register' : 'Login'}</a></p>
    </form>
  );
};

export default AuthForm;
