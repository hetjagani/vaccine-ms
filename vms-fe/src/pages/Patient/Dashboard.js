import React, { useState, useEffect } from 'react';
import PatientNavbar from './PatientNavbar';
import { Button, Col, Row } from 'react-bootstrap';

function Dashboard() {
  return (
    <div>
      <PatientNavbar />
      <div style={{ width: '100%' }}>
        <Row style={{ marginTop: '50px' }}>
          <Col>
            <Button style={{ width: '250px' }} onClick={() => console.log('lalalalalla')}>
              {' '}
              Get Past Appointments
            </Button>
          </Col>
          <Col>
            <Button style={{ width: '250px' }}> Book New Appointment </Button>
          </Col>
          <Col>
            <Button style={{ width: '250px' }}> Get Future Appointments </Button>
          </Col>
        </Row>
      </div>
    </div>
  );
}

export default Dashboard;
