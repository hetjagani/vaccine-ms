import React, { useEffect, useState } from 'react';
import PatientNavbar from './PatientNavbar';
import { getCookie } from 'react-use-cookie';
import axios from 'axios';
import { Table } from 'react-bootstrap';

function VaccinesDue() {
  const currentDateTime = new Date();
  const currentDate = `${
    currentDateTime.getMonth() + 1
  }-${currentDateTime.getDate()}-${currentDateTime.getFullYear()}`;
  const maxAvailableDate = `${currentDateTime.getMonth() + 1}-${currentDateTime.getDate()}-${
    currentDateTime.getFullYear() + 1
  }`;
  const [navbarStartDate, setNavbarStartDate] = useState(new Date(currentDate));
  const [vaccinesDue, setVaccinesDue] = useState([]);

  const getVaccinesDue = () => {
    const token = getCookie('auth');
    if (token) {
      axios
        .get('/vaccines/due', {
          params: {
            date: `${navbarStartDate.getFullYear()}-${String(
              navbarStartDate.getMonth() + 1
            ).padStart(2, '0')}-${String(navbarStartDate.getDate()).padStart(2, '0')}`,
          },
        })
        .then((res) => {
          if (res && res.data && res.data.length > 0) {
            setVaccinesDue(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  useEffect(() => {
    getVaccinesDue();
  }, [navbarStartDate]);

  return (
    <div>
      <PatientNavbar navbarStartDate={navbarStartDate} setNavbarStartDate={setNavbarStartDate} />
      <Table responsive>
        <thead>
          <tr style={{ textAlign: 'center' }}>
            <th>Vaccine name</th>
            <th>Manufacturer</th>
            <th>Num of shots</th>
            <th>Shot Interval</th>
          </tr>
        </thead>
        <tbody>
          {vaccinesDue && vaccinesDue.length > 0
            ? vaccinesDue.map((vaccine) => (
                <tr style={{ textAlign: 'center' }}>
                  <td>{vaccine.name}</td>
                  <td>{vaccine.manufacturer}</td>
                  <td>{`Remaining ${vaccine.numOfShots - vaccine.shotNumber + 1} shot of ${
                    vaccine.numOfShots
                  }`}</td>
                  <td>{`${vaccine.shotInterval} days`}</td>
                </tr>
              ))
            : null}
        </tbody>
      </Table>
    </div>
  );
}

export default VaccinesDue;
