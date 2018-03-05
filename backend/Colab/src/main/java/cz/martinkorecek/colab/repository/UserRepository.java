package cz.martinkorecek.colab.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	public static final String INSERT_USER_QUERY = "INSERT INTO user "
										+ "(username, password_hash) "
										+ "VALUES (:username, :passwordHash)";

	public User findByUsername(String username);   //spring boot je opravdu šikovná věc
	
	public Long countByUsername(String username);  //opravdu hodně šikovná :)
	
	@Modifying
	@Query(value = INSERT_USER_QUERY, nativeQuery = true)
	public void insertUser(@Param("username") String username, @Param("passwordHash") String passwordHash);
	
}
