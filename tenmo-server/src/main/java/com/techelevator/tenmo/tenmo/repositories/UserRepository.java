package com.techelevator.tenmo.tenmo.repositories;

import com.techelevator.tenmo.tenmo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

}
