import React, { useEffect, useState } from 'react';
import { Button, Card, Col, DropdownButton, Row, ButtonGroup, Dropdown } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import DatePicker from 'react-datepicker';
import axios from 'axios';
import toast from 'react-hot-toast';
import MultiSelect from 'multiselect-react-dropdown';
import { getCookie } from 'react-use-cookie';

function UpdateAppointmentModal({ selectedAppointment, showUpdateModal, setShowUpdateModal }) {
  const currentDateTime = new Date();
  const currentDate = `${
    currentDateTime.getMonth() + 1
  }-${currentDateTime.getDate()}-${currentDateTime.getFullYear()}`;
  const maxAvailableDate = `${currentDateTime.getMonth() + 1}-${currentDateTime.getDate()}-${
    currentDateTime.getFullYear() + 1
  }`;
  const [startDate, setStartDate] = useState(new Date(currentDate));
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(
    selectedAppointment && selectedAppointment.status ? selectedAppointment.status : ''
  );
  const [statusOptions, setStatusOptions] = useState(['INIT', 'CHECKIN', 'NOSHOW']);

  const getAvailableSlots = () => {
    axios
      .get('/appointments/slots', {
        params: {
          clinicId: selectedAppointment.clinic.id,
          date: `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`,
        },
      })
      .then((res) => {
        if (res && res.data) {
          setAvailableSlots(res.data);
          setSelectedSlot({ time: selectedAppointment.time });
        } else {
          setAvailableSlots([]);
        }
      })
      .catch((err) => {
        console.log(err);
      });
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

  const updateAppointment = async () => {
    if (!selectedSlot) {
      toast.error('Please select a time slot to update to!');
      return;
    }
    const vaccineIds = [];
    selectedAppointment.vaccines.forEach((vaccine) => {
      vaccineIds.push(vaccine.id);
    });
    const userDetails = await getUserDetails();
    const updateAppointmentObj = {
      time: selectedSlot.time,
      vaccineIds,
      clinicId: selectedAppointment.clinic.id,
      userId: userDetails.mrn,
      date: `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`,
      status: selectedStatus,
    };
    axios
      .put(`/appointments/${selectedAppointment.id}`, updateAppointmentObj)
      .then((res) => {
        if (res && res.data) {
          toast.success('Updated appointment');
          setAvailableSlots([]);
          setSelectedSlot(null);
          setShowUpdateModal(false);
        }
      })
      .catch((err) => {
        toast.error('Could not update appointment!');
        console.log(err);
      });
  };

  useEffect(() => {
    if (!selectedSlot) {
      return;
    }
    const timeSplitted = selectedSlot.time.split(':');
    if (timeSplitted && timeSplitted.length) {
      const selectedDateTime = new Date(
        startDate.getFullYear(),
        startDate.getMonth(),
        startDate.getDate(),
        parseInt(timeSplitted[0]),
        parseInt(timeSplitted[1])
      );
      const currDate = new Date();
      const diffTime = Math.abs(selectedDateTime - currDate);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      if (diffDays > 1) {
        setStatusOptions(['INIT', 'NOSHOW']);
      } else {
        setStatusOptions(['INIT', 'CHECKIN', 'NOSHOW']);
      }
    }
  }, [startDate, selectedSlot]);

  console.log(selectedAppointment);

  useEffect(() => {
    getAvailableSlots();
  }, [startDate]);

  return (
    <Modal
      dialogClassName="modalWidth"
      show={showUpdateModal}
      onHide={() => setShowUpdateModal(false)}
    >
      <Modal.Header closeButton>
        <div style={{ display: 'flex', flexDirection: 'column', marginBottom: '-20px' }}>
          <Modal.Title>
            {selectedAppointment && selectedAppointment.clinic
              ? selectedAppointment.clinic.name
              : ''}
          </Modal.Title>
          <p className="text-muted">
            {selectedAppointment && selectedAppointment.clinic && selectedAppointment.clinic.address
              ? `${selectedAppointment.clinic.address.street}, ${selectedAppointment.clinic.address.city}, ${selectedAppointment.clinic.address.state} - ${selectedAppointment.clinic.address.zipcode}`
              : ''}
          </p>
        </div>
      </Modal.Header>
      <Modal.Body>
        <div style={{ display: 'flex', flexDirection: 'column', width: '100%' }}>
          <p>Date: {selectedAppointment ? selectedAppointment.date : ''}</p>
          <p>Time: {selectedAppointment ? selectedAppointment.time : ''}</p>
          <p>Status: {selectedAppointment ? selectedAppointment.status : ''}</p>
          <p>
            Vaccines:
            {selectedAppointment &&
            selectedAppointment.vaccines &&
            selectedAppointment.vaccines.length > 0
              ? selectedAppointment.vaccines.map((vac, index) =>
                  index === selectedAppointment.vaccines.length - 1 ? (
                    <span class="text-muted"> {vac.name}</span>
                  ) : (
                    <>
                      <span class="text-muted" style={{ marginRight: '5px' }}>
                        {vac.name}
                        {','}
                      </span>
                    </>
                  )
                )
              : null}
          </p>
          <hr />
          <div style={{ display: 'flex', marginTop: '20px' }}>
            <p style={{ width: '200px' }}>Change date to: </p>
            <DatePicker
              selected={startDate}
              onChange={(date) => setStartDate(date)}
              onSelect={(date) => setStartDate(date)}
              minDate={new Date(currentDate ? currentDate : '12-01-2020')}
              maxDate={new Date(maxAvailableDate)}
              placeholderText="Select Date"
            />
          </div>
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <p style={{ width: '200px' }}>Change time to: </p>
            {availableSlots && availableSlots.length > 0 ? (
              <Row md={6} className="g-4">
                {availableSlots.map((slot) => (
                  <Col>
                    <Card
                      className={`${
                        selectedSlot && selectedSlot.time === slot.time ? 'selectedCard' : ''
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
          <div style={{ display: 'flex', marginTop: '15px' }}>
            <p style={{ width: '200px' }}>Update status: </p>
            <DropdownButton
              as={ButtonGroup}
              title={selectedStatus}
              onSelect={(e) => {
                setSelectedStatus(e);
              }}
            >
              {statusOptions.map((status) => (
                <Dropdown.Item eventKey={status}>{status}</Dropdown.Item>
              ))}
            </DropdownButton>
          </div>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="success"
          onClick={() => {
            updateAppointment();
          }}
        >
          Update Appointment
        </Button>

        <Button variant="secondary" onClick={() => setShowUpdateModal(false)}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default UpdateAppointmentModal;
