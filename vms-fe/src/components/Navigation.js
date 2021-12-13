import React from 'react';
import { Container, Navbar } from 'react-bootstrap';

function Navigation() {
  return (
    <div>
      <Navbar expand="lg" variant="light" bg="light">
        <Container>
          <Navbar.Brand href="#">Vaccine Management System</Navbar.Brand>
        </Container>
      </Navbar>
    </div>
  );
}

export default Navigation;
