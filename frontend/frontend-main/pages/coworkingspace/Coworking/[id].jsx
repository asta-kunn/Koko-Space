import axios from "axios";
import { SpaceDetail } from "components/modules/booking/coworkingspace";
import { COWORKING_ROOM } from "@constants";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const CoworkingDetailPage = () => {
  const router = useRouter();
  const { id } = router.query;
  const [detailRoom, setDetailRoom] = useState([]);
  useEffect(() => {
    if (id) {
      getDetailCoworkingRoom();
    }
  }, [id]);

  const getDetailCoworkingRoom = async () => {
    const TOKEN_CUSTOMER = localStorage.getItem("token") ?? "";
    try {
      const detailCoworkingRoom = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/id/${id}`,
        {
          headers: {
            Authorization: `Bearer ${TOKEN_CUSTOMER}`,
          },
        }
      );

      setDetailRoom(detailCoworkingRoom.data);
    } catch (error) {
      console.log(error);
    }
  };
  return (
    <div className="py-10 p-5 md:p-10">
      {router.isReady && <SpaceDetail detailRoom={detailRoom} />}
    </div>
  );
};

export default CoworkingDetailPage;
