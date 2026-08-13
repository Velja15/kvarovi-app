import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext.jsx";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        try {
            const response = await api.post("/auth/login", { username, password });
            login(response.data);

            if (response.data.role === "ROLE_ADMIN") {
                navigate("/admin");
            } else {
                navigate("/reports");
            }
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setError("Neispravno korisnicko ime ili lozinka");
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: "60px auto", fontFamily: "sans-serif" }}>
            <h2>Prijava</h2>
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
                        type="password"
                        placeholder="Lozinka"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        style={{ width: "100%", padding: 8 }}
                    />
                </div>
                {error && <p style={{ color: "red" }}>{error}</p>}
                <button type="submit" style={{ width: "100%", padding: 10 }}>
                    Prijavi se
                </button>
            </form>
            <p style={{ marginTop: 15 }}>
                Nemas nalog? <Link to="/register">Registruj se</Link>
            </p>
        </div>
    );
}