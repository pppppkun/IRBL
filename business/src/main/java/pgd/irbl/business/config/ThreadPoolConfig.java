package pgd.irbl.business.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cpu.core}")
    static Integer cpuCoreNum;

    @Bean
    public ExecutorService getThreadPool(){
        return Executors.newFixedThreadPool(cpuCoreNum);
    }

}
