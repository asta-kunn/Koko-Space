import React, { useState } from "react";
import { LoadingButton } from "@mui/lab";
import {
  Box,
  Button,
  FormControl,
  MenuItem,
  InputLabel,
  Modal,
  Select,
  TextField,
} from "@mui/material";
import { LoaderIcon, toast } from "react-hot-toast";
import axios from "axios";
import { useRouter } from "next/router";

const CoworkingAdminEditModal = (props) => {
  const { open, onClose, data, setOpenEditModal, setIsDataUpdated } = props;
  if (!open) {
    return null;
  }

  const [loadingEditRoom, setLoadingEditRoom] = useState(false);
  const [fields, setFields] = useState(data);
  const router = useRouter();
  const modalStyle = {
    position: "fixed",
    top: "55%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    maxHeight: "80vh",
    width: "80%",
    overflowY: "auto",
    backgroundColor: "#ffffff",
    padding: "20px",
  };

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

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    setLoadingEditRoom(true);
    const token = localStorage.getItem("token") ?? "";
    try {
      const isUpdate = await axios.put(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/update/${data.id}`,
        fields,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setLoadingEditRoom(false);
      setOpenEditModal(false);
      toast.success("Room Edited Successfully");
      setIsDataUpdated(true);
      router.replace(router.asPath);
    } catch (err) {
      console.log(err);
      toast.error(`Failed To Edit Room`);
      setLoadingEditRoom(false);
      setOpenEditModal(false);
      router.replace(router.asPath);
    }
  };

  const handleEditFields = (e) => {
    const name = e.target.name;
    const value = e.target.value;

    setFields((prevState) => ({
      ...prevState,
      [name]: value,
      availability: name === "availability" ? value : prevState.availability,
      type: name === "type" ? value : prevState.type,
    }));
  };

  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Add CoworkingSpace</p>
      <form
        onSubmit={(event) => handleEditSubmit(event)}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <FormControl>
          <InputLabel id="type">Type Room</InputLabel>
          <Select
            labelId="type"
            id="type"
            label="type"
            value={fields.type}
            onChange={(e) => handleEditFields(e)}
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
          onChange={handleEditFields}
          label="Price Daily"
          type="number"
          id="priceDaily"
          name="dailyPrice"
        />
        <TextField
          onChange={handleEditFields}
          label="Price Hourly"
          type="number"
          id="priceHourly"
          name="hourlyPrice"
        />
        <TextField
          onChange={handleEditFields}
          label="FilledSeat"
          type="number"
          id="FilledSeat"
          name="filledSeat"
        />
        <TextField
          onChange={handleEditFields}
          label="Images Room 1"
          type="text"
          id="imagesRoom"
          name="image"
        />
        <TextField
          onChange={handleEditFields}
          label="description"
          type="text"
          id="description"
          name="description"
        />
        <TextField
          onChange={handleEditFields}
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
            onChange={(e) => handleEditFields(e)}
            name="availability"
          >
            <MenuItem value={true}>True</MenuItem>
            <MenuItem value={false}>False</MenuItem>
          </Select>
        </FormControl>

        <div className="flex justify-center">
          {loadingEditRoom ? (
            <LoadingButton
              loading
              loadingPosition="start"
              startIcon={<LoaderIcon />}
              variant="outlined"
            >
              SAVING
            </LoadingButton>
          ) : (
            <Button type="submit" className="w-48 ">
              Edit Space
            </Button>
          )}
        </div>
      </form>
    </div>
  );
};

export default CoworkingAdminEditModal;
