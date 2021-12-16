import React from 'react';
import { Container, Navbar, Nav, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { setCookie } from 'react-use-cookie';
import { useHistory } from 'react-router-dom';

function AdminNavbar() {
  const history = useHistory();
  const logout = () => {
    setCookie('auth', '');
    history.push('/login');
  };

  return (
    <div>
      <Navbar bg="light" variant="light">
        <Container>
          <Navbar.Brand href="#home">Vaccine Management System</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link>
              {' '}
              <Link to="/disease">Disease</Link>{' '}
            </Nav.Link>
            <Nav.Link>
              {' '}
              <Link to="/vaccine">Vaccine</Link>{' '}
            </Nav.Link>
            <Nav.Link>
              {' '}
              <Link to="/clinic">Clinic</Link>{' '}
            </Nav.Link>
            <Nav.Link>
              {' '}
              <Link to="/admin/report">Admin Report</Link>{' '}
            </Nav.Link>
          </Nav>
          <Nav>
            <Button onClick={() => logout()}>Logout</Button>
          </Nav>
        </Container>
      </Navbar>
    </div>
  );
}
export default AdminNavbar;
