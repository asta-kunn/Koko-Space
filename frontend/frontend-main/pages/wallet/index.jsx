import React, { useEffect } from "react";
import { useRouter } from "next/router";
import { useAuthContext } from "components/context/AuthContext";
import { WalletHistory } from "components/modules/wallet";

const Wallet = () => {
  const { user } = useAuthContext();

  const HistoryButton = () => {
    const router = useRouter();

    const handleClick = () => {
      router.push("/wallet/history");
    };
    return (
      <button
        onClick={handleClick}
        className="bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 h-16 w-32 rounded-xl shadow"
      >
        See Spendings
      </button>
    );
  };
  const TopUpButton = () => {
    const router = useRouter();

    const handleClick = () => {
      router.push("/wallet/topup");
    };
    return (
      <button
        onClick={handleClick}
        className="bg-white hover:bg-gray-100 text-gray-800 font-semibold py-2 px-4 h-16 w-32 rounded-xl shadow"
      >
        Top Up
      </button>
    );
  };

  return (
    <div className="py-7">
      <div className="flex justify-center">
        <div className="grid grid-cols-1">
          <h1 className="text-center text-3xl font-bold">Wallet</h1>
          <div className="text-center p-5 m-5 rounded-3xl shadow-md w-80">
            <p>Saldo</p>
            <p className="text-2xl font-bold">
              Rp {user ? user.saldo : "no user yet"}
            </p>
          </div>
          <div className="flex justify-center">
            <div className="grid grid-cols-2 gap-10 w-4/5">
              <div>{HistoryButton()}</div>
              <div>{TopUpButton()}</div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <WalletHistory />
      </div>
    </div>
  );
};

export default Wallet;
