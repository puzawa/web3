package web3.point;

import io.r2dbc.spi.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PointDAO {

    private ConnectionFactory connectionFactory;

    @Inject
    private CfAppGetter cfAppGetter;

    @PostConstruct
    public void init(){
        this.connectionFactory = cfAppGetter.getConnectionFactory();
    }

    public Mono<Long> add(Point p) {
        String sql = """
            INSERT INTO points (x, y, r, duration, created_date, is_hit)
            VALUES ($1, $2, $3, $4, $5, $6)
            RETURNING id
        """;

        return Mono.usingWhen(
                connectionFactory.create(),
                conn -> Mono.from(conn.createStatement(sql)
                                .bind(0, p.getX())
                                .bind(1, p.getY())
                                .bind(2, p.getR())
                                .bind(3, p.getDuration())
                                .bind(4, p.getDate())
                                .bind(5, p.getCheck())
                                .execute())
                        .flatMap(result -> Mono.from(result.map((row, meta) -> row.get("id", Long.class)))),
                Connection::close
        );
    }

    public Flux<Point> getAll() {
        String sql = "SELECT id, x, y, r, duration, created_date, is_hit FROM points";
        return Flux.usingWhen(
                connectionFactory.create(),
                conn -> Flux.from(conn.createStatement(sql).execute())
                        .flatMap(result -> result.map(this::mapRowToPoint)),
                Connection::close
        );
    }
    public Mono<Long> deleteAll() {
        String sql = "DELETE FROM points";
        return Mono.usingWhen(
                connectionFactory.create(),
                conn -> Mono.from(conn.createStatement(sql).execute())
                        .flatMap(result -> Mono.from(result.getRowsUpdated())),
                Connection::close
        );
    }

    public boolean isDBAvailable() {
        try {
            return Mono.usingWhen(
                    connectionFactory.create(),
                    conn -> Mono.from(conn.validate(ValidationDepth.LOCAL)),
                    Connection::close
            ).block(Duration.ofSeconds(2));
        } catch (Exception e) {
            return false;
        }
    }

    private Point mapRowToPoint(Row row, RowMetadata meta) {
        return new Point(
                row.get("x", BigDecimal.class),
                row.get("y", BigDecimal.class),
                row.get("r", BigDecimal.class),
                row.get("duration", Long.class),
                row.get("created_date", LocalDateTime.class),
                row.get("is_hit", Boolean.class),
                row.get("id", Long.class)
        );
    }
}