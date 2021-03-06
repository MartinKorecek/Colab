import { Injectable } from "@angular/core";
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { UserService } from "../service/user-service";
import { tokenNotExpired } from "angular2-jwt";

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private router: Router, private userService: UserService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (tokenNotExpired('access_token', this.userService.accessToken)) {
            return true;
        } else {
            this.router.navigate(['login'], { queryParams: { redirectTo: state.url } });
            return false;
        }
    }
}
