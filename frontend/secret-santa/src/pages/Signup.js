import { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

//mui
import LoadingButton from '@mui/lab/LoadingButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { Avatar, TextField, Grid, Box, Container, Stack, Typography, Button } from '@mui/material';

//services
import { alertsService } from '../services/alerts.service';

//instantiate services
const alert = new alertsService();
const theme = new createTheme();

function Signup() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');


    function handleFirstNameChange(e) {
        setFirstName(e.target.value)
    }

    function handleLastNameChange(e) {
        setLastName(e.target.value)
    }

    function displayRejectMsg() {
        alert.showError("Santa is dead. Santa remains dead. And you have killed him");
    }

    async function handleSubmit(event) {
        setLoading(true);
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        var decision = {
            firstName: data.get('firstName'),
            lastName: data.get('lastName'),
            comment: data.get('comment'),
        }

        console.log(decision);
        /*
        try {
            await web3Service.deploy(profileData);
            navigate('/');
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
        */
    };

    return (
        <ThemeProvider theme={theme}>
            <Container id="signup-container" component="main" maxWidth="xs">
                <Box
                    sx={{
                        marginTop: 0,
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
                                    required
                                    fullWidth
                                    id="firstName"
                                    name="firstName"
                                    label="First Name"
                                    autoComplete="given-name"
                                    value={firstName} onChange={handleFirstNameChange}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    fullWidth
                                    id="lastName"
                                    name="lastName"
                                    label="Last Name"
                                    autoComplete="family-name"
                                    value={lastName} onChange={handleLastNameChange}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    multiline
                                    rows={4}
                                    id="comment"
                                    name="comment"
                                    label="Any comment to share with your secret santa?"
                                />
                            </Grid>
                        </Grid>
                        <Typography variant="body1" sx={{ mt: 2 }}>
                            Do you want to participate?
                        </Typography>
                        <Stack spacing={2} direction="row" sx={{ mt: 1, mb: 5 }}>
                            <Link to="/reject" style={{ textDecoration: 'none' }} onClick={displayRejectMsg}>
                                <Button
                                    fullWidth
                                    variant="contained"
                                    startIcon={<ThumbDownIcon />}
                                    color="error"
                                >
                                    NO
                                </Button>
                            </Link>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                loading={loading}
                                loadingPosition="start"
                                startIcon={<ThumbUpIcon />}
                                color="success"
                                disabled={!firstName || !lastName}
                            >
                                YES
                            </LoadingButton>

                        </Stack>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}

export default Signup