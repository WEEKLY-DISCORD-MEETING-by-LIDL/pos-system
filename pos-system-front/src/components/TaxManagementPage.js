import {useState, useEffect} from 'react';
import {TaxCard} from "./TaxCard";
import {TaxManagementStyle} from "../styles/TaxManagementStyle";
import {fetchTaxes} from "../api/TaxAPI";

export const TaxManagementPage = () => {

    const [taxes, setTaxes] = useState([]);

    useEffect(() => {
        fetchTaxes(null, setTaxes);
    }, []);

    return (
        <div>
            <ul style={TaxManagementStyle.list}>
                {taxes.map((tax) => (
                    <li key={tax.id} style={TaxManagementStyle.listItem}>
                        <TaxCard tax={tax}/>
                    </li>
                ))}
            </ul>
            <button>NEW TAX</button>
        </div>
    );
}