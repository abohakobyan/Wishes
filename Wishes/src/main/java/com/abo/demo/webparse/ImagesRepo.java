package com.abo.demo.webparse;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepo extends JpaRepository<Contentimages, Integer> {
	List<Contentimages> findByCid(String cid);
	Optional<Contentimages> findById(Integer img_id);
	void deleteByCid(String cid);
}