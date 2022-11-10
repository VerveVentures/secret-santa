import { alertsService } from './alerts.service';
const alert = new alertsService();

//const axios = require('axios');

export class coreService {

    async test(data) {
        try {
            console.log('test service')
        } catch (err) {
            alert.error(err);
        }
    }
}