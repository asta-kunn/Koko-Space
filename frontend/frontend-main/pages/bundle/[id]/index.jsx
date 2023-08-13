import { BundleDetail } from "@components";

import { IconButton } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import React from "react";
import toast from "react-hot-toast";
import { TrashIcon } from "@heroicons/react/20/solid";
import { useState } from "react";
import { useAuthContext } from "components/context/AuthContext";

const BundleDetailPage = ({ data }) => {
  const router = useRouter();
  const { id } = router.query;
  const handleDelete = async () => {
    const token = localStorage.getItem("token") ?? "";
    await axios
      .delete(`${process.env.NEXT_PUBLIC_KOST_API_URL}/bundle/delete/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        if (response.status == 200) {
          toast.success(`Deleted Bundle with id ${id}`);
        }
      })
      .catch((error) => {
        toast.error(error.message);
      });
  };

  const { user } = useAuthContext();

  return (
    <div className="py-10 px-5 lg:px-20">
      <BundleDetail {...data} />
      {user?.role === "PENGELOLA" && (
        <IconButton onClick={handleDelete}>
          <TrashIcon color="red" width={28} height={28} />
        </IconButton>
      )}
    </div>
  );
};
export const getServerSideProps = async (ctx) => {
  const id = ctx.params.id;
  // TODO: GET coworking space
  return await axios
    .get(`${process.env.NEXT_PUBLIC_KOST_API_URL}/bundle/${id}`)
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
export default BundleDetailPage;
