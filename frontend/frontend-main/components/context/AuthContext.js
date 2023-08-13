import { useState, createContext, useContext, useEffect } from "react";

import axios from "axios";

import Router, { useRouter } from "next/router";
import jwt from "jwt-decode";
import { toast } from "react-hot-toast";

const AuthContext = createContext();

export const useAuthContext = () => useContext(AuthContext);

export const AuthContextProvider = ({ children }) => {
  const [user, setUser] = useState();
  const router = useRouter();

  const login = async (data) => {
    const postLogin = axios
      .post("/api/auth/login", { email: data.email, password: data.password })
      .then((response) => {
        const { token, ...data } = response.data;
        // toast.success("Welcome back! You have successfully logged in");
        setUser(data);
        localStorage.setItem("token", token);
        Router.push("/");
      });

    toast.promise(postLogin, {
      loading: "Loading...",
      success: "Welcome back! You have successfully logged in",
      error: (err) => err.response.data.message,
    });
  };

  const logout = () => {
    setUser(null);
    toast.success("Successfully log out");
    localStorage.removeItem("token");
  };

  const assertAlive = (decoded) => {
    const now = Date.now().valueOf() / 1000;
    if (typeof decoded.exp !== "undefined" && decoded.exp < now) {
      localStorage.removeItem("token");
      setUser(null);
      Router.push("/auth/login");
      throw new Error(
        `Your session has expired. Please log in again to continue`
      );
    }
  };

  useEffect(() => {
    try {
      const token = localStorage.getItem("token");
      if (!token || token === "undefined") {
        throw new Error();
      }
      assertAlive(jwt(token));
      setUser(jwt(token));
    } catch (err) {
      if (err.message) toast.error(err.message);
    }
  }, [router.asPath]);

  const value = {
    user,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
