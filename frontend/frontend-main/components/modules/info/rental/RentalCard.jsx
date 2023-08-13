import React from "react";
import { useRouter } from "next/router";

export const RentalCard = ({ id, kostRent }) => {
  const router = useRouter();

  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
  };

  const handleServiceButton = () => {
    router.push(`/kost/rentals/${id}`);
  };

  return (
    <div>
      <div
        className="w-[95%] p-5 md:p-8 gap-8 rounded-2xl border-2 justify-between border-white"
        style={{
          display: "flex",
          justifyContent: "center",
          width: "100%",
          height: "100%",
        }}
      >
        <div className="w-[300px] p-5 md:p-8 gap-5 rounded-2xl border-2 justify-between border-gray-300">
          <div className="flex flex-wrap gap-6 justify-center">
            <div>
              <div>
                <p className="text-xl" style={settings}>
                  {kostRent.kostRoom.name}
                </p>
                <div className="text-base py-1" style={settings}>
                  {kostRent.kostRoom.address}
                </div>
                <div className="text-center p-4">
                  <img
                    style={{
                      width: "180px",
                      height: "180px",
                      borderRadius: "10%",
                      border: "1px solid",
                      objectFit: "cover",
                    }}
                    src={kostRent.kostRoom.images[0]}
                    alt="Unable to load picture"
                  />
                </div>
                <div>
                  <div className="text-lg pt-2 pb-3 flex flex-wrap justify-center">
                    <strong>Room {kostRent.roomNumber}</strong>
                  </div>
                  <div className="flex flex-wrap justify-between">
                    <p>Check-in:</p> {kostRent.checkInDate}
                  </div>
                  <div className="flex flex-wrap justify-between">
                    <p>Check-out:</p> {kostRent.checkOutDate}
                  </div>
                  <div className="pt-7" style={settings}>
                    <button
                      className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-4 border-2 border-gray-300 hover:border-transparent rounded"
                      onClick={handleServiceButton}
                    >
                      Room Services
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
