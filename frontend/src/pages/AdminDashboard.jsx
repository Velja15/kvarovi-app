import { useState, useEffect } from "react";
import api from "../api/axios";
import Navbar from "../components/Navbar";

export default function AdminDashboard() {
    const [reports, setReports] = useState([]);
    const [categories, setCategories] = useState([]);
    const [statusFilter, setStatusFilter] = useState("");
    const [categoryFilter, setCategoryFilter] = useState("");
    const [error, setError] = useState("");

    const loadReports = () => {
        const params = {};
        if (statusFilter) params.status = statusFilter;
        if (categoryFilter) params.categoryId = categoryFilter;

        api.get("/reports", { params })
            .then((res) => setReports(res.data))
            .catch(() => setError("Greska pri ucitavanju prijava"));
    };

    useEffect(() => {
        api.get("/categories").then((res) => setCategories(res.data)).catch(() => {});
    }, []);

    useEffect(() => {
        loadReports();
        // eslint-disable-next-line
    }, [statusFilter, categoryFilter]);

    const changeStatus = async (reportId, newStatus) => {
        try {
            await api.put(`/reports/${reportId}/status`, { newStatus });
            loadReports();
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError("Greska pri promeni statusa");
        }
    };


    const changePriority = async (reportId, priority, currentStatus) => {
        try {
            await api.put(`/reports/${reportId}/status`, { newStatus: currentStatus, priority });
            loadReports();
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError("Greska pri promeni prioriteta");
        }
    };

    return (
        <div>
            <Navbar />
            <div style={{ maxWidth: 1000, margin: "20px auto", fontFamily: "sans-serif" }}>
                <h2>Sve prijave (Administrator)</h2>

                <div style={{ display: "flex", gap: 15, marginBottom: 20 }}>
                    <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} style={{ padding: 8 }}>
                        <option value="">Svi statusi</option>
                        <option value="PRIJAVLJENO">Prijavljeno</option>
                        <option value="U_OBRADI">U obradi</option>
                        <option value="RESENO">Reseno</option>
                    </select>
                    <select value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)} style={{ padding: 8 }}>
                        <option value="">Sve kategorije</option>
                        {categories.map((c) => (
                            <option key={c.id} value={c.id}>{c.name}</option>
                        ))}
                    </select>
                </div>

                {error && <p style={{ color: "red" }}>{error}</p>}

                {reports.length === 0 ? (
                    <p>Nema prijava.</p>
                ) : (
                    <table style={{ width: "100%", borderCollapse: "collapse" }}>
                        <thead>
                        <tr style={{ background: "#ecf0f1" }}>
                            <th style={cellStyle}>Prijavio</th>
                            <th style={cellStyle}>Kategorija</th>
                            <th style={cellStyle}>Lokacija</th>
                            <th style={cellStyle}>Opis</th>
                            <th style={cellStyle}>Status</th>
                            <th style={cellStyle}>Prioritet</th>
                        </tr>
                        </thead>
                        <tbody>
                        {reports.map((r) => (
                            <tr key={r.id}>
                                <td style={cellStyle}>{r.reporterName}</td>
                                <td style={cellStyle}>{r.categoryName}</td>
                                <td style={cellStyle}>{r.location}</td>
                                <td style={cellStyle}>{r.description}</td>
                                <td style={cellStyle}>
                                    <select
                                        value={r.status}
                                        onChange={(e) => changeStatus(r.id, e.target.value)}
                                        style={{ padding: 4 }}
                                    >
                                        <option value="PRIJAVLJENO">Prijavljeno</option>
                                        <option value="U_OBRADI">U obradi</option>
                                        <option value="RESENO">Reseno</option>
                                    </select>
                                </td>
                                <td style={cellStyle}>
                                    <select
                                        value={r.priority}
                                        onChange={(e) => changePriority(r.id, e.target.value, r.status)}
                                        style={{ padding: 4 }}
                                    >
                                        <option value="NIZAK">Nizak</option>
                                        <option value="SREDNJI">Srednji</option>
                                        <option value="VISOK">Visok</option>
                                    </select>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

const cellStyle = {
    border: "1px solid #ddd",
    padding: 8,
    textAlign: "left",
};