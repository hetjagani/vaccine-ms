import React, { useEffect, useState } from 'react';
import AdminNavbar from './AdminNavbar';
import DatePicker from 'react-datepicker';
import axios from 'axios';
import { Button, ButtonGroup, Dropdown, DropdownButton, Table } from 'react-bootstrap';
import { PieChart } from 'react-minimal-pie-chart';
import toast from 'react-hot-toast';

function AdminReport() {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [clinics, setClinics] = useState([]);
  const [selectedClinic, setSelectedClinic] = useState('Select a clinic');
  const [selectedClinicID, setSelectedClinicID] = useState('');
  const [noShowRate, setNoShowRate] = useState(null);
  const [total, setTotal] = useState(null);
  const [clinic, setClinic] = useState(null);

  const getAdminReport = () => {
    console.log(typeof endDate);
    if (!selectedClinicID.length && !startDate && !endDate) {
      toast.error('Please Select All Fields');
      return;
    }
    axios
      .get(`/users/adminReport`, {
        params: {
          date1: `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(
            2,
            '0'
          )}-${String(startDate.getDate()).padStart(2, '0')}`,
          date2: `${endDate.getFullYear()}-${String(endDate.getMonth() + 1).padStart(
            2,
            '0'
          )}-${String(endDate.getDate()).padStart(2, '0')}`,
          clinicId: selectedClinicID,
        },
      })
      .then((res) => {
        console.log(res.data);
        setNoShowRate(res.data.noShowRate);
        setTotal(res.data.total);
        setClinic(res.data.clinic);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const getClinics = () => {
    axios
      .get(`/clinics`)
      .then((res) => {
        if (res && res.data) {
          setClinics(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    getClinics();
  }, []);

  return (
    <div>
      <AdminNavbar />
      <div>
        <h2 style={{ marginTop: '20px' }}>Admin Report</h2>
        <div style={{ display: 'flex', justifyContent: 'space-around' }}>
          <div style={{ marginLeft: '10px', marginRight: '10px' }}>
            Start Date:
            <DatePicker
              selected={startDate}
              onChange={(date) => setStartDate(date)}
              onSelect={(date) => setStartDate(date)}
              placeholderText="Select Date"
            />
          </div>
          <div style={{ marginLeft: '10px', marginRight: '10px' }}>
            End Date:
            <DatePicker
              selected={endDate}
              onChange={(date) => setEndDate(date)}
              onSelect={(date) => setEndDate(date)}
              placeholderText="Select Date"
            />
          </div>
          <div>
            Select Clinic:
            <br />
            {clinics && clinics.length > 0 ? (
              <DropdownButton
                as={ButtonGroup}
                title={selectedClinic}
                onSelect={(e) => {
                  setSelectedClinic(e);
                  clinics.forEach((clinic) => {
                    if (clinic.name === e) {
                      setSelectedClinicID(clinic.id);
                    }
                  });
                }}
              >
                {clinics.map((clinic) => (
                  <Dropdown.Item eventKey={clinic.name} id={clinic.id}>
                    {clinic.name}
                  </Dropdown.Item>
                ))}
              </DropdownButton>
            ) : null}
          </div>
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
        <Button style={{ width: '200px', marginLeft: '30px' }} onClick={() => getAdminReport()}>
          {' '}
          Get Report
        </Button>
      </div>
      <hr />
      {clinic ? (
        <div style={{ justifyContent: 'center', display: 'flex' }}>
          <Table responsive style={{ width: '800px' }}>
            <tbody>
              <tr style={{ textAlign: 'center' }}>
                <td>Name</td>
                <td>{clinic.name}</td>
              </tr>
              <tr>
                <td>Start Time</td>
                <td>{clinic.startTime}</td>
              </tr>
              <tr>
                <td>End Time </td>
                <td>{clinic.endTime}</td>
              </tr>
              <tr>
                <td>Number of Physicians </td>
                <td>{clinic.numberOfPhysicians}</td>
              </tr>
              <tr>
                <td>Address </td>
                <td>
                  {clinic?.address ? clinic.address.street : null},{' '}
                  {clinic?.address ? clinic.address.state : null},{' '}
                  {clinic?.address ? clinic.address.city : null}
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

export default AdminReport;
