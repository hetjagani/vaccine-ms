import axios from 'axios';
import React, { useEffect, useState, fieldset } from 'react';
import { Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import { getCookie } from 'react-use-cookie';
import Navigation from './Navigation';

function OAuthUserDetails() {
  const token = getCookie('auth');
  const history = useHistory();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [middleName, setMiddleName] = useState('');
  const [city, setCity] = useState('');
  const [stateName, setStateName] = useState('');
  const [street, setStreet] = useState('');
  const [zipcode, setZipcode] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('');
  const [gender, setGender] = useState('');
  const [validated, setValidated] = useState(false);

  const handleSubmit = (event) => {
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }
    
    setValidated(true);
    
    const data = { firstName, lastName, middleName,city, stateName, street, zipcode, dateOfBirth, gender, validated  };
  };

  const getUserDetails = () => {
    axios
      .get(`users/me`, {
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
        if (res && res.data) {
          if (res.data.gender) {
            history.push('/dashboard');
          }
          if (res.data.firstName) {
            setFirstName(res.data.firstName);
          }
          if (res.data.lastName) {
            setLastName(res.data.lastName);
          }
        }
      });
  };

  

  useEffect(() => {
    getUserDetails();
  }, []);

  return (
    <div>
      <Navigation />
      <Container>
        Please provide more Details
        <div style={{ margin: '20px' }}>
          <Form noValidate validated={validated} onSubmit={handleSubmit}>
            <Row className="mb-3">
              <Form.Group as={Col} md="4" controlId="validationCustom01">
                <Form.Label>First name</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="First name"
                  onChange={(e) => setFirstName(e.target.value)}
                  value={firstName ? firstName : null}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label>Middle name</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Middle name"
                  onChange={(e) => setMiddleName(e.target.value)}
                  value={middleName ? lastName : null}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label>Last name</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Last name"
                  onChange={(e) => setLastName(e.target.value)}
                  value={lastName ? lastName : null}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
              </Form.Group>
            </Row>
            <Row className="mb-3">
              <Form.Group as={Col} md="3" controlId="validationCustom03">
                <Form.Label>Street</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Street"
                  required
                  onChange={(e) => setStreet(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">
                  Please provide a valid city.
                </Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="3" controlId="validationCustom03">
                <Form.Label>City</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="City"
                  required
                  onChange={(e) => setCity(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">
                  Please provide a valid city.
                </Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="3" controlId="validationCustom04">
                <Form.Label>State</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="State"
                  required
                  onChange={(e) => setStateName(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">
                  Please provide a valid state.
                </Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="3" controlId="validationCustom05">
                <Form.Label>Zipcode</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Zip"
                  required
                  onChange={(e) => setZipcode(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">
                  Please provide a valid zip.
                </Form.Control.Feedback>
              </Form.Group>
            </Row>
            <Row md={3}>
            <fieldset>
              <Form.Group as={Row} className="mb-3">
              <Form.Label>Gender</Form.Label>

                <Col sm={3}>
                  <Form.Check
                    type="radio"
                    label="Male"
                    name="formHorizontalRadios"
                    id="formHorizontalRadios1"
                    value="Male"
                    onChange={(e) => setGender(e.target.value)}

                  />
                  <Form.Check
                    type="radio"
                    label="Female"
                    name="formHorizontalRadios"
                    id="formHorizontalRadios2"
                    value="Female"
                    onChange={(e) => setGender(e.target.value)}

                  />
                </Col>
              </Form.Group>
                </fieldset>

              <Col sm={7}>
                <Form.Label>Date Of Birth:</Form.Label>
                <Form.Control
                  type="date"
                  placeholder="date of birth"
                  required
                  onChange={(e) => setDateOfBirth(e.target.value)}
                />
                </Col>
            </Row>
            <Button type="submit">Submit form</Button>
          </Form>
        </div>
      </Container>
    </div>
  );
}

export default OAuthUserDetails;
