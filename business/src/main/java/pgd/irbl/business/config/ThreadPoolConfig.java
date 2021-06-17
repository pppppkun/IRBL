package pgd.irbl.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qin
 * @description 线程池配置
 * @date 2021-06-09
 */
@Configuration
public class ThreadPoolConfig {

//    @Bean(name = "threadPool")
//    public ThreadPoolExecutorFactoryBean executorServiceFactory () {
//        ThreadPoolExecutorFactoryBean factory = new ThreadPoolExecutorFactoryBean ();
//        factory.setThreadNamePrefix ( "query" );
//        factory.setMaxPoolSize ( 3 );
//        factory.setCorePoolSize ( 2 );
//        factory.setQueueCapacity ( 10000 );
//        return factory;
//    }

//    @Bean
//    public ExecutorService executorService() {
//        ThreadPoolExecutorFactoryBean factory = new ThreadPoolExecutorFactoryBean ();
//        factory.setThreadNamePrefix ( "query" );
//        factory.setMaxPoolSize ( 3 );
//        factory.setCorePoolSize ( 2 );
//        factory.setQueueCapacity ( 10000 );
//        return factory.getObject();
//    }
    @Bean
    ExecutorService getExecutorService(){
        return Executors.newFixedThreadPool(2);
    }

}
