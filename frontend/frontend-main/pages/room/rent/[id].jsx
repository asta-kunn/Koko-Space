import axios from "axios";
import { RentForm } from "components/modules/meetingroom/room";
import { MEETING_ROOM } from "constants/room/listroom";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const Rent = ({ datas }) => {
  const router = useRouter();
  const { id } = router.query;
  const [detailRoom, setDetailRoom] = useState([]);

  useEffect(() => {
    if (id) {
      getDetailMeetingRoom();
    }
  }, [id]);

  const getDetailMeetingRoom = async () => {
    const TOKEN_CUSTOMER = localStorage.getItem("token") ?? "";
    try {
      const detailMeetingRoom = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/meetingroom/id/${id}`,
        {
          headers: {
            Authorization: `Bearer ${TOKEN_CUSTOMER}`,
          },
        }
      );

      setDetailRoom(detailMeetingRoom.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="py-10 p-5 md:p-10">
      {router.isReady && (
        <RentForm
          detailRoom={detailRoom}
          // {...MEETING_ROOM[id - 1]}
        />
      )}
    </div>
  );
};
  
export const getServerSideProps = async () => {
  const res = await axios.get(
    `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/meetingroom/all`
  );

  const datas = await res.data;
  if (res.status === 200) {
    return {
      props: {
        datas,
      },
    };
  } else {
    return {
      props: "",
    };
  }
};

export default Rent;
