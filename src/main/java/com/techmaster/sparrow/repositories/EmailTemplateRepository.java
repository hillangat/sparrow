package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.email.EmailTemplate;
import org.springframework.data.repository.CrudRepository;

public interface EmailTemplateRepository extends CrudRepository<EmailTemplate, Long> {
}
