import React, { useEffect, useState } from "react";
import { getDifferenceInMonths } from "../../../util";
import axios from "axios";
import toast from "react-hot-toast";
import DatePicker from "react-datepicker";
import { Button } from "@components";
import { InputElement } from "../elements";

export const CouponCreate = ({ id, price, minDiscountDuration, discount }) => {
  const [duration, setDuration] = useState(1);
  // increments in month
  const NOW = new Date();
  const [startDate, setStartDate] = useState(
    new Date(NOW.getFullYear(), NOW.getMonth(), 1)
  );
  const [endDate, setEndDate] = useState(
    new Date(NOW.getFullYear(), NOW.getMonth() + 1, 1)
  );
  const [totalPrice, setTotalPrice] = useState(0);
  const [discountUsed, setDiscount] = useState(false);

  useEffect(() => {
    var durationNow = getDifferenceInMonths(startDate, endDate);
    setDuration(durationNow);

    if (durationNow >= minDiscountDuration) {
      setTotalPrice(price * durationNow * ((100 - discount) / 100));
      setDiscount(true);
    } else {
      setTotalPrice(price * durationNow);
      setDiscount(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [startDate, endDate]);

  const handleCreateCoupon = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    var body = {};

    await axios
      .post(``, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        if (response.status == 200) {
          toast.error("Coupon not created!");
        }
      })
      .catch((error) => {
        toast.error(error.message);
      });
  };

  return (
    <form className="px-5 items-center" onSubmit={(e) => handleCreateCoupon(e)}>
      <div className="w-fit">
        <div className="p-5 grid grid-cols-2 rounded-2xl border border-gray-600 gap-y-4 divide-y">
          <div className="col-span-2">
            <InputElement
              name="code"
              placeholder="code"
              type="text"

            />
          </div>
          <div className="col-span-2 pt-4">
            <p>Start Date </p>
            <DatePicker
              className="border rounded-lg w-24 md:w-auto px-2 py-1 shadow-md"
              selected={startDate}
              onChange={(date) => setStartDate(date)}
              selectsStart
              startDate={startDate}
              endDate={endDate}
              dateFormat="dd-MM-yyyy"
              showDayMonthYearPicker
            />
          </div>
          <div className="col-span-2 pt-4">
            <p>End Date</p>
            <DatePicker
              className="border rounded-lg w-24 md:w-auto px-2 py-1 shadow-md"
              selected={endDate}
              onChange={(date) => setEndDate(date)}
              selectsEnd
              startDate={startDate}
              endDate={endDate}
              dateFormat="dd-MM-yyyy"
              showDayMonthYearPicker
            />
          </div>
          <div className="col-span-2 pt-4">
            <InputElement
              name="discount"
              placeholder="discount"
              type="number"
            />
          </div>

          <div className="col-span-2 pt-4">
            <InputElement
              name="Minimum Price"
              placeholder="Minimum Price"
              type="number"
            />
          </div>

          <div className="col-span-2 pt-4">
            <InputElement
              name="Maximum Usages"
              placeholder="Maximum Usages"
              type="number"
            />
          </div>

          <div className="col-span-2 pt-4 ">
            <Button
              type="submit"
              variant="contained"
              className="w-full md:w-48"
              disabled={duration <= 0}
            >
              Create Coupon
            </Button>
          </div>
        </div>
      </div>
    </form>
  );
};
