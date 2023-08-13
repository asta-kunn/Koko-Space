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

const AdminEditModal = (props) => {
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

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    setLoadingEditRoom(true);
    const token = localStorage.getItem("token") ?? "";
    try {
      const isUpdate = await axios.put(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/meetingroom/update/${data.id}`,
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

  const handleImagesRoom1 = (e) => {
    const { value } = e.target;
    const newFields = { ...fields };
    newFields.images[0] = value;
    setFields(newFields);
  };

  const handleImagesRoom2 = (e) => {
    const { value } = e.target;
    const newFields = { ...fields };
    newFields.images[1] = value;
    setFields(newFields);
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
    <Modal
      keepMounted
      closeAfterTransition
      open={open}
      onClose={onClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      {/* <p>{props.data}</p> */}
      <Box sx={modalStyle}>
        <p className="text-center text-gray-500 font-semibold text-2xl">
          Edit Meeting Room
        </p>
        <form
          onSubmit={handleEditSubmit}
          className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
        >
          <TextField
            onChange={handleEditFields}
            label="Name Room"
            type="text"
            id="nameRoom"
            name="name"
            defaultValue={fields.name}
          />
          <TextField
            onChange={handleEditFields}
            label="Price Room"
            type="number"
            id="priceRoom"
            name="price"
            defaultValue={fields.price}
          />
          <div className="grid grid-cols-2 gap-5">
            <TextField
              onChange={handleImagesRoom1}
              label="Images Room 1"
              type="text"
              id="imagesRoom"
              name="images2"
              defaultValue={fields.images[0]}
            />
            <TextField
              onChange={handleImagesRoom2}
              label="Images Room 2"
              type="text"
              id="imagesRoom"
              name="images2"
              defaultValue={fields.images[1]}
            />
          </div>

          <TextField
            onChange={handleEditFields}
            label="City Room"
            type="text"
            id="cityRoom"
            name="city"
            defaultValue={fields.city}
          />
          <TextField
            onChange={handleEditFields}
            label="Country Room"
            type="text"
            id="countryRoom"
            name="country"
            defaultValue={fields.country}
          />
          <TextField
            onChange={handleEditFields}
            label="Address Room"
            type="text"
            id="addressRoom"
            name="address"
            defaultValue={fields.address}
          />
          <TextField
            onChange={handleEditFields}
            label="Capacity Room"
            type="number"
            id="capacityRoom"
            name="capacity"
            defaultValue={fields.capacity}
          />
          <FormControl>
            <InputLabel id="checkRoom">Availability Room</InputLabel>
            <Select
              labelId="checkRoom"
              id="checkRoom"
              label="Availability Room"
              value={fields.availability}
              onChange={(e) => handleEditFields(e)}
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
              <Button
                type="submit"
                variant="outlined"
                color="info"
                className="w-48 "
              >
                Edit Meeting Room
              </Button>
            )}
          </div>
        </form>
      </Box>
    </Modal>
  );
};

export default AdminEditModal;
