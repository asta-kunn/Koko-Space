import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";
import Link from "next/link";
import { console } from "next/dist/compiled/@edge-runtime/primitives/console";

const Verify = ({}) => {
  const router = useRouter();
  const { token } = router.query; // Access the value of the "token" query parameter
  const [tokenAja, setTokenAja] = useState(token);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTokenAja(token);
  }, [token]);

  const [datas, setDatas] = useState([]);
  useEffect(() => {
    setIsLoading(true)
    const fetchData = async () => {
      try {
        console.log(tokenAja);
        if (tokenAja === undefined) {
          setDatas("");
          return;
        }
        const res = await axios.post(
          `${process.env.NEXT_PUBLIC_AUTH_API_URL}/user/verify?token=${token}`
        );
        if (res.status === 200) {
          setIsLoading(false)
          setDatas("Account Activated");
        }
      } catch (e) {
        console.log(e);
        setDatas(e.response.data.message);
        setIsLoading(false)
      }
    };
    fetchData();
  }, [tokenAja]);
  if (isLoading){
    return (
        <div className="flex flex-wrap justify-center pt-8">
          <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
            <p className="text-xl font-semibold pt-3 text-center">Loading...</p>
          </div>
        </div>
    )
  }
  return (
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-3xl font-bold py-6">Verify Account</p>
        <h1 className="text-center pr-4 capitalize">{datas}</h1>
        <Link href="/auth/login">
          <p className="text-center mt-2 underline text-indigo-800 cursor-pointer">
            Click here to login
          </p>
        </Link>
      </div>

  );
};

export default Verify;
