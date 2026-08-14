import { useState, useEffect } from "react";
import api from "../api/axios";
import Navbar from "../components/Navbar";

export default function ReportsPage() {
    const [reports, setReports] = useState([]);
    const [categories, setCategories] = useState([]);
    const [description, setDescription] = useState("");
    const [location, setLocation] = useState("");
    const [categoryId, setCategoryId] = useState("");
    const [error, setError] = useState("");

    const loadData = () => {
        api.get("/reports/my")
            .then((res) => setReports(res.data))
            .catch(() => setError("Greska pri ucitavanju prijava"));
        api.get("/categories")
            .then((res) => setCategories(res.data))
            .catch(() => {});
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        try {
            await api.post("/reports", {
                description,
                location,
                categoryId: Number(categoryId),
            });
            setDescription("");
            setLocation("");
            setCategoryId("");
            loadData();
        } catch (err) {
            setError(err.response?.data?.error || "Greska pri prijavi kvara");
        }
    };

    return (
        <div>
            <Navbar />
            <div style={{ maxWidth: 800, margin: "20px auto", fontFamily: "sans-serif" }}>
                <h2>Prijavi novi kvar</h2>
                <form onSubmit={handleSubmit} style={{ marginBottom: 30 }}>
                    <div style={{ marginBottom: 10 }}>
                        <select
                            value={categoryId}
                            onChange={(e) => setCategoryId(e.target.value)}
                            style={{ width: "100%", padding: 8 }}
                            required
                        >
                            <option value="">-- Izaberi kategoriju --</option>
                            {categories.map((c) => (
                                <option key={c.id} value={c.id}>{c.name}</option>
                            ))}
                        </select>
                    </div>
                    <div style={{ marginBottom: 10 }}>
                        <input
                            type="text"
                            placeholder="Lokacija (npr. 3. sprat, hodnik)"
                            value={location}
                            onChange={(e) => setLocation(e.target.value)}
                            style={{ width: "100%", padding: 8 }}
                            required
                        />
                    </div>
                    <div style={{ marginBottom: 10 }}>
            <textarea
                placeholder="Opis problema"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                style={{ width: "100%", padding: 8, minHeight: 80 }}
                required
            />
                    </div>
                    {error && <p style={{ color: "red" }}>{error}</p>}
                    <button type="submit" style={{ padding: 10 }}>Prijavi kvar</button>
                </form>

                <h2>Moje prijave</h2>
                {reports.length === 0 ? (
                    <p>Nemate prijavljenih kvarova.</p>
                ) : (
                    <table style={{ width: "100%", borderCollapse: "collapse" }}>
                        <thead>
                        <tr style={{ background: "#ecf0f1" }}>
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
                                <td style={cellStyle}>{r.categoryName}</td>
                                <td style={cellStyle}>{r.location}</td>
                                <td style={cellStyle}>{r.description}</td>
                                <td style={cellStyle}>{r.status}</td>
                                <td style={cellStyle}>{r.priority}</td>
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