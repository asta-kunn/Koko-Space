import { useState } from "react";
import { InputElement } from "../elements";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";

export const ForgotPasswordForm = () => {
  const [isDisabled, setIsDisabled] = useState(false);

  const handleSubmit = async (event) => {
    setIsDisabled(true);
    event.preventDefault();
    const email = event.target.email.value;

    const sendTokenToEmail = axios
      .post("/api/auth/forgot-password", { email: email })
      .finally(() => setIsDisabled(false));

    toast.promise(sendTokenToEmail, {
      loading: "Sending Token...",
      success: (res) => `Token sent to ${res.data.email}`,
      error: (err) => err.response.data.message,
    });

    event.target.reset();
  };
  return (
    <form onSubmit={handleSubmit}>
      <h1 className="text-3xl text-center font-bold pb-3 text-indigo-800">
        Forgot Password
      </h1>
      <InputElement
        name="email"
        id="email"
        placeholder="example: koko@space.com"
        type="email"
      />
      <button
        disabled={isDisabled}
        className="mx-auto rounded-md border-2 block py-1 px-2 mt-[1.25rem] bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
      >
        Submit
      </button>
      <Toaster />
    </form>
  );
};
