import React, { useState, useEffect } from 'react';
import { Button, Form, Col } from 'react-bootstrap';
import Table from 'react-bootstrap/Table';
import AdminNavbar from './AdminNavbar';
import Modal from 'react-bootstrap/Modal';
import axios from 'axios';
import { getCookie } from 'react-use-cookie';

export default function Clinic() {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const [clinicName, setClinicName] = useState('');
  let [clincStartTime, setClincStartTime] = useState('');
  let [clincEndTime, setClinicEndTime] = useState('');
  const [clinicStreet, setClinicStreet] = useState('');
  const [clinicCity, setClinicCity] = useState('');
  const [clinicState, setClinicState] = useState('');
  const [clinicZipcode, setClinicZipcode] = useState('');
  const [clinicNumberOfPhysicians, setClinicNumberOfPhysicians] = useState('');

  let [newClincStartTime, setNewClincStartTime] = useState('');
  let [newClincEndTime, setNewClinicEndTime] = useState('');
  const [newClinicStreet, setNewClinicStreet] = useState('');
  const [newClinicCity, setNewClinicCity] = useState('');
  const [newClinicState, setNewClinicState] = useState('');
  const [newClinicZipcode, setNewClinicZipcode] = useState('');
  const [newClinicNumberOfPhysicians, setNewClinicNumberOfPhysicians] = useState('');


  const [clinicDescription, setClinicDescription] = useState('');
  const [newClinicName, setNewClinicName] = useState('');
  const [newClinicDescription, setNewClinicDescription] = useState('');
  const [clinics, setClinics] = useState([]);
  const [selectedClinicId, setSelectedClinicId] = useState('');
  const [showAddClinicModal, setshowAddClinicModal] = useState(false);

  const getClinics = () => {
    const token = getCookie('auth');
    if (token) {
      axios
        .get('/clinics', {
          headers: {
            Authorization: token,
          },
        })
        .then((res) => {
          console.log(res.data)
          if (res && res.data && res.data.length > 0) {
            setClinics(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const updateClincById = () => {
    const token = getCookie('auth');
    const address = {
      street: clinicStreet,
      city: clinicCity,
      state: clinicState,
      zipcode: clinicZipcode
    }
    clincStartTime = clincStartTime.slice(0,5);
    clincEndTime = clincEndTime.slice(0,5);
    const updateObj = {
      name: clinicName,
      startTime: clincStartTime,
      endTime: clincEndTime,
      numberOfPhysicians: clinicNumberOfPhysicians,
      address: address
    };
    console.log(updateObj);
    axios
      .put(`/clinics/${selectedClinicId}`, updateObj, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
        setShow(false);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const deleteClinicById = (id) => {
    console.log(id);
    const token = getCookie('auth');
    axios
      .delete(`/clinics/${id}`, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
          console.log(res);
        getClinics();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const createClinics = () => {
    const newAddress = {
      street: newClinicStreet,
      city: newClinicCity,
      state: newClinicState,
      zipcode: newClinicZipcode
    }
    newClincStartTime = newClincStartTime.slice(0,5);
    newClincEndTime = newClincEndTime.slice(0,5);
    const createClinicsObj = {
      name: newClinicName,
      startTime: newClincStartTime,
      endTime: newClincEndTime,
      numberOfPhysicians: newClinicNumberOfPhysicians,
      address: newAddress
    };
    console.log(createClinicsObj);

    const token = getCookie('auth');
    axios.post(`/clinics`, createClinicsObj, {
        headers: {
            Authorization: token,
        },
    }).then((res)=>{
        getClinics();
        setNewClinicName('');
        setNewClincStartTime('');
        setNewClinicEndTime('');
        setNewClinicCity('');
        setNewClinicState('');
        setNewClinicStreet('');
        setNewClinicZipcode('');
        setNewClinicNumberOfPhysicians('');
        setshowAddClinicModal(false);
    }).catch((err)=>{
        console.log(err);
    });
  };

  useEffect(() => {
    getClinics();
  }, [show]);

  return (
    <div>
      <AdminNavbar />
      <Modal show={showAddClinicModal} onHide={() => setshowAddClinicModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add Clinic </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Clinic Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicName}
              onChange={(e) => setNewClinicName(e.target.value)}
            />
            <br></br>
            
            <Form.Label>Clinic Start Time</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClincStartTime}
              onChange={(e) => setNewClincStartTime(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic End Time</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClincEndTime}
              onChange={(e) => setNewClinicEndTime(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Number Of Physicians</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicNumberOfPhysicians}
              onChange={(e) => setNewClinicNumberOfPhysicians(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Street</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicStreet}
              onChange={(e) => setNewClinicStreet(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic City</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicCity}
              onChange={(e) => setNewClinicCity(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic State</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicState}
              onChange={(e) => setNewClinicState(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Zipcode</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newClinicZipcode}
              onChange={(e) => setNewClinicZipcode(e.target.value)}
            />
            <br></br>
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setshowAddClinicModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={createClinics}>
            Add Clinic
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Clinic </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Clinic Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicName}
              onChange={(e) => setClinicName(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Start Time</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clincStartTime}
              onChange={(e) => setClincStartTime(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic End Time</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clincEndTime}
              onChange={(e) => setClinicEndTime(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Number Of Physicians</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicNumberOfPhysicians}
              onChange={(e) => setClinicNumberOfPhysicians(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Street</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicStreet}
              onChange={(e) => setClinicStreet(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic City</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicCity}
              onChange={(e) => setClinicCity(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic State</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicState}
              onChange={(e) => setClinicState(e.target.value)}
            />
            <br></br>
            <Form.Label>Clinic Zipcode</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={clinicZipcode}
              onChange={(e) => setClinicZipcode(e.target.value)}
            />
            <br></br>
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={updateClincById}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
      <Button style={{ margin: '20px' }} onClick={() => setshowAddClinicModal(true)}>
        {' '}
        Add Clinic{' '}
      </Button>

      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Clinic Name</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {clinics
            ? clinics.length > 0
              ? clinics.map((clinic) => {
                  return (
                    <tr>
                      <td>{clinic.name}</td>
                      <td>
                      <div style={{width:'80%',textAlign:'left',margin:'auto'}}>
                      Start Time: {clinic.startTime}<br/>End Time: {clinic.endTime}<br/>Number of Physicians: {clinic.numberOfPhysicians}<br/>Address: {clinic.address.street}, {clinic.address.city}, {clinic.address.state}-{clinic.address.zipcode}<br/>
                      </div>
                      </td>
                      <td>
                        <Button
                          variant="primary"
                          onClick={() => {
                            setSelectedClinicId(clinic?.id);
                            setClinicName(clinic?.name);
                            setClincStartTime(clinic?.startTime)
                            setClinicEndTime(clinic?.endTime)
                            setClinicCity(clinic?.address.city)
                            setClinicStreet(clinic?.address.street)
                            setClinicState(clinic?.address.state)
                            setClinicZipcode(clinic?.address.zipcode)
                            setClinicNumberOfPhysicians(clinic?.numberOfPhysicians)
                            handleShow();
                          }}
                        >
                          Update
                        </Button>
                        <Button
                          variant="danger"
                          style={{ marginLeft: '10px' }}
                          onClick={() => {
                            setSelectedClinicId(clinic.id);
                            deleteClinicById(clinic.id);
                          }}
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
