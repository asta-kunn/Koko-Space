import { CardsTopup } from ".";
import { useAuthContext } from "components/context/AuthContext";
import { TOPUP_REQUESTS } from "constants/wallet/topuprequests";
import axios from "axios";
import { toast } from "react-hot-toast";

import React, { useState, useEffect } from "react";

export const TopupList = () => {
  const [topupData, setTopupData] = useState([]);
  const [isTopupVerified, setIsTopupVerified] = useState(false);

  useEffect(() => {
    const getTopupData = async () => {
      axios
        .get("/api/wallet/get-all-topup-request", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((response) => {
          if (response.status == 200) {
            setTopupData(response.data);
          }
        })
        .catch((error) => {
          toast.error(error?.message);
        })
        .finally(() => {
          setIsTopupVerified(false);
        });
    };

    getTopupData();
  }, [isTopupVerified]);

  return (
    <div className="flex justify-center ">
      <div className="w-full content-center">
        <div className="grid grid-cols-1 py-5">
          <p className="text-2xl text-center font-bold py-10">
            Top Ups To Be Verified
          </p>
          <div className="flex justify-center grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-12 justify-items-center ">
            {topupData.map((topupData) => {
              return (
                <CardsTopup
                  key={topupData.id}
                  setIsTopupVerified={setIsTopupVerified}
                  {...topupData}
                />
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
