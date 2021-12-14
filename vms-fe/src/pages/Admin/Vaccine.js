import React, { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import AdminNavbar from './AdminNavbar';
import { Button, Col, Dropdown, DropdownButton, Form, Modal } from 'react-bootstrap';
import { getCookie } from 'react-use-cookie';

import axios from 'axios';
import MultiSelect from 'multiselect-react-dropdown';

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
  const [selectedDiseaseOptions, setSelectedDiseaseOptions] = useState([]);
  const [selectedVaccineId, setSelectedVaccineId] = useState(0);
  const handleClose = () => setShowVaccineModal(false);

  const updateVaccineDetails = () => {
    const token = getCookie('auth');
    const diseaseIds = [];
    for (var i = 0; i < selectedDiseaseOptions.length; i++) {
      diseaseIds.push(selectedDiseaseOptions[i].id);
    }
    console.log(diseaseIds);
    console.log('options', selectedDiseaseOptions);

    const updateVaccineObj = {
      name: vaccineName,
      manufacturer,
      numOfShots: numberOfShots,
      shotInterval,
      duration,
      diseaseIds,
    };
    axios
      .put(`/vaccines/${selectedVaccineId}`, updateVaccineObj, {
        headers: { Authorization: token },
      })
      .then((res) => {
        console.log(res);
        getVaccines();
        setSelectedDiseaseOptions([]);
        setShowVaccineModal(false);
      })
      .catch((err) => {
        console.log(err);
      });
  };

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
    const diseaseIds = [];

    for (var i = 0; i < selectedDiseaseOptions.length; i++) {
      diseaseIds.push(selectedDiseaseOptions[i].id);
    }

    const createVaccineObj = {
      name: vaccineName,
      manufacturer,
      numOfShots: numberOfShots,
      shotInterval,
      duration,
      diseaseIds,
    };

    axios
      .post('/vaccines', createVaccineObj, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
        getVaccines();
      })
      .catch((err) => {
        console.log(err);
      });
    setVaccineName('');
    setManufacturer('');
    setNumberOfShots(0);
    setShotInterval(0);
    setDuration(0);
    setSelectedDiseaseOptions([]);
    setShowAddVaccineModal(false);
  };

  const deleteVaccine = (id) => {
    const token = getCookie('auth');
    axios
      .delete(`/vaccines/${id}`, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
        console.log(res.data);
        getVaccines();
      })
      .catch((err) => {
        console.log(err);
      });
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
                id: disease.id,
                name: disease.name,
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

  useEffect(() => {
    getDiseases();
    getVaccines();
  }, [showAddVaccineModal]);

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
              type="Number"
              placeholder=""
              value={numberOfShots}
              onChange={(e) => setNumberOfShots(e.target.value)}
            />

            <br></br>
            <Form.Label>Duration</Form.Label>

            <Form.Control
              required
              type="Number"
              placeholder=""
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
            />
            <br></br>
            <Form.Label>Shot Interval</Form.Label>

            <Form.Control
              required
              type="Number"
              placeholder=""
              value={shotInterval}
              onChange={(e) => setShotInterval(e.target.value)}
            />
            <br />
            <MultiSelect
              options={diseases}
              displayValue="name"
              onSelect={(selected) => {
                console.log(selected);
                setSelectedDiseaseOptions(selected);
              }}
            />
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
            <br />
            <Form.Label>Disease Associated</Form.Label>
            <MultiSelect
              options={diseases}
              displayValue="name"
              selectedValues={selectedDiseaseOptions}
              onSelect={(selected) => {
                console.log(selected);
                setSelectedDiseaseOptions(selected);
              }}
              onRemove={(selected) => {
                console.log(selected);
                setSelectedDiseaseOptions(selected);
              }}
            />
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={updateVaccineDetails}>
            Save Changes
          </Button>
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
          setSelectedDiseaseOptions([]);
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
                            const tempArr = [];
                            const tempVAr = vaccine?.diseases
                              ? vaccine.diseases.length > 0
                                ? vaccine.diseases.map((ele) => {
                                    const tempObj = {
                                      id: ele.id,
                                      name: ele.name,
                                    };
                                    tempArr.push(tempObj);
                                  })
                                : null
                              : null;
                            setSelectedDiseaseOptions(tempArr);
                            setShowVaccineModal(true);
                            setSelectedVaccineId(vaccine?.id);
                          }}
                        >
                          Update
                        </Button>
                        <Button
                          variant="danger"
                          style={{ marginLeft: '10px' }}
                          onClick={() => deleteVaccine(vaccine?.id)}
                        >
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
