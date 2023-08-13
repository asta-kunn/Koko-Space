import React, { useRef, useState } from "react";
import { useRouter } from "next/router";
import Image from "next/image";
import Slider from "react-slick";
import { ChevronRightIcon, ChevronLeftIcon } from "@heroicons/react/20/solid";
import {
  Backdrop,
  Box,
  Button,
  CircularProgress,
  IconButton,
  Modal,
  Typography,
} from "@mui/material";
import { TrashIcon } from "@heroicons/react/20/solid";
import { PencilSquareIcon } from "@heroicons/react/20/solid";
import axios from "axios";
import toast from "react-hot-toast";
import CoworkingAdminEditModal from "./CoworkingAdminEditModal";

export const AdminCardsCoworking = ({
  id,
  type,
  capacity,
  hourlyPrice,
  dailyPrice,
  filledSeat,
  description,
  availability,
  image,
  setIsDataUpdated,
  setIsDataDeleted,
}) => {
  const [open, setOpen] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [loadingOpenModal, setLoadingOpenModal] = useState(false);
  const [detailRoomData, setDetailRoomData] = useState([]);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleOpenModalEdit = async () => {
    setLoadingOpenModal(true);
    const TOKEN_PENGELOLA = localStorage.getItem("token") ?? "";
    // Call API Only When Button Edit Clicked
    const detailMeetingRoom = await axios.get(
      `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/id/${id}`,
      {
        headers: {
          Authorization: `Bearer ${TOKEN_PENGELOLA}`,
        },
      }
    );
    setDetailRoomData(detailMeetingRoom?.data);
    setLoadingOpenModal(false);
    setOpenEditModal(true);
  };

  const handleCloseModalEdit = () => setOpenEditModal(false);

  const router = useRouter();
  const slider = useRef();
  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 500,
    height: "auto",
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
  };

  const handleDelete = async () => {
    const TOKEN_PENGELOLA = localStorage.getItem("token") ?? "";
    const isDelete = await axios.delete(
      `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/delete/${id}`,
      {
        headers: {
          Authorization: `Bearer ${TOKEN_PENGELOLA}`,
        },
      }
    );

    if (isDelete.status == 200) {
      toast.success(`Space Successfully Deleted`);
      setOpen(false);
      setIsDataDeleted(true);
      router.replace(router.asPath);
    } else {
      toast.error(error.message);
      router.replace(router.asPath);
    }
  };

  return (
    <div className="w-full md:w-[300px] p-5 md:p-3 mt-8 gap-5 rounded-2xl border-2 justify-between">
      <div className="relative z-10">
        <Image
          key={id}
          src={image}
          alt="test_image"
          width={300}
          height={100}
          objectFit="cover"
        />

        <div>
          <button
            className="absolute -left-4 top-[30%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickPrev();
            }}
          >
            <ChevronLeftIcon width={20} height={20} />
          </button>

          <button
            className="absolute -right-4 top-[30%] bg-white/90 shadow-lg rounded-full p-2"
            onClick={() => {
              slider.current.slickNext();
            }}
          >
            <ChevronRightIcon width={20} height={20} />
          </button>
        </div>
        <div
          onClick={() => {
            if (type === "COWORKING") {
              router.push(`/coworkingspace/Coworking/${id}`);
            } else {
              router.push(`/coworkingspace/Personal/${id}`);
            }
          }}
          className="pt-5 hover:cursor-pointer w-full"
        >
          <p>
            <strong>{hourlyPrice} </strong>: hourlyPrice
          </p>
          <p>
            <strong>{dailyPrice} </strong>: dailyPrice
          </p>
          <p>
            Capacity <strong> {capacity} </strong>
          </p>
          <div>
            <p>
              filled Seat <strong>{filledSeat}</strong>
            </p>
            <p>
              description <strong>{description}</strong>
            </p>
          </div>
          <p>
            Type <strong> {type} </strong>
          </p>
          <p>
            Availability{" "}
            <strong>
              {" "}
              {availability
                ? "Room Masih Tersedia"
                : "Room Tidak Tersedia"}{" "}
            </strong>
          </p>
        </div>
        <div className="flex items-center space-x-1 justify-end">
          <IconButton onClick={handleOpen}>
            <TrashIcon color="red" width={25} height={25} />
          </IconButton>
          <IconButton onClick={handleOpenModalEdit}>
            <PencilSquareIcon color="blue" width={25} height={25} />
          </IconButton>
        </div>

        {/* Edit Modal  */}
        {loadingOpenModal ? (
          <Backdrop
            sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
            open={true}
          >
            <Box sx={{ display: "flex" }}>
              <CircularProgress color="inherit" />
            </Box>
          </Backdrop>
        ) : (
          <CoworkingAdminEditModal
            open={openEditModal}
            onClose={handleCloseModalEdit}
            setOpenEditModal={setOpenEditModal}
            setIsDataUpdated={setIsDataUpdated}
            data={detailRoomData}
          />
        )}

        {/* Delete Modal */}
        <Modal
          keepMounted
          closeAfterTransition
          open={open}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description"
        >
          <Box sx={style}>
            <Typography
              id="modal-modal-title"
              variant="h6"
              component="h2"
              className="text-center uppercase text-red-500 font-semibold"
            >
              warning!
            </Typography>
            <Typography id="modal-modal-description" sx={{ mt: 2 }}>
              Are You Sure Want To Delete This Meeting Space?
            </Typography>
            <Button
              onClick={handleDelete}
              className="mt-3"
              variant="outlined"
              color="error"
            >
              Delete Now
            </Button>
          </Box>
        </Modal>
      </div>
    </div>
  );
};
