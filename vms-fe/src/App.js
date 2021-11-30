import './App.css';
import Login from './pages/Login/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import { getCookie } from 'react-use-cookie';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';

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
          <Route path="/oauth2/redirect" component={OAuth2RedirectHandler} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
