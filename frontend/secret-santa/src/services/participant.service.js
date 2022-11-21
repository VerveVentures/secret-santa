import { APIService } from './api.service';

export class ParticipantService {

    apiService = new APIService('participants');
   
    async exists(id) {
        return this.apiService.get(id).then(response => response.id === id);
    }

    async createParticipants(id, participants) {
        return Promise.all(participants.map(participant => this.apiService.post('', participant)));
    }

    async updateParticipant(participant) {
        return this.apiService.put('', participant);
    }
}