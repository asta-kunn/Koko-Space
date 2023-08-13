import React, { useState, useRef, useEffect } from "react";
import axios from "axios";
import {
  TextField,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { LoaderIcon, toast } from "react-hot-toast";
import { useRouter } from "next/router";
import { ButtonRoom } from "../elements";

export const AdminForm = ({ setIsDataCreated }) => {
  const router = useRouter();
  const [imagesRoom1, setImagesRoom1] = useState([]);
  const [imagesRoom2, setImagesRoom2] = useState([]);
  const [loadingRoom, setLoadingRoom] = useState(false);
  const [fields, setFields] = useState({
    availability: false,
    type: "SMALL",
  });
  const typesRoom = [
    {
      value: "SMALL",
      label: "SMALL",
    },
    {
      value: "MEDIUM",
      label: "MEDIUM",
    },
    {
      value: "LARGE",
      label: "LARGE",
    },
  ];

  // Handle Input Value Images1
  const handleImagesRoom1 = (e) => {
    const { value } = e.target;
    setImagesRoom1(value.split(","));
  };

  // Handle Input Value Images2
  const handleImagesRoom2 = (e) => {
    const { value } = e.target;
    setImagesRoom2(value.split(","));
  };

  const handleFields = (e) => {
    const name = e.target.name;
    const value = e.target.value;

    setFields((prevState) => ({
      ...prevState,
      [name]: value,
      availability: name === "availability" ? value : prevState.availability,
      type: name === "type" ? value : prevState.type,
      images: [...imagesRoom1, ...imagesRoom2],
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoadingRoom(true);
    const token = localStorage.getItem("token") ?? "";

    if (Object.keys(fields).length !== 0) {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/meetingroom/create`,
        fields,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
      if (res.status === 200) {
        e.target.reset();
        toast.success("Successfully added meetingroom"); // redirect ke detail pagenya
        setLoadingRoom(false);
        setIsDataCreated(true);
        router.replace(router.asPath);
      }
    } else {
      toast.error("Field Can't Be Null");
      setLoadingRoom(false);
    }
  };

  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Add Meeting Room</p>
      <form
        onSubmit={(event) => handleSubmit(event)}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <TextField
          onChange={handleFields}
          label="Name Room"
          type="text"
          id="nameRoom"
          name="name"
          required
        />
        <TextField
          onChange={handleFields}
          label="Price Room"
          type="number"
          id="priceRoom"
          name="price"
          required
          inputProps={{
            min: 0,
          }}
        />
        <TextField
          onChange={handleImagesRoom1}
          label="Images Room 1"
          type="text"
          id="imagesRoom"
          name="images1"
          required
        />
        <TextField
          onChange={handleImagesRoom2}
          label="Images Room 2"
          type="text"
          id="imagesRoom"
          name="images2"
          required
        />

        <TextField
          onChange={handleFields}
          label="City Room"
          type="text"
          id="cityRoom"
          name="city"
          required
        />
        <TextField
          onChange={handleFields}
          label="Country Room"
          type="text"
          id="countryRoom"
          name="country"
          required
        />
        <TextField
          onChange={handleFields}
          label="Address Room"
          type="text"
          id="addressRoom"
          name="address"
          required
        />
        <TextField
          onChange={handleFields}
          label="Capacity Room"
          type="number"
          id="capacityRoom"
          name="capacity"
          required
          inputProps={{
            min: 0,
          }}
        />
        <FormControl>
          <InputLabel id="checkRoom">Availability Room</InputLabel>
          <Select
            labelId="checkRoom"
            id="checkRoom"
            label="Availability Room"
            value={fields.availability}
            onChange={(e) => handleFields(e)}
            name="availability"
          >
            <MenuItem value={true}>True</MenuItem>
            <MenuItem value={false}>False</MenuItem>
          </Select>
        </FormControl>
        <FormControl>
          <InputLabel id="typeRoom">Type Room</InputLabel>
          <Select
            labelId="typeRoom"
            id="typeRoom"
            label="Type Room"
            value={fields.type}
            onChange={(e) => handleFields(e)}
            name="type"
          >
            {typesRoom.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <div className="flex justify-center">
          {loadingRoom ? (
            <LoadingButton
              loading
              loadingPosition="start"
              startIcon={<LoaderIcon />}
              variant="outlined"
            >
              SAVING
            </LoadingButton>
          ) : (
            <ButtonRoom type="submit" className="w-48 ">
              Add Bundle
            </ButtonRoom>
          )}
        </div>
      </form>
    </div>
  );
};
