import React, { useState } from 'react';
import { Container, Row, Nav, Navbar } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

function PatientNavbar({ navbarStartDate, setNavbarStartDate }) {
  const navbarCurrentDateTime = new Date();
  const [navbarCurrentTime, setNavbarCurrentTime] = useState('');
  const changeTime = () => {
    var today = new Date();
    var month = today.getMonth();
    var day = today.getDay();
    var year = today.getFullYear();

    var hour = today.getHours();
    var minute = today.getMinutes();
    var seconds = today.getSeconds();
    var milliseconds = today.getMilliseconds();

    var output =
      String(hour).padStart(2, '0') +
      ':' +
      String(minute).padStart(2, '0') +
      ':' +
      String(seconds).padStart(2, '0');
    setNavbarCurrentTime(output);
  };

  const currentDate = `${
    navbarCurrentDateTime.getMonth() + 1
  }-${navbarCurrentDateTime.getDate()}-${navbarCurrentDateTime.getFullYear()}`;
  const maxAvailableDate = `${
    navbarCurrentDateTime.getMonth() + 1
  }-${navbarCurrentDateTime.getDate()}-${navbarCurrentDateTime.getFullYear() + 1}`;

  setInterval(changeTime, 1000);

  return (
    <Navbar bg="light" variant="light">
      <Container>
        <Navbar.Brand href="#home">Vaccine Management System</Navbar.Brand>
        <Nav>
          Select Date for Appointment:
          <div style={{ marginLeft: '10px', marginRight: '10px' }}>
            <DatePicker
              selected={navbarStartDate}
              onChange={(date) => setNavbarStartDate(date)}
              onSelect={(date) => setNavbarStartDate(date)}
              minDate={new Date(currentDate ? currentDate : '12-01-2020')}
              maxDate={new Date(maxAvailableDate)}
              placeholderText="Select Date"
            />
          </div>
          Current Time: {navbarCurrentTime && navbarCurrentTime.length > 0 ? navbarCurrentTime : ''}
        </Nav>
      </Container>
    </Navbar>
  );
}

export default PatientNavbar;
