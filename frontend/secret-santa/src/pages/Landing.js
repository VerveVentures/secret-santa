
import Button from '@mui/material/Button';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import AddIcon from '@mui/icons-material/Add';
import Stack from '@mui/material/Stack';
import { Link } from "react-router-dom";

function Landing() {
    return (
        <Stack direction="row" spacing={2}>
            <Link to="/admin/" style={{ textDecoration: 'none' }}>
                <Button variant="contained" endIcon={<AdminPanelSettingsIcon />}>
                    Admin
                </Button>
            </Link>
            <Link to="/signup/" style={{ textDecoration: 'none' }}>
                <Button variant="contained" endIcon={<AddIcon />}>
                    Signup
                </Button>
            </Link>
        </Stack>
    );
}

export default Landing