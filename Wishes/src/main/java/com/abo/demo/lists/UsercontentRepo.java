package com.abo.demo.lists;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsercontentRepo extends JpaRepository<Usercontent, String> {
	List<Usercontent> findByUid(int Uid);
}
