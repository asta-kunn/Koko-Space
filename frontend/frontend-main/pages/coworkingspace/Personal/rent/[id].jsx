import { SpaceForm } from "components/modules/booking/coworkingspace";
import { COWORKING_ROOM } from "constants/coworkingspace/room";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import axios from "axios";

const Rent = ({ datas }) => {
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
      {router.isReady && (
        <SpaceForm
          detailRoom={detailRoom}
        // {...MEETING_ROOM[id - 1]}
        />
      )}
    </div>
  );
};

export const getServerSideProps = async () => {
  const res = await axios.get(
    `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/all`
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
