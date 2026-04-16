package com.disaster.relief_system;
import com.disaster.relief_system.decorator.Notification;
import com.disaster.relief_system.decorator.BasicNotification;
import com.disaster.relief_system.decorator.EmailNotification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.disaster.relief_system.decorator.Notification;
import com.disaster.relief_system.decorator.BasicNotification;
import com.disaster.relief_system.decorator.EmailNotification;
import com.disaster.relief_system.decorator.Notification;
import com.disaster.relief_system.decorator.BasicNotification;
import com.disaster.relief_system.decorator.EmailNotification;
@SpringBootApplication
public class ReliefSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReliefSystemApplication.class, args);
		Notification n = new EmailNotification(new BasicNotification());
		n.send();
	}

}
