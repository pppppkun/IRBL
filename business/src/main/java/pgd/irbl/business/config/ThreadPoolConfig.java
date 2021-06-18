package pgd.irbl.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

/**
 * @author qin
 * @description 线程池配置,使用线程池进行流量控制
 * @date 2021-06-18
 */
@Configuration
public class ThreadPoolConfig {

    private static int nThreads = 2;
    private static int limitThreads = 1000000;

    @Bean
    ExecutorService getExecutorService(){
        return new ThreadPoolExecutor(nThreads, nThreads,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(limitThreads));
    }

}
