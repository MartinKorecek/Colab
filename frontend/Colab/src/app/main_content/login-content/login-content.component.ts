import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../../service/authentication-service';
import { UserService } from '../../service/user-service';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-login-content',
  templateUrl: './login-content.component.html',
  styleUrls: ['./login-content.component.css']
})
export class LoginContentComponent implements OnInit {
  model: any = {};
  loading = false;
  loginError = false;
  registrationError = false;
  redirectUrl: string;

  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private userService: UserService) {
    this.redirectUrl = this.activatedRoute.snapshot.queryParams['redirectTo'];
  }

  ngOnInit() {
    this.userService.logout();
  }

  login() {
    this.loading = true;

    this.authenticationService.login(this.model.loginUsername, this.model.loginPassword)
      .subscribe(
        result => {
          this.loading = false;

          if (result) {
            this.userService.login(result, this.model.loginUsername);
            this.loginError = false;
            this.navigateAfterSuccess();
          } else {
            this.loginError = true;
          }
        },
        error => {
          this.loginError = true;
          this.loading = false;
        }
      );
  }

  signUp() {
      this.authenticationService.register(this.model.loginUsername, this.model.loginPassword)   //TODO -samozřejmě je třeba to dodělat
  }

  private navigateAfterSuccess() {
    if (this.redirectUrl) {
      this.router.navigateByUrl(this.redirectUrl);
    } else {
      this.router.navigate(['/']);
    }
  }

}
