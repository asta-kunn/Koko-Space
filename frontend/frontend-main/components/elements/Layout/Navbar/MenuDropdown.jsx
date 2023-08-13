import { Collapse } from "@mui/material";
import React, { useState } from "react";
import { ChevronDownIcon } from "@heroicons/react/20/solid";

import { useRouter } from "next/router";
import { activeStyle, inactiveStyle } from "./style";

const MenuDropdown = ({ name, menuItems }) => {
  const router = useRouter();
  const [openMenu, setOpenMenu] = useState(false);

  const toggleMenuDropdown = () => {
    setOpenMenu(!openMenu);
  };
  return (
    <React.Fragment>
      <button
        className={`${
          menuItems.some((menu) => {
            return router.asPath === menu.href;
          })
            ? activeStyle
            : inactiveStyle
        } flex flex-row items-center justify-start text-[16px]`}
        onClick={() => {
          toggleMenuDropdown();
        }}
      >
        {name}
        <span>
          <ChevronDownIcon width={20} height={20} />
        </span>
      </button>
      <Collapse in={openMenu} unmountOnExit>
        <div className="-mt-2 flex flex-col space-y-2 pl-2">
          {menuItems.map(({ href, name }) => {
            return (
              <div
                key={href + name}
                onClick={() => {
                  router.push(href);
                  toggleMenuDropdown();
                }}
              >
                <p
                  className={`${
                    router.asPath === href ? activeStyle : inactiveStyle
                  }`}
                >
                  {name}
                </p>
              </div>
            );
          })}
        </div>
      </Collapse>
    </React.Fragment>
  );
};

export default MenuDropdown;
