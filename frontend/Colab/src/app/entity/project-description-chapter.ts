import { Project } from "./project";

export class ProjectDescriptionChapter {
    id: number;
    title: string;
    text: string;
    order: number;
    project: Project;
    expanded: boolean = false;   //not a database column; useful in project-content.component

    constructor (title: string, text: string, projectId?: number, order?: number) {
        this.title = title;
        this.text = text;
        this.project = new Project(projectId);
        this.order = order;
    }

}