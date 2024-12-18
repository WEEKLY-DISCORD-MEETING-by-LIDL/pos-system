import { useState } from 'react';
import '../styles/TaxCardStyle.css';
import { deleteTax, updateTax } from "../api/TaxAPI";

export const TaxCard = ({ tax, onTaxDelete, onTaxUpdate }) => {
    const [newTitle, setNewTitle] = useState(tax.title);
    const [newPercentage, setNewPercentage] = useState(tax.percentage);
    const [isEditing, setIsEditing] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const updatedTaxData = {
            ...tax,
            title: newTitle,
            percentage: newPercentage,
        };

        try {
            await updateTax(tax.id, updatedTaxData);
            onTaxUpdate(tax.id, updatedTaxData);
            setIsEditing(false);
        } catch (error) {
            console.error("Error updating tax:", error.message);
        }
    };

    const removeTax = async () => {
        try {
            await deleteTax(tax.id);
            onTaxDelete(tax.id);
        } catch (error) {
            console.error("Error deleting tax:", error.message);
        }
    };

    return (
        <div className="tax-card">
            <h4>Tax: #{tax.id}</h4>
            <p>Title: {tax.title}</p>
            <p>Percentage: {tax.percentage * 100}%</p>
            <p>Merchant ID: {tax.merchantId}</p>
            <button onClick={removeTax}>DELETE ME</button>

            {isEditing ? (
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>
                            New Title:
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
                            New Percentage:
                            <input
                                type="number"
                                value={newPercentage}
                                onChange={(e) => setNewPercentage(parseFloat(e.target.value))}
                                min="0"
                                max="1"
                                step="0.1"
                                required
                            />
                        </label>
                    </div>
                    <button type="submit">Submit</button>
                    <button type="button" onClick={() => setIsEditing(false)}>Cancel</button>
                </form>
            ) : (
                <button onClick={() => setIsEditing(true)}>UPDATE ME</button>
            )}
        </div>
    );
};
