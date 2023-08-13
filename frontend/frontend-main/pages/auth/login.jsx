import { LoginForm } from "components/modules/auth/login/LoginForm";
import { useAuthContext } from "components/context/AuthContext";
import React from "react";

const Login = () => {
  return (
    <div className="w-fit border-2 mx-auto mt-16 border-gray-600 align-middle rounded-md px-12 py-6">
      <LoginForm />
    </div>
  );
};

export default Login;
