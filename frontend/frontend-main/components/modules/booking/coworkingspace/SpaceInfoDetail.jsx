import React, { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { Backdrop, Box, CircularProgress } from "@mui/material";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";

export const SpaceInfoDetail = ({ spaceRentDataDetail }) => {
  const [workspaces, setWorkspaces] = useState([]);
  const [selectedWorkspaceId, setSelectedWorkspaceId] = useState(null);
  const [elapsedTime, setElapsedTime] = useState({ hours: 0, minutes: 0, seconds: 0 });
  const [start, setStart] = useState(new Date());
  const [end, setEnd] = useState(new Date());
  const [currentTime, setCurrentTime] = useState(new Date());

  const router = useRouter();
  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
  };


  const handleSelect = (event) => {
    console.log("Select function called with value: ", event.target.value);
    setSelectedWorkspaceId(event.target.value);
  };

  useEffect(() => {
    fetchWorkspaces();
  }, []);
  const fetchWorkspaces = async () => {
    try {
      const response = await axios.get(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/workspace/all`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      const personalWorkspaces = response.data.filter(
        (workspace) => workspace.type === "PERSONAL" && workspace.availability === true
      );
      setWorkspaces(personalWorkspaces);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (spaceRentDataDetail && spaceRentDataDetail.rentStart && spaceRentDataDetail.rentEnd) {
      setStart(new Date(spaceRentDataDetail.rentStart));
      setEnd(new Date(spaceRentDataDetail.rentEnd));
    }
  }, [spaceRentDataDetail]);

  useEffect(() => {
    let intervalId;
    if (!isCountdownFinished()) {
      intervalId = setInterval(() => {
        setCurrentTime(new Date());
        setElapsedTime(getTimeRemaining(start, end));
      }, 1000);
    }

    return () => clearInterval(intervalId);
  }, [start, end, currentTime]);

  const getTimeRemaining = (start, end) => {
    let totalSeconds = (end - currentTime) / 1000;
    if (totalSeconds < 0) totalSeconds = 0;

    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = Math.floor(totalSeconds % 60);

    return {
      hours: formatTime(hours),
      minutes: formatTime(minutes),
      seconds: formatTime(seconds),
    };
  };

  const formatTime = (time) => {
    return time < 10 ? `0${time}` : time;
  };

  const isCountdownFinished = () => {
    return currentTime > end;
  };
  const handleUpgradeRent = async (e) => {
    // Make sure a workspace is selected before proceeding
    if (selectedWorkspaceId === null) {
      toast.error('Please select a workspace first.');
      return;
    }

    const token = localStorage.getItem("token") ?? "";
    const body = {
      workspaceId: selectedWorkspaceId,
      duration: 1,
    };
    try {
      const response = await axios.put(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/space-rent/upgrade/${spaceRentDataDetail.spaceRentId}`,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        toast.success("Space upgraded successfully.");
        router.push(
          `/coworkingspace/inforent/${spaceRentDataDetail.spaceRentId}`
        );
      }
    } catch (error) {
      toast.error(error.message);
      console.log(spaceRentDataDetail.workspace.image)
    }
  };
  const handleExtendRent = async (e) => {
    const token = localStorage.getItem("token") ?? "";
    const body = {
      workspaceId: spaceRentDataDetail.workspace.id,
      duration: 2,
    };
    try {
      const response = await axios.put(
        `${process.env.NEXT_PUBLIC_COWORKING_API_URL}/api/coworking-space/space-rent/extend/${spaceRentDataDetail.spaceRentId}`,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        toast.success("Space extended successfully.");
        router.push(
          `/coworkingspace/inforent/${spaceRentDataDetail.spaceRentId}`
        );
      }
    } catch (error) {
      toast.error(error.message);
    }
  };



  return (
    <div
      className="relative w-[95%] p-5 md:p-8 gap-8 rounded-2xl border-2 justify-between border-white mx-auto flex flex-col items-center"
      style={{ width: "50%", height: "50%" }}
    >
      <h2>Space Info Detail</h2>
      <p className="py-2">
        <strong>Room {spaceRentDataDetail.spaceRentId}</strong>
      </p>
      <div>
        <img
          style={{
            width: "100%",
            height: "100%",
            borderRadius: "10%",
            border: "1px solid",
            objectFit: "cover",
          }}
          src={spaceRentDataDetail.workspace ? spaceRentDataDetail.workspace.image : 'default-image.jpg'}
          alt="Unable to load picture"
        />
      </div>

      <p>Start Time: {start.toLocaleString()}</p>
      <p>End Time: {end.toLocaleString()}</p>
      <p>
        Time Remaining: {elapsedTime.hours} : {elapsedTime.minutes} :{" "}
        {elapsedTime.seconds}
      </p>
      <select
        value={selectedWorkspaceId || ""}
        onChange={(e) => handleSelect(e)}
      >
        <option value="" disabled>
          Select a workspace
        </option>
        {workspaces.map((workspace) => (
          <option value={workspace.id} key={workspace.id}>
            {workspace.id}
          </option>
        ))}
      </select>
      <button
        className="bg-transparent hover:bg-blue-500 text-gray-800 font-semibold hover:text-white py-2 px-4 border-2 border-blue-300 hover:border-transparent rounded"
        onClick={(e) => handleUpgradeRent(e)}
      >
        Upgrade
      </button>
      <div className="button-container flex justify-between w-full">
        <button
          className="bg-transparent hover:bg-green-500 text-gray-800 font-semibold hover:text-white py-2 px-4 border-2 border-green-300 hover:border-transparent rounded"
          onClick={(e) => handleExtendRent(e)}
        >
          Extend
        </button>
        <button
          className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-4 border-2 border-gray-300 hover:border-transparent rounded"
          onClick={() => {
            router.push(`/coworkingspace/inforent`);
          }}
        >
          Back
        </button>
      </div>

      <Toaster />
      {isCountdownFinished() && <h2>Time is up!</h2>}
    </div>
  );
};