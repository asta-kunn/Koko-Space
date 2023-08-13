import React, { useEffect, useState } from "react";
import { ChevronLeftIcon } from "@heroicons/react/20/solid";
import { useRouter } from "next/router";
import { ButtonRoom } from "../elements";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";
import toast, { LoaderIcon } from "react-hot-toast";
import { useAuthContext } from "components/context/AuthContext";
import {
  Backdrop,
  Box,
  Button,
  CircularProgress,
  IconButton,
  MenuItem,
  InputAdornment,
  Select,
  TextField,
  FormControl,
  InputLabel,
} from "@mui/material";
import { TrashIcon } from "@heroicons/react/20/solid";
import { LoadingButton } from "@mui/lab";

export const RentForm = ({ detailRoom }) => {
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
  const [isSuccessPayment, setIsSuccessPayment] = useState(true);

  const [coupons, setCoupons] = useState([]);
  const [selectedCoupon, setSelectedCoupon] = useState();
  const { user } = useAuthContext();
  const router = useRouter();
  const [rentStart, setRentStart] = useState(new Date());
  const [rentEnd, setRentEnd] = useState(new Date());
  const [duration, setDuration] = useState(1);
  const [totalPrice, setTotalPrice] = useState(detailRoom.price);
  const [inputPeopleJoin, setInputPoepleJoin] = useState([
    { name: "", statusType: "" },
  ]);
  const [maxInputPeopleJoin, setMaxInputPeopleJoin] = useState(false);
  const [inputCountPeopleJoin, setInputCountPeopleJoin] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const [isDisabled, setIsDisabled] = useState(false);
  const [errorDate, setErrorDate] = useState(false);
  const [errorAttendeeList, setErrorAttendeeList] = useState(false);

  const handleInputChange = (index, value) => {
    const updatedInputs = [...inputPeopleJoin];
    updatedInputs[index] = {
      ...updatedInputs[index],
      name: value,
    };
    setInputPoepleJoin(updatedInputs);
  };

  const handleSelect = (event) => {
    setSelectedCoupon(event.target.value);
  };

  const handleSelectChange = (index, event) => {
    const newSelectedVal = [...inputPeopleJoin];
    newSelectedVal[index] = {
      ...newSelectedVal[index],
      statusType: event.target.value,
    };
    setInputPoepleJoin(newSelectedVal);
  };

  const handleAddInput = () => {
    const updatedInputs = [...inputPeopleJoin, { name: "", statusType: "" }];
    setInputPoepleJoin(updatedInputs);
    setInputCountPeopleJoin(inputCountPeopleJoin + 1);
  };

  const handleRemoveInput = (index) => {
    const updatedInputs = [...inputPeopleJoin];
    updatedInputs.splice(index, 1);
    setInputPoepleJoin(updatedInputs);
    setInputCountPeopleJoin(inputCountPeopleJoin - 1);
  };

  // Number Format - Bisa Dijadikan Global Helper
  const formatMoney = (amount) => {
    return amount.toLocaleString("id-ID", {
      style: "currency",
      currency: "IDR",
      minimumFractionDigits: 0,
    });
  };

  const priceIdr = formatMoney(detailRoom.price);

  useEffect(() => {
    getDefaultRentStart();
    axios
      .get(`/api/pembayaran/coupon?id=${user.id}`)
      .then((res) => setCoupons(res.data));
  }, []);

  const getDefaultRentStart = () => {
    const dateString = localStorage.getItem("rentStart");
    const parsedDate = new Date(dateString);

    if (!isNaN(parsedDate)) {
      setRentStart(parsedDate);
    }
  };

  useEffect(() => {
    const diffTime = rentEnd - rentStart;

    if (diffTime < 0) {
      setTotalPrice(0);
      setDuration("Date Is Not Valid");
      setErrorDate(true);
      setIsDisabled(true);
    } else {
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      setDuration(diffDays);
      setErrorDate(false);
      setTotalPrice(detailRoom.price * diffDays);
    }
  }, [rentStart, rentEnd]);

  // Handle Input People Join Room
  useEffect(() => {
    if (
      (inputPeopleJoin[0]?.name == "" &&
        inputPeopleJoin[0]?.statusType == "") ||
      inputPeopleJoin.length === 0
    ) {
      setErrorAttendeeList(true);
      setIsDisabled(true);
    } else {
      setErrorAttendeeList(false);
      setIsDisabled(false);
    }
  }, [inputPeopleJoin]);

  //  Handle Max Capacity Meeting Room
  useEffect(() => {
    if (inputCountPeopleJoin === detailRoom.capacity) {
      setMaxInputPeopleJoin(true);
    } else {
      setMaxInputPeopleJoin(false);
    }
  }, [inputCountPeopleJoin]);

  const handleCreateRent = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    const token = localStorage.getItem("token") ?? "";
    let body = {
      meetingRoomId: detailRoom.id,
      rentStart: rentStart,
      rentEnd: rentEnd,
      duration: duration,
      cost: totalPrice,
      attendeeStatusList: inputPeopleJoin,
    };
    try {
      const resPay = await axios.post(
        "/api/pembayaran",
        {
          rentalName: detailRoom.name,
          amount: detailRoom.price,
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

    const res = await axios.post(
      `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/create`,
      body,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (res.status === 200) {
      setIsLoading(false);
      toast.success("Rent Is Created Successfully.");
      router.push(`/roomrent/me`);
    } else {
      setIsLoading(false);
    }
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
              <span className="text-xl uppercase">{detailRoom.name}</span> -{" "}
              <strong className="text-xl">{priceIdr} </strong>/ day
            </div>
            <div className="grid grid-cols-2 gap-3 pt-2 pb-2">
              <div>
                <strong>Check-in</strong>
                <p>09.00 AM</p>
              </div>
              <div>
                <strong>Check-out</strong>
                <p>09.00 PM</p>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="mt-2">
                <strong>Rent Start</strong>
                <DatePicker
                  selected={rentStart}
                  portalId="root-portal"
                  className="w-full p-2 cursor-pointer rounded focus:outline-none focus:ring focus:ring-violet-300"
                  onChange={(date) => setRentStart(date)}
                  dateFormat="dd/MM/yyyy"
                />
              </div>
              <div className="mt-2">
                <strong>Rent End</strong>
                <DatePicker
                  selected={rentEnd}
                  portalId="root-portal"
                  className="w-full p-2 cursor-pointer rounded focus:outline-none focus:ring focus:ring-violet-300"
                  onChange={(date) => setRentEnd(date)}
                  dateFormat="dd/MM/yyyy"
                  minDate={rentStart}
                />
              </div>
            </div>
            <div className="py-3">
              <TextField
                label="Duration Rent"
                id="durationRent"
                value={duration}
                sx={{ width: "50%" }}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">Day(s)</InputAdornment>
                  ),
                }}
              />
              {errorDate && (
                <p className="text-xs capitalize text-red-400 pt-1">
                  Please Give Valid Date For Rent Room
                </p>
              )}
            </div>
            <div className="flex flex-col space-y-3 my-3">
              <div className="textInput grid grid-cols-2 gap-3">
                {inputPeopleJoin.map((input, index) => (
                  <div key={index} className="grid grid-cols-8 gap-3">
                    <FormControl fullWidth className="col-start-1 col-span-5">
                      <TextField
                        label={`Name People Join The Meeting Room ${index + 1}`}
                        id="peopleJoin"
                        className="mt-3"
                        onChange={(e) =>
                          handleInputChange(index, e.target.value)
                        }
                        key={index}
                        value={inputPeopleJoin.name}
                        required
                      />
                    </FormControl>
                    <FormControl fullWidth className="col-span-2">
                      <InputLabel id="statusPeople">Status</InputLabel>
                      <Select
                        labelId="statusPeople"
                        id="statusPeople"
                        className="mt-3"
                        value={inputPeopleJoin.statusType}
                        label="Status"
                        name="statusPeople"
                        onChange={(e) => {
                          handleSelectChange(index, e);
                        }}
                      >
                        <MenuItem value="ABSENT">ABSENT</MenuItem>
                        <MenuItem value="EXCUSED">EXCUSED</MenuItem>
                        <MenuItem value="LATE">LATE</MenuItem>
                        <MenuItem value="PRESENT">PRESENT</MenuItem>
                      </Select>
                    </FormControl>
                    <div className="flex items-center">
                      <IconButton onClick={() => handleRemoveInput(index)}>
                        <TrashIcon color="red" width={25} height={25} />
                      </IconButton>
                    </div>
                  </div>
                ))}
              </div>
              <div className="buttonAddPeple">
                <Button
                  disabled={maxInputPeopleJoin}
                  onClick={handleAddInput}
                  sx={{
                    width: "20%",
                    fontSize: "10px",
                    textTransform: "capitalize",
                  }}
                  variant="outlined"
                  className={`py-2 ${maxInputPeopleJoin
                      ? "disabled:border-[1px] disabled:border-rose-600 disabled:cursor-not-allowed disabled:text-red-400"
                      : ""
                    }`}
                >
                  Add People Who's Join The Meeting Room?
                </Button>

                {errorAttendeeList && (
                  <p className="text-sm capitalize text-red-400 pt-1">
                    Invite People To Join The Meeting Room
                  </p>
                )}
                {maxInputPeopleJoin && (
                  <p className="text-sm capitalize text-red-400 pt-1">
                    capacity for meeting room has been reached
                  </p>
                )}
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
                <ButtonRoom
                  type="submit"
                  variant="contained"
                  className={`w-full md:w-48 ${isDisabled
                      ? "disabled:bg-blue-300 disabled:cursor-not-allowed"
                      : ""
                    }`}
                  disabled={isDisabled}
                >
                  Rent
                </ButtonRoom>
              )}
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};
