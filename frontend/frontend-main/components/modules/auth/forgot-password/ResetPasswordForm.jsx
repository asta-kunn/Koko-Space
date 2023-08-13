import React, { useState } from "react";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { useRouter } from "next/router";

export const ResetPasswordForm = ({ token }) => {
  const router = useRouter();

  const [passwordInput, setPasswordInput] = useState({
    password: "",
    repeatPassword: "",
  });

  const [error, setError] = useState({
    password: "",
    repeatPassword: "",
  });

  const passwordInputChange = (e) => {
    const { name, value } = e.target;
    setPasswordInput((prev) => ({
      ...prev,
      [name]: value,
    }));
    validatePasswordInput(e);
  };

  const validatePasswordInput = (e) => {
    let { name, value } = e.target;
    setError((prev) => {
      const stateObj = { ...prev, [name]: "" };

      switch (name) {
        case "password":
          if (!value) {
            stateObj[name] = "Please enter Password";
          } else if (
            passwordInput.repeatPassword &&
            value !== passwordInput.repeatPassword
          ) {
            stateObj["repeatPassword"] =
              "Password and Repeat Password does not match";
          } else {
            stateObj["repeatPassword"] = passwordInput.repeatPassword
              ? ""
              : error.repeatPassword;
          }
          break;

        case "repeatPassword":
          if (!value) {
            stateObj[name] = "Please enter Repeat Password.";
          } else if (
            passwordInput.password &&
            value !== passwordInput.password
          ) {
            stateObj[name] = "Password and Repeat Password does not match";
          }
          break;

        default:
          break;
      }

      return stateObj;
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const password = event.target.password.value;

    const reset = axios.patch("/api/auth/change-password", {
      newPassword: password,
      secretToken: token,
    });

    toast.promise(reset, {
      loading: "Loading...",
      success: (res) => `${res.data}`,
      error: (err) => err.response.data.message,
    });

    setTimeout(() => router.push("/"), 3000);

    event.target.reset();
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1 className="text-3xl text-center font-bold pb-3 text-indigo-800">
        Reset Password Form
      </h1>

      <div className="flex justify-between my-2 mx-2">
        <label htmlFor="password" className="pr-4">
          Password
        </label>
        <div className="flex flex-col max-w-min">
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Password"
            className="border-2 px-1 rounded-md"
            value={passwordInput.password}
            onChange={passwordInputChange}
            onBlur={validatePasswordInput}
          ></input>
          {error.password && (
            <span className="text-rose-500 text-sm">{error.password}</span>
          )}
        </div>
      </div>
      <div className="flex justify-between my-2 mx-2">
        <label htmlFor="repeat-password" className="pr-4">
          Repeat Password
        </label>
        <div className="flex flex-col max-w-min">
          <input
            type="password"
            id="repeatPassword"
            name="repeatPassword"
            placeholder="Repeat Password"
            className="border-2 px-1 rounded-md"
            onChange={passwordInputChange}
            onBlur={validatePasswordInput}
          ></input>
          {error.repeatPassword && (
            <span className="text-rose-500 text-sm">
              {error.repeatPassword}
            </span>
          )}
        </div>
      </div>
      <button
        className="mx-auto rounded-md border-2 block py-1 px-2 mt-4 bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
        disabled={
          !passwordInput.password ||
          !passwordInput.repeatPassword ||
          passwordInput.password !== passwordInput.repeatPassword
        }
      >
        Register
      </button>
      <Toaster />
    </form>
  );
};
