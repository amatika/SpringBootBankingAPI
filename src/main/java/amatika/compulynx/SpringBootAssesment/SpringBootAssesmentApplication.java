package amatika.compulynx.SpringBootAssesment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import amatika.compulynx.SpringBootAssesment.security.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SpringBootAssesmentApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(SpringBootAssesmentApplication.class, args);
	}

}
