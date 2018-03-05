export class User {
    username: string;
    passwordHash: string;    //only for new user registration; of course not a 'hash' - called hash for successful mapping on backend

    constructor (username: string, password?: string) {
        this.username = username;
        this.passwordHash = password;
    }
}