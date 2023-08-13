import React from "react";
import LoadingButton from "@mui/lab/LoadingButton";

export const Button = ({ className, ...props }) => {
  return (
    <LoadingButton
      {...props}
      sx={{
        color: "#1e3a8a",
        ":hover": {
          bgcolor: "#1e3a8a",
          color: "white",
        },
      }}
      variant="outlined"
      className={`${className} py-2 rounded-lg`}
    >
      {...props.children}
    </LoadingButton>
  );
};
