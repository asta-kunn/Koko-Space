import { useRouter } from "next/router";
import React, { useRef, useState } from "react";
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

export const AdminDetailCoworking = ({
  id,
  name,
  price,
  images = [],
  city,
  country,
  address,
  capacity,
  availability,
  onDelete,
}) => {
  const router = useRouter();
  const slider = useRef();
  const [startDate, setStartDate] = useState(new Date());
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
        <Slider ref={slider} id={id} {...settings}>
          {images.slice(0, 5).map((imgUrl) => {
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
          })}
        </Slider>

        <div></div>
      </div>

      <div>
        <div className="flex flex-col md:flex-row pt-10 md:gap-x-20">
          <div className="py-5 w-full md:w-2/3 flex flex-col space-y-5">
            <div>
              <p className="text-3xl font-bold">{name}</p>
              <p className="flex flex-row items-center gap-x-1">
                <MapPinIcon width={20} height={20} color="navy" />
                {address}
              </p>
              <p>
                {city}, {country}
              </p>
            </div>

            <div className="">
              <p className="text-xl font-bold">Capacity</p>
              <p className="text-lg">{capacity} people</p>
            </div>
          </div>

          <div className="w-full md:w-1/3">
            <div className="p-5 grid grid-cols-2 place-items-stretch rounded-2xl border border-gray-600 gap-y-4 divide-y">
              <p className="col-span-2">
                <strong className="text-xl">Rp. {price}</strong> / day
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
                <DatePicker
                  selected={startDate}
                  onChange={(date) => setStartDate(date)}
                />
              </div>

              <div className="">
                <strong>Estimated Cost</strong>
                <p>Rp. {price} / days</p>
              </div>

              <p className="flex items-end font-bold">Rp{price}</p>

              <div className="col-span-2 pt-4">
                <ButtonRoom
                  onClick={() => {
                    router.push(`/room/rent/${id}`);
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
