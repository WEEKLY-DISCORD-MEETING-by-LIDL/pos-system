import React, { useState } from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
} from 'reactstrap';

const NavMenu = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    return (
        <header>
            <Navbar color="dark" dark expand="md">
                <NavbarBrand href="/">Website</NavbarBrand>
                <NavbarToggler onClick={toggle} />
                <Collapse isOpen={isOpen} navbar>
                    <Nav className="w-100 justify-content-between" navbar>
                      <div className="d-flex">
                        <NavItem>
                          <NavLink href="/create-order">Products</NavLink>
                        </NavItem>
                        <NavItem>
                          <NavLink href="/services">Reservations</NavLink>
                        </NavItem>
                        <NavItem>
                          <NavLink href="/taxes">Taxes</NavLink>
                        </NavItem>
                      </div>
                    </Nav>
                    <Nav className='mr-auto' navbar>
                        <NavItem>
                            <NavLink href="/employees">Employees</NavLink>
                        </NavItem>
                    </Nav>
                </Collapse>
            </Navbar>
        </header>
    );
};

export default NavMenu;