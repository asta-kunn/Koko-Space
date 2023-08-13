import React, { useRef } from "react";
import { useRouter } from "next/router";
import Image from "next/image";
import Slider from "react-slick";
import { ChevronRightIcon, ChevronLeftIcon } from "@heroicons/react/20/solid";

export const RoomCards = ({
  id,
  type,
  name,
  capacity,
  price,
  city,
  country,
  address,
  availability,
  images,
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

  if (!availability) {
    return null; // If availability is false, hide the meeting room from the menu
  }
  return (
    <div className="w-full md:w-[300px] p-5 md:p-8 gap-5 rounded-2xl border-2 justify-between">
      <div className="relative z-10">
        <Slider ref={slider} id={id} {...settings}>
          {images.slice(0, 5).map((imgUrl) => {
            return (
              <>
                <Image
                  key={id}
                  src={imgUrl}
                  alt="test_image"
                  width={200}
                  height={100}
                  style={{ objectFit: "cover" }}
                />
              </>
            );
          })}
        </Slider>

        <div>
          <button
            className="absolute -left-4 top-[30%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickPrev();
            }}
          >
            <ChevronLeftIcon width={20} height={20} />
          </button>

          <button
            className="absolute -right-4 top-[30%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickNext();
            }}
          >
            <ChevronRightIcon width={20} height={20} />
          </button>
        </div>
        <div
          onClick={() => {
            router.push(`/room/${id}`);
          }}
          className="pt-5 hover:cursor-pointer w-full"
        >
          <p className="text-xl">{name}</p>
          <p>
            <strong> Rp{price} </strong>/ day
          </p>
          <p>
            <strong> {capacity} </strong>capacity
          </p>
          <p>
            <strong>
              {" "}
              {city}, {country}, {address}{" "}
            </strong>
          </p>
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
