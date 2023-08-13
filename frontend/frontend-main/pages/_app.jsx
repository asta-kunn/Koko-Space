import "@/styles/globals.css";
import { Layout } from "components/elements";
import { AuthContextProvider } from "components/context/AuthContext";
import { Toaster } from "react-hot-toast";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import Head from "next/head";

export default function App({ Component, pageProps }) {
  const theme = createTheme({
    typography: {
      fontFamily: ["Poppins", "sans-serif"].join(","),
    },
  });
  return (
    <>
      <Head>
        <title>Koko Space</title>
      </Head>
      <ThemeProvider theme={theme}>
        <AuthContextProvider>
          <Layout>
            <Toaster />
            <Component {...pageProps} />
          </Layout>
        </AuthContextProvider>
      </ThemeProvider>
    </>
  );
}
