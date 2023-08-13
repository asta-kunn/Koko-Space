import React, { useState } from "react";
import { TextField, MenuItem } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import { Button } from "../elements";

import { useAuthContext } from "components/context/AuthContext";

export const MemesanForm = () => {
  const router = useRouter();
  const [type, setType] = useState("");
  const [startDate, setStartDate] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { user } = useAuthContext();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    if (!type) {
      setError("Please select an option");
      setLoading(false);
      return;
    }

    if (!startDate) {
      setError("Please provide a start date");
      setLoading(false);
      return;
    }

    const formattedStartDate = new Date(startDate).toISOString().split('T')[0];

    var body = {
      option: type,
      startDate: formattedStartDate,
      userId: user.id
    };

    const token = localStorage.getItem("token")
    try {
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_KOST_API_URL}/cleaningService-orders/create`,
        body,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (response.status === 201) {
        router.push(`/CleaningService/monitoring`); // Redirect to the monitoring page
      }
    } catch (error) {
      console.log(error);
      setError("Error occurred while ordering Cleaning Service");
    } finally {
      setLoading(false);
    }
  };

  const types = [
    {
      value: "Perhari",
      label: "Perhari",
    },
    {
      value: "Perbulan",
      label: "Perbulan",
    },
  ];

  return (
    <div>
      <p className="text-xl md:text-3xl font-bold p-5">Pilihan Berlangganan</p>
      <form
        onSubmit={handleSubmit}
        className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
      >
        <TextField
          value={type}
          onChange={(event) => setType(event.target.value)}
          required
          select
          label="Option"
          id="type"
          defaultValue=""
          error={Boolean(error)}
          helperText={error}
        >
          {types.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>

        <TextField
          value={startDate}
          onChange={(event) => setStartDate(event.target.value)}
          required
          label="Start Date"
          type="date"
          id="startDate"
          InputLabelProps={{
            shrink: true,
          }}
        />

        <div className="flex justify-center">
          <Button
            loading={loading}
            type="submit"
            className="w-48"
            disabled={loading}
          >
            {loading ? "Submitting..." : "Submit"}
          </Button>
        </div>
      </form>
    </div>
  );
};

export default MemesanForm;
