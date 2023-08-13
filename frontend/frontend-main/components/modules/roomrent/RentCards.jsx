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
import RentEditModal from "./RentEditModal";

export const RentCards = ({
  roomRentId,
  meetingRoom,
  name,
  duration,
  cost,
  rentStart,
  rentEnd,
}) => {
  const [open, setOpen] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [loadingOpenModal, setLoadingOpenModal] = useState(false);
  const [detailRoomRentData, setDetailRoomRentData] = useState([]);

  const handleOpenModalEdit = async () => {
    setLoadingOpenModal(true);
    const TOKEN_PENGELOLA = localStorage.getItem("token") ?? "";
    // Call API Only When Button Edit Clicked
    const detailRoomRent = await axios.get(
      `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/room-rent/${roomRentId}`,
      {
        headers: {
          Authorization: `Bearer ${TOKEN_PENGELOLA}`,
        },
      }
    );
    setDetailRoomRentData(detailRoomRent?.data);
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

  // // Number Format - Bisa Dijadikan Global Helper
  // const formatMoney = (amount) => {
  //     return amount.toLocaleString("id-ID", {
  //         style: "currency",
  //         currency: "IDR",
  //         minimumFractionDigits: 0,
  //     });
  // };

  // const priceIdr = formatMoney(price);

  return (
    <div className="w-full md:w-[300px] p-5 md:p-3 mt-8 gap-5 rounded-2xl border-2 justify-between">
      <div className="relative z-10">
        <Slider ref={slider} id={roomRentId} {...settings}>
          {meetingRoom.images.slice(0, 5).map((imgUrl) => {
            return (
              <Image
                key={imgUrl}
                src={imgUrl}
                alt="test_image"
                width={300}
                height={100}
                style={{ objectFit: "cover" }}
              />
            );
          })}
        </Slider>

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
        <div className="pt-5 hover:cursor-pointer w-full">
          <p className="text-xl">{meetingRoom.name}</p>
          <p>
            Price : <strong> Rp{cost} </strong>/{duration} day(s)
          </p>
          <p>
            Start<strong> {rentStart} </strong>
          </p>
          <p>
            End<strong> {rentEnd} </strong>
          </p>
        </div>
        <div className="flex items-center space-x-1 justify-end">
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
          <RentEditModal
            open={openEditModal}
            onClose={handleCloseModalEdit}
            setOpenEditModal={setOpenEditModal}
            data={detailRoomRentData}
          />
        )}
      </div>
    </div>
  );
};
