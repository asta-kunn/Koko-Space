import { SpaceInfoDetail } from "@components";
import axios from "axios";
import { CUSTOMER_SPACE_INFO } from "constants/coworkingspace/customer_space";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const RentalSpaceInfo = () => {
  const router = useRouter();
  const { id } = router.query;
  const [spaceRentDataDetail, setSpaceRentDataDetail] = useState([]);

  useEffect(() => {
    if (id) {
      getDetailCustomerSpaceInfo();
    }
  }, [id]);
  const getDetailCustomerSpaceInfo = async () => {
    const TOKEN_PELANGGAN = localStorage.getItem("token") ?? "";
    try {
      const res = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/space-rent/id/${id}`,
        {
          headers: {
            Authorization: `Bearer ${TOKEN_PELANGGAN}`,
          },
        }
      );
      setSpaceRentDataDetail(res.data);
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <div className="py-10 p-5 md:p-9">
      {router.isReady && (
        <SpaceInfoDetail spaceRentDataDetail={spaceRentDataDetail} />
      )}
    </div>
  );
};
export default RentalSpaceInfo;
