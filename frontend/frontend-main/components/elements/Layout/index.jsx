import React from "react";

import { Footer } from "./Footer";
import { Navbar } from "./Navbar";

export const Layout = (props) => {
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />
      <div className="pt-16">{props.children}</div>
      <Footer />
    </div>
  );
};
