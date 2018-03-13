import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { User } from "../entity/user";
import { Http, Response } from "@angular/http";
import { Configuration } from "./configuration";

//login and logout method done following tutorial: https://medium.com/@juliapassynkova/angular-springboot-jwt-integration-p-1-800a337a4e0
@Injectable()
export class UserService {
    accessToken: string;
    authenticatedUsername: string;

    private componentMethodCallSource = new Subject<any>();
    componentMethodCalled$ = this.componentMethodCallSource.asObservable();

    constructor(private http: Http, private configuration: Configuration) {
    }

    login(accessToken: string, authenticatedUsername: string) {
        this.authenticatedUsername = authenticatedUsername;
        this.accessToken = accessToken;
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('authenticatedUsername', authenticatedUsername);
        this.componentMethodCallSource.next();        //let 'listening' components know the login state has changed
    }
    
    logout() {
        this.accessToken = null;
        this.authenticatedUsername = null;
        localStorage.removeItem('access_token');
        localStorage.removeItem('authenticatedUsername');
        this.componentMethodCallSource.next();        //let 'listening' components know the login state has changed
    }

    //unsafe if not using https protocol! (which, in 'production', should be used anyways)
    public register(user: User) {
        return this.http.post(this.configuration.ServerWithApiUrl + '/user/persistUser', user);
    }

}