import React, { useEffect, useState } from "react";
import { Table } from "../elements/Table";
import axios from "axios";
import { TOKEN_PELANGGAN, TOKEN_PENGELOLA } from "@constants";

export const MonitoringForm = () => {
  const [visible, setVisible] = useState(false);
  const [cleaningServiceOrders, setCleaningServiceOrders] = useState([])

  const getCleaningServiceOrders = async () => {
    const { data } = await axios.get(`${process.env.NEXT_PUBLIC_KOST_API_URL}/cleaningService-orders/`)

    setCleaningServiceOrders(data)
  }

  useEffect(() => {
    getCleaningServiceOrders()
  }, [])

  const handleEdit = () => {
    setVisible((prevVisible) => !prevVisible); // toggle the value of visible
  };

  const handleStatusChange = async (event, id) => {
    await axios.put(
      `${process.env.NEXT_PUBLIC_KOST_API_URL}/cleaningService-orders/update/${id}`,
      {
        status: event.target.value
      },
      { headers: { Authorization: `Bearer ${TOKEN_PENGELOLA}` } }
    )

    getCleaningServiceOrders()
  };

  return (
    <div className="container mx-auto py-5">
      <p className="text-xl md:text-3xl font-bold p-5">
        Monitoring Cleaning Service
      </p>
      <div className="container items-center">
        <Table
          onEdit={handleEdit}
          onStatusChange={handleStatusChange}
          data={cleaningServiceOrders}
        />
      </div>
    </div>
  );
};
