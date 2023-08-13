import { useRouter } from "next/router";
import React, { use, useEffect, useRef, useState } from "react";
import {
  MapPinIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
} from "@heroicons/react/20/solid";
import Slider from "react-slick";
import Image from "next/image";
import { ButtonRoom } from "./elements";
import { Backdrop, Box, CircularProgress } from "@mui/material";

export const SpaceDetail = ({ detailRoom }) => {
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
  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  return (
    <div className="w-full pt-10">
      <div className="relative mx-auto md:w-3/4">
        <Image
          key={detailRoom.id}
          src={detailRoom.image}
          width={200}
          height={100}
          style={{ objectFit: "cover" }}
          alt={detailRoom.image}
        />
        <div></div>
      </div>

      <div>
        <div className="flex flex-col md:flex-row pt-10 md:gap-x-20">
          <div className="py-5 w-full md:w-2/3 flex flex-col space-y-5">
            <div>
              <p className="text-3xl font-bold">{detailRoom.id}</p>
              <p className="flex flex-row items-center gap-x-1">DESCRIPTION</p>
              <p>{detailRoom.description}</p>
            </div>

            <div className="">
              <p className="text-xl font-bold">Jenis</p>
              <p className="text-lg">{detailRoom.type}</p>
            </div>
          </div>

          <div className="w-full md:w-1/3">
            <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y">
              <p className="col-span-2">
                <strong className="text-xl">
                  Rp. {detailRoom.hourlyPrice}
                </strong>{" "}
                :Hourly
              </p>
              <p className="col-span-2">
                <strong className="text-xl">Rp. {detailRoom.dailyPrice}</strong>{" "}
                :Daily
              </p>
              <div className="col-span-2 pt-4">
                <ButtonRoom
                  onClick={() => {
                    if (detailRoom.type === "COWORKING") {
                      router.push(
                        `/coworkingspace/Coworking/rent/${detailRoom.id}`
                      );
                    } else {
                      router.push(
                        `/coworkingspace/Personal/rent/${detailRoom.id}`
                      );
                    }
                  }}
                  className="pt-5 hover:cursor-pointer w-full"
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
