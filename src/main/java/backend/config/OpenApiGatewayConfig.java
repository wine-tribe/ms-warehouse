package backend.config;

import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Configuration
public class OpenApiGatewayConfig {

    @Bean
    public OpenApiCustomizer gatewayServerCustomizer() {
        return openApi -> {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;

            var req = attrs.getRequest();
            String prefix = req.getHeader("X-Forwarded-Prefix");
            if (prefix == null || prefix.isBlank()) {
                return; // напрямую на сервис
            }

            // 1) если прилетело списком - берём первое
            prefix = prefix.split(",")[0].trim();

            // 2) нормализуем слэши
            prefix = prefix.replaceAll("/+$", "");     // убрать хвостовые /
            if (!prefix.startsWith("/")) prefix = "/" + prefix;

            // 3) ДЕДУП: если вдруг стало "/warehouse/warehouse" — свернём до "/warehouse"
            prefix = dedupePrefix(prefix);

            openApi.setServers(List.of(new Server().url(prefix)));
        };
    }

    private static String dedupePrefix(String prefix) {
        // работает для любого префикса вида "/x/x"
        String[] parts = prefix.split("/");
        // parts[0] пустой из-за ведущего /
        if (parts.length >= 3 && parts[1].equals(parts[2])) {
            return "/" + parts[1];
        }
        return prefix;
    }
}
