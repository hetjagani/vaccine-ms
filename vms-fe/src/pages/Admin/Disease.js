import React, { useState, useEffect } from 'react';
import { Button, Form, Col } from 'react-bootstrap';
import Table from 'react-bootstrap/Table';
import AdminNavbar from './AdminNavbar';
import Modal from 'react-bootstrap/Modal';
import axios from 'axios';
import { getCookie } from 'react-use-cookie';

export default function Disease() {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const [diseaseName, setDiseaseName] = useState('');
  const [diseaseDescription, setDiseaseDescription] = useState('');
  const [newDiseaseName, setNewDiseaseName] = useState('');
  const [newDiseaseDescription, setNewDiseaseDescription] = useState('');
  const [diseases, setDiseases] = useState([]);
  const [selectedDiseaseId, setSelectedDiseaseId] = useState('');
  const [showAddDiseaseModal, setShowAddDiseaseModal] = useState(false);

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
            setDiseases(res.data);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const updateDiseaseById = () => {
    const token = getCookie('auth');
    const updateObj = {
      name: diseaseName,
      description: diseaseDescription,
    };

    axios
      .put(`/diseases/${selectedDiseaseId}`, updateObj, {
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

  const deleteDiseaseById = (id) => {
    console.log(id);
    const token = getCookie('auth');
    axios
      .delete(`/diseases/${id}`, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
          console.log(res);
        getDiseases();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const createDisease = () => {
    const createDiseaseObj = {
        'name': newDiseaseName,
        'description': newDiseaseDescription,
    }

    const token = getCookie('auth');
    axios.post(`/diseases`, createDiseaseObj, {
        headers: {
            Authorization: token,
        },
    }).then((res)=>{
        getDiseases();
        setNewDiseaseDescription('');
        setNewDiseaseName('');
        setShowAddDiseaseModal(false);
    }).catch((err)=>{
        console.log(err);
    });
  };

  useEffect(() => {
    getDiseases();
  }, [show]);

  return (
    <div>
      <AdminNavbar />
      <Modal show={showAddDiseaseModal} onHide={() => setShowAddDiseaseModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add Disease </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Disease Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={newDiseaseName}
              onChange={(e) => setNewDiseaseName(e.target.value)}
            />
            <br></br>
            <Form.Label>Disease Description</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={newDiseaseDescription}
              onChange={(e) => setNewDiseaseDescription(e.target.value)}
            />
            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowAddDiseaseModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={createDisease}>
            Add Disease
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Disease </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group as={Col} md="12" controlId="validationCustom02">
            <Form.Label>Disease Name</Form.Label>
            <Form.Control
              required
              type="text"
              placeholder=""
              value={diseaseName}
              onChange={(e) => setDiseaseName(e.target.value)}
            />
            <br></br>
            <Form.Label>Disease Description</Form.Label>

            <Form.Control
              required
              type="text"
              placeholder=""
              value={diseaseDescription}
              onChange={(e) => setDiseaseDescription(e.target.value)}
            />

            <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={updateDiseaseById}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
      <Button style={{ margin: '20px' }} onClick={() => setShowAddDiseaseModal(true)}>
        {' '}
        Add Disease{' '}
      </Button>

      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th>Disease Name</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {diseases
            ? diseases.length > 0
              ? diseases.map((disease) => {
                  return (
                    <tr>
                      <td>{disease.name}</td>
                      <td>{disease.description}</td>
                      <td>
                        <Button
                          variant="primary"
                          onClick={() => {
                            setSelectedDiseaseId(disease?.id);
                            setDiseaseName(disease?.name);
                            setDiseaseDescription(disease?.description);
                            handleShow();
                          }}
                        >
                          Update
                        </Button>
                        <Button
                          variant="danger"
                          style={{ marginLeft: '10px' }}
                          onClick={() => {
                            setSelectedDiseaseId(disease.id);
                            deleteDiseaseById(disease.id);
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
