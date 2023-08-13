import React from "react";

export const InputElement = ({ name, placeholder, type, refs }) => {
  return (
    <div className="flex justify-between my-2 mx-2 items-center">
      <label htmlFor={name} className="pr-4 capitalize">
        {name}
      </label>
      <input
        ref={refs}
        type={type}
        id={name}
        name={name}
        placeholder={placeholder}
        className="border-2 px-1 rounded-md"
      ></input>
    </div>
  );
};
