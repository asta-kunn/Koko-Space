import React from "react";

export const TemanMenginapCard = ({id, name, email, reason, bookingStatus}) => {
    return (
        <div className="w-full md:w-[300px] p-5 md:p-8 gap-5 rounded-2xl border-2 justify-between">
            <div className="p-4">
                <h2 className="text-xl font-bold">{name}</h2>
                <p className="text-gray-600">{email}</p>
                <p>{reason}</p>
                <p className="font-semibold">{bookingStatus}</p>
            </div>
        </div>
    );
};
