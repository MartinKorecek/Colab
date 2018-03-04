package cz.martinkorecek.colab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.martinkorecek.colab.entity.ProjectResource;

@Repository
public interface ProjectResourceRepository extends JpaRepository<ProjectResource, Long> {

}
