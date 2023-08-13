import React, { useState, useEffect } from "react";
import { ChevronLeftIcon } from "@heroicons/react/20/solid";
import { useRouter } from "next/router";
import "react-datepicker/dist/react-datepicker.css";
import { SpaceDetail } from ".";
import {
  duration,
  Backdrop,
  Box,
  CircularProgress,
  Select,
  MenuItem,
} from "@mui/material";
import { useAuthContext } from "components/context/AuthContext";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { Button } from "../elements";

export const SpaceForm = ({ detailRoom }) => {
  if (detailRoom.length === 0) {
    return (
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={true}
      >
        <Box sx={{ display: "flex" }}>
          <CircularProgress color="inherit" />
        </Box>
      </Backdrop>
    );
  }

  const [coupons, setCoupons] = useState([]);
  const [selectedCoupon, setSelectedCoupon] = useState();
  const { user } = useAuthContext();
  const router = useRouter();
  const [rentStart, setRentStart] = useState(new Date());
  const [rentEnd, setRentEnd] = useState(new Date());
  const [totalPrice, setTotalPrice] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [duration, setDuration] = useState(2);
  const handleSelect = (event) => {
    setSelectedCoupon(event.target.value);
  };
  const formatMoney = (amount) => {
    return amount.toLocaleString("id-ID", {
      style: "currency",
      currency: "IDR",
      minimumFractionDigits: 0,
    });
  };
  const dailypriceIdr = formatMoney(Math.abs(detailRoom.dailyPrice));
  const hourlyPriceIdr = formatMoney(Math.abs(detailRoom.hourlyPrice));

  useEffect(() => {
    getDefaultRentStart();
    axios
      .get(`/api/pembayaran/coupon?id=${user.id}`)
      .then((res) => setCoupons(res.data));
  }, []);

  const getDefaultRentStart = () => {
    setRentStart(new Date());
  };

  //Untuk durasi
  useEffect(() => {
    let intDuration = parseInt(duration);
    let newRentEnd = new Date(rentStart);
    newRentEnd.setHours(newRentEnd.getHours() + intDuration);

    // Format tanggal dan waktu
    let dateString = newRentEnd.toLocaleString();

    if (intDuration < 12) {
      setRentEnd(dateString);
      setTotalPrice(detailRoom.hourlyPrice * intDuration);
    } else {
      // Logika untuk diffTime > 12 jam
      let newRentEnd = new Date(rentStart);
      newRentEnd.setHours(newRentEnd.getHours() + 24);

      // Format tanggal dan waktu
      let dateString = newRentEnd.toLocaleString();

      setRentEnd(dateString);
      setTotalPrice(detailRoom.dailyPrice);
    }
  }, [rentStart, duration]); // Perubahan, rentEnd tidak perlu di sini karena Anda tidak mengandalkan rentEnd dalam useEffect ini.

  const handleCreateRent = async (event) => {
    event.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    var body = {
      workspaceId: detailRoom.id,
      duration: duration,
    };
    try {
      const resPay = await axios.post(
        "/api/pembayaran",
        {
          rentalName: detailRoom.type + " " + detailRoom.id,
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
      toast.error(err.response.data.message);
    }

    const rest = await axios
      .post(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/space-rent/create`,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((response) => {
        if (response.status == 200) {
          toast.success("Space is made successfully.");
          router.push(`/coworkingspace/inforent`);
        }
      })
      .catch((error) => {
        toast.error(error.message);
      });
  };

  return (
    <div>
      <div>
        <p className="flex flex-row items-center gap-x-1 text-2xl font-bold pb-5">
          <ChevronLeftIcon
            onClick={() => {
              router.back();
            }}
            width={26}
            height={26}
            className="cursor-pointer"
          />
          Confirm and Pay
        </p>
        <form onSubmit={(e) => handleCreateRent(e)}>
          <div className="w-full p-5 rounded-2xl border border-gray-600 gap-y-4 divide-y">
            <div className="my-3">
              <span className="text-xl uppercase">{detailRoom.name}</span>{" "}
              <strong className="text-xl">{hourlyPriceIdr} </strong>: hour
              <br></br>
              <strong className="text-xl">{dailypriceIdr} </strong>: daily
            </div>
            <div>
              <strong>Check-in</strong>
              <p>{rentStart.toLocaleString()}</p>
            </div>
            <div>
              <strong>Check-out</strong>
              <p>{rentEnd.toLocaleString()}</p>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="mt-2">
                <strong>Duration</strong>
              </div>
              <div>
                <select
                  value={duration}
                  onChange={(e) => setDuration(e.target.value)}
                  className="form-select block w-full mt-1"
                >
                  <option value={2}>2 hours</option>
                  <option value={4}>4 hours</option>
                  <option value={6}>6 hours</option>
                  <option value={8}>8 hours</option>
                  <option value={10}>10 hours</option>
                  <option value={24}>Daily</option>
                </select>
              </div>
            </div>
            <div className="pt-4 w-1/2">
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
            <div className="pt-3 pb-3">
              <strong className="uppercase text-xl">Total Cost</strong>
              <p>Rp. {totalPrice.toLocaleString("id-ID")}</p>
            </div>

            <div className="col-span-2 pt-4">
              {isLoading ? (
                <LoadingButton
                  loading
                  loadingPosition="start"
                  startIcon={<LoaderIcon />}
                  variant="outlined"
                >
                  SAVING
                </LoadingButton>
              ) : (
                <Button
                  type="submit"
                  variant="contained"
                  className={`w-full md:w-48 ${isDisabled
                    ? "disabled:bg-blue-300 disabled:cursor-not-allowed"
                    : ""
                    }`}
                  disabled={isDisabled}
                >
                  Rent
                </Button>
              )}
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};
