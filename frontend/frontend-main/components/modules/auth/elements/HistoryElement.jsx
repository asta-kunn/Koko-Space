import React from "react";

export const HistoryElement = ({ id, rentalName, fee, date }) => {
  return (
    <div className="lg:w-1/2 w-3/4 mx-auto px-3 py-2 border-2 mb-2 border-black rounded-md">
      <h5>{rentalName}</h5>
      <h5>{fee}</h5>
      <h5>{date}</h5>
    </div>
  );
};
