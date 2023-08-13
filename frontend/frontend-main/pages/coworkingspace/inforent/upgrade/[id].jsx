import { UpgradeDetail } from "components/modules/booking/coworkingspace/upgradenextend";
import { COWORKING_ROOM } from "@constants";
import { useRouter } from "next/router";
import React from "react";

const UpgradeDetailPage = () => {
  const router = useRouter();
  const { id } = router.query;
  return (
    <div className="py-10 p-5 md:p-10">
      {router.isReady && <UpgradeDetail {...COWORKING_ROOM[id - 1]} />}
    </div>
  );
};

export default UpgradeDetailPage;
