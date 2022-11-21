import * as React from 'react';
import PropTypes from 'prop-types';
import { useState } from 'react';
import { useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";

import { Chip, TextField, Grid, Container, styled, Stepper, Step, StepLabel, Box, Button, StepConnector, stepConnectorClasses, Typography } from '@mui/material';
import Check from '@mui/icons-material/Check';
import LoadingButton from '@mui/lab/LoadingButton';
import SendIcon from '@mui/icons-material/Send';
import TornadoIcon from '@mui/icons-material/Tornado';
import EmailIcon from '@mui/icons-material/Email';
import GroupIcon from '@mui/icons-material/Group';

import { SessionService } from '../services/session.service';
import { ParticipantService } from '../services/participant.service';

const sessionService = new SessionService();
const participantsService = new ParticipantService();

const steps = [
    'Name Session',
    'Add Participants',
    'Send Invitations',
    'Scramble',
];

function Admin() {
    const params = new useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [sessionName, setSessionName] = useState('');
    const [participants, setParticipants] = useState([]);
    const [activeStep, setActiveStep] = React.useState(0);
    const localStorageSessionId = 'secret-santa-session-id';

    useEffect(() => {checkSessionStage()}, []);

    async function checkSessionStage() {
        setLoading(true);
        let session;
        if (params.id) {
            session = await sessionService.getSession(params.id);
        }
        if (session) {
            if (session.sessionScrambled) {
                setActiveStep(4);
            } else if (session.emailsSent) {
                setActiveStep(3);
            } else if(session.name) {
                setActiveStep(1);
            }
        }
        setLoading(false);
    }

    function handleSessionNameChange(e) {
        setSessionName(e.target.value)
    }

    function handleParticipantsChange(e) {
        setParticipants(e.target.value)
    }

    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleReset = () => {
        localStorage.clear();
        setActiveStep(0);
    };

    const QontoConnector = styled(StepConnector)(({ theme }) => ({
        [`&.${stepConnectorClasses.alternativeLabel}`]: {
            top: 10,
            left: 'calc(-50% + 16px)',
            right: 'calc(50% + 16px)',
        },
        [`&.${stepConnectorClasses.active}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#784af4',
            },
        },
        [`&.${stepConnectorClasses.completed}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#784af4',
            },
        },
        [`& .${stepConnectorClasses.line}`]: {
            borderColor: theme.palette.mode === 'dark' ? theme.palette.grey[800] : '#eaeaf0',
            borderTopWidth: 3,
            borderRadius: 1,
        },
    }));

    const QontoStepIconRoot = styled('div')(({ theme, ownerState }) => ({
        color: theme.palette.mode === 'dark' ? theme.palette.grey[700] : '#eaeaf0',
        display: 'flex',
        height: 22,
        alignItems: 'center',
        ...(ownerState.active && {
            color: '#784af4',
        }),
        '& .QontoStepIcon-completedIcon': {
            color: '#784af4',
            zIndex: 1,
            fontSize: 18,
        },
        '& .QontoStepIcon-circle': {
            width: 8,
            height: 8,
            borderRadius: '50%',
            backgroundColor: 'currentColor',
        },
    }));

    function QontoStepIcon(props) {
        const { active, completed, className } = props;

        return (
            <QontoStepIconRoot ownerState={{ active }} className={className}>
                {completed ? (
                    <Check className="QontoStepIcon-completedIcon" />
                ) : (
                    <div className="QontoStepIcon-circle" />
                )}
            </QontoStepIconRoot>
        );
    }

    QontoStepIcon.propTypes = {
        /**
         * Whether this step is active.
         * @default false
         */
        active: PropTypes.bool,
        className: PropTypes.string,
        /**
         * Mark the step as completed. Is passed to child components.
         * @default false
         */
        completed: PropTypes.bool,
    };

    async function createSession(event) {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        setSessionName(data.get('sessionName'));
        await sessionService.createSession(sessionName).then(session => {
            localStorage.setItem(localStorageSessionId, session.id);
            navigate(session.id)
        })
        handleNext();
    };

    async function addParticipants(event) {
        setLoading(true);
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        if (!data.get('participants')) {
            handleNext();
            setLoading(false);
            return;
        }
        const participants = data.get('participants').split(/\r\n|\r|\n/);
        setParticipants(participants);
        await participantsService.createParticipants(
            params.id,
            participants.map(participant => {
                let parts = participant.split('\t');
                return ({
                    sessionId: params.id,
                    email: parts[0],
                    firstName: parts[1],
                    lastName: parts[2]
                });
            })
        );
        handleNext();
        setLoading(false);
    };

    function sendInvitations(event) {
        sessionService.sendInvitations(params.id);
        handleNext();
    };

    async function scramble(event) {
        sessionService.scramble(params.id);
        handleNext();
    };

    return (
        <>
            <Stepper activeStep={activeStep} alternativeLabel connector={<QontoConnector />} sx={{ mt: 2 }}>
                {steps.map((label, index) => {
                    return (
                        <Step key={label}>
                            <StepLabel StepIconComponent={QontoStepIcon}>{label}</StepLabel>
                        </Step>
                    );
                })}
            </Stepper>

            {activeStep === steps.length ? (
                <React.Fragment>
                    <Typography sx={{ mt: 2, mb: 1 }}>
                        All steps completed - you&apos;re finished
                    </Typography>
                    <Box sx={{ display: 'flex', flexDirection: 'row', pt: 2 }}>
                        <Box sx={{ flex: '1 1 auto' }} />
                        <Button onClick={handleReset}>Reset</Button>
                    </Box>
                </React.Fragment>
            ) : (
                <React.Fragment>
                    <Container maxWidth="sm" sx={{ mt: 10 }}>
                        {activeStep === 0 ? (
                            <Box sx={{ flexDirection: 'column', alignItems: 'center' }}>
                                <Box component="form" noValidate onSubmit={createSession} sx={{ mt: 3 }}>
                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <TextField
                                                required
                                                fullWidth
                                                id="sessionName"
                                                name="sessionName"
                                                label="Session Name"
                                                value={sessionName} onChange={handleSessionNameChange}
                                            />
                                        </Grid>
                                    </Grid>
                                    <LoadingButton
                                        type="submit"
                                        fullWidth
                                        variant="contained"
                                        loadingPosition="start"
                                        startIcon={<SendIcon />}
                                        color="primary"
                                        sx={{ mt: 2 }}
                                    >
                                        Create Session
                                    </LoadingButton>
                                </Box>
                            </Box>
                        ) : activeStep === 1 ? (
                            <Box sx={{ flexDirection: 'column', alignItems: 'center' }}>
                                <Box component="form" noValidate onSubmit={addParticipants} sx={{ mt: 3 }}>
                                    <Grid container spacing={2}>
                                        <Grid item xs={12}>
                                            <TextField
                                                required
                                                fullWidth
                                                multiline
                                                rows={4}
                                                id="participants"
                                                name="participants"
                                                label="Add Participants"
                                                value={participants} onChange={handleParticipantsChange}
                                            />
                                        </Grid>
                                    </Grid>
                                    <LoadingButton
                                        type="submit"
                                        fullWidth
                                        variant="contained"
                                        loadingPosition="start"
                                        startIcon={<GroupIcon />}
                                        color="primary"
                                        sx={{ mt: 2 }}
                                    >
                                        Add Participants
                                    </LoadingButton>
                                </Box>
                            </Box>
                        ) : activeStep === 2 ? (
                            <>
                                {participants.map(p => (<Chip label={p} key={p} sx={{ ml: 1, mt: 1 }} />))}
                                <LoadingButton
                                    fullWidth
                                    variant="contained"
                                    loadingPosition="start"
                                    startIcon={<EmailIcon />}
                                    color="primary"
                                    onClick={sendInvitations}
                                    sx={{ mt: 2 }}
                                >
                                    Send Invitation to participants
                                </LoadingButton>
                            </>
                        ) : (
                            <LoadingButton
                                fullWidth
                                variant="contained"
                                loadingPosition="start"
                                startIcon={<TornadoIcon />}
                                color="secondary"
                                onClick={scramble}
                                sx={{ mt: 2 }}
                            >
                                Scramble
                            </LoadingButton>
                        )}
                        <Box sx={{ display: 'flex', flexDirection: 'row', pt: 2 }}>
                            <Button
                                disabled={activeStep === 0 || activeStep === 3}
                                onClick={handleBack}
                            >
                                Back
                            </Button>
                        </Box>
                    </Container>
                </React.Fragment>
            )}
        </>
    );
}

export default Admin