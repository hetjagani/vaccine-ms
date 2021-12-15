import './App.css';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import { getCookie } from 'react-use-cookie';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import OAuthUserDetails from './components/OAuthUserDetails';
import CustomerDashboard from './components/CustomerDashboard';
import UserVerification from './pages/UserVerification/UserVerification'

axios.defaults.baseURL = window.BACKEND_API_URL;
axios.interceptors.request.use((req) => {
  const token = getCookie('auth');
  if (token) {
    req.headers.Authorization = `Bearer ${token}`;
  }

  return req;
});

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Switch>
          <Route path="/login" component={Login} />
          <Route path="/signup" component={Signup}/>
          <Route path='/userVerification' component={UserVerification}/>
          <Route path="/oauth2/redirect" component={OAuth2RedirectHandler} />
          <Route path="/oauth2/getdetails" component={OAuthUserDetails} />
          <Route path="/dashboard" component={CustomerDashboard} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
