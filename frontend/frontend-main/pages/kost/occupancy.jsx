import { TenantCard, WithoutTenantCard } from "@components";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import Select from "react-select";
import axios from "axios";
import dynamic from "next/dynamic";
import { useAuthContext } from "components/context/AuthContext";

const Occupancy = () => {
  const router = useRouter();
  const { user } = useAuthContext();
  const [tenantData, setTenantData] = useState([]);
  const [withoutTenantData, setWithoutTenantData] = useState([]);
  const [selectedOption, setSelectedOption] = useState("all");
  const [searchInput, setSearchInput] = useState("");

  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(true); // Add loading state

  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
  };

  useEffect(() => {
    const token = localStorage.getItem("token")
    const fetchOccupancyData = async () => {

      try {
        setErrorMessage(""); // Reset the error message
        setIsLoading(true); // Set loading state
        
        const submittedInput = router.query.submittedInput || "";
        let tenantResponse, withoutTenantResponse;

        if (submittedInput === undefined || submittedInput === "") {
          tenantResponse = await axios.get(
            `${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/tenants/all`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          withoutTenantResponse = await axios.get(
            `${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/without-tenants/all`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
        } else {
          tenantResponse = await axios.get(
            `${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/tenants/all/${submittedInput}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          withoutTenantResponse = await axios.get(
            `${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/without-tenants/all/${submittedInput}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
        }
        if (tenantResponse.status === 200 && withoutTenantResponse.status === 200) {
          setTenantData(tenantResponse.data);
          setWithoutTenantData(withoutTenantResponse.data);
          setIsLoading(false); // Data fetching completed
        }
      } catch (error) {
        if (
          error.response &&
          error.response.data &&
          error.response.data.httpStatus === "NOT_FOUND"
        ) {
          setErrorMessage(
            error.response.data.message
            );
        } else {
          setErrorMessage(
            "There's a problem when trying to fetch the data. Please try reloading this page"
          );
        }
        setIsLoading(false); // Data fetching completed
      }
    };
    if (user && user.id) {
      fetchOccupancyData();
    }
  }, [user, router.query.submittedInput]);

  const Select = dynamic(() => import("react-select"), { ssr: false });

  const handleSelectChange = (selected) => {
    setSelectedOption(selected.value);
  };

  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    router.push({
      pathname: router.pathname,
      query: { submittedInput: searchInput },
    });
  };

  const handleHomeButton = () => {
    router.push(`/`);
  };

  const options = [
    { value: "occupancy", label: "View all" },
    { value: "tenants", label: "View tenants" },
    { value: "without-tenants", label: "View without tenants" },
  ];

  const customStyles = {
    menu: (provided) => ({
      ...provided,
      width:
        selectedOption === "tenants"
          ? "148px"
          : selectedOption === "without-tenants"
            ? "203px"
            : "110px",
    }),
    control: (provided) => ({
      ...provided,
      border: "none",
      boxShadow: "none",
      backgroundColor: "grey-800",
    }),
    option: (provided, { isFocused }) => ({
      ...provided,
      backgroundColor: isFocused ? "black" : "transparent",
      color: isFocused ? "white" : "gray",
      ":hover": {
        backgroundColor: "black",
        color: "white",
      },
    }),
  };

  return (
    <div>
      {isLoading && user !== undefined && user !== null ? ( // Show loading state while fetching data
        <div className="pb-8 pt-10 lg:px-20">
          <div className="flex flex-wrap justify-center pt-8">
            <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
              <p className="text-xl font-semibold pt-3 text-center">Loading...</p>
            </div>
          </div>
      </div>
      ) : user !== undefined && user !== null && user.role === "PENGELOLA" ? (
        <div className="pb-8 pt-10 lg:px-20">
          <p className="text-3xl font-bold pt-8 pb-10 flex flex-wrap justify-center">
            The State of Occupancy
          </p>
          <form className="pt-2 pb-4" onSubmit={handleSearchSubmit}>
            <div className="flex flex-wrap justify-center">
              <Select
                className="flex-shrink-0 z-10 inline-flex items-center pl-3 text-sm font-medium text-gray-900 bg-gray-100 border border-gray-300 rounded-l-lg hover:bg-gray-200 focus:ring-4 focus:outline-none focus:ring-gray-100"
                options={options}
                value={options.find(
                  (option) => option.value === selectedOption
                )}
                onChange={handleSelectChange}
                styles={customStyles}
              />
              <div className="relative" style={{ width: "500px" }}>
                <input
                  type="search"
                  className="block p-2.5 w-full z-20 text-sm text-gray-900 bg-gray-50 rounded-r-lg border-l-gray-50 border-l-2 border border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Search by room name"
                  value={searchInput}
                  onChange={handleSearchInputChange}
                />
                <button
                  type="submit"
                  className="absolute top-0 right-0 p-2.5 text-sm font-medium text-white bg-blue-700 rounded-r-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300"
                >
                  <svg
                    className="w-5 h-5"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                    ></path>
                  </svg>
                </button>
              </div>
            </div>
          </form>
          {errorMessage === "" ? (
            <div className="flex flex-wrap justify-center">
              {selectedOption === "tenants" ? (
                tenantData.length > 0 && withoutTenantData.length !== 0 ? (
                  tenantData.map((tenant) => (
                    <TenantCard key={tenant.id} {...tenant} />
                  ))
                ) : tenantData.length === 0 &&
                  withoutTenantData.length === 0 ? (
                  <div className="flex flex-wrap justify-center pt-8">
                    <div className="w-[340px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                      <p className="text-lg pt-3 text-center">
                        There aren't any rooms yet
                      </p>
                    </div>
                  </div>
                ) : (
                  <div className="flex flex-wrap justify-center pt-8">
                    <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                      <p className="text-lg pt-3 text-center">
                        There are no tenants that are currently occupying that
                        room
                      </p>
                    </div>
                  </div>
                )
              ) : selectedOption === "without-tenants" ? (
                withoutTenantData.length > 0 && tenantData !== 0 ? (
                  withoutTenantData.map((withoutTenant) => (
                    <WithoutTenantCard
                      key={withoutTenant.id}
                      {...withoutTenant}
                    />
                  ))
                ) : tenantData.length === 0 &&
                  withoutTenantData.length === 0 ? (
                  <div className="flex flex-wrap justify-center pt-8">
                    <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                      <p className="text-lg pt-3 text-center">
                        There aren't any rooms yet
                      </p>
                    </div>
                  </div>
                ) : (
                  <div className="flex flex-wrap justify-center pt-8">
                    <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                      <p className="text-lg pt-3 text-center">
                        All rooms are currently occupied
                      </p>
                    </div>
                  </div>
                )
              ) : tenantData.length === 0 && withoutTenantData.length === 0 ? (
                <div className="flex flex-wrap justify-center pt-8">
                  <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                    <p className="text-lg pt-3 text-center">
                      There aren't any rooms yet
                    </p>
                  </div>
                </div>
              ) : (
                <React.Fragment>
                  {tenantData.map((tenant) => (
                    <TenantCard key={tenant.id} {...tenant} />
                  ))}
                  {withoutTenantData.map((without_tenant) => (
                    <WithoutTenantCard
                      key={without_tenant.id}
                      {...without_tenant}
                    />
                  ))}
                </React.Fragment>
              )}
            </div>
          ) : (
            <div className="flex flex-wrap justify-center pt-8">
              <div className="w-[370px] p-5 pb-8 rounded-2xl border-2 border-gray-300 ">
                <p className="text-lg pt-3 text-center">{errorMessage}</p>
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="py-10 p-5 md:p-9 flex flex-wrap justify-center items-center mt-20">
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
        </div>
      )}
    </div>
  );
};

// export const getServerSideProps = async (context) => {
//   try {
//     const { submittedInput } = context.query;
//     let tenantResponse, withoutTenantResponse;

//     if (submittedInput === undefined || submittedInput === "") {
//       tenantResponse = await axios.get(
//         `${process.env.NEXT_PUBLIC_API_URL}/kost/tenants/all`, {
//         headers: {
//           Authorization: `Bearer ${token}`
//         }
//       })
//       withoutTenantResponse = await axios.get(
//         `${process.env.NEXT_PUBLIC_API_URL}/kost/without-tenants/all`, {
//         headers: {
//           Authorization: `Bearer ${token}`
//         }
//       })
//     } else {
//       tenantResponse = await axios.get(
//         `${process.env.NEXT_PUBLIC_API_URL}/kost/tenants/all/${submittedInput}`, {
//         headers: {
//           Authorization: `Bearer ${token}`
//         }
//       })
//       withoutTenantResponse = await axios.get(
//         `${process.env.NEXT_PUBLIC_API_URL}/kost/without-tenants/all/${submittedInput}`, {
//         headers: {
//           Authorization: `Bearer ${token}`
//         }
//       })
//     }
//     return {
//       props: {
//         tenantData: tenantResponse.data,
//         withoutTenantData: withoutTenantResponse.data,
//       },
//     };
//   } catch (error) {
//     if (
//       error.response &&
//       error.response.data &&
//       error.response.data.httpStatus === "NOT_FOUND"
//     ) {
//       return {
//         props: {
//           errorMessage: error.response.data.message,
//         },
//       };
//     }
//     return {
//       props: {
//         errorMessage:
//           "There's a problem when trying to fetch the data. Please try reloading this page",
//       },
//     };
//   }
// };

export default Occupancy;
