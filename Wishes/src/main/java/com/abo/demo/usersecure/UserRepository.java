package com.abo.demo.usersecure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<users, Long> {
	users findByUsername(String username);
	
}
