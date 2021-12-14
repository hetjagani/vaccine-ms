import React from 'react';
import { Container, Navbar, Nav } from 'react-bootstrap';
import { Link } from 'react-router-dom';


function AdminNavbar() {
  return (
    <div>
      
        <Navbar bg="light" variant="light">
          <Container>
            <Navbar.Brand href="#home">Vaccine Management System</Navbar.Brand>
            <Nav className="me-auto">
              <Nav.Link> <Link to="/disease">Disease</Link> </Nav.Link>
              <Nav.Link> <Link to="/vaccine">Vaccine</Link> </Nav.Link>
              <Nav.Link> <Link to="/clinic">Clinic</Link> </Nav.Link>

            </Nav>
          </Container>
        </Navbar>
    </div>
  );
}
export default AdminNavbar;

