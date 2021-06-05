package com.notificationsystem.notificationproducer;

import com.notificationsystem.notificationproducer.model.RegisteredClient;
import com.notificationsystem.notificationproducer.repository.RegisteredClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class NotificationProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationProducerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RegisteredClientRepository clientRepository) {
		return args -> {
			RegisteredClient client1 = RegisteredClient.builder()
					.clientId(456)
					.topicName("client-1-events")
					.build();
			List<RegisteredClient> clients = Arrays.asList(client1);
			clients.forEach(client -> clientRepository.save(client));
		};
	}

}
