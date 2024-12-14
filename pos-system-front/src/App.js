import React from "react";
import { useNavigate } from "react-router-dom";

function App() {
  const navigate = useNavigate();

  const goHome = () => {
    navigate("/order")
  }

  return (
    <div className="App">
      <h1>Welcome</h1>
      <button onClick={goHome}>go to Home</button>
    </div>
  );
}

export default App;