import { useRoutes } from "react-router-dom";
//pages
import Landing from './pages/Landing';
import Create from './pages/Create';
import Signup from './pages/Signup';
import Reject from './pages/Reject';
import Session from './pages/Session';
// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    { path: '/', element: <Landing /> },
    { path: '/create', element: <Create /> },
    { path: '/signup', element: <Signup /> },
    { path: '/reject', element: <Reject /> },
    { path: '/session', element: <Session /> }
  ]);
}