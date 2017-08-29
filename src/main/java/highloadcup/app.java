package highloadcup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Created by Alikin E.A. on 24.08.17.
 */
@ComponentScan("highloadcup")
@EnableAutoConfiguration
@Configuration
public class app {

    public static void main(String[] args){
        SpringApplication.run(app.class, args);

        Runtime runtime = Runtime.getRuntime();
        final NumberFormat format = NumberFormat.getInstance();
        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = "MB";
        System.out.println("========================== Memory Info ==========================");
        System.out.println("Free memory: " + format.format(freeMemory / mb) + mega);
        System.out.println("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
        System.out.println("Max memory: " + format.format(maxMemory / mb) + mega);
        System.out.println("Total free memory: " + format.format(freeMemory+(maxMemory - allocatedMemory)/mb)  + mega);
        System.out.println("=================================================================\n");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new ShallowEtagHeaderFilter());
        filterBean.setUrlPatterns(Arrays.asList("*"));
        return filterBean;
    }

}
