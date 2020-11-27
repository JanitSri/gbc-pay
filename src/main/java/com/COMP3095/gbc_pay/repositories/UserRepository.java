package com.COMP3095.gbc_pay.repositories;

import com.COMP3095.gbc_pay.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
