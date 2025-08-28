# Arquitectura Propuesta - StrafeHCF

## Visión Arquitectónica

**StrafeHCF** adoptará una **arquitectura hexagonal (ports and adapters)** con **principios DDD (Domain-Driven Design)** y **clean architecture**, priorizando la testabilidad, mantenibilidad y escalabilidad sobre la compatibilidad legacy.

## Principios Arquitectónicos

### 🎯 Principios Core

1. **Separation of Concerns**: Cada módulo tiene una responsabilidad específica
2. **Dependency Inversion**: Dependencias hacia abstracciones, no implementaciones
3. **Single Responsibility**: Una clase, una razón para cambiar
4. **Open/Closed**: Abierto para extensión, cerrado para modificación
5. **Interface Segregation**: Interfaces pequeñas y específicas

### 🏗️ Architectural Patterns

- **Hexagonal Architecture**: Core business logic independiente de frameworks
- **CQRS**: Command Query Responsibility Segregation para operaciones
- **Event Sourcing**: Para tracking de cambios críticos
- **Repository Pattern**: Abstracción de persistencia
- **Factory Pattern**: Creación de objetos complejos

## Estructura de Alto Nivel

```
StrafeHCF/
├── 📦 core/                    # Business Logic (Framework Independent)
│   ├── 🏛️ domain/              # Entities, Value Objects, Domain Services
│   ├── 🎯 usecases/            # Application Services, Commands, Queries
│   └── 🔌 ports/               # Interfaces for external dependencies
├── 📱 adapters/                # External Framework Integration
│   ├── 🎮 spigot/              # Spigot/Paper API integration
│   ├── 💾 persistence/         # Database, File storage
│   ├── 🌐 web/                 # REST API, WebSockets
│   └── 🔧 external/            # Third-party integrations
├── 🏃‍♂️ application/            # Application Bootstrap
│   ├── 📝 config/              # Configuration management
│   ├── 🚀 startup/             # Application initialization
│   └── 🔄 lifecycle/           # Plugin lifecycle management
└── 🧪 tests/                   # All types of tests
    ├── 🔬 unit/                # Unit tests
    ├── 🔗 integration/         # Integration tests
    └── 🎭 e2e/                 # End-to-end tests
```

## Arquitectura por Capas

### 🏛️ Capa de Dominio (Core)

**Responsabilidad:** Contiene la lógica de negocio pura del HCF

```java
// Domain Entities
public class Team {
    private final TeamId id;
    private final TeamName name;
    private final Set<PlayerId> members;
    private DTR dtr;
    private final Set<Claim> claims;
    
    public DTR calculateDTR() {
        // Pure business logic
    }
    
    public boolean canBeRaided() {
        return dtr.isRaidable();
    }
}

// Value Objects
public class DTR {
    private final double value;
    private final Instant lastDeath;
    private final boolean isRegenerating;
    
    public boolean isRaidable() {
        return value <= 0.0;
    }
    
    public DTR regenerate(RegenConfig config) {
        // DTR regeneration logic
    }
}

// Domain Services
public class CombatService {
    public CombatResult processCombat(Player attacker, Player defender) {
        // Combat calculations
    }
}
```

**Características:**
- ❌ No dependencias externas
- ✅ Lógica de negocio pura
- ✅ Fácil testing unitario
- ✅ Inmutable cuando sea posible

### 🎯 Capa de Casos de Uso (Application)

**Responsabilidad:** Orquesta las operaciones del dominio

```java
// Commands (Write operations)
public class CreateTeamCommand {
    private final PlayerId leaderId;
    private final TeamName teamName;
}

public class CreateTeamUseCase {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final EventPublisher eventPublisher;
    
    public Result<Team> execute(CreateTeamCommand command) {
        // 1. Validate business rules
        // 2. Create domain objects
        // 3. Persist changes
        // 4. Publish events
    }
}

// Queries (Read operations)
public class GetTeamStatsQuery {
    private final TeamId teamId;
}

public class GetTeamStatsQueryHandler {
    private final TeamStatsProjection teamStats;
    
    public TeamStatsView handle(GetTeamStatsQuery query) {
        return teamStats.getStats(query.getTeamId());
    }
}
```

**Características:**
- ✅ Transaccional
- ✅ Validación de negocio
- ✅ Event publishing
- ✅ Error handling

### 🔌 Capa de Puertos (Interfaces)

**Responsabilidad:** Define contratos para dependencias externas

```java
// Repository Interfaces
public interface TeamRepository {
    Optional<Team> findById(TeamId id);
    void save(Team team);
    List<Team> findAllActive();
    void delete(TeamId id);
}

public interface PlayerRepository {
    Optional<Player> findById(PlayerId id);
    void save(Player player);
    List<Player> findByTeam(TeamId teamId);
}

// External Service Interfaces
public interface NotificationService {
    void sendTeamNotification(TeamId teamId, String message);
    void broadcastGlobal(String message);
}

public interface PermissionService {
    boolean hasPermission(PlayerId playerId, Permission permission);
    void grantPermission(PlayerId playerId, Permission permission);
}
```

### 📱 Capa de Adaptadores (Infrastructure)

**Responsabilidad:** Implementa los puertos usando tecnologías específicas

```java
// Spigot Adapter
@Component
public class SpigotTeamRepository implements TeamRepository {
    
    @Override
    public Optional<Team> findById(TeamId id) {
        // Load from database/file
        TeamEntity entity = database.findTeam(id.getValue());
        return entity != null ? 
            Optional.of(TeamMapper.toDomain(entity)) : 
            Optional.empty();
    }
    
    @Override
    public void save(Team team) {
        // Convert domain to entity and persist
        TeamEntity entity = TeamMapper.toEntity(team);
        database.saveTeam(entity);
    }
}

// Event Adapter
@Component
public class SpigotEventPublisher implements EventPublisher {
    
    @Override
    public void publish(DomainEvent event) {
        // Convert to Bukkit event and fire
        BukkitEvent bukkitEvent = EventMapper.toBukkitEvent(event);
        Bukkit.getPluginManager().callEvent(bukkitEvent);
    }
}
```

## Gestión de Estado

### 📊 State Management Strategy

**1. Domain State (Authoritative)**
```java
// Single source of truth in domain
public class GameState {
    private final Map<TeamId, Team> teams;
    private final Map<PlayerId, Player> players;
    private final Set<ActiveKOTH> activeKOTHs;
    
    // State transitions through domain methods only
    public Result<Void> createTeam(CreateTeamCommand command) {
        // Business rules validation
        // State mutation
    }
}
```

**2. Read Models (Projections)**
```java
// Optimized for queries
public class TeamStatsProjection {
    private final Cache<TeamId, TeamStats> statsCache;
    
    @EventHandler
    public void on(TeamMemberKilledEvent event) {
        // Update read model based on events
        updateTeamStats(event.getTeamId(), event.getKills());
    }
}
```

**3. Cache Strategy**
```java
@Component
public class CacheManager {
    private final RedisTemplate redisTemplate;
    private final LoadingCache<TeamId, Team> teamCache;
    
    // Multi-level caching
    // L1: In-memory (Caffeine)
    // L2: Redis (distributed)
    // L3: Database (persistent)
}
```

## Sistema de Eventos

### 🔄 Event-Driven Architecture

**Domain Events:**
```java
// Domain events for business-critical changes
public record TeamCreatedEvent(
    TeamId teamId,
    PlayerId leaderId,
    Instant createdAt
) implements DomainEvent {}

public record PlayerKilledEvent(
    PlayerId victim,
    PlayerId killer,
    Location location,
    Instant timestamp
) implements DomainEvent {}

public record DTRUpdatedEvent(
    TeamId teamId,
    DTR previousDTR,
    DTR newDTR,
    Instant timestamp
) implements DomainEvent {}
```

**Event Handlers:**
```java
@EventHandler
public class TeamEventHandler {
    
    @Subscribe
    public void handle(TeamCreatedEvent event) {
        // Update projections
        // Send notifications
        // Trigger side effects
    }
    
    @Subscribe  
    public void handle(PlayerKilledEvent event) {
        // Update statistics
        // Apply deathban
        // Update DTR
    }
}
```

### 🎯 Event Sourcing (Selected Operations)

```java
// Critical operations stored as events
public class TeamEventStore {
    
    public void append(TeamId teamId, DomainEvent event) {
        EventEntity entity = new EventEntity(
            teamId.getValue(),
            event.getClass().getSimpleName(),
            serialize(event),
            Instant.now()
        );
        
        eventRepository.save(entity);
    }
    
    public Team reconstructTeam(TeamId teamId) {
        List<DomainEvent> events = loadEvents(teamId);
        return Team.fromEvents(events);
    }
}
```

## Base de Datos y Persistencia

### 💾 Database Strategy

**Primary Database: PostgreSQL**
```sql
-- Teams
CREATE TABLE teams (
    id UUID PRIMARY KEY,
    name VARCHAR(16) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    dtr DECIMAL(4,2) NOT NULL,
    is_raidable BOOLEAN DEFAULT FALSE
);

-- Players
CREATE TABLE players (
    id UUID PRIMARY KEY,
    minecraft_uuid UUID NOT NULL UNIQUE,
    username VARCHAR(16) NOT NULL,
    team_id UUID REFERENCES teams(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Events (Event Store)
CREATE TABLE domain_events (
    id BIGSERIAL PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_data JSONB NOT NULL,
    version INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```

**Cache Layer: Redis**
```redis
# Active sessions
player:session:{uuid} -> {team_id, permissions, timers}

# Team cache
team:{id} -> {name, members, dtr, claims}

# Cooldowns
cooldown:{player_id}:{type} -> {expires_at}
```

**File Storage: Configuration**
```yaml
# application.yml
database:
  primary:
    type: postgresql
    host: localhost:5432
    database: strafehcf
    
cache:
  type: redis
  host: localhost:6379
  ttl: 3600
  
storage:
  type: local
  path: ./data
```

## Sistema de Comandos

### 🎮 Command Architecture

**Command Pattern Implementation:**
```java
// Command Interface
public interface Command {
    String getName();
    String getPermission();
    Result<Void> execute(CommandContext context);
}

// Command Implementation
public class CreateTeamCommand implements Command {
    private final CreateTeamUseCase useCase;
    
    @Override
    public Result<Void> execute(CommandContext context) {
        // Parse arguments
        // Validate permissions
        // Execute use case
        // Send response
    }
}

// Command Registry
@Component
public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();
    
    public void register(Command command) {
        commands.put(command.getName(), command);
    }
    
    public Optional<Command> findCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }
}
```

**Command Processing Pipeline:**
```java
public class CommandProcessor {
    
    public void process(Player player, String commandLine) {
        CommandContext context = parseCommand(commandLine);
        
        // Pipeline stages
        Result<Void> result = validatePermissions(context)
            .flatMap(this::validateArguments)
            .flatMap(this::executeCommand)
            .flatMap(this::logExecution);
            
        sendResponse(player, result);
    }
}
```

## Testing Strategy

### 🧪 Testing Pyramid

**Unit Tests (70%):**
```java
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    
    @Mock TeamRepository teamRepository;
    @Mock EventPublisher eventPublisher;
    
    @InjectMocks CreateTeamUseCase useCase;
    
    @Test
    void shouldCreateTeamSuccessfully() {
        // Given
        CreateTeamCommand command = new CreateTeamCommand(
            PlayerId.of("player-1"),
            TeamName.of("TestTeam")
        );
        
        // When
        Result<Team> result = useCase.execute(command);
        
        // Then
        assertThat(result.isSuccess()).isTrue();
        verify(teamRepository).save(any(Team.class));
        verify(eventPublisher).publish(any(TeamCreatedEvent.class));
    }
}
```

**Integration Tests (20%):**
```java
@SpringBootTest
@Testcontainers
class TeamRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @Autowired TeamRepository teamRepository;
    
    @Test
    void shouldPersistTeamCorrectly() {
        // Test with real database
    }
}
```

**End-to-End Tests (10%):**
```java
@ExtendWith(MockBukkitExtension.class)
class CreateTeamE2ETest {
    
    @Test
    void shouldCreateTeamThroughCommand() {
        // Simulate full player command execution
    }
}
```

## Deployment y DevOps

### 🚀 Build Pipeline

```yaml
# .github/workflows/ci.yml
name: CI/CD Pipeline

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: ./mvnw test
      
  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - run: ./mvnw package
      - uses: actions/upload-artifact@v3
        with:
          name: strafehcf-plugin
          path: target/*.jar
```

### 📊 Monitoring y Observabilidad

```java
// Metrics
@Component
public class HCFMetrics {
    private final MeterRegistry meterRegistry;
    
    public void recordCommand(String command, long duration) {
        Timer.Sample.start(meterRegistry)
            .stop(Timer.builder("hcf.command.duration")
                .tag("command", command)
                .register(meterRegistry));
    }
}

// Health Checks  
@Component
public class HCFHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // Check database connectivity
        // Check cache availability
        // Check plugin state
        return Health.up().build();
    }
}
```

## Migration Strategy

### 🔄 Migración desde GelicHCF

**Fase 1: Data Migration**
```java
@Component
public class GelicDataMigrator {
    
    public void migrateTeams() {
        // Read GelicHCF team data
        // Convert to new format
        // Import to new system
    }
    
    public void migratePlayers() {
        // Preserve player statistics
        // Convert cooldowns
        // Migrate balances
    }
}
```

**Fase 2: Feature Parity**
- Implementar core features (P0)
- Testing exhaustivo
- Performance benchmarking

**Fase 3: Enhancement**
- Nuevas features (P1, P2)
- UI improvements
- Performance optimizations

---

*Arquitectura diseñada: 28 de Agosto 2025*
*Target: Production-ready en Q4 2025*
*Stack: Java 21, Spring Boot, PostgreSQL, Redis*
