import { useState } from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { TextField, Grid, Box, Container} from '@mui/material';
import LoadingButton from '@mui/lab/LoadingButton';
import {useNavigate} from 'react-router-dom';

const create = new createTheme();

function Create() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [sessionName, setSessionName] = useState('');
  const [passphrase, setPassphrase] = useState('');

  function handleSessionNameChange(e) {
    setSessionName(e.target.value)
  }

  function handlePassphraseChange(e) {
    setPassphrase(e.target.value)
  }

  async function handleSubmit(event) {
    setLoading(true);
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    var newSession = {
      sessionName: data.get('sessionName'),
      passphrase: data.get('passphrase'),
    }

    console.log(newSession);
    const sessionId = await getSessionId(newSession);
    if(sessionId) {
      navigateToSession(sessionId);
    }
  };

  async function getSessionId(newSession) {
    //TODO: get SessionId
    return 'sessionId';
  };

  const navigateToSession = (sessionId) => {
    navigate(`../session/${sessionId}/`, {replace: true});
  };

    return (
      <ThemeProvider theme={create}>
        <Container id="create-session-container" component="main" maxWidth="xs">
        <Box
          sx={{
              marginTop: 10,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
          }}
          > 
            <h1>
                Create Session
            </h1>
            <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="sessionName"
                    name="sessionName"
                    label="Session Name"
                    autoComplete="session-name"
                    value={sessionName} onChange={handleSessionNameChange}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="passphrase"
                    name="passphrase"
                    label="Passphrase"
                    autoComplete="passphrase"
                    value={passphrase} onChange={handlePassphraseChange}
                  />
                </Grid>
                <Grid item xs={12} sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                  <LoadingButton
                    type="submit"
                    size="medium"
                    variant="contained"
                    loading={loading}
                    color="success"
                    disabled={!sessionName || !passphrase}
                  >
                    Create
                  </LoadingButton>
                </Grid>
              </Grid>
           </Box>
          </Box>
        </Container>
      </ThemeProvider>
    );
}

export default Create