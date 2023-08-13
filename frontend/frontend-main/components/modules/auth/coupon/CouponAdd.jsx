import React from "react";

export const CouponAdd = () => {
  const handleAddCoupon = () => {
    window.location.href = "/coupon/CreateCoupon";
  };

  return (
    <button
      onClick={handleAddCoupon}
      className="mx-auto rounded-md border-2 block py-1 px-2 mt-[1.25rem] bg-indigo-600 text-white border-indigo-900 hover:bg-transparent hover:text-black"
    >
      Create Coupon
    </button>
  );
};
