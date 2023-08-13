import { useRouter } from "next/router";
import React, { use, useEffect, useRef, useState } from "react";
import {
  MapPinIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
} from "@heroicons/react/20/solid";
import Slider from "react-slick";
import Image from "next/image";
import { ButtonRoom } from "../elements";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Backdrop, Box, CircularProgress } from "@mui/material";

export const RoomDetail = ({ detailRoom }) => {
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

  const router = useRouter();
  const slider = useRef();
  const [rentStart, setRentStart] = useState(new Date());
  const [rentEnd, setRentEnd] = useState(new Date());
  const [duration, setDuration] = useState(1);

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  // handle duration with rentStart and rentEnd
  useEffect(() => {
    const diffTime = Math.abs(rentEnd - rentStart);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24) + 1);
    setDuration(diffDays);
  }, [rentStart, rentEnd]);

  return (
    <div className="w-full pt-10">
      <div className="relative mx-auto md:w-3/4">
        <Slider ref={slider} id={detailRoom.id} {...settings}>
          {detailRoom.images.slice(0, 5).map((imgUrl) => {
            return (
              <Image
                key={detailRoom.id}
                src={imgUrl}
                width={200}
                height={100}
                style={{ objectFit: "cover" }}
                alt={imgUrl}
              />
            );
          })}
        </Slider>

        <div>
          <button
            className="absolute -left-4 top-[50%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickPrev();
            }}
          >
            <ChevronLeftIcon width={20} height={20} />
          </button>
          <button
            className="absolute -right-4 top-[50%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickNext();
            }}
          >
            <ChevronRightIcon width={20} height={20} />
          </button>
        </div>
      </div>
      <div>
        <div className="flex flex-col md:flex-row pt-10 md:gap-x-20">
          <div className="py-5 w-full md:w-2/3 flex flex-col space-y-5">
            <div>
              <p className="text-3xl font-bold uppercase">{detailRoom.name}</p>
              <p className="flex flex-row items-center gap-x-1">
                <MapPinIcon width={20} height={20} color="navy" />
                {detailRoom.address}
              </p>
              <p>
                {detailRoom.city}, {detailRoom.country}
              </p>
            </div>

            <div className="">
              <p className="text-xl font-bold">Capacity</p>
              <p className="text-lg">{detailRoom.capacity} people</p>
            </div>
          </div>

          <div className="w-full md:w-1/3">
            <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y">
              <p className="col-span-2">
                <strong className="text-xl">Rp. {detailRoom.price}</strong> /
                day
              </p>
              <div className="col-span-1 ">
                <strong>Check-in</strong>
                <p>09:00 AM</p>
              </div>
              <div className="col-span-1">
                <strong>Check-out</strong>
                <p>09:00 PM</p>
              </div>
              <div className="col-span-2">
                <strong>Rent Start</strong>
                <DatePicker
                  selected={rentStart}
                  onChange={(date) => setRentStart(date)}
                />
              </div>

              <div className="col-span-2">
                <strong>Rent End</strong>
                <DatePicker
                  selected={rentEnd}
                  onChange={(date) => setRentEnd(date)}
                />
              </div>

              <div className="">
                <strong>Estimated Cost</strong>
                <p>Rp. {detailRoom.price * duration}</p>
              </div>

              <p className="flex items-end font-bold">Rp{detailRoom.price}</p>

              <div className="col-span-2 pt-4">
                <ButtonRoom
                  onClick={() => {
                    if (rentStart) {
                      localStorage.setItem("rentStart", rentStart);
                      router.push(`/room/rent/${detailRoom.id}`);
                    } else {
                      console.log("rentStart is empty");
                    }
                  }}
                >
                  Book Now
                </ButtonRoom>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
