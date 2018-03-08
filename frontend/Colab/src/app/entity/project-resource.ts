import { Project } from "./project";

export class ProjectResource {
    id: number;
    link: string;
    order: number;
    project: Project;

    constructor (link: string, projectId?: number, order?: number) {
        this.link = link;
        this.project = new Project(projectId);
        this.order = order;
    }
    
}