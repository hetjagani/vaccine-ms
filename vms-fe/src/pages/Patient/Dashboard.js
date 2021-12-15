import React, { useState, useEffect } from 'react';
import PatientNavbar from './PatientNavbar';
import {
  Button,
  Col,
  Row,
  Table,
  DropdownButton,
  ButtonGroup,
  Dropdown,
  CardGroup,
  Card,
} from 'react-bootstrap';
import { getCookie } from 'react-use-cookie';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import MultiSelect from 'multiselect-react-dropdown';

import './Dashboard.css';

const Dashboard = () => {
  const [appointments, setAppointments] = useState([]);
  const [selectedButton, setSelectedButton] = useState(1);
  const [showAppointmentDetails, setShowAppointmentDetails] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);

  const [selectedClinic, setSelectedClinic] = useState('Select a clinic');

  const [clinics, setClinics] = useState([]);
  const [selectedClinicID, setSelectedClinicID] = useState('');
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState();

  const [vaccinesDue, setVaccinesDue] = useState([]);
  const [selectedVaccineOptions, setSelectedVaccineOptions] = useState([]);

  const currentDateTime = new Date();
  const currentDate = `${
    currentDateTime.getMonth() + 1
  }-${currentDateTime.getDate()}-${currentDateTime.getFullYear()}`;
  const maxAvailableDate = `${currentDateTime.getMonth() + 1}-${currentDateTime.getDate()}-${
    currentDateTime.getFullYear() + 1
  }`;
  const [startDate, setStartDate] = useState(new Date(currentDate));

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

  const getVaccinesDue = () => {
    const token = getCookie('auth');
    if (token) {
      axios
        .get('/vaccines/due', {
          params: {
            date: `${currentDateTime.getFullYear()}-${
              currentDateTime.getMonth() + 1
            }-${currentDateTime.getDate()}`,
          },
        })
        .then((res) => {
          if (res && res.data && res.data.length > 0) {
            var tempArr = [];
            res.data.map((vaccine) => {
              const tempObj = {
                id: vaccine.id,
                name: vaccine.name,
              };
              tempArr.push(tempObj);
            });
            setVaccinesDue(tempArr);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const getUserDetails = async () => {
    const token = getCookie('auth');
    if (token) {
      return axios
        .get('/users/me')
        .then((res) => {
          if (res && res.data) {
            return res.data;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const createAppointment = async () => {
    const vaccineIds = [];
    for (var i = 0; i < selectedVaccineOptions.length; i++) {
      vaccineIds.push(selectedVaccineOptions[i].id);
    }
    const userDetails = await getUserDetails();
    const createAppointmentObj = {
      time: selectedSlot.time,
      vaccineIds,
      clinicId: selectedClinicID,
      userId: userDetails.mrn,
      date: `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`,
    };
    axios
      .post('/appointments', createAppointmentObj)
      .then((res) => {
        console.log(res);
        setSelectedButton(1);
        getFutureAppointments();
      })
      .catch((err) => console.log(err));
  };

  const getAvailableSlots = () => {
    axios
      .get('/appointments/slots', {
        params: {
          clinicId: selectedClinicID,
          date: `${currentDateTime.getFullYear()}-${
            currentDateTime.getMonth() + 1
          }-${currentDateTime.getDate()}`,
        },
      })
      .then((res) => {
        if (res && res.data) {
          setAvailableSlots(res.data);
        } else {
          setAvailableSlots([]);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const getFutureAppointments = () => {
    axios
      .get(`/appointments`, {
        params: {
          past: false,
        },
      })
      .then((res) => {
        if (res && res.data) {
          setAppointments(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const getPastAppointments = () => {
    axios
      .get(`/appointments`, {
        params: {
          past: true,
        },
      })
      .then((res) => {
        if (res && res.data) {
          setAppointments(res.data);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    getFutureAppointments();
    getClinics();
  }, []);

  return (
    <>
      <Modal
        dialogClassName="modalWidth"
        show={showAppointmentDetails}
        onHide={() => setShowAppointmentDetails(false)}
      >
        <Modal.Header closeButton>
          <div style={{ display: 'flex', flexDirection: 'column', marginBottom: '-20px' }}>
            <Modal.Title>
              {selectedAppointment && selectedAppointment.clinic
                ? selectedAppointment.clinic.name
                : ''}
            </Modal.Title>
            <p className="text-muted">
              {selectedAppointment &&
              selectedAppointment.clinic &&
              selectedAppointment.clinic.address
                ? `${selectedAppointment.clinic.address.street}, ${selectedAppointment.clinic.address.city}, ${selectedAppointment.clinic.address.state} - ${selectedAppointment.clinic.address.zipcode}`
                : ''}
            </p>
          </div>
        </Modal.Header>
        <Modal.Body>
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <p>Date: {selectedAppointment ? selectedAppointment.date : ''}</p>
            <p>Time: {selectedAppointment ? selectedAppointment.time : ''}</p>
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
                {selectedAppointment &&
                selectedAppointment.vaccines &&
                selectedAppointment.vaccines.length > 0
                  ? selectedAppointment.vaccines.map((vaccine) => (
                      <tr style={{ textAlign: 'center' }}>
                        <td>{vaccine.name}</td>
                        <td>{vaccine.manufacturer}</td>
                        <td>{vaccine.numOfShots}</td>
                        <td>{vaccine.shotInterval}</td>
                      </tr>
                    ))
                  : null}
              </tbody>
            </Table>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowAppointmentDetails(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
      <div>
        <PatientNavbar />
        <div style={{ width: '100%' }}>
          <Row style={{ marginTop: '50px' }}>
            <Col>
              <Button
                style={{ width: '250px' }}
                onClick={() => {
                  setSelectedButton(1);
                  getFutureAppointments();
                }}
              >
                {' '}
                Get Future Appointments
              </Button>
            </Col>
            <Col>
              <Button
                style={{ width: '250px' }}
                onClick={() => {
                  setSelectedButton(2);
                  getVaccinesDue();
                  setAvailableSlots([]);
                  setSelectedVaccineOptions([]);
                  setSelectedClinic('Select a clinic');
                }}
              >
                {' '}
                Book New Appointment
              </Button>
            </Col>
            <Col>
              <Button
                style={{ width: '250px' }}
                onClick={() => {
                  setSelectedButton(3);
                  getPastAppointments();
                }}
              >
                {' '}
                Get past Appointments{' '}
              </Button>
            </Col>
          </Row>
          <Row>
            <Col>
              {selectedButton === 1 ? (
                <>
                  <h2 style={{ marginTop: '20px' }}>Future Appointments</h2>
                  <div
                    style={{
                      marginTop: '20px',
                      alignItems: 'center',
                      display: 'flex',
                      flexDirection: 'column',
                    }}
                  >
                    {appointments.map((app) => {
                      return (
                        <div class="card" style={{ marginTop: '10px', width: '900px' }}>
                          <div class="card-body">
                            <div
                              style={{
                                textAlign: 'left',
                                display: 'flex',
                                justifyContent: 'space-between',
                              }}
                            >
                              <div style={{ display: 'flex', flexDirection: 'column' }}>
                                <h3>{app && app.clinic ? app.clinic.name : ''}</h3>
                                <div
                                  style={{
                                    display: 'flex',
                                    justifyContent: 'flex-start',
                                    marginTop: '20px',
                                    textAlign: 'left',
                                  }}
                                >
                                  {app && app.vaccines && app.vaccines.length > 0
                                    ? app.vaccines.map((vac, index) =>
                                        index === app.vaccines.length - 1 ? (
                                          <h6 class="card-subtitle text-muted">{vac.name}</h6>
                                        ) : (
                                          <>
                                            <h6
                                              class="card-subtitle text-muted"
                                              style={{ marginRight: '5px' }}
                                            >
                                              {vac.name}
                                              {','}
                                            </h6>
                                          </>
                                        )
                                      )
                                    : null}
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                                  <p class="card-text" style={{ marginTop: '20px' }}>
                                    Date: {app && app.date}
                                  </p>
                                  <p
                                    class="card-text"
                                    style={{ marginTop: '20px', marginLeft: '10px' }}
                                  >
                                    Time: {app && app.time}
                                  </p>
                                </div>
                              </div>

                              <div style={{ display: 'flex', alignItems: 'center' }}>
                                <Button
                                  style={{ width: '250px' }}
                                  onClick={() => {
                                    setSelectedAppointment(app);
                                    setShowAppointmentDetails(true);
                                  }}
                                >
                                  View details
                                </Button>
                              </div>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </>
              ) : selectedButton === 2 ? (
                <>
                  <hr />
                  <h2 style={{ marginTop: '20px' }}>Book an appointment</h2>
                  <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <div>
                      <DatePicker
                        selected={startDate}
                        onChange={(date) => setStartDate(date)}
                        onSelect={(date) => setStartDate(date)}
                        minDate={new Date(currentDate ? currentDate : '12-01-2020')}
                        maxDate={new Date(maxAvailableDate)}
                        placeholderText="Select Date"
                      />
                    </div>
                    <div>
                      <MultiSelect
                        options={vaccinesDue}
                        displayValue="name"
                        selectedValues={selectedVaccineOptions}
                        onSelect={(selected) => {
                          setSelectedVaccineOptions(selected);
                        }}
                        onRemove={(selected) => {
                          setSelectedVaccineOptions(selected);
                        }}
                      />
                    </div>
                    <div>
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
                  <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                    <Button style={{ width: '200px' }} onClick={() => getAvailableSlots()}>
                      {' '}
                      Get Appointments{' '}
                    </Button>
                    {selectedVaccineOptions.length > 0 &&
                    selectedClinicID !== '' &&
                    selectedSlot ? (
                      <Button
                        style={{ width: '200px', marginLeft: '30px' }}
                        variant="success"
                        onClick={() => createAppointment()}
                      >
                        {' '}
                        Create Appointment
                      </Button>
                    ) : null}
                  </div>

                  <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                    {availableSlots && availableSlots.length > 0 ? (
                      <Row md={6} className="g-4">
                        {availableSlots.map((slot) => (
                          <Col>
                            <Card
                              className={`${
                                selectedSlot && selectedSlot.time === slot.time
                                  ? 'selectedCard'
                                  : ''
                              }`}
                              style={{ cursor: 'pointer' }}
                              onClick={() => setSelectedSlot(slot)}
                            >
                              <Card.Body>
                                <Card.Title>{slot.time}</Card.Title>
                                <Card.Text>Available slots: {slot.slots}</Card.Text>
                              </Card.Body>
                            </Card>
                          </Col>
                        ))}
                      </Row>
                    ) : null}
                  </div>
                </>
              ) : (
                <>
                  <h2 style={{ marginTop: '20px' }}>Past Appointments</h2>
                  <div
                    style={{
                      marginTop: '20px',
                      alignItems: 'center',
                      display: 'flex',
                      flexDirection: 'column',
                    }}
                  >
                    {appointments.map((app) => {
                      return (
                        <div class="card" style={{ marginTop: '10px', width: '900px' }}>
                          <div class="card-body">
                            <div
                              style={{
                                textAlign: 'left',
                                display: 'flex',
                                justifyContent: 'space-between',
                              }}
                            >
                              <div style={{ display: 'flex', flexDirection: 'column' }}>
                                <h3>{app && app.clinic ? app.clinic.name : ''}</h3>
                                <div
                                  style={{
                                    display: 'flex',
                                    justifyContent: 'flex-start',
                                    marginTop: '20px',
                                    textAlign: 'left',
                                  }}
                                >
                                  {app && app.vaccines && app.vaccines.length > 0
                                    ? app.vaccines.map((vac, index) =>
                                        index === app.vaccines.length - 1 ? (
                                          <h6 class="card-subtitle text-muted">{vac.name}</h6>
                                        ) : (
                                          <>
                                            <h6
                                              class="card-subtitle text-muted"
                                              style={{ marginRight: '5px' }}
                                            >
                                              {vac.name}
                                              {','}
                                            </h6>
                                          </>
                                        )
                                      )
                                    : null}
                                </div>
                                <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                                  <p class="card-text" style={{ marginTop: '20px' }}>
                                    Date: {app && app.date}
                                  </p>
                                  <p
                                    class="card-text"
                                    style={{ marginTop: '20px', marginLeft: '10px' }}
                                  >
                                    Time: {app && app.time}
                                  </p>
                                </div>
                              </div>

                              <div style={{ display: 'flex', alignItems: 'center' }}>
                                <Button
                                  style={{ width: '250px' }}
                                  onClick={() => {
                                    setSelectedAppointment(app);
                                    setShowAppointmentDetails(true);
                                  }}
                                >
                                  View details
                                </Button>
                              </div>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </>
              )}
            </Col>
          </Row>
        </div>
      </div>
    </>
  );
};

export default Dashboard;
