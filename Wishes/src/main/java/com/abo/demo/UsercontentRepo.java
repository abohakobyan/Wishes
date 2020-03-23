package com.abo.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsercontentRepo extends JpaRepository<Usercontent, Long> {
	List<Usercontent> findByUid(int Uid);
}
