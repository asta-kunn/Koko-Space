import { KostDetail } from "@components";

import { IconButton } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import React from "react";
import toast from "react-hot-toast";
import { TrashIcon, PencilSquareIcon } from "@heroicons/react/20/solid";
import { useAuthContext } from "components/context/AuthContext";

const KostDetailPage = ({ data }) => {
  const router = useRouter();

  const { user } = useAuthContext();
  const { id } = router.query;

  const handleDelete = async () => {
    const token = localStorage.getItem("token") ?? "";
    await axios
      .delete(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/delete/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        if (response.status == 200) {
          toast.success(`Deleted Kost with id ${id}`);
        }
      })
      .catch((error) => {
        toast.error(error.message);
      });
  };

  return (
    <div className="py-10 px-5 lg:px-20">
      <KostDetail {...data} />
      {user?.role === "PENGELOLA" && (
        <>
          <IconButton onClick={handleDelete}>
            <TrashIcon color="red" width={28} height={28} />
          </IconButton>
          <IconButton
            onClick={() => {
              router.push(`/kost/${id}/update`);
            }}
          >
            <PencilSquareIcon color="blue" width={28} height={28} />
          </IconButton>
        </>
      )}
    </div>
  );
};

export const getServerSideProps = async (ctx) => {
  const id = ctx.params.id;
  return await axios
    .get(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/${id}`)
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
export default KostDetailPage;
