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

//services
import { alertsService } from '../services/alerts.service';
import { coreService } from '../services/core.service';

//instantiate services
const alert = new alertsService();
const core = new coreService();

const steps = [
    'Name Session',
    'Add Participants',
    'Send Invitations',
    'Scramble',
];

function Admin() {

    const params = new useParams();
    const [loading, setLoading] = useState(false);
    const [sessionName, setSessionName] = useState('');
    const [participants, setParticipants] = useState('');
    const [activeStep, setActiveStep] = React.useState(0);


    //on init that triggers before page load
    useEffect(() => {
        checkSessionStage()
    }, []);

    //EDDY TO ADD API CALLS
    async function checkSessionStage() {
        //send this ID to check for session stage
        console.log(params.id)
        
        //this variable should be coming from the back end
        var sessionStage = 'pending';
        
        //if the session is not created yet then start from step 0
        if(sessionStage == 'pending'){
            setActiveStep(0)
        } else if (sessionStage == 'created'){
            //if session was already created then start from scramble
            setActiveStep(3)
        }
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




    function createSession(event) {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        setSessionName(data.get('sessionName'));
        //console.log(sessionName);
        handleNext();
    };

    function addParticipants(event) {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        setParticipants(data.get('participants').split(","));
        //console.log(participants);
        handleNext();
    };

    
    //EDDY TO ADD API CALLS
    async function sendInvitations(event) {
        var payload = {
            sessionName: sessionName,
            participants: participants
        }

        console.log(payload);
        //send payload to BE to send emails and set the stage as CREATED
        handleNext();
    };

    async function scramble(event) {
        console.log(participants);
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
                                    Send Invitation to {participants.length} participants
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
                                disabled={activeStep === 0}
                                onClick={handleBack}
                            >
                                Back
                            </Button>
                            {/* 
                            <Box sx={{ flex: '1 1 auto' }} />
                            <Button onClick={handleNext}>
                                {activeStep === steps.length - 1 ? 'Finish' : 'Next'}
                            </Button>
                            */}
                        </Box>
                    </Container>
                </React.Fragment>
            )}
        </>
    );
}

export default Admin