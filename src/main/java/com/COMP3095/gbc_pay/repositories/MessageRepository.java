package com.COMP3095.gbc_pay.repositories;

import com.COMP3095.gbc_pay.models.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
