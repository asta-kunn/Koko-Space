import React from 'react';

export const TemanMenginapHistory = ({ history }) => {
    return (
        <table className="table-auto w-full">
            <thead>
            <tr>
                <th className="px-4 py-2">Name</th>
                <th className="px-4 py-2">Email</th>
                <th className="px-4 py-2">Reason</th>
                <th className="px-4 py-2">ID</th>
            </tr>
            </thead>
            <tbody>
            {history.map((record, index) => (
                <tr key={index} className={index % 2 === 0 ? 'bg-gray-100' : ''}>
                    <td className="border px-4 py-2">{record.name}</td>
                    <td className="border px-4 py-2">{record.email}</td>
                    <td className="border px-4 py-2">{record.reason}</td>
                    <td className="border px-4 py-2">{record.id}</td>
                </tr>
            ))}
            </tbody>
        </table>
    );
};
