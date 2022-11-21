import { APIService } from './api.service';

export class ParticipantService {

    apiService = new APIService('participants');
   
    async get(id) {
        return this.apiService.get(id);
    }

    async createParticipants(id, participants) {
        return Promise.all(participants.map(participant => this.apiService.post('', participant)));
    }

    async updateParticipant(id, participant) {
        return this.apiService.put(id, participant);
    }
}