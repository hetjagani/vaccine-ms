import axios from 'axios';
import React, { useState } from 'react';
import PatientNavbar from './PatientNavbar';
import DatePicker from 'react-datepicker';
import { Button, Table } from 'react-bootstrap';
import { PieChart } from 'react-minimal-pie-chart';

function PatientReport() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [noShowRate, setNoShowRate] = useState(null);
  const [user, setUser] = useState(null);
  const [total, setTotal] = useState(null);

  const getPatientReport = () => {
    axios
      .get(`/users/report`, {
        params: {
          date1: `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(
            2,
            '0'
          )}-${String(startDate.getDate()).padStart(2, '0')}`,
          date2: `${endDate.getFullYear()}-${String(endDate.getMonth() + 1).padStart(
            2,
            '0'
          )}-${String(endDate.getDate()).padStart(2, '0')}`,
        },
      })
      .then((res) => {
        setNoShowRate(res.data.noShowRate);
        setUser(res.data.user);
        setTotal(res.data.total);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <div>
      <PatientNavbar showDateFlag={false} />
      <h2 style={{ marginTop: '20px' }}>Patient Report</h2>
      <div style={{ display: 'flex', justifyContent: 'space-around' }}>
        <div style={{ marginLeft: '10px', marginRight: '10px' }}>
          <DatePicker
            selected={startDate}
            onChange={(date) => setStartDate(date)}
            onSelect={(date) => setStartDate(date)}
            placeholderText="Select Date"
          />
        </div>
        <div style={{ marginLeft: '10px', marginRight: '10px' }}>
          <DatePicker
            selected={endDate}
            onChange={(date) => setEndDate(date)}
            onSelect={(date) => setEndDate(date)}
            placeholderText="Select Date"
          />
        </div>
      </div>
      <div
        style={{
          marginBottom: '10px',
          display: 'flex',
          justifyContent: 'center',
          marginTop: '20px',
        }}
      >
        <Button style={{ width: '200px', marginLeft: '30px' }} onClick={() => getPatientReport()}>
          {' '}
          Get Report
        </Button>
      </div>
      <hr />
      {user ? (
        <div style={{ justifyContent: 'center', display: 'flex' }}>
          <Table responsive style={{ width: '800px' }}>
            <tbody>
              <tr style={{ textAlign: 'center' }}>
                <td>MRN</td>
                <td>{user.mrn}</td>
              </tr>
              <tr>
                <td>First Name</td>
                <td>{user.firstName}</td>
              </tr>
              <tr>
                <td>Last Name </td>
                <td>{user.lastName}</td>
              </tr>
              <tr>
                <td>Gender </td>
                <td>{user.gender}</td>
              </tr>
              <tr>
                <td>Date of Birth </td>
                <td>{user.dateOfBirth}</td>
              </tr>
              <tr>
                <td>Address </td>
                <td>
                  {user?.address ? user.address.street : null},{' '}
                  {user?.address ? user.address.state : null},{' '}
                  {user?.address ? user.address.city : null}
                </td>
              </tr>
              <tr>
                <td>Total Number of Appointment </td>
                <td>{total ? total : null}</td>
              </tr>
            </tbody>
          </Table>
        </div>
      ) : null}
      {noShowRate ? (
        <div style={{ marginTop: '40px' }}>
          <PieChart
            labelStyle={{
              fontSize: '10px',
              fontFamily: 'sans-serif',
              fill: 'black',
            }}
            label={({ dataEntry }) => `${Math.round(dataEntry.percentage)}%`}
            data={[
              { title: 'No Show', value: noShowRate, color: 'red' },
              { title: 'Show', value: 1 - noShowRate, color: 'green' },
            ]}
            style={{ height: '200px' }}
          />
          <div style={{ display: 'flex', justifyContent: 'center', marginTop: '30px' }}>
            <div
              style={{
                backgroundColor: 'red',
                width: '100px',
                height: '30px',
                marginRight: '15px',
              }}
            />
            - <p style={{ marginLeft: '10px' }}>No Show</p>
            <div
              style={{
                backgroundColor: 'green',
                width: '100px',
                height: '30px',
                marginRight: '15px',
                marginLeft: '40px',
              }}
            />
            - <p style={{ marginLeft: '10px' }}>Show</p>
          </div>
        </div>
      ) : null}
    </div>
  );
}

export default PatientReport;
