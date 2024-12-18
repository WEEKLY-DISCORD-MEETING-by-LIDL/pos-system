import { useEffect, useState } from 'react';
import { fetchMerchant } from '../api/MerchantAPI';
import '../styles/MerchantStyle.css';

export const MerchantManagementPage = () => {

    const [merchants, setMerchant] = useState(0);

    useEffect(() => {
        fetchMerchant(setMerchant);
    }, [])

    return (
        <div className="business-details">
            <h1>Business Details</h1>
            <div className="detail-item">
                <h2><strong>Business Name:</strong> {merchants.name}</h2>
            </div>
            <div className="detail-item">
                <h2><strong>Location:</strong></h2>
            </div>
            {merchants.address && (
                <>
                    <div className="detail-item">
                    <h2><strong>Address 1:</strong> {merchants.address.address1}</h2>
                    <h2><strong>Address 2:</strong> {merchants.address.address2}</h2>
                    <h2><strong>City:</strong> {merchants.address.city}</h2>
                    <h2><strong>Country:</strong> {merchants.address.country}</h2>
                    <h2><strong>Country Code:</strong> {merchants.address.countryCode}</h2>
                    <h2><strong>Zip Code:</strong> {merchants.address.zipCode}</h2>
                    </div>
                </>
            )}
            <div className="detail-item">
                <h2><strong>Contact Info:</strong></h2>
            </div>
            <div className="detail-item">
                <h2><strong>Email:</strong> {merchants.email}</h2>
                <h2><strong>Phone:</strong> +{merchants.phone}</h2>
            </div>
        </div>
    );

}