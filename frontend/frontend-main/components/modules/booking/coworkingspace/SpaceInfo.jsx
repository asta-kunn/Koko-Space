import React, { useRef } from "react";
import { useRouter } from "next/router";
import Slider from "react-slick";
import { ChevronRightIcon, ChevronLeftIcon } from "@heroicons/react/20/solid";

export const SpaceInfo = ({ spaceRentData }) => {
  const router = useRouter();
  const slider = useRef();

  const settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    centerMode: true,
    arrows: false, // disable default arrows
  };

  return (
    <div
      className="relative w-[95%] p-5 md:p-8 gap-8 rounded-2xl border-2 justify-between border-white mx-auto"
      style={{ justifyContent: "center", width: "50%", height: "50%" }}
    >
      <Slider ref={slider} {...settings}>
        {spaceRentData.map((space) => (
          <div
            key={space.spaceRentId}
            className="w-[300px] p-5 md:p-8 gap-5 rounded-2xl border-2 justify-between border-gray-300 relative"
          >
            {" "}
            {/* added relative positioning here */}
            <img
              style={{
                width: "100%",
                height: "100%",
                borderRadius: "10%",
                border: "1px solid",
                objectFit: "cover",
              }}
              src={space.workspace.image}
              alt="Unable to load picture"
            />
            <div className="flex flex-wrap gap-6 justify-center">
              <div className="flex flex-col justify-center items-center">
                <div className="text-xl">Room</div>
                <div className="text-2xl pb-3">
                  <strong>{space.spaceRentId}</strong>
                </div>
                <p>Type Room {space.workspace.type}</p>
                <p>Capacity {space.workspace.capacity}</p>
                <p>Duration Rent {space.duration} Day(s)</p>
                <div className="text-base pt-1 pb-4">
                  Cost Rp.{space.cost.toLocaleString("id-ID")}
                </div>
                <div className="text-base text-center pt-1 pb-4">
                  {space.workspace.description}
                </div>
                <button
                  className="bg-transparent hover:bg-white text-gray-800 font-semibold hover:text-black py-2 px-4 border-2 border-gray-300 hover:border-transparent rounded"
                  onClick={() => {
                    router.push(
                      `/coworkingspace/inforent/${space.spaceRentId}`
                    );
                  }}
                >
                  Detail
                </button>
              </div>
            </div>
          </div>
        ))}
      </Slider>
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
  );
};
