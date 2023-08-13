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

const RentEditModal = (props) => {
  const { open, onClose, data, setOpenEditModal } = props;
  if (!open) {
    return null;
  }
  const [fields, setFields] = useState(data);
  const [loadingEditRoom, setLoadingEditRoom] = useState(false);
  const [attendeeStatusList, setAttendeeStatusList] = useState(
    fields.attendeeStatusList
  );

  const handleStatusChange = (index, value) => {
    const updatedList = [...attendeeStatusList];
    updatedList[index].statusType = value;
    setAttendeeStatusList(updatedList);
  };
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

  const typesStatus = [
    {
      value: "PRESENT",
      label: "PRESENT",
    },
    {
      value: "ABSENT",
      label: "ABSENT",
    },
    {
      value: "LATE",
      label: "LATE",
    },
    {
      value: "EXCUSED",
      label: "EXCUSED",
    },
  ];

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    setLoadingEditRoom(true);
    const token = localStorage.getItem("token") ?? "";
    console.log("token ", token);
    console.log("id : ", fields.roomRentId);
    try {
      const updatedData = { attendeeStatusList, ...fields };
      updatedData.meetingRoomId = updatedData.meetingRoom.id;
      console.log(updatedData);
      const isUpdate = await axios.put(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/update/${fields.roomRentId}`,
        updatedData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setLoadingEditRoom(false);
      setOpenEditModal(false);
      toast.success("Attendee Status Edited Successfully");
      router.replace(router.asPath);
    } catch (err) {
      console.log(err);
      toast.error(`Failed To Edit Attendee Status`);
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
          Edit Attendee Status
        </p>
        <form
          onSubmit={handleEditSubmit}
          className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
        >
          {fields.attendeeStatusList.map((attendee, index) => (
            <FormControl>
              <InputLabel id="typeRoom">{attendee.name}</InputLabel>
              <Select
                id={`status${index}`}
                value={attendee.statusType}
                onChange={(e) => handleStatusChange(index, e.target.value)}
              >
                {typesStatus.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          ))}

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
                Edit Attendee Status
              </Button>
            )}
          </div>
        </form>
      </Box>
    </Modal>
  );
};

export default RentEditModal;
