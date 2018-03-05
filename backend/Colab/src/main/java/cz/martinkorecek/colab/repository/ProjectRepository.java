package cz.martinkorecek.colab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	public static final String GET_TIMELINE_PROJECT_CAPTIONS_QUERY = "SELECT p.id, p.caption FROM Project p ORDER BY p.creationDate DESC";
	public static final String GET_PROJECT_DATA_QUERY = "SELECT p.id, p.caption, p.description, p.author.username, commenter.username, comment.text, comment.id, comment.parentComment.id FROM Project p "
													+ "left join p.projectComments comment "
													+ "left join comment.author commenter "
													+ "WHERE p.id=:id";
	public static final String GET_PROJECT_DESCRIPTION_CHAPTERS = "SELECT pdc.title, pdc.text FROM ProjectDescriptionChapter pdc "
														+ "WHERE pdc.project.id=:projectId "
														+ "ORDER BY pdc.id";
	public static final String GET_PROJECT_RESOURCES = "SELECT pr.link FROM ProjectResource pr "
												+ "WHERE pr.project.id=:projectId";
	
	@Query(GET_TIMELINE_PROJECT_CAPTIONS_QUERY)
	public List<Object[]> getTimelineProjectCaptions();
	
	@Query(GET_PROJECT_DATA_QUERY)
	public List<Object[]> getProjectData(@Param("id") Long id);
	
	@Query(GET_PROJECT_DESCRIPTION_CHAPTERS)
	public List<Object[]> getProjectDescriptionChapters(@Param("projectId") Long projectId);
	
	@Query(GET_PROJECT_RESOURCES)
	public List<String> getProjectResources(@Param("projectId") Long projectId);
	
	public Long countById(Long id);
	
}
