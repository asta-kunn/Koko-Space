import React, { useState } from "react";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";
import { NAV_ROUTE } from "@constants";
import Link from "next/link";
import { Bars3BottomLeftIcon } from "@heroicons/react/20/solid";
import { useRouter } from "next/router";
import MenuDropdown from "./MenuDropdown";

export const NavbarMobile = () => {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const toggleDrawer = (open) => (event) => {
    if (
      event.type === "keydown" &&
      (event.key === "Tab" || event.key === "Shift")
    ) {
      return;
    }
    //changes the function state according to the value of open
    setOpen(open);
  };
  return (
    <React.Fragment>
      <a onClick={toggleDrawer(true)}>
        <Bars3BottomLeftIcon width={32} height={32} />
      </a>
      <SwipeableDrawer
        anchor="right"
        open={open}
        onClose={toggleDrawer(false)}
        onOpen={toggleDrawer(true)}
      >
        <div className="bg-indigo-900 h-full w-48 flex flex-col gap-4 px-10 py-5">
          {NAV_ROUTE.map(({ id, href, name, dropdownMenu }) => {
            return !!dropdownMenu ? (
              <MenuDropdown
                key={id}
                name={name}
                menuItems={dropdownMenu}
                toggleDrawer={toggleDrawer}
              />
            ) : (
              <Link key={id} href={href} onClick={toggleDrawer(false)}>
                <div>
                  <button
                    className={
                      router.asPath === href
                        ? "font-semibold text-[16px] text-sky-200"
                        : "text-[16px] text-white"
                    }
                  >
                    {name}
                  </button>
                </div>
              </Link>
            );
          })}
        </div>
      </SwipeableDrawer>
    </React.Fragment>
  );
};
