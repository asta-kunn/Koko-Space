import React, { useState } from "react";
import { useRouter } from "next/router";
import axios from "axios";
import { toast } from "react-hot-toast";

export const TopupForm = () => {
  const router = useRouter();

  //form input
  const [amount, setAmount] = useState("");
  const [method, setMethod] = useState("");
  const [detail, setDetail] = useState("");

  //handle form change
  const handleAmountChange = (event) => {
    setAmount(event.target.value);
  };
  const handleMethodChange = (event) => {
    setMethod(event.target.value);
  };
  const handleDetailChange = (event) => {
    setDetail(event.target.value);
  };

  //submit form
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    setLoading(true);
    console.log("testingg");

    var body = {
      amount: amount,
      method: method,
      detail: detail,
      name: name,
    };

    await axios
      .post(`/api/wallet/topup`, body, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then((response) => {
        console.log(response);
        if (response.status == 200) {
          toast.success("Successfully request top up");
          // redirect to wallet page
          router.push(`/wallet`);
        }
      })
      .catch((error) => {
        toast.error(error?.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <div className="flex justify-center">
      <div className="max-w-2xl">
        <h1 className="text-3xl text-center font-bold pb-3 text-indigo-800">
          Top Up Form
        </h1>
        <div className="flex justify-between my-2 mx-2 items-center">
          <label htmlFor="amount" className="pr-4 capitalize">
            Amount
          </label>
          <input
            type="number"
            pattern="[0-9]*"
            id="amount"
            name="amount"
            placeholder="amount"
            className="border-2 px-1 rounded-md w-60"
            value={amount}
            onChange={handleAmountChange}
          ></input>
        </div>
        <div className="flex justify-between my-2 mx-2 items-center">
          <label htmlFor="method" className="pr-4 capitalize">
            Method
          </label>
          <select
            name="method"
            className="border-2 px-1 rounded-md w-60"
            value={method}
            onChange={handleMethodChange}
          >
            <option value="" disabled selected>
              Choose Payment Method
            </option>
            <option value="PayPal">PayPal</option>
            <option value="OVO">OVO</option>
            <option value="GOPAY">GOPAY</option>
          </select>
        </div>
        <div className="flex justify-between my-2 mx-2 items-center">
          <label htmlFor="detail" className="pr-4 capitalize">
            Detail
          </label>
          <input
            type="text"
            id="detail"
            name="detail"
            placeholder="Insert details"
            className="border-2 px-1 rounded-md w-60"
            value={detail}
            onChange={handleDetailChange}
          ></input>
        </div>
        <button
          type="submit"
          onClick={handleSubmit}
          className="mx-auto rounded-md border-2 block py-1 px-2 mt-4 bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
        >
          Top Up
        </button>
      </div>
    </div>
  );
};
