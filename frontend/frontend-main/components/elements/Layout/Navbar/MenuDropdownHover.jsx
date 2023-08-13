import React from "react";
import HoverMenu from "material-ui-popup-state/HoverMenu";
import MenuItem from "@mui/material/MenuItem";
import {
  usePopupState,
  bindHover,
  bindMenu,
} from "material-ui-popup-state/hooks";
import { useRouter } from "next/router";
import {
  activeDropdownItemStyle,
  activeStyle,
  inactiveStyle,
  inactiveStyleDropdown,
} from "./style";
import { ChevronDownIcon } from "@heroicons/react/20/solid";

const MenuDropdownHover = ({ name, menuItems }) => {
  const popupState = usePopupState({
    variant: "popover",
    popupId: "demoMenu",
  });
  const router = useRouter();
  return (
    <React.Fragment>
      <button
        className={`${
          menuItems.some((menu) => {
            return router.asPath === menu.href;
          })
            ? activeStyle
            : inactiveStyle
        }  flex flex-row items-center`}
        {...bindHover(popupState)}
      >
        {name}
        <span>
          <ChevronDownIcon width={20} height={20} />
        </span>
      </button>

      <HoverMenu
        {...bindMenu(popupState)}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
        transformOrigin={{ vertical: "top", horizontal: "left" }}
      >
        {menuItems.map(({ href, name }) => {
          return (
            <MenuItem
              key={name}
              onClick={() => {
                router.push(href);
                popupState.close;
              }}
            >
              <p
                className={`${
                  router.asPath === href
                    ? activeDropdownItemStyle
                    : inactiveStyleDropdown
                }`}
              >
                {name}
              </p>
            </MenuItem>
          );
        })}
      </HoverMenu>
    </React.Fragment>
  );
};

export default MenuDropdownHover;
