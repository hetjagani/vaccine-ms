import { Button, Container, Navbar } from 'react-bootstrap';
import React, { useState } from 'react';
import { Form } from 'react-bootstrap';
import styles from './Login.module.css';
import axios from 'axios';
import { setCookie } from 'react-use-cookie';
import Navigation from '../../components/Navigation';
import iconGoogle from '../../assets/images/icon-google.png';

const Login = () => {
  const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';
  const GOOGLE_AUTH_URL =
    window.BACKEND_API_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const submitForm = (e) => {
    e.preventDefault();
    const data = { email, password };

    axios
      .post(`/auth/login`, data)
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
      <Container className={styles.main} fluid="sm">
        <Form onSubmit={submitForm}>
          <Form.Group className={styles.formGroup} controlId="formBasicEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              type="email"
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter email"
            />
            <Form.Text className="text-muted">
              We'll never share your email with anyone else.
            </Form.Text>
          </Form.Group>

          <Form.Group className={styles.formGroup} controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Login
          </Button>
          <Button variant="primary" type="submit" style={{marginLeft: '15px'}}>
            Sign Up
          </Button>
          <span style={{marginLeft: '15px'}}>
            or Sign in with
          </span>
            <a href={GOOGLE_AUTH_URL} style={{ color: 'white', textDecoration: 'none' }}>
              <img src={iconGoogle} alt="Sign in with google" style={{height:'40px' }}/>
            </a>
        </Form>
      </Container>
    </div>
  );
};

export default Login;
