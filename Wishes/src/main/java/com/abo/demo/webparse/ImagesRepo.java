package com.abo.demo.webparse;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepo extends JpaRepository<Contentimages, Long> {
	List<Contentimages> findByUid(int Uid);
}