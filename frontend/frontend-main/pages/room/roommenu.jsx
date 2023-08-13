import { RoomCards } from "components/modules/meetingroom/room";
import { MEETING_ROOM } from "constants/room/listroom";
import axios from "axios";
import { useAuthContext } from "components/context/AuthContext";
import { useRouter } from "next/router";

const RoomMenu = ({ datas }) => {
  const { user } = useAuthContext();
  const router = useRouter();
  if (user?.role != "PELANGGAN") {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-3xl font-bold py-6">Koko Space Meeting Room Menu</p>
        <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
      </div>
    );
  }
  return (
    <div className="px-5 lg:px-20">
      <div className="flex flex-row justify-between">
        <p className="text-3xl font-bold py-6">Koko Meeting Room Menu</p>
      </div>

      <div className="flex flex-wrap gap-6 justify-center md:justify-start">
        {datas.map((room) => {
          return <RoomCards key={room.id} {...room} />;
        })}
      </div>
    </div>
  );
};

export const getServerSideProps = async (ctx) => {
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
  }
};

export default RoomMenu;
