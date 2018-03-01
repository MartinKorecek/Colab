import { Injectable } from "@angular/core";
import { AuthHttp } from "angular2-jwt";
import { Configuration } from "./configuration";
import { ProjectComment } from "../entity/project-comment";

@Injectable()
export class ProjectCommentService {

    private actionUrl: string;

    constructor(private http: AuthHttp, private configuration: Configuration) {
        this.actionUrl = configuration.ServerWithApiUrl + 'projectComment/';
    }

    public postComment(comment: ProjectComment) {
        return this.http.post(this.actionUrl + 'persistComment', comment);
    }

}