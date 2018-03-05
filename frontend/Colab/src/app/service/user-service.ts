import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";
import { User } from "../entity/user";
import { Http, Response } from "@angular/http";
import { Configuration } from "./configuration";

//login a logout metoda podle tutoriálu https://medium.com/@juliapassynkova/angular-springboot-jwt-integration-p-1-800a337a4e0
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
        localStorage.setItem('access_token', accessToken);  //'access_token' by samozřejmě mělo správně být v konstantě
        localStorage.setItem('authenticatedUsername', authenticatedUsername);
        this.componentMethodCallSource.next();        //dej odebirajicim komponentam vedet, ze se stav prihlaseni zmenil
    }
    
    logout() {
        this.accessToken = null;
        this.authenticatedUsername = null;
        localStorage.removeItem('access_token');
        localStorage.removeItem('authenticatedUsername');
        this.componentMethodCallSource.next();        //dej odebirajicim komponentam vedet, ze se stav prihlaseni zmenil
    }

    //really bad way to do this! (doing it this way for code simplicity)
    public register(user: User) {
        return this.http.post(this.configuration.ServerWithApiUrl + '/user/persistUser', user);
    }

}