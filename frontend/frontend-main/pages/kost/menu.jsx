import { MenuCards } from "@components";
import axios from "axios";
import { useAuthContext } from "components/context/AuthContext";
import { useRouter } from "next/router";
import { IconButton } from "@mui/material";
import { PlusCircleIcon } from "@heroicons/react/20/solid";
import React from "react";

const Menu = ({ data }) => {
  const { user } = useAuthContext();
  const router = useRouter();

  return (
    <div className="px-5 lg:px-20">
      <div className="flex flex-row justify-between">
        <p className="text-3xl font-bold py-6">Koko Kost Menu</p>
        {user?.role === "PENGELOLA" && (
          <IconButton
            onClick={() => {
              router.push("/kost/create");
            }}
          >
            <PlusCircleIcon width={24} height={24} />
          </IconButton>
        )}
      </div>
      <div className="flex flex-wrap gap-6 justify-center md:justify-start">
        {data.map((room) => {
          return <MenuCards key={room.id} {...room} />;
        })}
      </div>
    </div>
  );
};
export const getServerSideProps = async (ctx) => {
  return await axios
    .get(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/all`)
    .then((response) => {
      if (response.status == 200) {
        return {
          props: {
            data: response.data,
          },
        };
      }
    })
    .catch((error) => {
      return {
        redirect: {
          permanent: false,
          destination: "/",
        },
      };
    });
};
export default Menu;
