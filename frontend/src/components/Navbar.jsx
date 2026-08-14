import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Navbar() {
    const { username, role, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <div style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: "12px 20px",
            background: "#2c3e50",
            color: "white",
        }}>
            <div>
                <strong>Prijava kvarova</strong>
            </div>
            <div style={{ display: "flex", alignItems: "center", gap: 15 }}>
                <span>{username} ({role === "ROLE_ADMIN" ? "Administrator" : "Stanar"})</span>
                <button onClick={handleLogout} style={{ padding: "6px 12px" }}>
                    Odjava
                </button>
            </div>
        </div>
    );
}