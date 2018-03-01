import { Injectable } from "@angular/core";
import { Subject } from "rxjs/Subject";

//podle tutoriálu https://medium.com/@juliapassynkova/angular-springboot-jwt-integration-p-1-800a337a4e0
@Injectable()
export class UserService {
    accessToken: string;
    authenticatedUsername: string;

    private componentMethodCallSource = new Subject<any>();
    componentMethodCalled$ = this.componentMethodCallSource.asObservable();

    constructor() {
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

}