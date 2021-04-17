package pgd.irbl.business.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-14 18:59
 */
@Configuration
public class CORSConfig {

    private static final String[] originsVal = new String[]{
            "localhost:8000",
            "127.0.0.1:8000",
            "localhost:8016",
            "127.0.0.1",
            "localhost",
            "172.19.144.143",
            "144.34.132.147",
            "irbl.chper.cn",
            "172.19.144.143:8000",
            "106.15.248.13:8000"
    };


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        addAllowedOrigins(corsConfiguration); // 1
        //允许token放置于请求头
        corsConfiguration.addExposedHeader("nju-token,nju-long-token");
        //corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        corsConfiguration.setAllowCredentials(true); // 跨域session共享
        source.registerCorsConfiguration("/**", corsConfiguration); // 4
        return new CorsFilter(source);
    }

    private void addAllowedOrigins(CorsConfiguration corsConfiguration) {
        for (String origin : originsVal) {
            corsConfiguration.addAllowedOrigin("http://" + origin);
            corsConfiguration.addAllowedOrigin("https://" + origin);
        }
    }

}
