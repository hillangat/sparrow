package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.email.EmailReceiver;
import org.springframework.data.repository.CrudRepository;

public interface EmailReceiverRepo extends CrudRepository<EmailReceiver, Long> {
}
