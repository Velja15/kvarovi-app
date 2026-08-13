import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext.jsx";

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [fullName, setFullName] = useState("");
    const [buildingId, setBuildingId] = useState("");
    const [buildings, setBuildings] = useState([]);
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        api.get("/buildings")
            .then((res) => setBuildings(res.data))
            .catch(() => setError("Greska pri ucitavanju zgrada"));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        try {
            const response = await api.post("/auth/register", {
                username,
                password,
                fullName,
                buildingId: buildingId ? Number(buildingId) : null,
            });
            login(response.data);
            navigate("/reports");
        } catch (err) {
            setError(err.response?.data?.error || "Greska pri registraciji");
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "60px auto", fontFamily: "sans-serif" }}>
            <h2>Registracija</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 10 }}>
                    <input
                        type="text"
                        placeholder="Korisnicko ime"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={{ width: "100%", padding: 8 }}
                    />
                </div>
                <div style={{ marginBottom: 10 }}>
                    <input
                        type="text"
                        placeholder="Ime i prezime"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                        style={{ width: "100%", padding: 8 }}
                    />
                </div>
                <div style={{ marginBottom: 10 }}>
                    <input
                        type="password"
                        placeholder="Lozinka"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={{ width: "100%", padding: 8 }}
                    />
                </div>
                <div style={{ marginBottom: 10 }}>
                    <select
                        value={buildingId}
                        onChange={(e) => setBuildingId(e.target.value)}
                        style={{ width: "100%", padding: 8 }}
                    >
                        <option value="">-- Izaberi zgradu --</option>
                        {buildings.map((b) => (
                            <option key={b.id} value={b.id}>
                                {b.name} ({b.address})
                            </option>
                        ))}
                    </select>
                </div>
                {error && <p style={{ color: "red" }}>{error}</p>}
                <button type="submit" style={{ width: "100%", padding: 10 }}>
                    Registruj se
                </button>
            </form>
            <p style={{ marginTop: 15 }}>
                Vec imas nalog? <Link to="/login">Prijavi se</Link>
            </p>
        </div>
    );
}