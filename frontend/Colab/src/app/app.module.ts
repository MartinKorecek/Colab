import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { AppComponent } from './app.component';
import { TopbarComponent } from './top_and_bottom/topbar/topbar.component';
import { BottombarComponent } from './top_and_bottom/bottombar/bottombar.component';
import { TimelineContentComponent } from './main_content/timeline-content/timeline-content.component';
import { LoginContentComponent } from './main_content/login-content/login-content.component';
import { LogoutContentComponent } from './main_content/logout-content/logout-content.component';
import { ProjectContentComponent } from './main_content/project-content/project-content.component';
import { ProjectButtonComponent } from './timeline_components/project-button/project-button.component';
import { ProjectCommentComponent } from './project_components/project-comment/project-comment.component';
import { ProjectService } from './service/project-service';
import { HttpClientModule } from '@angular/common/http';
import { Configuration } from './service/configuration';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';
import { NewProjectContentComponent } from './main_content/new-project-content/new-project-content.component';
import { Http } from '@angular/http';
import { AuthHttp, AuthConfig } from 'angular2-jwt';
import { AuthenticationService } from './service/authentication-service';
import { AuthGuard } from './guard/auth-guard.service';
import { UserService } from './service/user-service';
import { HttpModule } from '@angular/http';
import { ProjectCommentService } from './service/project_comment-service';
import { UnauthGuard } from './guard/unauth-guard.service';

//tento postup jsem převzal z tutoriálu https://github.com/ipassynk/angular-springboot-jwt/blob/master/src/app/app.module.ts
export function authHttpServiceFactory(http: Http) {
  return new AuthHttp(new AuthConfig({
    headerPrefix: 'Bearer',
    tokenName: 'access_token',   //spraávně bych měl 'access token dát do konstanty!...
    globalHeaders: [{'Content-Type': 'application/json'}],
    noJwtError: false,
    noTokenScheme: true,
    tokenGetter: (() => localStorage.getItem('access_token'))
  }), http);
}

@NgModule({
  declarations: [
    AppComponent,
    TopbarComponent,
    BottombarComponent,
    TimelineContentComponent,
    LoginContentComponent,
    LogoutContentComponent,
    ProjectContentComponent,
    ProjectButtonComponent,
    ProjectCommentComponent,
    NewProjectContentComponent,
  ],
  imports: [
    HttpClientModule,
    HttpModule,
    BrowserModule,
    FormsModule,
    RouterModule.forRoot([
      {
        path: 'timeline',
        component: TimelineContentComponent
      },
      {
        path: 'login',
        component: LoginContentComponent,
        canActivate: [UnauthGuard]
      },
      {
        path: 'logout',
        component: LogoutContentComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'project/:id',
        component: ProjectContentComponent
      },
      {
        path: 'newProject',
        component: NewProjectContentComponent,
        canActivate: [AuthGuard]
      },
      {
        path: '', redirectTo: 'timeline', pathMatch: 'full'
      }
    ])
  ],
  providers: [
    {provide: AuthHttp, useFactory: authHttpServiceFactory, deps: [Http]},
    HttpClientModule,
    ProjectService,
    ProjectCommentService,
    UserService,
    AuthenticationService,
    AuthGuard,
    UnauthGuard,
    Configuration,
    { provide: LocationStrategy, useClass: PathLocationStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
