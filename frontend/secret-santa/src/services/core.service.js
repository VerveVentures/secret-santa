import { alertsService } from './alerts.service';
const alert = new alertsService();

//const axios = require('axios');

export class coreService {


    /*
    we need:

    1. check which stage of the session we are
    2. send session name and participants to the BE to (i) send emails and (ii) set stage of the session to CREATED
    3. check individual sign up validation, i.e. if they belong to a CREATED session
    4. store individual response whether to partipate

    */

    async test(data) {
        try {
            console.log('test service')
        } catch (err) {
            alert.error(err);
        }
    }
}