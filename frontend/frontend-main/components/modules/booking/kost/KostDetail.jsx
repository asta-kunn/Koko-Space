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
import { TextField, Dialog, DialogTitle } from "@mui/material";
import { IMAGE_PLACEHOLDER } from "@constants";
import { BookingForm } from ".";

export const KostDetail = ({
  id,
  name,
  type,
  roomNumber,
  price,
  images,
  city,
  country,
  address,
  facilities,
  stock,
  isAvailable,
  minDiscountDuration,
  discount,
}) => {
  const router = useRouter();
  const slider = useRef();
  const [duration, setDuration] = useState(1);
  const [totalPrice, setTotalPrice] = useState(price);
  const [discountUsed, setDiscount] = useState(false);

  const [open, setOpen] = useState(false);
  const handleClose = () => {
    setOpen(false);
  };

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };
  useEffect(() => {
    if (duration >= minDiscountDuration) {
      setTotalPrice(price * duration * ((100 - discount) / 100));
      setDiscount(true);
    } else {
      setTotalPrice(price * duration);
      setDiscount(false);
    }
  }, [duration]);

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
              alt={"empty"}
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
      <div className="flex flex-col md:flex-row pt-10 md:gap-x-20">
        <div className="py-5 w-full md:w-2/3 flex flex-col space-y-5">
          <div>
            <p className="text-3xl font-bold">{name}</p>
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
          <p>{stock}</p>
        </div>

        <div className="w-full md:w-1/3">
          <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y sticky top-24">
            <p className="col-span-2">
              <strong className="text-xl"> Rp{price} </strong>month
              <br />
              <span className="text-sm text-red-600">
                * Get {discount}% discount for a minimum duration of{" "}
                {minDiscountDuration} months!
              </span>
            </p>

            <div className="col-span-2 pt-4">
              <TextField
                type="number"
                value={duration}
                onChange={(e) => {
                  setDuration(e.target.value);
                }}
                variant="outlined"
                label="months"
              />
            </div>

            <div className="">
              <strong>Estimated cost</strong>
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

            <div className="col-span-2 pt-4">
              <Button
                disabled={!isAvailable}
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
            Reserve Kost Room
          </DialogTitle>
          <BookingForm
            id={id}
            price={price}
            minDiscountDuration={minDiscountDuration}
            discount={discount}
            kostName={name}
          />
          <p className="text-sm text-red-600 pt-4">
            *Click outside the box to close this dialog.
          </p>
        </div>
      </Dialog>
    </div>
  );
};
