import React, { useEffect, useState } from "react";
import { toast } from "react-hot-toast";

import { InputAdornment, MenuItem, TextField } from "@mui/material";
import { CloudinaryUpload } from "components/elements";
import axios from "axios";
import { useRouter } from "next/router";
import { Button } from "../elements";

export const CreateKostForm = () => {
  const router = useRouter();

  // form imputs
  const [name, setName] = useState("");
  const [type, setType] = useState("");
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("");
  const [facilities, setFacilities] = useState(""); // nanti di convert ke array
  const [stock, setStock] = useState(0);
  const [price, setPrice] = useState(0);
  const [discount, setDiscount] = useState(0);
  const [minDiscount, setMinDiscount] = useState(0);
  // upload to cloudinary
  const [info, setInfo] = useState([]);
  const [errorCloudinary, setErrorCloudinary] = useState();

  // form submit
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    setLoading(true);

    let images = info.map((a) => a.secure_url);
    var body = {
      name: name,
      type: type,
      address: address,
      city: city,
      country: country,
      facilities: facilities.split(", ").filter((element) => element),
      stock: stock,
      price: price,
      discount: discount,
      minDiscountDuration: minDiscount,
      images: images,
    };

    await axios
      .post(`/api/kost/create`, body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        if (response.status == 201) {
          toast.success("Successfully added kost");
          // redirect ke detail pagenya
          router.push(`/kost/${response?.data?.id}`);
        }
      })
      .catch((error) => {
        toast.error(error?.message);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const types = [
    {
      value: "PUTRI",
      label: "PUTRI",
    },
    {
      value: "PUTRA",
      label: "PUTRA",
    },
    {
      value: "CAMPUR",
      label: "CAMPUR",
    },
  ];

  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Add Kost</p>
      <form
        onSubmit={(event) => handleSubmit(event)}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <TextField
          value={name}
          onChange={(event) => {
            setName(event.target.value);
          }}
          required
          label="Name"
          type="text"
          id="name"
        />
        <TextField
          value={type}
          onChange={(event) => {
            setType(event.target.value);
          }}
          required
          select
          label="Type"
          type="text"
          id="type"
          defaultValue="PUTRI"
        >
          {types.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          value={address}
          onChange={(event) => {
            setAddress(event.target.value);
          }}
          required
          label="Address"
          type="text"
          id="address"
        />
        <div className="flex flex-row gap-x-2">
          <TextField
            value={city}
            onChange={(event) => {
              setCity(event.target.value);
            }}
            required
            label="City"
            type="text"
            id="city"
            className="w-1/2"
          />
          <TextField
            value={country}
            onChange={(event) => {
              setCountry(event.target.value);
            }}
            required
            label="Country"
            type="text"
            id="country"
            className="w-1/2"
          />
        </div>

        <TextField
          value={facilities}
          onChange={(event) => {
            setFacilities(event.target.value);
          }}
          required
          label="Facilities"
          type="text"
          id="facilities"
          multiline
          rows={2}
          helperText="Write comma-seperated values."
        />
        <div className="grid grid-cols-2 gap-x-2 gap-y-4">
          <TextField
            value={stock}
            onChange={(event) => {
              setStock(event.target.value);
            }}
            required
            label="Stock"
            type="number"
            id="stock"
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">room&#40;s&#41;</InputAdornment>
              ),
            }}
          />
          <TextField
            value={price}
            onChange={(event) => {
              setPrice(event.target.value);
            }}
            required
            label="Price"
            type="number"
            id="price"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">Rp</InputAdornment>
              ),
            }}
          />

          <TextField
            value={discount}
            onChange={(event) => {
              setDiscount(event.target.value);
            }}
            className="col-span-2 md:col-span-1"
            required
            label="Discount"
            type="number"
            id="discount"
            InputProps={{
              endAdornment: <InputAdornment position="end">%</InputAdornment>,
            }}
          />
          <TextField
            value={minDiscount}
            onChange={(event) => {
              setMinDiscount(event.target.value);
            }}
            className="col-span-2 md:col-span-1"
            required
            label="Minimal Duration"
            type="number"
            id="minDiscount"
            helperText="Minimum duration for discount to be applied."
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">months</InputAdornment>
              ),
            }}
          />
        </div>

        <p className="text-gray-600">Upload images to Cloudinary *</p>
        <CloudinaryUpload
          id="images"
          info={info}
          setInfo={setInfo}
          error={errorCloudinary}
          setError={setErrorCloudinary}
        />
        {!!errorCloudinary && <p>{errorCloudinary}</p>}
        <div className="flex justify-center">
          <Button loading={loading} type="submit" className="w-48 ">
            Add Kost
          </Button>
        </div>
      </form>
    </div>
  );
};
