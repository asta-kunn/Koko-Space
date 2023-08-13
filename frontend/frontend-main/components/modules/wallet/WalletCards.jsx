import React, { useRef } from "react";
import { useRouter } from "next/router";

export const CardsTransaction = ({ id, type, amount, createdAt, verified }) => {
  const date = () => {
    const dateValue = new Date(createdAt);

    const month = (dateValue.getUTCMonth() + 1).toString().padStart(2, "0"); // Months are zero-based
    const day = dateValue.getUTCDate().toString().padStart(2, "0");
    const year = dateValue.getUTCFullYear().toString();

    const mdyFormat = `${month}-${day}-${year}`;

    return mdyFormat;
  };

  const verif = () => {
    return verified ? "Has Been Verified" : "Not Yet Verified";
  };

  return (
    <div>
      <div className="grid grid-cols-2 gap-2 p-3 m-3 rounded-xl shadow-md">
        <div>
          <p> {type} </p>
          <p> {amount} </p>
        </div>
        <div className="text-right">
          <p> {date()} </p>
          <p> {verif()}</p>
        </div>
      </div>
    </div>
  );
};
