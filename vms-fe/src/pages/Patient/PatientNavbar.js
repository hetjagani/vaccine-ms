import React, { useState } from 'react';
import { Container, Form, Nav, Navbar } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

function PatientNavbar() {
  const currentDateTime = new Date();
  const [currentTime, setCurrentTime] = useState('');
  const changeTime = () => {
    const time = new Date();
    var today = new Date();
    var month = today.getMonth();
    var day = today.getDay();
    var year = today.getFullYear();

    var hour = today.getHours() > 12 ? today.getHours() - 12 : today.getHours();
    var minute = today.getMinutes();
    var seconds = today.getSeconds();
    var milliseconds = today.getMilliseconds();

    var output = hour + ':' + minute + ':' + seconds;
    setCurrentTime(output);
  };

  const currentDate = `${
    currentDateTime.getMonth() + 1
  }-${currentDateTime.getDate()}-${currentDateTime.getFullYear()}`;
  const maxAvailableDate = `${currentDateTime.getMonth() + 1}-${currentDateTime.getDate()}-${
    currentDateTime.getFullYear() + 1
  }`;
  const [startDate, setStartDate] = useState(new Date(currentDate));

  setInterval(changeTime, 1000);

  return (
    <div>
      <Navbar bg="light" variant="light">
        <Container>
          <Navbar.Brand href="#home">Vaccine Management System</Navbar.Brand>
          <Nav>
            Select Date for Appointment:
            <div style={{ marginLeft: '10px', marginRight: '10px' }}>
              <DatePicker
                selected={startDate}
                onChange={(date) => setStartDate(date)}
                onSelect={(date) => setStartDate(date)}
                minDate={new Date(currentDate ? currentDate : '12-01-2020')}
                maxDate={new Date(maxAvailableDate)}
                placeholderText="Select Date"
              />
            </div>
            Current Time: {currentTime && currentTime.length > 0 ? currentTime : ''}
          </Nav>
        </Container>
      </Navbar>
    </div>
  );
}

export default PatientNavbar;
