import { useState, useEffect } from 'react';
import { TaxCard } from "./TaxCard";
import { TaxManagementStyle } from "../styles/TaxManagementStyle";
import { fetchTaxes, createTax } from "../api/TaxAPI";
import { useNavigate } from "react-router-dom";

export const TaxManagementPage = () => {
    const navigate = useNavigate();
    const [taxes, setTaxes] = useState([]);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [newTitle, setNewTitle] = useState("");
    const [newPercentage, setNewPercentage] = useState(0);

    const returnHome = () => {
        navigate("/home")
    }

    useEffect(() => {
        fetchTaxes(null, setTaxes);
    }, []);

    const handleNewTaxSubmit = async (event) => {
        event.preventDefault();

        const newTaxData = {
            title: newTitle,
            percentage: parseFloat(newPercentage),
        };

        try {
            const createdTax = await createTax(newTaxData);
            setTaxes((prevTaxes) => [...prevTaxes, createdTax]);
        } catch (error) {
            console.error("Error creating tax:", error.message);
        }

        setNewTitle("");
        setNewPercentage(0);
        setIsFormOpen(false);
    };

    const handleTaxDelete = (id) => {
        setTaxes((prevTaxes) => prevTaxes.filter((tax) => tax.id !== id));
    };

    const handleTaxUpdate = (id, updatedTax) => {
        setTaxes((prevTaxes) =>
            prevTaxes.map((tax) => (tax.id === id ? updatedTax : tax))
        );
    };

    return (
        <div>
            <ul style={TaxManagementStyle.list}>
                {taxes.map((tax) => (
                    <li key={tax.id} style={TaxManagementStyle.listItem}>
                        <TaxCard
                            tax={tax}
                            onTaxDelete={handleTaxDelete}
                            onTaxUpdate={handleTaxUpdate}
                        />
                    </li>
                ))}
            </ul>
            <div style={TaxManagementStyle.container}>

            <button style={TaxManagementStyle.cool_button} onClick={returnHome}>Back To Home</button>    
            <button style={TaxManagementStyle.cool_button} onClick={() => setIsFormOpen(true)}>NEW TAX</button>

            {isFormOpen && (
                <form onSubmit={handleNewTaxSubmit} style={{ marginTop: "20px" }}>
                    <div>
                        <label>
                            Title:
                            <input
                                type="text"
                                value={newTitle}
                                onChange={(e) => setNewTitle(e.target.value)}
                                required
                            />
                        </label>
                    </div>
                    <div>
                        <label>
                            Percentage:
                            <input
                                type="number"
                                value={newPercentage}
                                onChange={(e) => setNewPercentage(e.target.value)}
                                min="0"
                                max="1"
                                step="0.1"
                                required
                            />
                        </label>
                    </div>
                    <button type="submit">Submit</button>
                    <button type="button" onClick={() => setIsFormOpen(false)}>Cancel</button>
                </form>
            )}
            </div>
            
        </div>
    );
};
