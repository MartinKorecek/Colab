import { ProjectComment } from "./project-comment";
import { User } from "./user";
import { ProjectDescriptionChapter } from "./project-description-chapter";
import { ProjectResource } from "./project-resource";

export class Project {
    id: number;
    author: User;
    caption: string;
    description: string;
    creationDate: string;
    comments: ProjectComment[];
    projectDescriptionChapters: ProjectDescriptionChapter[];
    projectResources: ProjectResource[];

    totalCount: number;    //doesn't have a column; only used in timeline.content
                           //represents the total amount of projects in the db

    constructor(id: number, authorId?: number, authorName?: string, caption?: string, description?: string, comments?: ProjectComment[]
    , chapters?: ProjectDescriptionChapter[], resources?: ProjectResource[]) {
        this.id = id;
        this.author = new User(authorName);
        this.caption = caption;
        this.description = description;
        this.comments = comments;
        this.projectDescriptionChapters = chapters;
        this.projectResources = resources;
    }

}