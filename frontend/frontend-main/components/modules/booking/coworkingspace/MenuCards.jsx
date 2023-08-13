import React, { useRef } from "react";
import { useRouter } from "next/router";
import Image from "next/image";
import Slider from "react-slick";
import { ChevronRightIcon, ChevronLeftIcon } from "@heroicons/react/20/solid";

export const CoworkingCard = ({
  id,  
  type,
  capacity,
  hourlyPrice,
  dailyPrice,
  filledSeat,
  description,
  availability,
  image,
}) => {
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
    <div className="w-full md:w-[300px] p-5 md:p-8 gap-5 rounded-2xl border-2 justify-between">
      <div className="relative z-10">
        <Image
          key={id}
          src={image}
          alt="test_image"
          width={200}
          height={100}
          style={{ objectFit: "cover" }}
        />

        <div></div>
        <div
          onClick={() => {
            if (type === "COWORKING") {
              router.push(`/coworkingspace/Coworking/${id}`);
            } else {
              router.push(`/coworkingspace/Personal/${id}`);
            }
          }}
          className="pt-5 hover:cursor-pointer w-full"
        >
          <p className="text-xl">{description}</p>
          <p>
            <strong> Rp{hourlyPrice} </strong>: Hourly
          </p>
          <p>
            <strong> Rp{dailyPrice} </strong>: Daily
          </p>
          <p>
            <strong> {capacity} </strong>capacity
          </p>
          <p>filled Seat:{filledSeat}</p>
          <p>
            <strong> {type} </strong>type
          </p>
          <p>
            {/* parse booleang availability to string */}
            <strong> {availability?.toString()} </strong>availability
          </p>
        </div>
      </div>
    </div>
  );
};
