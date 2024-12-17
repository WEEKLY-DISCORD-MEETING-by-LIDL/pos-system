import { useEffect, useState } from 'react';
import { fetchMerchant } from '../api/MerchantAPI';
import '../styles/MerchantStyle.css';

export const MerchantManagementPage = () => {

    const [merchants, setMerchant] = useState(0);

    useEffect(() => {
        fetchMerchant(1, setMerchant); //placeholder
    }, [])

    return (
        <div className="business-details">
            <h1>Business Details</h1>
            <div className="detail-item">
                <h2><strong>Name:</strong> {merchants.name}</h2>
            </div>
            <div className="detail-item">
                <h2><strong>Email:</strong> {merchants.email}</h2>
            </div>
            <div className="detail-item">
                <h2><strong>Phone:</strong> +{merchants.phone}</h2>
            </div>
            <div className="detail-item">
                <h2><strong>VAT:</strong> {merchants.vat}</h2>
            </div>
        </div>
    );

}