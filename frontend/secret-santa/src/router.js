import { useRoutes } from "react-router-dom";
//pages
import Landing from './pages/Landing';
import Admin from './pages/Admin';
import Signup from './pages/Signup';
// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    { path: '/', element: <Landing /> },
    { path: '/admin', element: <Admin /> },
    { path: '/signup', element: <Signup /> }
  ]);
}