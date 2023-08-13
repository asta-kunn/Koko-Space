import React, {useEffect, useState} from 'react';
import {TemanMenginapHistory} from '@components';
import axios from "axios";
import {useAuthContext} from "../../../components/context/AuthContext";

const TemanMenginapHistoryPage = () => {
    const [temanMenginapHistory, setTemanMenginapHistory] = useState([]);
    const {user} = useAuthContext();

    const fetchHistory = async (url) => {
        const token = localStorage.getItem("token") ?? "";
        try {
            const result = await axios.get(url, {
                headers: {Authorization: `Bearer ${token}`}
            });

            if (result.status === 200) {
                setTemanMenginapHistory(result.data);
                console.log('History fetch')
            } else {
                console.error('Failed to fetch history', result);
            }
        } catch (error) {
            console.error('Error fetching history', error);
        }
    };

    useEffect(() => {
        if (user?.role === "PENGELOLA") {
            fetchHistory(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/temanMenginap/all`);
        } else if (user?.role === "PENGGUNA") {
            fetchHistory(`${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/temanMenginap/all`);
        }
    }, []);

    return (
        <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <h1 className="text-3xl mb-4">Teman Menginap History</h1>
            <TemanMenginapHistory history={temanMenginapHistory}/>
        </div>
    );
};

export default TemanMenginapHistoryPage;
