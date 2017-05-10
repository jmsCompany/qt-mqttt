package qt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import qt.mqtt.MqttConfiguration;


@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@Import({ MqttConfiguration.class })
public class Application {
	
	AccessControlAllowFilter acaFilter = new AccessControlAllowFilter();
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	public FilterRegistrationBean acaFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(acaFilter);
		return filterRegBean;
	}

}
