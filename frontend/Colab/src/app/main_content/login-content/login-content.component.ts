import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../../service/authentication-service';
import { UserService } from '../../service/user-service';
import { Observable } from 'rxjs/Observable';
import { User } from '../../entity/user';

@Component({
  selector: 'app-login-content',
  templateUrl: './login-content.component.html',
  styleUrls: ['./login-content.component.css']
})
export class LoginContentComponent implements OnInit {
  model: any = {};
  loading = false;
  loginError = false;
  nonMatchingPasswords = false;
  registrationError = false;
  registrationErrorMessage: string = "ERROR";
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

  /*
  * HUGE antipattern! password should be encoded (as in authentication-service.login, and the request should be posted AT LEAST using https protocol)
  * Only doing it this way for bigger simplicity (not a project based on security afer all)
  * DEFINITELY change this for production
  */
  signUp() {
    let username = this.model.signupUsername;
    let password = this.model.signupPassword;

    if (password != this.model.signupRepeatPassword) {
      this.nonMatchingPasswords = true;
      return;
    }

    //attempt to register:
    this.userService.register(new User(username, password))
      .subscribe(
        a => this.authenticateAfterRegistration(username, password),
        err => {
          this.registrationError = true;
          this.registrationErrorMessage = 'Registration unsuccessful. Entered username might already exist';
          //TODO logging (optional)
          console.log(err);
          return;
        }
      );
  }

  authenticateAfterRegistration(username: string, password: string) {
    this.authenticationService.login(username, password)
        .subscribe(
        result => {
          this.loading = false;
           if (result) {
            this.userService.login(result, username);
            this.registrationError = false;
            this.navigateAfterSuccess();
          } else {
            this.registrationError = true;
            this.registrationErrorMessage = 'Registration was successfull but we are unable to generate your access token. Please try logging in with your new credentials in the form above';
            this.loading = false;
          }
        },
        error => {
          this.registrationError = true;
          this.registrationErrorMessage = 'Registration was successfull but we are unable to generate your access token. Please try logging in with your new credentials in the form above';
          this.loading = false;
        }
      );
  }

  private navigateAfterSuccess() {
    if (this.redirectUrl) {
      this.router.navigateByUrl(this.redirectUrl);
    } else {
      this.router.navigate(['/']);
    }
  }

}
