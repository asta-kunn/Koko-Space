import React, { useRef } from "react";
import axios from "axios";
import { useRouter } from "next/router";
import toast, { Toaster } from "react-hot-toast";

export const CardsTopup = ({
  id,
  user,
  amount,
  createdAt,
  method,
  detail,
  setIsTopupVerified,
}) => {
  const router = useRouter();

  const date = () => {
    const dateValue = new Date(createdAt);

    const month = (dateValue.getUTCMonth() + 1).toString().padStart(2, "0"); // Months are zero-based
    const day = dateValue.getUTCDate().toString().padStart(2, "0");
    const year = dateValue.getUTCFullYear().toString();

    const mdyFormat = `${month}-${day}-${year}`;

    return mdyFormat;
  };

  const handleAccept = async () => {
    const postVerifyTopup = axios
      .post(
        "/api/wallet/verify-topup",
        { WalletHistoryId: id, accepted: true },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      .then((response) => {
        if (response.status == 200) {
          toast.success("Successfully verify topup");
        }
      })
      .catch((error) => {
        toast.error(error?.message);
      })
      .finally(() => {
        router.push(`/adminwallet/adminmenu`);
      });
  };

  const handleReject = async () => {
    const postVerifyTopup = axios
      .post(
        "/api/wallet/verify-topup",
        { WalletHistoryId: id, accepted: false },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      )
      .then((response) => {
        if (response.status == 200) {
          toast.success("Successfully verify topup");
        }
      })
      .catch((error) => {
        toast.error(error?.message);
      })
      .finally(() => {
        router.push(`/adminwallet/adminmenu`);
      });
  };

  return (
    <div className=" rounded-xl shadow-md max-w-lg">
      <div className="grid grid-cols-2 gap-2 p-3 m-3">
        <div>
          <p>User: {user} </p>
          <p>Wallet Id: {id} </p>
        </div>
        <div className="text-right">
          <p>Amount: {amount} </p>
          <p>Method: {method}</p>
          <p> {date()} </p>
        </div>
        <div lassName="col-span-2">{detail}</div>
      </div>
      <div className="flex justify-center">
        <div className="grid grid-cols-2 gap-10 w-4/5">
          <button
            className="bg-green-500 hover:bg-green-600 text-white text-center font-bold py-2 px-4 my-4 rounded-xl"
            onClick={handleAccept}
          >
            Accept
          </button>
          <button
            className="bg-red-500 hover:bg-red-600 text-white text-center font-bold py-2 px-4 my-4 rounded-xl"
            onClick={handleReject}
          >
            Reject
          </button>
        </div>
      </div>
    </div>
  );
};
