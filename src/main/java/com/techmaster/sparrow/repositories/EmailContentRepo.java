package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.email.EmailContent;
import org.springframework.data.repository.CrudRepository;

public interface EmailContentRepo extends CrudRepository<EmailContent, Long> {
}
