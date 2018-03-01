import { ProjectComment } from "./project-comment";
import { User } from "./user";

export class Project {
    id: number;
    author: User;
    caption: string;
    description: string;
    creationDate: Date;
    comments: ProjectComment[];
    
    constructor (id: number, authorId?: number, authorName?: string, caption?: string, description?: string, comments?: ProjectComment[]) {
        this.id = id;
        this.author = new User(authorName);
        this.caption = caption;
        this.description = description;
        this.comments = comments;
    }

}