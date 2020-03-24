package com.abo.demo.lists;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsercontentRepo extends JpaRepository<Usercontent, Long> {
	List<Usercontent> findByUid(int Uid);
}
