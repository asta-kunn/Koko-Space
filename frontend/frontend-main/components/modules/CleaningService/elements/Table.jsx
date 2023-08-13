import React from "react";

export const Table = ({ onStatusChange, data }) => {
  const getStatusColor = (status) => {
    switch (status) {
      case "PENDING":
        return "bg-red-500";
      case "ON_PROCESS":
        return "bg-yellow-500";
      case "FINISHED":
        return "bg-green-500";
      default:
        return "bg-gray-500";
    }
  };

  return (
    <table className="min-w-full table-auto">
      <thead>
        <tr className="bg-gray-800">
          <th className="px-16 py-2">
            <span className="text-gray-200">User Id</span>
          </th>
          <th className="px-16 py-2">
            <span className="text-gray-200">Start Date</span>
          </th>
          <th className="px-16 py-2">
            <span className="text-gray-200">End Date</span>
          </th>
          <th className="px-16 py-2">
            <span className="text-gray-200">Check Out Date</span>
          </th>
          <th className="px-16 py-2">
            <span className="text-gray-200">Option</span>
          </th>
          <th className="px-16 py-2">
            <span className="text-gray-200">Status</span>
          </th>
        </tr>
      </thead>
      <tbody className="bg-gray-200">
        {data.map(({ id, startDate, endDate, status, option, checkOutDate, userId }) =>
          <tr className="bg-gray-50 text-center">
            <td className="px-16 py-2 flex-row items">
              <img src="#" alt="" />
              <span className="text-center ml-2 font-semibold">{userId}</span>
            </td>
            <td className="px-16 py-2">
              <span>{startDate}</span>
            </td>
            <td className="px-16 py-2">
              <span>{endDate}</span>
            </td>
            <td className="px-16 py-2">
              <span>{checkOutDate}</span>
            </td>
            <td className="px-16 py-2">
              <span>{option}</span>
            </td>
            <td className="px-16 py-2">
              <select
                value={status}
                onChange={(event) => onStatusChange(event, id)}
                className={`px-5 py-1 rounded-lg ${getStatusColor(status)}`}
              >
                <option value="PENDING" selected={status === 'PENDING'}>Pending</option>
                <option value="ON_PROCESS" selected={status === 'ON_PROCESS'}>On Process</option>
                <option value="FINISHED" selected={status === 'FINISHED'}>Finished</option>
              </select>
            </td>
          </tr>)}
      </tbody>
    </table>
  );
};
