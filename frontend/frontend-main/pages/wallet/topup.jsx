import { TopupForm } from "components/modules/wallet";
import { useAuthContext } from "components/context/AuthContext";
import { useRouter } from "next/router";
import React from "react";

const TopUp = () => {
  const { user } = useAuthContext();
  return (
    <div className="py-10 p-5 md:p-10">
      <TopupForm />
    </div>
  );
};

export default TopUp;
