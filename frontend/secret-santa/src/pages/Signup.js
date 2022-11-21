import { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";

//mui
import LoadingButton from '@mui/lab/LoadingButton';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import ThumbDownIcon from '@mui/icons-material/ThumbDown';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { Avatar, TextField, Grid, Box, Container, Stack, Typography, Button } from '@mui/material';

//services
import { alertsService } from '../services/alerts.service';
import { ParticipantService } from '../services/participant.service';

//instantiate services
const alert = new alertsService();
const participantService = new ParticipantService();

const theme = new createTheme();

function Signup() {

    const params = new useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    useEffect(() => {verifySession()}, []);

    async function verifySession() {
        if (params.id) {
            if (await participantService.exists(params.id)) {
                // get and set data
            } else {
                navigate("/");
                alert.showError("Your session is not valid.")
            }
        } else {
            navigate("/");
        }
    }


    function handleFirstNameChange(e) {
        setFirstName(e.target.value)
    }

    function handleLastNameChange(e) {
        setLastName(e.target.value)
    }

    async function handleSubmit(event) {
        setLoading(true);
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        var participant = {
            id: params.id,
            firstName: data.get('firstName'),
            lastName: data.get('lastName'),
            comment: data.get('comment'),
            participates: event.nativeEvent.submitter.id
        }

        if (participant.participating === 'yes') {
            participantService.updateParticipant(participant)
        } else {
            navigate("/reject");
            alert.showError("Santa is dead. Santa remains dead. And you have killed him, it's your fault. You should really reevaluate your life choices and question why you don't want to makes someone else's christmas better you ungrateful little");
        }
        setLoading(false);
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
                                    label="Write your wish to your Santa, keep in mind that it is recession"
                                />
                            </Grid>
                        </Grid>
                        <Typography variant="body1" sx={{ mt: 2 }}>
                            Do you want to participate?
                        </Typography>
                        <Stack spacing={2} direction="row" sx={{ mt: 1, mb: 5 }}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                startIcon={<ThumbDownIcon />}
                                color="error"
                                id="no"
                                name="No"
                            >
                                NO
                            </Button>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                loading={loading}
                                loadingPosition="start"
                                startIcon={<ThumbUpIcon />}
                                color="success"
                                disabled={!firstName || !lastName}
                                id="yes"
                                name="Yes"
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