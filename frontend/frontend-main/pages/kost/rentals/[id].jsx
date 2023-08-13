import { ServiceCard } from "@components";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { useAuthContext } from "components/context/AuthContext";
import axios from "axios";

const RentalService = () => {
  const router = useRouter();
  const { id } = router.query;
  const { user } = useAuthContext();
  const [rentalData, setRentalData] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(true); // Add loading state

  useEffect(() => {
    const token = localStorage.getItem("token")
    const fetchRentalData = async () => {
      try {
        const rentalResponse = await axios.get(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/rentals/all`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        if (rentalResponse.status === 200) {
          setRentalData(rentalResponse.data);
          setIsLoading(false); // Data fetching completed
        }
      } catch (error) {
        setErrorMessage(
          "There's a problem when trying to fetch the data. Please try reloading this page"
        );
        setIsLoading(false); // Data fetching completed
      }
    };

    if (user && user.id) {
      fetchRentalData();
    }
  }, [user]);

  const handleHomeButton = () => {
    router.push(`/`);
  };

  const handleBackButton = () => {
    router.push(`/kost/rentals`);
  };

  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
  };

  const isFound =
    rentalData !== undefined &&
    Number(id) >= 0 &&
    rentalData.length - 1 >= Number(id)
      ? true
      : false;

  return (
    <div className="py-10 p-5 md:p-9 flex flex-wrap justify-center items-center mt-20">
      {isLoading && user !== undefined && user !== null ? ( // Show loading state while fetching data
        <div className="flex flex-wrap justify-center pt-8">
          <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
            <p className="text-xl font-semibold pt-3 text-center">Loading...</p>
          </div>
        </div>
      ) : user !== undefined && user !== null && user.role === "PELANGGAN" ? (
        <div>
          {errorMessage === "" ? (
            !isFound ? (
              <div className="w-[400px] p-5 md:p-8 gap-5 rounded-2xl border-2 border-gray-300">
                <p className="text-xl font-semibold pt-3 text-center">
                  You don't have that rental
                </p>
                <div className="pt-8" style={settings}>
                  <button
                    className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-5 border-2 border-gray-300 hover:border-transparent rounded"
                    onClick={handleBackButton}
                  >
                    Back
                  </button>
                </div>
              </div>
            ) : (
              <div>
                <p className="text-3xl font-bold pt-8 pb-10 flex flex-wrap justify-center">
                  Room {rentalData[Number(id)].kostRent.roomNumber}'s Services
                </p>
                {rentalData[Number(id)].roomServices.length === 0 ? (
                  <div className="pt-5">
                    <div className="w-[370px] px-6 pt-4 pb-6 rounded-2xl border-2 border-gray-300">
                      <p className="text-lg pt-3 text-center">
                        You haven't ordered any services for this room
                      </p>
                      <div className="pt-7" style={settings}>
                        <button
                          className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-5 border-2 border-gray-300 hover:border-transparent rounded"
                          onClick={handleBackButton}
                        >
                          Back
                        </button>
                      </div>
                    </div>
                  </div>
                ) : (
                  <div>
                    {router.isReady && (
                      <ServiceCard {...rentalData[Number(id)]} />
                    )}
                    <div className="pt-8" style={settings}>
                      <button
                        className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-6 border-2 border-gray-300 hover:border-transparent rounded"
                        onClick={handleBackButton}
                      >
                        Back
                      </button>
                    </div>
                  </div>
                )}
              </div>
            )
          ) : (
            <div className="flex flex-wrap justify-center items-center pt-8">
              <div className="w-[370px] p-5 pb-7 rounded-2xl border-2 border-gray-300 ">
                <p className="text-lg pt-3 text-center">{errorMessage}</p>
                <div className="pt-8" style={settings}>
                  <button
                    className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-5 border-2 border-gray-300 hover:border-transparent rounded"
                    onClick={handleBackButton}
                  >
                    Back
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="w-[400px] p-5 md:p-8 gap-5 rounded-2xl border-2 border-gray-300">
          <p className="text-xl font-semibold pt-3 text-center">
            Only admins can view this page
          </p>
          <div className="pt-9" style={settings}>
            <button
              className="bg-transparent hover:bg-black text-gray-800 font-semibold hover:text-white py-2 px-5 border-2 border-gray-300 hover:border-transparent rounded"
              onClick={handleHomeButton}
            >
              Home
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RentalService;
