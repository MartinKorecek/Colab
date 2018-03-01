package cz.martinkorecek.colab.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.martinkorecek.colab.entity.Project;
import cz.martinkorecek.colab.repository.ProjectRepository;

@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	/*
	 * Metoda navrzena tak, aby nebylo treba selectovat vsechny sloupce tabulky 'project'
	 * viz take ProjectRepository
	 */
	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public List<TimelineProjectDto> getTimelineProjectContent() {
		List<Object[]> projectList = projectRepository.getTimelineProjectCaptions();
		List<TimelineProjectDto> timelineProjectContent = new ArrayList<>();
		for (Object[] p : projectList) {
			timelineProjectContent.add(new TimelineProjectDto((Long)p[0], (String)p[1]));
		}
		return timelineProjectContent;
	}
	
	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public ProjectData getProjectData(@RequestParam("id") long projectId) {
		List<Object[]> dataList = projectRepository.getProjectData(projectId);
		ProjectData projectData = new ProjectData((Long)dataList.get(0)[0], (String)dataList.get(0)[1], (String)dataList.get(0)[2]);
		for (Object[] data : dataList) {
			String commentAuthorName = (String)data[3];
			String commentText = (String)data[4];
			
			if (commentAuthorName != null && commentText != null) {
				projectData.addComment(commentAuthorName, commentText);
			}
		}
		
		return projectData;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('STANDARD_USER') and #project.author.getUsername() == principal")
	@RequestMapping(value = "/persistProject", method = RequestMethod.POST)
	public ResponseEntity<String> insertProject(@RequestBody Project project) {
		projectRepository.insertProject(project.getAuthor().getUsername(), project.getCaption(), project.getDescription());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	//special DTOs:
	public class ProjectData {
		
		private long id;
		private String caption;
		private String description;
		private List<ProjectCommentData> comments;
		
		public ProjectData(long id, String caption, String description) {
			this.id = id;
			this.caption = caption;
			this.description = description;
			this.comments = new ArrayList<>();
		}
		
		public void addComment(String authorName, String text) {
			this.comments.add(new ProjectCommentData(authorName, text));
		}
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		
		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

		public List<ProjectCommentData> getComments() {
			return comments;
		}

		public void setComments(List<ProjectCommentData> comments) {
			this.comments = comments;
		}
		
	}
	
	public class ProjectCommentData {
		
		private ProjectCommentAuthor author;
		private String text;
		
		public ProjectCommentData(String authorName, String text) {
			setAuthor(new ProjectCommentAuthor(authorName));
			this.text = text;
		}

		public ProjectCommentAuthor getAuthor() {
			return author;
		}

		public void setAuthor(ProjectCommentAuthor author) {
			this.author = author;
		}

		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
	}
	
	public class ProjectCommentAuthor {
		
		private String username;
		
		public ProjectCommentAuthor(String username) {
			this.username = username;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
	}
	
	public class TimelineProjectDto {
		
		private long id;
		private String caption;
		
		public TimelineProjectDto(long id, String caption) {
			this.setId(id);
			this.setCaption(caption);
		}

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}

		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
		
	}

}
