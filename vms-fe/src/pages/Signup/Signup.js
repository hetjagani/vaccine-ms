import axios from 'axios';
import React, { useEffect, useState, fieldset } from 'react';
import { Button, Col, Container, Form, InputGroup, Row } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import { getCookie } from 'react-use-cookie';
import { setCookie } from 'react-use-cookie';
import Navigation from '../../components/Navigation';

function Signup() {
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
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();

    const data = {
      firstName,
      lastName,
      middleName,
      city,
      stateName,
      street,
      zipcode,
      dateOfBirth,
      gender,
    };

    axios
      .post(`/auth/signup`, data)
      .then((res) => {
        const token = res?.data?.token;

        if (token) {
          setCookie('auth', token);
        }
      })
      .catch((err) => {
        console.error(err);
      });
  };

  return (
    <div>
      <Navigation />
      <Container>
        Please provide Your Details
        <div style={{ margin: '20px' }}>
          <Form onSubmit={handleSubmit}>
            <Row className="mb-3">
              <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  required
                  type="email"
                  placeholder="Enter email"
                  onChange={(e) => setEmail(e.target.value)}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
              </Form.Group>
              <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  required
                  type="password"
                  placeholder="Password"
                  onChange={(e) => setPassword(e.target.value)}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
              </Form.Group>
            </Row>
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
            <Button type="submit">Sign Up</Button>
          </Form>
        </div>
      </Container>
    </div>
  );
}

export default Signup;
