import { Injectable } from "@angular/core";
import { AuthHttp } from "angular2-jwt";
import { HttpClient } from '@angular/common/http';
import { Response } from '@angular/http';
import { Configuration } from "./configuration";
import { Project } from "../entity/project";
import { Observable } from "rxjs/Observable";

@Injectable()
export class ProjectService {

    private actionUrl: string;

    constructor(private http: HttpClient, private authHttp: AuthHttp, private configuration: Configuration) {
        this.actionUrl = configuration.ServerWithApiUrl + 'project/';
    }

    public getTimelineProjectCaptions(): Observable<Project[]> {
        return this.http.get<Project[]>(this.actionUrl + 'timeline');
    }

    public getProjectData(id: number): Observable<Project> {
        return this.http.get<Project>(this.actionUrl + 'project?id=' + id);
    }

    public postProject(project: Project) {
        return this.authHttp.post(this.actionUrl + 'persistProject', project);
    }

}