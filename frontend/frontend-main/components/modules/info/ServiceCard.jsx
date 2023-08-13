import React from "react";
import { useRouter } from "next/router";

export const ServiceCard = ({ roomServices }) => {
  const router = useRouter();

  const settings = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
    height: "100%",
  };

  const handleBackButton = () => {
    router.push(`/kost/rentals`);
  };

  return (
    <div>
      <div
        className="w-[95%] p-5 md:p-8 gap-8 rounded-2xl border-2 justify-between border-white flex flex-col"
        style={settings}
      >
        <div className="relative overflow-x-auto shadow-md sm:rounded-lg flex justify-center">
          <table className="w-full text-sm text-left text-gray-500">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Service name
                </th>
                <th scope="col" className="px-6 py-3">
                  Start date
                </th>
                <th scope="col" className="px-6 py-3">
                  End date
                </th>
                <th scope="col" className="px-6 py-3">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {roomServices.map((service) => (
                <tr key={service.id} className="bg-white border-b">
                  <th
                    scope="row"
                    className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap"
                  >
                    Cleaning Service
                  </th>
                  <td className="px-6 py-4">{service.startDate}</td>
                  <td className="px-6 py-4">{service.endDate}</td>
                  <td className="px-6 py-4">{service.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
