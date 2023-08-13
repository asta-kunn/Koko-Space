// import { RentCards } from "components/modules/roomrent/RentCards";
// import { MEETING_ROOM } from "constants/room/listroom";
// import axios from "axios";
// import { useAuthContext } from "components/context/AuthContext";
// import { useRouter } from "next/router";

// const RoomRentMe = ({ datas }) => {
//     const { user } = useAuthContext();
//     const router = useRouter();

//     return (
//         <div className="px-5 lg:px-20">
//             <div className="flex flex-row justify-between">
//                 <p className="text-3xl font-bold py-6">My Room Rent Menu</p>
//             </div>

//             <div className="flex flex-wrap gap-6 justify-center md:justify-start">
//                 {datas.map((roomrent) => {
//                     return <RentCards key={roomrent.id} {...roomrent} />;
//                 })}
//             </div>
//         </div>
//     );
// };

// export const getServerSideProps = async (ctx) => {
//     const res = await axios.get(
//         `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/me`
//     );

//     const datas = await res.data;
//     console.log("halo ", res.status)
//     if (res.status === 200) {
//         return {
//             props: {
//                 datas,
//             },
//         };
//     }
// };

// export default RoomRentMe;

import { RentCards } from "components/modules/roomrent/RentCards";
import { MEETING_ROOM } from "constants/room/listroom";
import axios from "axios";
import { useAuthContext } from "components/context/AuthContext";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const RoomRentMe = ({}) => {
  const { user } = useAuthContext();
  const router = useRouter();
  const [data, setData] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token") ?? "";
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/me`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setData(response.data);
        console.log(response.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  if (data === null) {
    return (
      <div className="px-5 lg:px-20">
        <div className="flex flex-row justify-between">
          <p className="text-3xl font-bold py-6">My Room Rent Menu</p>
        </div>
      </div>
    );
  }
  return (
    <div className="px-5 lg:px-20">
      <div className="flex flex-row justify-between">
        <p className="text-3xl font-bold py-6">My Room Rent Menu</p>
      </div>

      <div className="flex flex-wrap gap-6 justify-center md:justify-start">
        {data.map((roomrent) => {
          return <RentCards key={roomrent.roomRentId} {...roomrent} />;
        })}
      </div>
    </div>
  );
};

// export const getServerSideProps = async (ctx) => {
//     const res = await axios.get(
//         `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/me`
//     );

//     const datas = await res.data;
//     console.log("halo ", res.status)
//     if (res.status === 200) {
//         return {
//             props: {
//                 datas,
//             },
//         };
//     }
// };

export default RoomRentMe;
