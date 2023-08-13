import React from "react";
import { useAuthContext } from "components/context/AuthContext";
import axios from "axios";
import toast from "react-hot-toast";

const Verification = () => {
  const { user } = useAuthContext();

  const handleSendVerification = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    var body = {
      id: user.id,
    };

    const sendTokenToEmail = axios.post("/api/auth/verification", body, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    toast.promise(sendTokenToEmail, {
      loading: "Sending Token...",
      success: (res) => `Token sent to ${res.data.email}`,
      error: (err) => err.response.data.message,
    });
  };
  if (!user) {
    return (
    <div className="flex flex-col items-center justify-center h-screen">
      <p className="text-3xl font-bold py-6">Send Verification Menu</p>
      <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
    </div>
    )
  }
  return (
    <div className="w-fit border-2 mx-auto mt-16 border-gray-600 align-middle rounded-md px-12 py-6">
      <h1 className="text-3xl text-center font-bold pb-3 text-indigo-800">
        Account and Email Verification
      </h1>
      <button
        onClick={handleSendVerification}
        className="mx-auto rounded-md border-2 block py-1 px-2 mt-[1.25rem] bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
      >
        Send Email
      </button>
    </div>
  );
};

export default Verification;
