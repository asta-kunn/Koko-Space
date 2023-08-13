import React, { useEffect, useState } from "react";
import { ChevronLeftIcon } from "@heroicons/react/20/solid";
import { useRouter } from "next/router";
import { Button } from "../elements";
import DatePicker from "react-datepicker";
import { Select, MenuItem } from "@mui/material";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";
import toast from "react-hot-toast";
import { useAuthContext } from "components/context/AuthContext";

export const OrderForm = ({ id, bundlePrice, duration, rentalName }) => {
  const router = useRouter();
  const { user } = useAuthContext();

  const NOW = new Date();
  // yg di set enddatenya aja

  const [isSuccessBooking, setIsSuccessBooking] = useState(false);
  const [coupons, setCoupons] = useState([]);
  const [selectedCoupon, setSelectedCoupon] = useState();

  const [startDate, setStartDate] = useState(
    new Date(NOW.getFullYear(), NOW.getMonth(), 1)
  );
  const [endDate, setEndDate] = useState(
    new Date(NOW.getFullYear(), NOW.getMonth() + duration, 1)
  );

  useEffect(() => {
    setEndDate(
      new Date(startDate.getFullYear(), startDate.getMonth() + duration, 1)
    );
    axios
      .get(`/api/pembayaran/coupon?id=${user.id}`)
      .then((res) => setCoupons(res.data));
  }, [startDate]);

  const handleSelect = (event) => {
    setSelectedCoupon(event.target.value);
  };
  const handleCreateRent = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    var body = {
      bundleId: id,
      checkInDate: startDate.toISOString().slice(0, 10),
      checkOutDate: endDate.toISOString().slice(0, 10),
    };
    try {
      const res = await axios.post(
        "/api/pembayaran",
        {
          rentalName: rentalName,
          amount: bundlePrice,
          kuponId: selectedCoupon ? selectedCoupon.id : null,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
    } catch (err) {
      toast.error(err.response.data.message);
      return;
    }

    // bayar dlu
    // await axios.post()
    await axios
      .post(`/api/kost/order`, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        if (response.status == 201) {
          setIsSuccessBooking(true);
          toast.success("Order is made successfully.");
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
                <strong className="text-xl"> Rp{bundlePrice} </strong>month
              </p>
              <div className="col-span-2 md:col-span-1 pt-4">
                <strong>Check-in </strong>
                <DatePicker
                  className="border w-24 md:w-auto rounded-lg px-2 py-1 shadow-md"
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
                  className="border rounded-lg w-24 md:w-auto px-2 py-1 shadow-md cursor-not-allowed"
                  selected={endDate}
                  disabled
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
                      bundlePrice >= coupon.minPrice && (
                        <MenuItem value={coupon} key={index}>
                          {coupon.code} - Rp{coupon.discount}
                        </MenuItem>
                      )
                  )}
                </Select>
              </div>
              <div className="pt-4">
                <strong>Total cost</strong>
              </div>
              <p className="flex items-end font-bold">Rp{bundlePrice}</p>

              <div className="col-span-2 pt-4 ">
                <Button
                  type="submit"
                  variant="contained"
                  className="w-full md:w-48"
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
