import { User } from "./user";
import { Project } from "./project";

export class ProjectComment {
    id: number;
    text: string;
    author: User;
    date: string;
    project: Project;
    parentComment: ProjectComment;
    subcomments: ProjectComment[];
    subcommentsAllowed: boolean = false;

    constructor (authorName: string, text: string, projectId?: number, parentId?: number, subcommentsAllowed?: boolean) {
        this.author = new User(authorName);
        this.text = text;
        this.project = new Project(projectId);
        if (parentId != null) {
            this.parentComment = new ProjectComment(null, null);
            this.parentComment.id = parentId;
        }
        this.subcommentsAllowed = subcommentsAllowed;
    }

}