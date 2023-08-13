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

export const AdminFormCoworking = ({ setIsDataCreated }) => {
  const router = useRouter();
  const [imagesRoom1, setImagesRoom1] = useState([]);
  const [loadingRoom, setLoadingRoom] = useState(false);
  const [fields, setFields] = useState({
    availability: false,
    type: "COWORKING",
  });
  const typesRoom = [
    {
      value: "PERSONAL",
      label: "PERSONAL",
    },
    {
      value: "COWORKING",
      label: "COWORKING",
    },
  ];

  const handleFields = (e) => {
    const name = e.target.name;
    const value = e.target.value;

    setFields((prevState) => ({
      ...prevState,
      [name]: value,
      availability: name === "availability" ? value : prevState.availability,
      type: name === "type" ? value : prevState.type,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoadingRoom(true);
    const token = localStorage.getItem("token") ?? "";

    if (!fields.dailyPrice || fields.dailyPrice <= 0) {
      toast.error("Daily price must be greater than 0");
      setLoadingRoom(false);
      return;
    }

    if (!fields.hourlyPrice || fields.hourlyPrice <= 0) {
      toast.error("Hourly price must be greater than 0");
      setLoadingRoom(false);
      return;
    }

    if (!fields.filledSeat || fields.filledSeat <= -1) {
      toast.error("Filled seat must not negative");
      setLoadingRoom(false);
      return;
    }

    if (!fields.capacity || fields.capacity <= 0) {
      toast.error("Capacity must be greater than 0");
      setLoadingRoom(false);
      return;
    }

    if (!fields.image) {
      toast.error("Image field can't be empty");
      setLoadingRoom(false);
      return;
    }

    if (!fields.description) {
      toast.error("Description field can't be empty");
      setLoadingRoom(false);
      return;
    }

    if (Object.keys(fields).length !== 0) {
      const res = await axios.post(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/create`,
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
        toast.success("Successfully added meeting room");
        setLoadingRoom(false);
        setIsDataCreated(true);
        router.replace(router.asPath);
      }
    } else {
      toast.error("Fields Can't Be Null");
      setLoadingRoom(false);
    }
  };


  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Add CoworkingSpace</p>
      <form
        onSubmit={(event) => handleSubmit(event)}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <FormControl>
          <InputLabel id="type">Type Room</InputLabel>
          <Select
            labelId="type"
            id="type"
            label="type"
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

        <TextField
          onChange={handleFields}
          label="Price Daily"
          type="number"
          id="priceDaily"
          name="dailyPrice"
        />
        <TextField
          onChange={handleFields}
          label="Price Hourly"
          type="number"
          id="priceHourly"
          name="hourlyPrice"
        />
        <TextField
          onChange={handleFields}
          label="FilledSeat"
          type="number"
          id="filledSeat"
          name="filledSeat"
        />
        <TextField
          onChange={handleFields}
          label="Images Room 1"
          type="text"
          id="imagesRoom"
          name="image"
        />
        <TextField
          onChange={handleFields}
          label="description"
          type="text"
          id="description"
          name="description"
        />
        <TextField
          onChange={handleFields}
          label="Capacity"
          type="number"
          id="capacity"
          name="capacity"
        />
        <FormControl>
          <InputLabel id="checkRoom">Availability</InputLabel>
          <Select
            labelId="checkRoom"
            id="checkRoom"
            label="Availability"
            value={fields.availability}
            onChange={(e) => handleFields(e)}
            name="availability"
          >
            <MenuItem value={true}>True</MenuItem>
            <MenuItem value={false}>False</MenuItem>
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
