import React, { useState } from "react";
import { toast } from "react-hot-toast";

import { InputAdornment, MenuItem, TextField } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import { Button } from "../elements";

export const UpdateKostForm = ({ data }) => {
  const router = useRouter();

  const [name, setName] = useState(data.name);
  const [type, setType] = useState(data.type);

  const [facilities, setFacilities] = useState(data.facilities.join(", "));
  const [stock, setStock] = useState(data.stock);
  const [price, setPrice] = useState(data.price);
  const [discount, setDiscount] = useState(data.discount);
  const [minDiscount, setMinDiscount] = useState(data.minDiscountDuration);

  // form submit
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token") ?? "";
    setLoading(true);
    var body = {
      name: name,
      type: type,
      facilities: facilities.split(", ").filter((element) => element),
      stock: stock,
      price: price,
      discount: discount,
      minDiscountDuration: minDiscount,
    };
    // will use cookies later on

    await axios
      .patch(`/api/kost/update/${data.id}`, body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        if (response.status == 200) {
          toast.success("Successfully updated kost");
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
      label: "Putri",
    },
    {
      value: "PUTRA",
      label: "Putra",
    },
    {
      value: "CAMPUR",
      label: "Campur",
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
          disabled
          value={data.address}
          required
          label="Address"
          type="text"
          id="address"
        />
        <div className="flex flex-row gap-x-2">
          <TextField
            disabled
            value={data.city}
            required
            label="City"
            type="text"
            id="city"
            className="w-1/2"
          />
          <TextField
            disabled
            value={data.country}
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

        <p className="text-gray-600">Images are not editable</p>

        <div className="flex justify-center">
          <Button loading={loading} type="submit" className="w-48 ">
            Update Kost
          </Button>
        </div>
      </form>
    </div>
  );
};
