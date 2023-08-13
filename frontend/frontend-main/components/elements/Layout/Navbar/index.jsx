import React from "react";
import MenuDropdownHover from "./MenuDropdownHover";
import { NAV_ROUTE } from "@constants";
import Link from "next/link";
import { useRouter } from "next/router";
import { NavbarMobile } from "./NavbarMobile";
import { useAuthContext } from "components/context/AuthContext";

export const Navbar = () => {
  const router = useRouter();
  const { user, logout } = useAuthContext();

  return (
    <div className="fixed z-[100] items-center h-16 top-0 w-full text-white flex flex-row justify-between px-10 py-4 bg-indigo-900">
      <a className="text-xl font-bold" href="/">
        KOKO SPACE
      </a>
      <div className="flex justify-end items-center">
        <div className="hidden md:flex flex-row gap-10 ">
          {NAV_ROUTE.map(({ id, href, name, dropdownMenu }) => {
            return !!dropdownMenu ? (
              name != "Auth" || !user ? (
                  name == "Account" && !user ? (
                    <div/>
              ) : (
                <MenuDropdownHover
                    key={id}
                    name={name}
                    menuItems={dropdownMenu}
                    />
                  ) ): (
                <div
                  className="text-[16px] text-white cursor-pointer"
                  onClick={logout}
                  key={id}
                >
                  Log out
                </div>
              )
            ) : (
              <Link key={id} href={href}>
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
        <div className="md:hidden">
          <NavbarMobile />
        </div>
      </div>
    </div>
  );
};
