import { APIService } from './api.service';

export class SessionService {

    apiService = new APIService('sessions');

    async createSession(name) {
        return this.apiService.post(null, {name, passphrase: ''});
    }

    async getSession(id) {
        return this.apiService.get(id);
    }

    async sendInvitations(id) {
        return this.apiService.post(id + '/launch', {});
    }

    async scramble(id) {
        return this.apiService.post(id + '/scramble', {});
    }
}