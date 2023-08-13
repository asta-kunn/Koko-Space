import React from "react";
import axios from "axios";
import { useAuthContext } from "../../../context/AuthContext";
import toast from "react-hot-toast";

export const CouponCard = ({
  id,
  code,
  discount,
  startDate,
  endDate,
  minPrice,
  maxUsage,
  stat,
}) => {
  const { user } = useAuthContext();
  const handleDelete = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";

    var body = {
      userId: user.id,
      couponId: id,
    };
    console.log(body);
    try {
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_AUTH_API_URL}/coupon/delete/${id}`,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        toast.success("Coupon deleted!");
        window.location.reload();
      }
    } catch (e) {
      toast.error(e.response.data.message);
    }
  };

  return (
    <div className="lg:w-1/6 w-3/4 mx-auto px-3 py-5 border-2 mb-2 border-black rounded-md">
      <h5>Kode : {code}</h5>
      <h5>Diskon : {discount}</h5>
      <h5>Tanggal Mulai : {startDate}</h5>
      <h5>Tanggal Berakhir : {endDate}</h5>
      <h5>Minimal Harga : {minPrice}</h5>
      <h5>Maksimal Pengguna : {maxUsage}</h5>
      <h5>Status Kupon : {stat}</h5>
      <button
        onClick={handleDelete}
        className="mx-auto rounded-md border-2 block py-1 px-2 mt-[1.25rem] bg-red-600 text-white border-red-900 hover:bg-red-900 hover:text-white"
      >
        Delete
      </button>
    </div>
  );
};
