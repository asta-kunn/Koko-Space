import { useAuthContext } from "components/context/AuthContext";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { COUPON_DATA } from "@constants";
import { CouponCard } from "../../components/modules/auth/coupon/CouponCard";
import { console } from "next/dist/compiled/@edge-runtime/primitives/console";
import { CouponAdd } from "../../components/modules/auth/coupon/CouponAdd";

const MyCoupon = () => {
  const { user } = useAuthContext();
  const [curUser, setCurUser] = useState(user);
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);


  useEffect(() => {
    setCurUser(user);
  }, [user]);

  useEffect(() => {
    setIsLoading(false)
    const fetchData = async () => {
      const token = localStorage.getItem("token") ?? "";
      try {
        console.log(user.id);
        if (user.id === undefined) {
          setIsLoading(true);
          return;
        }
        const res = await axios.get(
          `${process.env.NEXT_PUBLIC_AUTH_API_URL}/coupon/get/user/${user.id}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (res.status === 200) {

          var coupons = [];

          for (var i = 0; i < res.data.length; i++) {
            coupons.push(res.data[i]);
          }
          setData(coupons);
          setIsLoading(false)
          console.log(data);
        }
      } catch (e) {
        console.log(e);
        setIsLoading(false)
      }
    };
    fetchData();
  }, [curUser]);

  if (!isLoading) {
    return (
        <>
          <div>
            <h1 className="text-center text-3xl py-4">My Coupons</h1>
            { user?.role != "PENGELOLA" ? (
                <div className="flex flex-col items-center justify-center h-screen">
                  <p className="text-3xl font-bold py-6">My Coupon Menu</p>
                  <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
                </div>
            ) : (
                data.map((coupons) => {
                  return (
                      <CouponCard
                          id={coupons.id}
                          code={coupons.code}
                          discount={coupons.discount}
                          startDate={coupons.startDate}
                          endDate={coupons.endDate}
                          minPrice={coupons.minPrice}
                          maxUsage={coupons.maxUsage}
                          stat={coupons.status}
                      />
                  );
                }))}
            <div className="mb-4">
              <CouponAdd/>
            </div>
          </div>
        </>
    );
  } else {
    return (
        <div className="flex flex-wrap justify-center pt-8">
          <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
            <p className="text-xl font-semibold pt-3 text-center">Loading...</p>
          </div>
        </div>
    );
  }
};

export default MyCoupon;
