import React, { useState } from "react";
import { toast } from "react-hot-toast";
import { TextField } from "@mui/material";
import axios from "axios";
import { useRouter } from "next/router";
import { Button } from "@components";
import {useAuthContext} from "../../context/AuthContext";

export const TemanMenginapForm = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [reason, setReason] = useState("");
    const [loading, setLoading] = useState(false);

    const router = useRouter();
    const { user } = useAuthContext();

    const handleSubmit = async (event) => {
        event.preventDefault();

        const token = localStorage.getItem("token") ?? "";
        setLoading(true);
        const body = {
            name : name,
            email : email,
            reason :reason,
            userId : user?.userId,
        };

        try {
            await axios
                .post(`/api/kost/teman_menginap`, body, {
                    headers: { Authorization: `Bearer ${token}` },
                })
                .then((response) => {
                    if (response.status === 201) {
                        toast.success("Successfully created Teman Menginap");
                        router.push(`/kost/teman_menginap/history`);
                    }
                })
                .catch((error) => {
                    toast.error(error?.message);
                })
                .finally(() => {
                    setLoading(false);
                });
        } catch (error) {
            toast.error(`Teman Menginap creation failed.`);
            setLoading(false);
        }
    };

    return (
        <div>
            <p className="text-xl md:text-3xl font-bold p-5">Create Teman Menginap</p>
            <form
                onSubmit={handleSubmit}
                className="flex flex-col space-y-4 rounded-2xl shadow-lg p-5"
            >
                <TextField
                    value={name}
                    onChange={(event) => {
                        setName(event.target.value);
                    }}
                    required
                    label="Name"
                    type="text"
                    id="name"
                />
                <TextField
                    value={email}
                    onChange={(event) => {
                        setEmail(event.target.value);
                    }}
                    required
                    label="Email"
                    type="email"
                    id="email"
                />
                <TextField
                    value={reason}
                    onChange={(event) => {
                        setReason(event.target.value);
                    }}
                    required
                    label="Reason"
                    type="text"
                    id="reason"
                />
                <div className="flex justify-center">
                    <Button loading={loading} type="submit" className="w-48 ">
                        Create Teman Menginap
                    </Button>
                </div>
            </form>
        </div>
    );
};
