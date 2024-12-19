import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/LoginAPI";
import "../styles/App.css";

function App() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async () => {
    try {
      const token = await login(username, password);
      localStorage.setItem("token", token);
      navigate("/home");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="login-box">
      <h2 className="login-header">Login</h2>
      <div className="input-group">
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="input-field"
        />
      </div>
      <div className="input-group">
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="input-field"
        />
      </div>
      {error && <p className="error-text">{error}</p>}
      <button onClick={handleLogin} className="login-button">
        Log In
      </button>
    </div>
  );
}

export default App;