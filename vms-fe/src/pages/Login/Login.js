import { Button, Container, Navbar } from 'react-bootstrap';
import React, { useState } from 'react';
import { Form } from 'react-bootstrap';
import styles from './Login.module.css';
import axios from 'axios';
import { setCookie } from 'react-use-cookie';
import Navigation from '../../components/Navigation';
import iconGoogle from '../../assets/images/icon-google.png';
import { useHistory } from 'react-router-dom';

const Login = () => {
  const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect';
  const GOOGLE_AUTH_URL =
    window.BACKEND_API_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;

  const history = useHistory();
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
          history.push('/dashboard');
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
        <h2>Login Here,</h2>
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
          <Button variant="primary" type="submit" style={{ marginLeft: '15px' }} onClick={() => history.push("/signup")}>
            Sign Up
          </Button>
        </Form>
        <div style={{ margin: '50px', backgroundColor: '#3569bd', padding: '10px', borderRadius: '10px' }}>
          <a href={GOOGLE_AUTH_URL} style={{ textDecoration: 'none', color: 'white'}}>
            <span style={{ margin: '15px' }}>or Sign in with</span>
            <img src={iconGoogle} alt="Sign in with google" style={{ height: '40px' }} />
          </a>
        </div>
      </Container>
    </div>
  );
};

export default Login;
