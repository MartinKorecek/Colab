import { User } from "./user";
import { Project } from "./project";

export class ProjectComment {
    id: number;
    text: string;
    author: User;
    project: Project;

    constructor (authorName: string, text: string, projectId?: number) {
        this.author = new User(authorName);
        this.text = text;
        this.project = new Project(projectId);
    }

}