import { useState } from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { TextField, Grid, Box, Container, Button} from '@mui/material';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

const sessionTheme = new createTheme();

function Session() {
  const [passphrase, setPassphrase] = useState('');
  const [participantName, setParticipantName] = useState('');
  const [participantEmail, setParticipantEmail] = useState('');
  const [isAdmin, setAdmin] = useState(false);
	const session = {
    id: 'sessionId',
	  name: 'sessionName',
    passphrase: 'passphrase',
    sessionScrambled: false,
    emailsSent: false
  }

  //TODO: Replace with real data
  let participants = [
    {name: 'name1', email: 'email1'},
    {name: 'name2', email: 'email2'},
    {name: 'name3', email: 'email3'},
    {name: 'name4', email: 'email4'},
    {name: 'name5', email: 'email5'},
    {name: 'name1', email: 'email1'},
    {name: 'name2', email: 'email2'},
    {name: 'name3', email: 'email3'},
    {name: 'name4', email: 'email4'},
    {name: 'name5', email: 'email5'},
    {name: 'name1', email: 'email1'},
    {name: 'name2', email: 'email2'},
    {name: 'name3', email: 'email3'},
    {name: 'name4', email: 'email4'},
    {name: 'name5', email: 'email5'}
  ];

  function handlePassphraseChange(e) {
    setPassphrase(e.target.value);
  }

  function handleParticipantNameChange(e) {
    setParticipantName(e.target.value);
  }
  function handleParticipantEmailChange(e) {
    setParticipantEmail(e.target.value);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

      var newSession = {
        passphrase: data.get('passphrase'),
      }

      console.log(newSession);
      setAdmin(session.passphrase === data.get('passphrase'));
      console.log('admin', isAdmin);
  };

  async function addParticipant(event) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

      const newParticipant = {
        participantName: data.get('participantName'),
        participantEmail: data.get('participantEmail'),
      }

      //TODO: Add newParticipant to DB
      participants.push(newParticipant)

      console.log(newParticipant);

  };

  async function sendEmail() {
    //TODO: Implement sendEmail function
  }


    return (
      <ThemeProvider theme={sessionTheme}>
        {!isAdmin && <Container id="session-check-container" component="main" maxWidth="xs">
          <Box
            sx={{
                marginTop: 0,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}
            > 
            <h1>
                {session.name}
            </h1>
            <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
              <Grid container spacing={2}>
                <Grid item xs={9}>
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
                <Grid item xs={3} sx={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}> 
                  <Button
                    type="submit"
                    size="medium"
                    variant="contained"
                    color="success"
                    disabled={!passphrase}
                  >
                    Submit
                  </Button>
                </Grid>
              </Grid>
           </Box>
          </Box>
        </Container>}
        {isAdmin && <Container id="session-container" component="main" maxWidth="xs"  align='center'>
          <h1 align='center'>
            {session.name}
          </h1>
          <Box
              sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
              }}
              >

            <Box component="form" noValidate onSubmit={addParticipant}>
              <Grid container spacing={2}>
                <Grid item xs={5}>
                  <TextField
                    required
                    fullWidth
                    id="participantName"
                    name="participantName"
                    label="Participant Name"
                    autoComplete="paritcipant-name"
                    value={participantName} onChange={handleParticipantNameChange}
                  />
                </Grid>
                <Grid item xs={5}>
                  <TextField
                    required
                    fullWidth
                    id="participantEmail"
                    name="participantEmail"
                    label="Participant Email"
                    autoComplete="paritcipant-email"
                    value={participantEmail} onChange={handleParticipantEmailChange}
                  />
                </Grid>
                <Grid item xs={2} sx={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}> 
                  <Button
                    type="submit"
                    size="medium"
                    variant="contained"
                    color="success"
                    disabled={!participantName || !participantEmail}
                  >
                    Add
                  </Button>
                </Grid>
              </Grid>
            </Box>
          </Box>
          <Box
            sx={{
                marginTop: 5,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}> 

            <h3>
                Participants
            </h3>
            <div style={{ width: '100%'}}>
                <ListItem>
                  <ListItemText primary='Name' />
                  <ListItemText primary='Email'/>
                </ListItem>
                {/* TODO: Display participant's name and email */}
            </div>
            <Box onClick={sendEmail} sx={{position: "fixed", bottom: 30}}>
              <Button
                size="medium"
                variant="contained"
                color="success"
              >
                Send Email
              </Button>
          </Box>
          </Box>

        </Container>}
      </ThemeProvider>
    );
}

export default Session