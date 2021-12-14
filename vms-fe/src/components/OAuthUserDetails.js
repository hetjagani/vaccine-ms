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
    event.preventDefault();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    }
    
    setValidated(true);
    const address = {'street': street,'city': city,'state': stateName,'zipcode':zipcode}
    const data = { firstName, lastName, middleName,address, dateOfBirth, gender, validated  };
    console.log("update Data", data);
    
    axios
      .put(`users/me`, data,{
        headers: {
          Authorization: token,
        },
      })
      .then((res) => {
        console.log(res);
       console.log("Updated!!!")
      });
    
    // update the user 
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
          // if (res.data.gender) {
          //   history.push('/dashboard');
          // }
          console.log(res.data);
          if (res.data.firstName) {
            setFirstName(res.data.firstName);
          }
          if (res.data.lastName) {
            setLastName(res.data.lastName);
          }
          if (res.data.middleName) {
            setMiddleName(res.data.middleName);
          }
          if (res.data?.address?.city) {
            setCity(res.data.address?.city);
          }
          if (res.data?.address?.state) {
            setStateName(res.data.address?.state);
          }
          if (res.data?.address?.street) {
            setStreet(res.data.address?.street);
          }
          if (res.data?.address?.zipcode) {
            setZipcode(res.data?.address?.zipcode);
          }
          if (res.data.gender) {
            setGender(res.data.gender);
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
    <Container style={{ width:'50%'}}>
      Please provide Your Details
      <div style={{ margin: '20px', textAlign:'left' }}>
        <Form onSubmit={handleSubmit}>
          <Row className="mb-3">
            <Form.Group controlId="validationCustom01">
              <Form.Label style={{textAlign:'left'}}>First name</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="First name"
                onChange={(e) => setFirstName(e.target.value)}
                value={firstName ? firstName : null}
              />
              <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group controlId="validationCustom02">
              <Form.Label>Middle name</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Middle name"
                onChange={(e) => setMiddleName(e.target.value)}
                value={middleName ? middleName : null}
              />
              <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group controlId="validationCustom02">
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
            <Form.Group controlId="validationCustom03">
              <Form.Label>Street</Form.Label>
              <Form.Control
                type="text"
                placeholder="Street"
                required
                onChange={(e) => setStreet(e.target.value)}
                value={street ? street : null}

              />
              <Form.Control.Feedback type="invalid">
                Please provide a valid city.
              </Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group controlId="validationCustom03">
              <Form.Label>City</Form.Label>
              <Form.Control
                type="text"
                placeholder="City"
                required
                onChange={(e) => setCity(e.target.value)}
                value={city ? city : null}

              />
              <Form.Control.Feedback type="invalid">
                Please provide a valid city.
              </Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group controlId="validationCustom04">
              <Form.Label>State</Form.Label>
              <Form.Control
                type="text"
                placeholder="State"
                required
                onChange={(e) => setStateName(e.target.value)}
                value={stateName ? stateName : null}

              />
              <Form.Control.Feedback type="invalid">
                Please provide a valid state.
              </Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group controlId="validationCustom05">
              <Form.Label>Zipcode</Form.Label>
              <Form.Control
                type="text"
                placeholder="Zip"
                required
                onChange={(e) => setZipcode(e.target.value)}
                value={zipcode ? zipcode : null}

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

                <Col>
                  <Form.Check
                    type="radio"
                    label="Male"
                    name="formHorizontalRadios"
                    id="formHorizontalRadios1"
                    value="MALE"
                    onChange={(e) => setGender(e.target.value)}
                  />
                  <Form.Check
                    type="radio"
                    label="Female"
                    name="formHorizontalRadios"
                    id="formHorizontalRadios2"
                    value="FEMALE"
                    onChange={(e) => setGender(e.target.value)}
                  />
                </Col>
              </Form.Group>
            </fieldset>
            </Row>

            <Row className="mb-3">
            <Form.Group controlId="validationCustom05">

              <Form.Label>Date Of Birth:</Form.Label>
              <Form.Control
                type="date"
                placeholder="date of birth"
                required
                onChange={(e) => setDateOfBirth(e.target.value)}
              />
              </Form.Group>
              </Row>
              <Row className="mb-3">
                <div style={{textAlign:'center'}} >
                  <Button type="submit">Sign Up</Button>
                </div>
              </Row>
        </Form>
      </div>
    </Container>
  </div>
  );
}

export default OAuthUserDetails;
