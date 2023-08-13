import React from "react";
import { useRouter } from "next/router";

export const WithoutTenantCard = ({ roomNumber, kostRoom }) => {
  const router = useRouter();

  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
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
                  {kostRoom.name}
                </p>
                <div className="text-base py-1" style={settings}>
                  {kostRoom.address}
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
                    src={kostRoom.images[0]}
                    alt="Unable to load picture"
                  />
                </div>
                <div className="pt-2 pb-3 flex flex-wrap justify-center">
                  <strong>Room {roomNumber}</strong>
                </div>
                <div className="flex flex-wrap justify-center">
                  Not occupied
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
