import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { LoginPage, ServiceDashboard, ServicePage } from './pages';
import { PrivateRoute } from './components';
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/service" element={<ServicePage />} />
        <Route path='*' element='404 Not Found' />
        <Route path='/service/dashboard' element={
          < PrivateRoute>
            <ServiceDashboard />
          </PrivateRoute>
        } />
      </Routes>
    </Router>
  );
}

export default App;
