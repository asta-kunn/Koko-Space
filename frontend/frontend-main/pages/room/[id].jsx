import axios from "axios";
import { RoomDetail } from "components/modules/meetingroom/room";
import { MEETING_ROOM } from "constants/room/listroom";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const RoomDetailPage = () => {
  const router = useRouter();
  const { id } = router.query;
  const [detailRoom, setDetailRoom] = useState([]);

  useEffect(() => {
    if (id) {
      getDetailRoom();
    }
  }, [id]);

  const getDetailRoom = async () => {
    const TOKEN_CUSTOMER = localStorage.getItem("token") ?? "";
    try {
      const detailRoom = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/meetingroom/id/${id}`,
        {
          headers: {
            Authorization: `Bearer ${TOKEN_CUSTOMER}`,
          },
        }
      );

      setDetailRoom(detailRoom.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="py-10 p-5 md:p-10">
      {router.isReady && <RoomDetail detailRoom={detailRoom} />}
    </div>
  );
};

export default RoomDetailPage;
