package com.notificationsystem.notificationproducer.repository;

import com.notificationsystem.notificationproducer.model.RegisteredClient;
import org.springframework.data.repository.CrudRepository;

public interface RegisteredClientRepository extends CrudRepository<RegisteredClient, Long> {

    RegisteredClient findByClientId(Integer clientId);
}
