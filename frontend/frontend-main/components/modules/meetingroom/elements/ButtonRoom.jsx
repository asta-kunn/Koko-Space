import React from "react";

export const ButtonRoom = ({ className, ...props }) => {
  return (
    <button
      {...props}
      className={`${className} px-8 py-4 bg-blue-700 rounded-2xl`}
    >
      <p className="text-[14px] text-center text-white font-bold">
        {...props.children}
      </p>
    </button>
  );
};
