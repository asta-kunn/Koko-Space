import { data } from "autoprefixer";
import axios from "axios";
import {
  AdminFormCoworking,
  AdminCardsCoworking,
} from "components/modules/booking/coworkingspace/admin";

import React, { useEffect, useState } from "react";
import { useAuthContext } from "components/context/AuthContext";

const AdminMenuCoworking = () => {
  const [meetingData, setMeetingData] = useState([]);
  const [isDataCreated, setIsDataCreated] = useState(false);
  const [isDataUpdated, setIsDataUpdated] = useState(false);
  const [isDataDeleted, setIsDataDeleted] = useState(false);
  const { user } = useAuthContext();
  useEffect(() => {
    const getDataMeetingRoom = async () => {
      try {
        const res = await axios.get(
          `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/all`
        );
        const datas = await res.data;

        setMeetingData(datas);
      } catch (error) {
        console.log(error);
      }
      setIsDataCreated(false);
      setIsDataUpdated(false);
      setIsDataDeleted(false);
    };
    getDataMeetingRoom();
  }, [isDataUpdated, isDataCreated, isDataDeleted]);
  if (user?.role != "PENGELOLA") {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-3xl font-bold py-6">CoworkingSpace Admin Menu</p>
        <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
      </div>
    );
  }
  return (
    <div className="py-10 p-5 md:p-10">
      <p className="text-3xl font-bold py-6">CoworkingSpace Admin Menu</p>
      <AdminFormCoworking setIsDataCreated={setIsDataCreated} />

      <div className="flex flex-wrap gap-6 justify-center md:justify-start">
        {meetingData.map((room) => {
          return (
            <AdminCardsCoworking
              key={room.id}
              setIsDataDeleted={setIsDataDeleted}
              setIsDataUpdated={setIsDataUpdated}
              {...room}
            />
          );
        })}
      </div>
    </div>
  );
};

export default AdminMenuCoworking;
