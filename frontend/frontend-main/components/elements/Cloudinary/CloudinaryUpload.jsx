import React from "react";
import { CldUploadButton } from "next-cloudinary";

export const CloudinaryUpload = ({ info, setInfo, error, setError }) => {
  return (
    <div>
      <CldUploadButton
        className="border-blue-800 border px-4 py-2 rounded-lg text-blue-800"
        uploadPreset={process.env.NEXT_PUBLIC_CLOUDINARY_UPLOAD_PRESET}
        onError={(error) => {
          setError(error);
        }}
        onUpload={(result, widget) => {
          setInfo([...info, result.info]); // Updating local state with asset details
        }}
      >
        Upload Images
      </CldUploadButton>
      {error && <p>{error.status}</p>}

      {info.map((a) => {
        <>
          {a.resource_type === "image" && (
            <p>
              <img
                width={a.width}
                height={a.height}
                src={a.secure_url}
                alt="Uploaded image"
              />
              Test
            </p>
          )}
          <p> {a?.secure_url}</p>
        </>;
      })}
    </div>
  );
};
