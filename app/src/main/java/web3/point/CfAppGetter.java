package web3.point;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.io.Serializable;
import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@ApplicationScoped
public class CfAppGetter implements Serializable {
    private static final long serialVersionUID = 1L;

    private ConnectionPool pooledFactory;

    @PostConstruct
    public void init() {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 44401)
                .option(USER, "s413039")
                .option(PASSWORD, "cUjGdh3up1srj9Po")
                .option(DATABASE, "studs")
                .build();

        ConnectionFactory baseFactory = ConnectionFactories.get(options);

        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(baseFactory)
                .initialSize(5)
                .maxSize(20)
                .maxIdleTime(Duration.ofMinutes(10))
                .validationQuery("SELECT 1")
                .build();

        this.pooledFactory = new ConnectionPool(configuration);
    }

    @Produces
    @ApplicationScoped
    public ConnectionFactory getConnectionFactory() {
        return pooledFactory;
    }

    @PreDestroy
    public void destroy() {
        if (pooledFactory != null) {
            pooledFactory.disposeLater().subscribe();
        }
    }
}