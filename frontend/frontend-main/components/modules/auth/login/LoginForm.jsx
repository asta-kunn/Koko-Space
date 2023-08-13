import React, { useRef } from "react";
import { InputElement } from "../elements";
import Link from "next/link";
import { useAuthContext } from "components/context/AuthContext";

export const LoginForm = () => {
  const { login } = useAuthContext();
  const emailInputRef = useRef();
  const passwordInputRef = useRef();

  const handleSubmit = () => {
    login({
      email: emailInputRef.current.value,
      password: passwordInputRef.current.value,
    });
  };

  return (
    <>
      <h1 className="text-3xl text-center font-bold pb-3 text-indigo-800">
        Login Form
      </h1>
      <InputElement
        name="username"
        placeholder="Email"
        type="text"
        refs={emailInputRef}
      />
      <InputElement
        name="password"
        placeholder="Password"
        type="password"
        refs={passwordInputRef}
      />
      <button
        onClick={handleSubmit}
        className="mx-auto rounded-md border-2 block py-1 px-2 mt-[1.25rem] bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
      >
        Login
      </button>
      <Link href="/auth/forgot-password">
        <p className="text-center mt-2 underline text-indigo-800 cursor-pointer">
          Forgot Password?
        </p>
      </Link>
    </>
  );
};
