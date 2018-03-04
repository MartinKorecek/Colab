package cz.martinkorecek.colab.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cz.martinkorecek.colab.entity.ProjectDescriptionChapter;
import cz.martinkorecek.colab.entity.ProjectResource;
import cz.martinkorecek.colab.entity.User;
import cz.martinkorecek.colab.repository.ProjectDescriptionChapterRepository;
import cz.martinkorecek.colab.repository.ProjectRepository;
import cz.martinkorecek.colab.repository.ProjectResourceRepository;
import cz.martinkorecek.colab.repository.UserRepository;

@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectDescriptionChapterRepository chapterRepository;
	
	@Autowired
	private ProjectResourceRepository projectResourceRepository;
	
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
		Long pId = (Long)dataList.get(0)[0];
		ProjectData projectData = new ProjectData(pId, (String)dataList.get(0)[1], (String)dataList.get(0)[2]);
		
		addComments(projectData, dataList, pId);
		addProjectResourcesAndDescriptionChapters(projectData, projectId);
		
		return projectData;
	}
	
	/*
	 * metoda zajišťuje hlavně propojení 'rodičovských' komentářů s jejich potenciálními 'subkomentáři'
	 */
	private void addComments(ProjectData projectData, List<Object[]> dataList, long projectId) {
		Map<Long, ProjectCommentData> parentCommentMap = new HashMap<>();        //mapa 'první vrstvy' komentářů                  -klíč: id komentáře
		Map<Long, List<ProjectCommentData>> subcommentMap = new HashMap<>();     //mapa všech subkomentů rodičovských komentářů   -klíč: id parentCommentu
		
		//zmapuj, co jsou rodicovske komenty a co subkomentare:
		for (Object[] data : dataList) {
			String commentAuthorName = (String)data[3];
			String commentText = (String)data[4];
			Long commentId = (Long)data[5];
			Long parentId = (Long)data[6];
			if (commentAuthorName == null || commentText == null || commentId == null)
				continue;
			
			if (parentId == null) {
				parentCommentMap.put(commentId, new ProjectCommentData(commentAuthorName, commentText, commentId, projectId, true, null));
			} else {
				List<ProjectCommentData> subList = subcommentMap.get(parentId);
				if (subList == null) {
					List<ProjectCommentData> sl = new ArrayList<>();
					sl.add(new ProjectCommentData(commentAuthorName, commentText, commentId, projectId, false, parentId));
					subcommentMap.put(parentId, sl);
				} else {
					subList.add(new ProjectCommentData(commentAuthorName, commentText, commentId, projectId, false, parentId));
				}
			}
		}
		
		//pripoj subkomenty k rodicovskym komentarum:
		//DA SE DELAT LAMBDOU, ALE MUSEL BYCH ZMENIT TARGET VERZI JAVY.. NENI TO POTREBA
		for (Map.Entry<Long, List<ProjectCommentData>> entry : subcommentMap.entrySet()) {
			ProjectCommentData parentData = parentCommentMap.get(entry.getKey());
			if (parentData == null) continue;
			
			parentData.setSubcomments(entry.getValue());
		}
		
		//premen mapu na list a vloz jej do projektu:
		List<ProjectCommentData> finalList = new ArrayList<>();
		for (Map.Entry<Long, ProjectCommentData> entry : parentCommentMap.entrySet()) {
			finalList.add(entry.getValue());
		}
		
		projectData.setComments(finalList);
	}
	
	private void addProjectResourcesAndDescriptionChapters(ProjectData projectData, long projectId) {
		List<Object[]> chapterDataList = projectRepository.getProjectDescriptionChapters(projectId);
		List<String> resourceDataList = projectRepository.getProjectResources(projectId);
		for (Object[] chapterData : chapterDataList)  {
			String title = (String)chapterData[0];
			String text = (String)chapterData[1];
			
			if (title != null && text != null) {
				projectData.addChapter(title, text);
			}
		}
		for (String resourceData : resourceDataList) {
			if (resourceData != null) {
				projectData.addResource(resourceData);
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	@PreAuthorize("hasAuthority('STANDARD_USER') and #project.author.getUsername() == principal")
	@RequestMapping(value = "/persistProject", method = RequestMethod.POST)
	public ResponseEntity<String> insertProject(@RequestBody Project project) {
		User author = new User();
		author.setId(userRepository.findByUsername(project.getAuthor().getUsername()).getId());
		project.setAuthor(author);
		project.setCreationDate(new Date());
		Set<ProjectDescriptionChapter> chapters = project.getProjectDescriptionChapters();
		project.setProjectDescriptionChapters(null);
		Set<ProjectResource> resources = project.getProjectResources();
		project.setProjectResources(null);
		long projectId = projectRepository.save(project).getId();
		persistChaptersAndResources(chapters, resources, projectId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	private void persistChaptersAndResources(Set<ProjectDescriptionChapter> chapters, Set<ProjectResource> resources, long projectId) {
		Project projectForId = new Project();
		projectForId.setId(projectId);
		
		persistChapters(chapters, projectForId);
		persistResources(resources, projectForId);
	}
	
	private void persistChapters(Set<ProjectDescriptionChapter> chapters, Project projectForId) {
		if (chapters == null || chapters.isEmpty()) return;
		
		for (ProjectDescriptionChapter chapter : chapters) {
			chapter.setProject(projectForId);
		}
		chapterRepository.save(chapters);
	}
	
	private void persistResources(Set<ProjectResource> resources, Project projectForId) {
		if (resources == null || resources.isEmpty()) return;
		
		for (ProjectResource resource : resources) {
			resource.setProject(projectForId);
		}
		projectResourceRepository.save(resources);
	}
	
	
	//special DTOs:
	public class ProjectData {
		
		private long id;
		private String caption;
		private String description;
		private List<ProjectCommentData> comments;
		private List<ProjectDescriptionChapterData> projectDescriptionChapters;
		private List<ProjectResourceData> projectResources;
		
		public ProjectData(long id, String caption, String description) {
			this.id = id;
			this.caption = caption;
			this.description = description;
			this.projectDescriptionChapters = new ArrayList<>();
			this.projectResources = new ArrayList<>();
		}
		
		public void addChapter(String title, String text) {
			this.projectDescriptionChapters.add(new ProjectDescriptionChapterData(title, text));
		}
		
		public void addResource(String link) {
			this.projectResources.add(new ProjectResourceData(link));
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

		public List<ProjectDescriptionChapterData> getProjectDescriptionChapters() {
			return projectDescriptionChapters;
		}

		public void setProjectDescriptionChapters(List<ProjectDescriptionChapterData> projectDescriptionChapters) {
			this.projectDescriptionChapters = projectDescriptionChapters;
		}

		public List<ProjectResourceData> getProjectResources() {
			return projectResources;
		}

		public void setProjectResources(List<ProjectResourceData> projectResources) {
			this.projectResources = projectResources;
		}

	}
	
	public class ProjectCommentData {
		
		private long id;
		private ProjectData project;
		private ProjectCommentAuthor author;
		private String text;
		private ProjectCommentData parentComment;
		private List<ProjectCommentData> subcomments;
		private boolean subcommentsAllowed;
		
		public ProjectCommentData(String authorName, String text, long id, long projectId, boolean subcommentsAllowed, Long parentId) {
			setAuthor(new ProjectCommentAuthor(authorName));
			setProject(new ProjectData(projectId, null, null));
			this.text = text;
			this.id = id;
			this.subcommentsAllowed = subcommentsAllowed;
			if (parentId != null) {
				this.parentComment = new ProjectCommentData(null, null, parentId, 0, false, null);
			}
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public ProjectData getProject() {
			return project;
		}

		public void setProject(ProjectData project) {
			this.project = project;
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

		public ProjectCommentData getParentComment() {
			return parentComment;
		}

		public void setParentComment(ProjectCommentData parentComment) {
			this.parentComment = parentComment;
		}

		public List<ProjectCommentData> getSubcomments() {
			return subcomments;
		}

		public void setSubcomments(List<ProjectCommentData> subcomments) {
			this.subcomments = subcomments;
		}

		public boolean isSubcommentsAllowed() {
			return subcommentsAllowed;
		}

		public void setSubcommentsAllowed(boolean subcommentsAllowed) {
			this.subcommentsAllowed = subcommentsAllowed;
		}
		
	}
	
	public class ProjectDescriptionChapterData {
		
		private String title;
		private String text;
		
		public ProjectDescriptionChapterData(String title, String text) {
			this.title = title;
			this.text = text;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
	}
	
	public class ProjectResourceData {
		
		private String link;
		
		public ProjectResourceData(String link) {
			this.link = link;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
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
