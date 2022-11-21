import { APIService } from './api.service';

export class ParticipantService {

    apiService = new APIService('participants');
   
    async exists(id) {
        return this.apiService.get(id).then(response => response.status === 200)
    }

    async createParticipants(id, participants) {
        participants.map(participant => this.apiService.post(participant))
    }

    async updateParticipant(participant) {

    }
}