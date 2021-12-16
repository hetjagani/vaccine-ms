import './App.css';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import { getCookie } from 'react-use-cookie';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import OAuthUserDetails from './components/OAuthUserDetails';
import UserVerification from './pages/UserVerification/UserVerification';
import Disease from './pages/Admin/Disease';
import Clinic from './pages/Admin/Clinic';
import Vaccine from './pages/Admin/Vaccine';
import Dashboard from './pages/Patient/Dashboard';
import { Toaster } from 'react-hot-toast';
import VaccinesDue from './pages/Patient/VaccinesDue';
import PatientReport from './pages/Patient/PatientReport';
import AdminReport from './pages/Admin/AdminReport';

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
        <Toaster position="top-center" reverseOrder={false} />
        <Switch>
          <Route path="/login" component={Login} />
          <Route path="/signup" component={Signup} />
          <Route path="/userVerification" component={UserVerification} />
          <Route path="/disease" component={Disease} />
          <Route path="/clinic" component={Clinic} />
          <Route path="/vaccine" component={Vaccine} />
          <Route path="/oauth2/redirect" component={OAuth2RedirectHandler} />
          <Route path="/oauth2/getdetails" component={OAuthUserDetails} />
          <Route path="/dashboard/vaccines/due" component={VaccinesDue} />
          <Route path="/dashboard/report" component={PatientReport} />
          <Route path="/admin/report" component={AdminReport} />
          <Route path="/dashboard" component={Dashboard} />
          <Route path="/" component={Login} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
