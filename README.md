Secret santa project for hacktoberfest

Backend API:

### Sessions
- GET: /api/sessions/<session_id> - get session by id
- POST: /api/sessions - create session  
body: 
```
  {
    name: String,
    passphrase: String
  }
```
- PUT: /api/sessions/<session_id> - update session  
body: - all fields are optional
```
  {
    name: String,
    sessionScrambled: Boolean,
    emailsSent: Boolean
  }
```
- POST: /api/sessions/<session_id>/scramble - scramble a session (generates the matches between participants) 


### Participants
- GET: /api/participants/session/<session_id> - get all participants in a session
- GET: /api/participants/<participant_id> - get participant
- POST: /api/participants - create participant  
body: - participates and comment fields are optional
```
  {
    sessionId: String,
    firstName: String,
    lastName: String,
    email: String,
    participates: Boolean,
    comment: String
  }
```
- PUT: /api/participants/<participant_id> - update participant  
body: - all fields are optional
```
  {
    firstName: String,
    lastName: String,
    email: String,
    participates: Boolean,
    comment: String
  }
```

### Matches
- GET: /api/matches/session/<session_id> - get all matches in a session
- GET: /api/matches/<match_id> - get match  

