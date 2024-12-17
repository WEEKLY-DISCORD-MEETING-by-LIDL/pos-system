import { useState } from 'react';
import '../styles/TaxCardStyle.css';
import { deleteTax, updateTax } from "../api/TaxAPI";

export const TaxCard = (props) => {
    const [newTitle, setNewTitle] = useState(props.tax.title);
    const [newPercentage, setNewPercentage] = useState(props.tax.percentage);
    const [isEditing, setIsEditing] = useState(false); 

    const handleSubmit = (event) => {
        event.preventDefault();
        const updatedTaxData = {
            ...props.tax,
            title: newTitle,
            percentage: newPercentage,
        };
        updateTax(props.tax.id, updatedTaxData);
        setIsEditing(false); 
    };

    const removeTax = () => {
        deleteTax(props.tax.id);
    };

    return (
        <div className="tax-card">
            <h4>Tax: #{props.tax.id}</h4>
            <p>Title: {props.tax.title}</p>
            <p>Percentage: {props.tax.percentage * 100}%</p>
            <p>Merchant ID: {props.tax.merchantId}</p>
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
                                step="0.01"
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
