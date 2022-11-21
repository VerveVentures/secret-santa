
import { Container, Box, Typography } from '@mui/material';
import { Link } from "react-router-dom";
import '../style.css';

function Landing() {
    return (
        <Container id="signup-container" component="main" maxWidth="xs">
            <Box
                sx={{
                    marginTop: 10,
                    display: 'flex',
                    flexDirection: 'column',
                    border: 1,
                    borderRadius: '10px'
                }}
                class="border-gradient"
            >
                <Typography variant="h4" align="center" sx={{ mt: 2 }}>
                    FAQ
                </Typography>
                <Typography variant="subtitle1" align="left" sx={{ mt: 2, ml: 2, mr: 2 }}>
                    <b>How much should I spend in the present?</b>
                </Typography>
                <Typography variant="body1" align="left" sx={{ mb: 2, ml: 2, mr: 2 }}>
                    Of course depends on who you giving the present to. But think around CHF 20.
                </Typography>

                <Typography variant="subtitle1" align="left" sx={{ mt: 2, ml: 2, mr: 2 }}>
                    <b>What should I buy?</b>
                </Typography>
                <Typography variant="body1" align="left" sx={{ mb: 2, ml: 2, mr: 2 }}>
                    That's your problem. Don't over think it, he/she won't like anyway, just buy something but ideally with some meanings for the person.
                </Typography>

                <Typography variant="subtitle1" align="left" sx={{ mt: 2, ml: 2, mr: 2 }}>
                    <b>Do I need to wrap the present?</b>
                </Typography>
                <Typography variant="body1" align="left" sx={{ mb: 2, ml: 2, mr: 2 }}>
                    Not required, just add a name.
                </Typography>

                <Typography variant="subtitle1" align="left" sx={{ mt: 2, ml: 2, mr: 2 }}>
                    <b>Where I do leave the present?</b>
                </Typography>
                <Typography variant="body1" align="left" sx={{ mb: 2, ml: 2, mr: 2 }}>
                    On a to-be-announced desk on the day of the Christmas dinner in the Verve Office. Idea is to open it before the dinner and avoid the carrying up and down.
                </Typography>
            </Box>
        </Container>
    );
}

export default Landing