package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.email.EmailAttachment;
import org.springframework.data.repository.CrudRepository;

public interface EmailAttachmentRepo extends CrudRepository<EmailAttachment, Long> {
}
