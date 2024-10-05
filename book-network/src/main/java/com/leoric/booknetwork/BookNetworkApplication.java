package com.leoric.booknetwork;

import com.leoric.booknetwork.repositories.RoleRepository;
import com.leoric.booknetwork.role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApplication.class, args);
    }

    @Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		// intellij suggested to replace args with underscore, need to check out if it's ok. App works tho
		return _ -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}

}
