import { Injectable } from "@angular/core";
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { tokenNotExpired } from "angular2-jwt";
import { UserService } from "../service/user-service";

@Injectable()
export class UnauthGuard implements CanActivate {
    constructor(private router: Router, private userService: UserService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (!tokenNotExpired('access_token', this.userService.accessToken)) {
            return true;
        } else {
            this.router.navigate(['timeline']);
            return false;
        }
    }
}