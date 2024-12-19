import styled from 'styled-components';

export const PageContainer = styled.div`
  padding: 20px;
`;

export const SectionTitle = styled.h2`
  margin-bottom: 20px;
`;

export const ContentLayout = styled.div`
  display: flex;
  gap: 20px;
`;

export const SectionContainer = styled.div`
  flex: 1;
`;

export const SectionHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
`;

export const ListContainer = styled.div`
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 10px;
`;

export const ItemCard = styled.div`
  padding: 10px;
  border: 1px solid #eee;
  margin-bottom: 5px;
  cursor: pointer;
  background-color: ${props => props.isSelected ? '#f0f0f0' : 'white'};
`;

export const CardContent = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const CardInfo = styled.div`
  flex: 1;
`;

export const CardTitle = styled.h4`
  margin: 0 0 8px 0;
`;

export const CardDetails = styled.div`
  font-size: 0.9em;
  color: #666;
`;

export const DetailText = styled.p`
  margin: 4px 0;
`;

export const ButtonGroup = styled.div`
    display: flex;
    gap: 8px;
    align-items: center;
`;

export const Button = styled.button.withConfig({
    shouldForwardProp: (prop) => prop !== 'primary' // Prevent `primary` prop from being forwarded to the DOM
})`
    padding: 20px 20px;
    border-radius: 4px;
    border: 1px solid #ccc;
    background-color: ${props => props.primary ? '#007bff' : 'white'};
    color: ${props => props.primary ? 'white' : 'black'};
    cursor: pointer;
    
  &:hover {
    background-color: ${props => props.primary ? '#0056b3' : '#f0f0f0'};
  }
`;

export const Modal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  border-radius: 4px;
  min-width: 300px;
`;

export const FormGroup = styled.div`
  margin-bottom: 10px;
`;

export const Label = styled.label`
  display: block;
  margin-bottom: 5px;
`;

export const Input = styled.input`
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

export const Select = styled.select`
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;
