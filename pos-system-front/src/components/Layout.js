import React from 'react';
import NavMenu from './NavMenu';
import { useLocation } from 'react-router-dom';
import { Container } from 'reactstrap';

const Layout = ({ children }) => {
    const location = useLocation();
    const hideNavMenuPages = ['/'];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
        {!hideNavMenuPages.includes(location.pathname) && <NavMenu />}     
        <Container fluid>
            <main>{children}</main>
        </Container>
    </div>
  );
};

export default Layout;
