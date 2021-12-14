import React, { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import AdminNavbar from './AdminNavbar';
import { Button, Col, Dropdown, DropdownButton, Form, Modal } from 'react-bootstrap';
import { getCookie } from 'react-use-cookie';

import axios from 'axios';
import DropdownMultiselect from 'react-multiselect-dropdown-bootstrap';

export default function Vaccine() {
  const [vaccines, setVaccines] = useState([]);
  const [showVaccineModal, setShowVaccineModal] = useState(false);
  const [showAddVaccineModal, setShowAddVaccineModal] = useState(false);
  const [vaccineName, setVaccineName] = useState('');
  const [diseases, setDiseases] = useState([]);
  const [manufacturer, setManufacturer] = useState('');
  const [numberOfShots, setNumberOfShots] = useState(0);
  const [shotInterval, setShotInterval] = useState(0);
  const [duration, setDuration] = useState(0);
  const [selectedDisease, setSelectedDisease] = useState('');
  const [selectedDiseaseId, setselectedDiseaseId] = useState(0);
  const handleClose = () => setShowVaccineModal(false);

  const getVaccines = () => {
    const token = getCookie('auth');
    if (token) {
      axios
        .get('/vaccines', {
          headers: {
            Authorization: token,
          },
        })
        .then((res) => {
          if (res && res.data && res.data.length > 0) {
            setVaccines(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const createVaccine = () => {
    const token = getCookie('auth');
    const createVaccineObj = {
      name: vaccineName,
      manufacturer: manufacturer,
      numOfShots: numberOfShots,
      shotInterval: shotInterval,
      duration: duration,
    };
  };

  const getDiseases = () => {
    const token = getCookie('auth');
    if (token) {
      axios
        .get('/diseases', {
          headers: {
            Authorization: token,
          },
        })
        .then((res) => {
          if (res && res.data && res.data.length > 0) {
            var tempArr = [];
            res.data.map((disease) => {
              const tempObj = {
                key: disease.id,
                label: disease.name,
              };
              tempArr.push(tempObj);
            });
            setDiseases(tempArr);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };
  console.log('diseases', diseases);

  const handleSelectDisease = (id) => {
    setselectedDiseaseId(id);
    const diseaseMap = new Map();
    console.log(diseases);
    const temp = diseases
      ? diseases.length > 0
        ? diseases.map((disease) => {
            diseaseMap[disease.id] = disease;
          })
        : null
      : null;
    setSelectedDisease(diseaseMap[id].name);
  };

  useEffect(() => {
    getDiseases();
    getVaccines();
  }, []);

  return (
    <div>
      <AdminNavbar />
      <Modal show={showAddVaccineModal} onHide={() => setShowAddVaccineModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add Vaccine </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Vaccine Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={vaccineName}
              onChange={(e) => setVaccineName(e.target.value)}
            />
            <br></br>
            <Form.Label>Vaccine Manufacturer</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={manufacturer}
              onChange={(e) => setManufacturer(e.target.value)}
            />
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
            <br></br>
            <Form.Label>Number of Shots</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={numberOfShots}
              onChange={(e) => setNumberOfShots(e.target.value)}
            />

            <br></br>
            <Form.Label>Duration</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
            />
            <br></br>
            <Form.Label>Shot Interval</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={shotInterval}
              onChange={(e) => setShotInterval(e.target.value)}
            />
            <br />
            <DropdownMultiselect
              options={diseases}
              name="diseases"
            />
            <br />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowAddVaccineModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={createVaccine}>
            Add Vaccine
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showVaccineModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Vaccine</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Vaccine Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={vaccineName}
              onChange={(e) => setVaccineName(e.target.value)}
            />
            <br></br>
            <Form.Label>Vaccine Manufacturer</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={manufacturer}
              onChange={(e) => setManufacturer(e.target.value)}
            />
            <br></br>
            <Form.Label>Number of Shots</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={numberOfShots}
              onChange={(e) => setNumberOfShots(e.target.value)}
            />

            <br></br>
            <Form.Label>Duration</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
            />
            <br></br>
            <Form.Label>Shot Interval</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={shotInterval}
              onChange={(e) => setShotInterval(e.target.value)}
            />
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary">Save Changes</Button>
        </Modal.Footer>
      </Modal>
      <Button
        variant="primary"
        onClick={() => {
          setVaccineName('');
          setManufacturer('');
          setNumberOfShots(0);
          setShotInterval(0);
          setDuration(0);
          setShowAddVaccineModal(true);
        }}
        style={{ margin: '10px' }}
      >
        Add Vaccine
      </Button>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Vaccine Name</th>
            <th>Manufacturer</th>
            <th>Number of Shots</th>
            <th>Shot Interval</th>
            <th>Duration</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {vaccines
            ? vaccines.length > 0
              ? vaccines.map((vaccine) => {
                  return (
                    <tr>
                      <td>{vaccine?.name}</td>
                      <td>{vaccine?.manufacturer}</td>
                      <td>{vaccine?.numOfShots}</td>
                      <td>{vaccine?.shotInterval}</td>
                      <td>{vaccine?.duration}</td>
                      <td>
                        <Button
                          variant="primary"
                          onClick={() => {
                            setVaccineName(vaccine?.name);
                            setManufacturer(vaccine?.manufacturer);
                            setNumberOfShots(vaccine?.numOfShots);
                            setShotInterval(vaccine?.shotInterval);
                            setDuration(vaccine?.duration);
                            setShowVaccineModal(true);
                          }}
                        >
                          Update
                        </Button>
                        <Button variant="danger" style={{ marginLeft: '10px' }}>
                          Delete
                        </Button>
                      </td>
                    </tr>
                  );
                })
              : null
            : null}
        </tbody>
      </Table>
    </div>
  );
}
