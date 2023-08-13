import React from "react";
import { TemanMenginapForm }from "@components";

const AddTemanMenginap = () => {


    return (
        <div className="flex flex-col items-center">
            <h1 className="text-3xl font-bold mt-8 mb-6">Add a new Teman Menginap</h1>
            <TemanMenginapForm />
        </div>
    );
};

export default AddTemanMenginap;
