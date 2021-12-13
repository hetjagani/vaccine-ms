import React from 'react';
import { Redirect, useLocation } from 'react-router-dom';
import { setCookie } from 'react-use-cookie';

// http://localhost:3000/oauth2/redirect?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoZXRwYXRlbDU3MkBnbWFpbC5jb20iLCJpYXQiOjE2MzgyNDI3MzIsInJvbGVzIjoiUEFUSUVOVCIsImV4cCI6MTYzOTEwNjczMn0.rVmWjB2p3GdYpNVEiEzN1VgVb969Q8QBKwefEYsSViMr1UNppXPPVnShBBTOoeOwGhXHTEbInjB2DqCxaIXfOQ#

const OAuth2RedirectHandler = (props) => {
  const getUrlParameter = (name) => {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

    var results = regex.exec(props.location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  };

  const token = getUrlParameter('token');
  const error = getUrlParameter('error');

  console.log(token);
  console.log(error);

  if (token) {
    setCookie('auth', token);
    return (
      <Redirect
        to={{
          pathname: '/oauth2/getdetails',
          state: { from: props.location },
        }}
      />
    );
  } else {
    return (
      <Redirect
        to={{
          pathname: '/login',
          state: {
            from: props.location,
            error: error,
          },
        }}
      />
    );
  }
};

export default OAuth2RedirectHandler;
