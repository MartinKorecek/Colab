package cz.martinkorecek.colab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	
	public static final String GET_TIMELINE_PROJECT_CAPTIONS_QUERY = "SELECT p.id, p.caption FROM Project p ORDER BY p.creationDate DESC";
	public static final String GET_PROJECT_DATA_QUERY = "SELECT p.id, p.caption, p.description, commenter.username, comment.text FROM Project p "
													+ "left join p.projectComments comment "
													+ "left join comment.author commenter "
													+ "WHERE p.id=:id";
	public static final String INSERT_PROJECT_QUERY = "INSERT INTO project "
										+ "(author_id, caption, description) "
										+ "SELECT id, :caption, :description FROM user "
										+ "WHERE username=:authorUsername";
	
	@Query(GET_TIMELINE_PROJECT_CAPTIONS_QUERY)
	public List<Object[]> getTimelineProjectCaptions();
	
	@Query(GET_PROJECT_DATA_QUERY)
	public List<Object[]> getProjectData(@Param("id") Long id);
	
	@Modifying
	@Query(value = INSERT_PROJECT_QUERY, nativeQuery = true)
	public void insertProject(@Param("authorUsername") String authorUsername, @Param("caption") String caption, @Param("description") String description);
	
	public Long countById(Long id);
	
}
