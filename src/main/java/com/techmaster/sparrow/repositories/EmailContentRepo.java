package com.techmaster.sparrow.repositories;

import com.techmaster.sparrow.entities.email.EmailContent;
import com.techmaster.sparrow.enums.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EmailContentRepo extends CrudRepository<EmailContent, Long> {

   @Query("UPDATE EmailContent e SET e.lifeStatus = ?0 WHERE e.contentId = ?1")
   void updateLifeStatus(long emailContentId, Status status);

    @Query("UPDATE EmailContent e SET e.deliveryStatus = ?0 WHERE e.contentId = ?1")
   void updateDeliveryStatus(long emailContentId, Status status);

}
