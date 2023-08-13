import React from "react";
import { CouponCreate } from "../../components/modules/auth/coupon/CouponCreate";
import { useAuthContext } from "components/context/AuthContext";
import { useState } from "react";

const CreateCoupon = () => {
    const { user } = useAuthContext();

    if (user?.role != "PENGELOLA") {
        return (
            <div className="flex flex-col items-center justify-center h-screen">
                <p className="text-3xl font-bold py-6">Create Coupon Menu</p>
                <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
            </div>
        );
    }

  return (
      <div className="flex justify-center">
          <div className="w-full">
            <h1 className="text-center text-3xl py-4">Create Coupon</h1>
            <div className="flex justify-center">
                <CouponCreate
                  id="1"
                  price="100"
                  minDiscountDuration="1"
                  discount="10"
                />
            </div>
      </div>
    </div>
  );
};

export default CreateCoupon;
