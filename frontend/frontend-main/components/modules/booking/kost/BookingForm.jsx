import React, { useEffect, useState } from "react";

import { Button } from "../elements";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { getDifferenceInMonths } from "components/util";
import axios from "axios";
import toast from "react-hot-toast";
import { useAuthContext } from "components/context/AuthContext";
import { Select, MenuItem } from "@mui/material";
export const BookingForm = ({
  id,
  price,
  minDiscountDuration,
  discount,
  kostName,
}) => {
  const [duration, setDuration] = useState(1);

  const [isSuccessBooking, setIsSuccessBooking] = useState(false);
  const [coupons, setCoupons] = useState([]);
  const [selectedCoupon, setSelectedCoupon] = useState();

  const { user } = useAuthContext();
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

    axios
      .get(`/api/pembayaran/coupon?id=${user.id}`)
      .then((res) => setCoupons(res.data));

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [startDate, endDate, totalPrice]);

  const handleSelect = (event) => {
    setSelectedCoupon(event.target.value);
  };

  const handleCreateRent = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    var body = {
      kostRoomId: id,
      checkInDate: startDate.toISOString().slice(0, 10),
      checkOutDate: endDate.toISOString().slice(0, 10),
      duration: duration,
      totalPrice: totalPrice,
    };
    try {
      const resPay = await axios.post(
        "/api/pembayaran",
        {
          rentalName: kostName,
          amount: totalPrice,
          kuponId: selectedCoupon ? selectedCoupon.id : null,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
    } catch (err) {
      console.log(err.response.data.message);
      toast.error(err.response.data.message);
      return;
    }

    await axios
      .post(`/api/kost/rent`, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        if (response.status == 201) {
          setIsSuccessBooking(true);
          toast.success("Rent is made successfully.");
          // redirect ke kost rent blabla
        }
      })
      .catch((error) => {
        setIsSuccessBooking(false);
        toast.error(error.message);
      });
  };

  return (
    <div>
      <div>
        <p className="flex flex-row items-center gap-x-1 text-2xl font-bold pb-5">
          Confirm and Pay
        </p>
        <form onSubmit={(e) => handleCreateRent(e)}>
          <div className="w-full">
            <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y">
              <p className="col-span-2">
                <strong className="text-xl"> Rp{price} </strong>month
                <br />
                <span className="text-sm text-red-600">
                  * Get {discount}% discount for a minimum duration of{" "}
                  {minDiscountDuration}
                </span>
              </p>
              <div className="col-span-2 md:col-span-1 pt-4">
                <strong>Check-in </strong>
                <DatePicker
                  className="border rounded-lg w-24 md:w-auto px-2 py-1 shadow-md"
                  selected={startDate}
                  onChange={(date) => setStartDate(date)}
                  selectsStart
                  startDate={startDate}
                  endDate={endDate}
                  dateFormat="MM/yyyy"
                  showMonthYearPicker
                />

                <p className="text-xs">*Date starts from 1st of the month</p>
              </div>
              <div className="col-span-2 md:col-span-1 pt-4">
                <strong>Check-out</strong>
                <DatePicker
                  className="border rounded-lg w-24 md:w-auto px-2 py-1 shadow-md"
                  selected={endDate}
                  onChange={(date) => setEndDate(date)}
                  selectsEnd
                  startDate={startDate}
                  endDate={endDate}
                  dateFormat="MM/yyyy"
                  showMonthYearPicker
                />
                <p className="text-xs">*Date starts from 1st of the month</p>
              </div>
              <div className="col-span-2 pt-4">
                <p>
                  Duration:{" "}
                  <span className="border border-black/90 rounded px-2 py-1">
                    {duration}
                  </span>{" "}
                  months
                </p>
              </div>
              <div className="col-span-2 pt-4">
                <p>Kupon</p>{" "}
                <Select
                  labelId="demo-simple-select-label"
                  id="demo-simple-select"
                  label="Coupon"
                  className="w-full"
                  onChange={handleSelect}
                >
                  {coupons.map(
                    (coupon, index) =>
                      totalPrice >= coupon.minPrice && (
                        <MenuItem value={coupon} key={index}>
                          {coupon.code} - Rp{coupon.discount}
                        </MenuItem>
                      )
                  )}
                </Select>
              </div>

              <div className="">
                <strong>Total cost</strong>
                <p>
                  {price} x {duration} months
                </p>
              </div>
              <div className="">
                {discountUsed && (
                  <p className="line-through font-bold text-red-600">
                    Rp{price * duration}
                  </p>
                )}
                <p className="flex items-end font-bold ">Rp{totalPrice}</p>
              </div>
              <p className="flex items-end font-bold">Rp{price * duration}</p>

              <div className="col-span-2 pt-4 ">
                <Button
                  type="submit"
                  variant="contained"
                  className="w-full md:w-48"
                  disabled={duration <= 0}
                >
                  Book
                </Button>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};
