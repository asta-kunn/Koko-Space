import { HistoryElement } from "components/modules/auth/elements/HistoryElement";
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuthContext } from "components/context/AuthContext";

const History = () => {
  const { user } = useAuthContext();
  const [historyData, setHistoryData] = useState([]);

  useEffect(() => {
    const fetchHistoryData = async () => {
      axios
        .get("/api/wallet/get-all-expenses", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((response) => {
          if (response.status == 200) {
            setHistoryData(response.data);
            console.log(response.data)
          }
        })
        .catch((error) => {
          toast.error(error?.message);
        });
    };

    fetchHistoryData()
  }, [user]);

  return (
    <div>
      <h1 className="text-center text-3xl py-4">Payment History</h1>
      
          <>
            {historyData.map((history) => {
              return (
                <HistoryElement
                  key={history.id}
                  rentalName={history.rentalName}
                  fee={history.fee}
                  date={history.date}
                />
              );
            })}
          </>
        </div>
  )
};

export default History;
