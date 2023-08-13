import React from "react";
import { Button, IconButton, Typography } from "@mui/material";
import { PlusCircleIcon } from "@heroicons/react/20/solid";
import { useAuthContext } from "../../../components/context/AuthContext";
import { useRouter } from "next/router";

const TemanMenginap = () => {
    const { user } = useAuthContext();
    const router = useRouter();
    return (
        <div className="p-10 md:p-20 bg-white shadow rounded-lg flex flex-col items-center">
            <Typography variant="h3" className="font-bold pb-6">Teman Menginap</Typography>
            <div className="flex flex-wrap gap-6 justify-center md:justify-start">
                <Button
                    onClick={() => {
                        router.push("/kost/teman_menginap/form");
                    }}
                    color="primary"
                    size="large"
                >
                    Create
                </Button>
                <Button
                    onClick={() => {
                        router.push("/kost/teman_menginap/history");
                    }}
                    size="large"
                    color="primary"
                >
                    History
                </Button>
            </div>
        </div>
    );
};

export default TemanMenginap;
