import { CoworkingCard } from ".";
import { PERSONAL_ROOM } from "constants/coworkingspace/personalroom";
import axios from "axios";
import { useAuthContext } from "components/context/AuthContext";
import { useRouter } from "next/router";
import React, { useState, useEffect } from "react";

const MenuPersonal = () => {
  const [datas, setDatas] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const res = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/all`
      );

      const coworkingSpaces = res.data.filter(
        (room) => room.type === "PERSONAL"
      );
      if (res.status === 200) {
        setDatas(coworkingSpaces);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="px-5 lg:px-20">
      <div className="flex flex-row justify-between">
        <p className="text-3xl font-bold py-6">Menu Personal</p>
      </div>

      <div className="flex flex-wrap gap-6 justify-center md:justify-start">
        {datas && datas.length > 0 ? (
          datas.map((room) => {
            return <CoworkingCard key={room.id} {...room} />;
          })
        ) : (
          <p>No data available</p>
        )}
      </div>
    </div>
  );
};

export default MenuPersonal;
