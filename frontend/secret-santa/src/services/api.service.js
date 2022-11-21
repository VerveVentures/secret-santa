export class APIService {
    
    api = "https://verve-secret-santa.herokuapp.com/api/"

    constructor(root) {
        this.root = root
    }

    buildUrl(path) {
        return this.api + this.root + '/' + path;
    }

    handleResponse(response) {
        if (response.status >= 300) {
            return { json: null, status: response.status};
        }
        return response.json();
    }

    async get(path) {
        let options = {
            method: 'GET'
        }
        return fetch(this.buildUrl(path), options).then(this.handleResponse)
    }

    async post(path, data) {
        let options = {
            body: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json'},
            method: 'POST'
        }
        return fetch(this.buildUrl(path), options).then(this.handleResponse)
    }

    async put(path, data) {
        let options = {
            body: JSON.stringify(data),
            headers: { 'Content-Type': 'application/json'},
            method: 'PUT'
        }
        return fetch(this.buildUrl(path), options).then(this.handleResponse)
    }
}