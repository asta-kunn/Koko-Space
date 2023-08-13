import { CardsTransaction } from ".";
import { useState, useEffect } from "react";
import axios from "axios";
import toast, { Toast } from "react-hot-toast";
import { TRANSACTIONS } from "constants/wallet/transactions";

import React from "react";

export const WalletHistory = () => {
  const [walletData, setWalletData] = useState([]);

  useEffect(() => {
    const getWalletData = async () => {
      axios
        .get("/api/wallet/get-wallet-history", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((response) => {
          if (response.status == 200) {
            setWalletData(response.data);
          }
        })
        .catch((error) => {
          toast.error(error?.message);
        });
    };

    getWalletData();
  });

  return (
    <div className="flex justify-center ">
      <div className="w-full sm:w-3/5 md:w-2/5 content-center">
        <div className="grid grid-cols-1 py-10">
          <p className="text-2xl text-center font-bold">Wallet History</p>
          <div className="flex justify-center grid grid-cols-1 content-center">
            {walletData.map((transaction) => {
              return <CardsTransaction key={transaction.id} {...transaction} />;
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
