import React, { useState } from 'react';
import { Nav, Navbar } from 'react-bootstrap';
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
      <Navbar.Brand href="#home">
        <div style={{ display: 'flex', alignItems: 'center', marginLeft: '10px' }}>
          <span>Vaccine Management System</span>
          <span style={{ marginLeft: '20px', display: 'flex' }}>
            <Nav.Link href="/dashboard">Appointments</Nav.Link>
            <Nav.Link href="/dashboard/vaccines/due">Vaccines Due</Nav.Link>
          </span>
        </div>
      </Navbar.Brand>
      <Navbar.Toggle />
      <Navbar.Collapse className="justify-content-end">
        <Navbar.Text>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            Date:
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
            <span>
              Current Time:{' '}
              {navbarCurrentTime && navbarCurrentTime.length > 0 ? navbarCurrentTime : ''}
            </span>
          </div>
        </Navbar.Text>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default PatientNavbar;
