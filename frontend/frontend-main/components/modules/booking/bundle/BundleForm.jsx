import React, { useState } from "react";
import { toast } from "react-hot-toast";

import { TextField } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import { Button } from "../elements";

export const BundleForm = ({ data }) => {
  const router = useRouter();
  // form imputs
  const [name, setName] = useState("");
  const [kostRoomId, setKostRoomId] = useState(0);
  const [coworkingId, setCoworkingId] = useState(0);
  const [duration, setDuration] = useState(0);
  const [bundlePrice, setBundlePrice] = useState(0);

  // form submit
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("token") ?? "";
    setLoading(true);
    var body = {
      name: name,
      kostRoomId: kostRoomId,
      coworkingId: coworkingId,
      duration: duration,
      bundlePrice: bundlePrice,
    };

    try {
      const coworking = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/id/${coworkingId}`
      );

      if (coworking.status === 200) {
        await axios
          .post(`/api/kost/bundle`, body, {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then((response) => {
            if (response.status == 201) {
              toast.success("Successfully added bundle");
              // redirect ke detail pagenya
              router.push(`/bundle/${response?.data?.id}`);
            }
          })
          .catch((error) => {
            toast.error(error?.message);
          })
          .finally(() => {
            setLoading(false);
          });
      }
    } catch (error) {
      toast.error(`Coworking space dengan id ${coworkingId} tidak ditemukan.`);
      setLoading(false);
    }
  };

  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Add Bundle</p>
      <form
        onSubmit={(event) => handleSubmit(event)}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <TextField
          value={name}
          onChange={(event) => {
            setName(event.target.value);
          }}
          required
          label="Name"
          type="text"
          id="name"
        />
        <TextField
          value={kostRoomId}
          onChange={(event) => {
            setKostRoomId(event.target.value);
          }}
          required
          label="Kost Room ID"
          type="number"
          id="kostRoomId"
        />
        <TextField
          value={coworkingId}
          onChange={(event) => {
            setCoworkingId(event.target.value);
          }}
          required
          label="Coworking Space ID"
          type="number"
          id="coworkingId"
        />

        <TextField
          value={bundlePrice}
          onChange={(event) => {
            setBundlePrice(event.target.value);
          }}
          required
          label="Bundle Price"
          type="number"
          id="bundlePrice"
        />

        <TextField
          value={duration}
          onChange={(event) => {
            setDuration(event.target.value);
          }}
          required
          label="Duration"
          type="number"
          id="duration"
        />

        <div className="flex justify-center">
          <Button loading={loading} type="submit" className="w-48 ">
            Add Bundle
          </Button>
        </div>
      </form>
    </div>
  );
};
