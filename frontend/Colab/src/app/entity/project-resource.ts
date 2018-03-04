import { Project } from "./project";

export class ProjectResource {
    id: number;
    link: string;
    project: Project;

    constructor (link: string, projectId?: number) {
        this.link = link;
        this.project = new Project(projectId);
    }
    
}