import { useRouter } from "next/router";
import React, { useEffect, useRef, useState } from "react";
import {
  MapPinIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
} from "@heroicons/react/20/solid";
import Slider from "react-slick";
import Image from "next/image";
import { Button } from "../elements";
import { IMAGE_PLACEHOLDER } from "@constants";
import { Dialog, DialogTitle } from "@mui/material";
import { OrderForm } from ".";
import axios from "axios";
import { toast } from "react-hot-toast";

export const BundleDetail = ({
  id,
  name: bundleName,
  kostRoom: {
    name,
    type,
    price,
    images,
    city,
    country,
    address,
    facilities,
    stock,
  },
  coworkingId,
  isAvailable,
  bundlePrice,
  duration,
}) => {
  const [open, setOpen] = useState(false);

  const [coworkingData, setCoworkingData] = useState();
  const handleClose = () => {
    setOpen(false);
  };

  const router = useRouter();
  const slider = useRef();

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  useEffect(() => {
    axios
      .get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/id/${coworkingId}`
      )
      .then((res) => {
        setCoworkingData(res.data);
      })
      .catch((err) => {
        toast.error(err?.message);
      });
  }, []);

  return (
    <div className="w-full pt-10">
      <div className="relative mx-auto md:w-3/4">
        <Slider ref={slider} id={id} {...settings}>
          {images.length > 0 ? (
            images.slice(0, 5).map((imgUrl) => {
              return (
                <Image
                  key={id}
                  src={imgUrl}
                  width={200}
                  height={100}
                  style={{ objectFit: "cover" }}
                  alt={imgUrl}
                />
              );
            })
          ) : (
            <Image
              key={id}
              src={IMAGE_PLACEHOLDER}
              width={200}
              height={100}
              style={{ objectFit: "cover" }}
              alt={"placeholder"}
            />
          )}
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
            className="absolute -right-4 top-[45%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickNext();
            }}
          >
            <ChevronRightIcon width={20} height={20} />
          </button>
        </div>
      </div>
      <div className="flex flex-col md:flex-row pt-10 md:gap-x-20 ">
        <div className="py-5 w-full md:w-2/3 flex flex-col space-y-5 divide-y-2 divide-gray-400">
          <p className="font-bold text-3xl">{bundleName}</p>

          <div className="space-y-2 flex flex-col py-4">
            <div>
              <p className="text-2xl font-bold">{name}</p>
              <p className="text-md">
                Type: <span className="font-bold">{type}</span>
              </p>

              <p className="flex flex-row items-center gap-x-1">
                <MapPinIcon width={20} height={20} color="navy" /> {address}
              </p>
              <p>
                {city}, {country}
              </p>
            </div>
            <div>
              <p className="text-xl font-bold">Facilities</p>
              <ul className="border border-gray-700 rounded-2xl p-5">
                {facilities.map((facility, idx) => {
                  return <li key={idx}>&bull; {facility}</li>;
                })}
              </ul>
            </div>
          </div>

          <div className="py-4">
            <p className="font-bold text-2xl">COWORKING SPACE</p>
            <p className="text-md">
              Type: <span className="font-bold">{coworkingData?.type}</span>
            </p>
            <p>{coworkingData?.description}</p>
          </div>
        </div>

        <div className="w-full md:w-1/3 ">
          <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y sticky top-24">
            <p className="col-span-2">
              <strong className="text-xl"> Rp{bundlePrice} </strong> for{" "}
              {duration} month
            </p>

            <div className="col-span-2 pt-4">
              <p>
                All in for {duration} months! Much cheaper than booking
                individually.
              </p>
            </div>

            <div className="col-span-2 pt-4">
              <Button
                disabled={!isAvailable || coworkingData?.isAvailable}
                onClick={() => {
                  setOpen(true);
                }}
                className="w-full"
              >
                Reserve
              </Button>
            </div>
          </div>
        </div>
      </div>
      <Dialog open={open} onClose={handleClose}>
        <div className="p-10">
          <DialogTitle className="font-bold text-lg">
            Reserve Bundle
          </DialogTitle>
          <OrderForm
            id={id}
            bundlePrice={bundlePrice}
            duration={duration}
            rentalName={name}
          />
          <p className="text-sm text-red-600 pt-4">
            *Click outside the box to close this dialog.
          </p>
        </div>
      </Dialog>
    </div>
  );
};
