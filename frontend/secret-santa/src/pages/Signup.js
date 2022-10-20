import { useState } from 'react';
import { useNavigate } from "react-router-dom";


import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import LoadingButton from '@mui/lab/LoadingButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import SendIcon from '@mui/icons-material/Send';
import {
    Avatar
    , TextField
    , Grid
    , Box
    , Container
} from '@mui/material';

const theme = createTheme();

function Signup() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (event) => {
        /*
        setLoading(true);
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        var profileData = {
            firstName: data.get('firstName'),
            lastName: data.get('lastName'),
            email: data.get('email'),
        }

        try {
            await web3Service.deploy(profileData);
            navigate('/');
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
        */
        console.log('submit')

    };

    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="xs">
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ width: '100%', height: '100%' }} src="/VERVE_Logo_DarkBlue_Orange.png"></Avatar>
                    <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="given-name"
                                    name="firstName"
                                    required
                                    fullWidth
                                    id="firstName"
                                    label="First Name"
                                    autoFocus
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    fullWidth
                                    id="lastName"
                                    label="Last Name"
                                    name="lastName"
                                    autoComplete="family-name"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    multiline
                                    rows={4}
                                    id="comment"
                                    label="Any comment to share with your secret santa?"
                                    name="comment"
                                    autoComplete="comment"
                                />
                            </Grid>
                        </Grid>
                        <LoadingButton
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            loading={loading}
                            loadingPosition="start"
                            startIcon={<SendIcon />}
                        >
                            Participate
                        </LoadingButton>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}

export default Signup