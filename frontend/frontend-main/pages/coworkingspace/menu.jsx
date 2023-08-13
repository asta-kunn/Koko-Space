import React, { useState } from "react";
import MenuCoworking from "components/modules/booking/coworkingspace/MenuCoworking";
import MenuPersonal from "components/modules/booking/coworkingspace/MenuPersonal";
import { useAuthContext } from "components/context/AuthContext";

const Menu = ({ datas }) => {
  const { user } = useAuthContext();
  const [selectedMenu, setSelectedMenu] = useState("coworking");
  const [showDropdown, setShowDropdown] = useState(false);

  const handleMenuChange = (menuType) => {
    setSelectedMenu(menuType);
    setShowDropdown(false);
  };
  if (user?.role != "PELANGGAN") {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-3xl font-bold py-6">CoworkingSpace Menu</p>
        <p className="text-2xl font-bold py-6">Anda tidak memiliki akses</p>
      </div>
    );
  }

  return (
    <div className="px-5 lg:px-20">
      <p className="text-3xl font-bold py-6">CoworkingSpace Menu</p>
      <div className="relative inline-block">
        <button
          onClick={() => setShowDropdown("coworking")}
          className="block w-full text-left px-6 py-3 text-lg font-bold bg-white hover:bg-gray-200 focus:outline-none rounded-lg"
        >
          Select a Menu
        </button>
        {showDropdown && (
          <div className="absolute left-0 mt-1 w-full bg-white border rounded-md shadow-lg">
            <button
              onClick={() => handleMenuChange("coworking")}
              className="block w-full text-left px-4 py-2 hover:bg-gray-200 focus:outline-none"
            >
              CoworkingSpace
            </button>
            <button
              onClick={() => handleMenuChange("personal")}
              className="block w-full text-left px-4 py-2 hover:bg-gray-200 focus:outline-none"
            >
              PersonalSpace
            </button>
          </div>
        )}
      </div>
      {selectedMenu === "coworking" ? (
        <MenuCoworking datas={datas} />
      ) : selectedMenu === "personal" ? (
        <MenuPersonal datas={datas} />
      ) : null}
    </div>
  );
};

export default Menu;
