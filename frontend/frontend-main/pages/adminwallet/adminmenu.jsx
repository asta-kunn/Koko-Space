import axios from "axios";
import { TopupList } from "components/modules/wallet";
import { useAuthContext } from "components/context/AuthContext";

import React, { useEffect, useState } from "react";

const AdminMenu = () => {
  const { user } = useAuthContext();

  return (
    <div className="py-10 p-5 md:p-10">
      <p className="text-3xl text-center font-bold py-6">
        Koko Wallet Admin Menu
      </p>
      <TopupList />
    </div>
  );
};

export default AdminMenu;
