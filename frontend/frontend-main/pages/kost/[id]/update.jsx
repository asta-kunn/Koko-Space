import { UpdateKostForm } from "@components";
import axios from "axios";
import React from "react";

const UpdateKost = ({ data }) => {
  return (
    <div className="py-10 p-5 md:p-10">
      <UpdateKostForm data={data} />
    </div>
  );
};

export const getServerSideProps = async (ctx) => {
  const { id } = ctx.params;
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
          destination: "/kost/menu",
        },
      };
    });
};
export default UpdateKost;
