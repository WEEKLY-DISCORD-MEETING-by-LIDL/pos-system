import React from "react";
import { useNavigate } from "react-router-dom";

function App() {
  const navigate = useNavigate();

  const goHome = () => {
    navigate("/create-order")
  }

  return (
    <div className="App">
      <h1>Welcome</h1>
      <button onClick={goHome}>new order</button>
      <button onClick={() => {navigate("/all-orders")}}>view orders</button>
    </div>
  );
}

export default App;