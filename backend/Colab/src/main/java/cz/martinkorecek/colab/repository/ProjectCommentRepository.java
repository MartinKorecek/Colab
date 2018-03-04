package cz.martinkorecek.colab.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.ProjectComment;

@Repository
public interface ProjectCommentRepository extends CrudRepository<ProjectComment, Long> {
	
	public static final String INSERT_COMMENT_QUERY = "INSERT INTO project_comment "
										+ "(author_id, text, project_id) "
										+ "SELECT id, :text, :projectId FROM user "
										+ "WHERE username=:authorUsername";
	public static final String INSERT_SUBCOMMENT_QUERY = "INSERT INTO project_comment "
			+ "(author_id, text, project_id, parent_comment_id) "
			+ "SELECT id, :text, :projectId, :parentId FROM user "
			+ "WHERE username=:authorUsername";
	
	@Modifying
	@Query(value = INSERT_COMMENT_QUERY, nativeQuery = true)
	public void insertComment(@Param("authorUsername") String authorUsername, @Param("text") String text, @Param("projectId") Long projectId);
	
	@Modifying
	@Query(value = INSERT_SUBCOMMENT_QUERY, nativeQuery = true)
	public void insertSubcomment(@Param("authorUsername") String authorUsername, @Param("text") String text, @Param("projectId") Long projectId, @Param("parentId") Long parentCommentId);
	
}
