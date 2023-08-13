import { SpaceInfo } from "@components";
import axios from "axios";
import { CUSTOMER_SPACE_INFO } from "constants/coworkingspace/customer_space";
import React, { useEffect, useState } from "react";
import { useAuthContext } from "components/context/AuthContext";

const InfoRentSpace = () => {
  const { user } = useAuthContext();
  const [spaceRentData, setSpaceRentData] = useState([]);
  useEffect(() => {
    const getCustomerSpaceInfo = async () => {
      const TOKEN_PELANGGAN = localStorage.getItem("token") ?? "";
      try {
        const res = await axios.get(
          `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/space-rent/me`,
          {
            headers: {
              Authorization: `Bearer ${TOKEN_PELANGGAN}`,
            },
          }
        );
        setSpaceRentData(res.data);
      } catch (error) {
        console.log(error);
      }
    };

    getCustomerSpaceInfo();
  }, []);

  if (user?.role != "PELANGGAN") {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-3xl font-bold py-6">Koko Space Info Rent Menu</p>
        <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
      </div>
    );
  }
  return (
    <div className="py-10 px-5 lg:px-20">
      <p className="text-3xl font-bold pt-8 pb-10 flex flex-wrap justify-center">
        Rented Rooms
      </p>
      {spaceRentData.length === 0 ? (
        <p className="flex justify-center">
          You are currently not renting any room
        </p>
      ) : (
        <SpaceInfo spaceRentData={spaceRentData} />
      )}
    </div>
  );
};
export default InfoRentSpace;
