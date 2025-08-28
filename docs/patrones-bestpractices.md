# Patrones y Best Practices - StrafeHCF

## Introducción

Este documento recopila los patrones de diseño, mejores prácticas y lecciones aprendidas del análisis de GelicHCF, proporcionando una guía para el desarrollo de **StrafeHCF** con código limpio, arquitectura sólida y mantenibilidad a largo plazo.

## Patrones Arquitectónicos

### 🏛️ Hexagonal Architecture (Ports & Adapters)

**Principio**: Aislar la lógica de negocio de los detalles de implementación.

```java
// ✅ BIEN: Domain independiente de frameworks
public class Team {
    private final TeamId id;
    private final TeamName name;
    private DTR dtr;
    
    public boolean canBeRaided() {
        return dtr.getValue() <= 0.0;
    }
    
    public void memberDied() {
        this.dtr = dtr.reduceDTR(1.0);
    }
}

// ✅ BIEN: Puerto para persistencia  
public interface TeamRepository {
    Optional<Team> findById(TeamId id);
    void save(Team team);
}

// ✅ BIEN: Adaptador específico de tecnología
@Repository
public class PostgresTeamRepository implements TeamRepository {
    // Implementación específica de PostgreSQL
}
```

```java
// ❌ MAL: Lógica de dominio acoplada a framework
public class Team extends BukkitRunnable {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Lógica de negocio mezclada con eventos Bukkit
    }
}
```

### 🎯 Command Query Responsibility Segregation (CQRS)

**Principio**: Separar operaciones de lectura y escritura.

```java
// ✅ BIEN: Command para operaciones de escritura
public record CreateTeamCommand(
    PlayerId leaderId,
    TeamName teamName
) implements Command {}

public class CreateTeamHandler {
    public Result<TeamId> handle(CreateTeamCommand command) {
        // Validaciones de negocio
        // Crear entidad
        // Persistir cambios
        // Publicar eventos
    }
}

// ✅ BIEN: Query para operaciones de lectura
public record GetTeamStatsQuery(TeamId teamId) implements Query {}

public class GetTeamStatsHandler {
    public TeamStatsView handle(GetTeamStatsQuery query) {
        // Solo lectura, optimizada para display
    }
}
```

### 📧 Event-Driven Architecture

**Principio**: Comunicación desacoplada mediante eventos.

```java
// ✅ BIEN: Eventos de dominio
public record PlayerKilledEvent(
    PlayerId victim,
    PlayerId killer,
    Location location,
    Instant timestamp
) implements DomainEvent {}

// ✅ BIEN: Handler especializado
@EventHandler
public class DeathbanEventHandler {
    @Subscribe
    public void handle(PlayerKilledEvent event) {
        // Aplicar deathban
        // Actualizar estadísticas
        // Notificar equipo
    }
}
```

## Patrones de Diseño Específicos

### 🏭 Factory Pattern para Creación Compleja

```java
// ✅ BIEN: Factory para diferentes tipos de eventos
public interface EventFactory {
    HCFEvent createEvent(EventType type, EventConfig config);
}

@Component
public class HCFEventFactory implements EventFactory {
    
    @Override
    public HCFEvent createEvent(EventType type, EventConfig config) {
        return switch (type) {
            case KOTH -> new KothEvent(config);
            case CITADEL -> new CitadelEvent(config);
            case CONQUEST -> new ConquestEvent(config);
            default -> throw new UnsupportedEventException(type);
        };
    }
}
```

### 🎭 Strategy Pattern para Algoritmos Variables

```java
// ✅ BIEN: Strategy para diferentes sistemas de DTR
public interface DTRCalculationStrategy {
    DTR calculateDTR(Team team, List<DeathEvent> deaths);
}

public class StandardDTRStrategy implements DTRCalculationStrategy {
    @Override
    public DTR calculateDTR(Team team, List<DeathEvent> deaths) {
        double baseDTR = team.getMemberCount() * 1.0 + 0.01;
        double lostDTR = deaths.size() * 1.0;
        return DTR.of(Math.max(0.0, baseDTR - lostDTR));
    }
}

public class CustomDTRStrategy implements DTRCalculationStrategy {
    // Implementación alternativa
}
```

### 🏪 Repository Pattern para Abstracción de Datos

```java
// ✅ BIEN: Repository interface limpia
public interface UserRepository {
    Optional<User> findById(PlayerId id);
    Optional<User> findByMinecraftUUID(UUID uuid);
    List<User> findByTeam(TeamId teamId);
    void save(User user);
    void delete(PlayerId id);
    
    // Queries específicas
    List<User> findTopKillers(int limit);
    List<User> findActiveUsers(Duration since);
}

// ✅ BIEN: Implementación con transacciones
@Repository
@Transactional
public class JpaUserRepository implements UserRepository {
    
    @Autowired private UserEntityRepository entityRepo;
    @Autowired private UserMapper mapper;
    
    @Override
    public void save(User user) {
        UserEntity entity = mapper.toEntity(user);
        entityRepo.save(entity);
    }
}
```

## Best Practices de Desarrollo

### 💎 Clean Code Principles

**1. Nombres Descriptivos**
```java
// ✅ BIEN: Nombres que expresan intención
public class TeamDTRService {
    public DTRCalculationResult calculateNewDTR(
        Team team, 
        PlayerDeathEvent deathEvent
    ) {}
}

// ❌ MAL: Nombres ambiguos
public class TeamManager {
    public void process(Object data) {}
}
```

**2. Funciones Pequeñas y Específicas**
```java
// ✅ BIEN: Una responsabilidad por función
public class CombatService {
    
    public boolean isInCombat(Player player) {
        return combatTags.containsKey(player.getId());
    }
    
    public void tagPlayerForCombat(Player player, Duration duration) {
        Instant expiresAt = Instant.now().plus(duration);
        combatTags.put(player.getId(), expiresAt);
    }
    
    public void clearCombatTag(Player player) {
        combatTags.remove(player.getId());
    }
}

// ❌ MAL: Función que hace demasiado
public void handlePlayerCombat(Player p1, Player p2, Item weapon) {
    // 100+ líneas mezclando validaciones, cálculos, persistencia...
}
```

**3. Immutability Cuando Sea Posible**
```java
// ✅ BIEN: Value Objects inmutables
public record TeamName(String value) {
    public TeamName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        if (value.length() < 3 || value.length() > 12) {
            throw new IllegalArgumentException("Team name must be 3-12 characters");
        }
    }
}

// ✅ BIEN: Builder pattern para objetos complejos
public class Team {
    private final TeamId id;
    private final TeamName name;
    private final Set<PlayerId> members;
    
    public static TeamBuilder builder() {
        return new TeamBuilder();
    }
    
    public Team withNewMember(PlayerId playerId) {
        Set<PlayerId> newMembers = new HashSet<>(members);
        newMembers.add(playerId);
        return new Team(id, name, newMembers);
    }
}
```

### 🧪 Testing Best Practices

**1. Test Pyramid Structure**
```java
// ✅ BIEN: Unit test para lógica de dominio
@Test
void shouldCalculateDTRCorrectly() {
    // Given
    Team team = Team.builder()
        .withMembers(List.of(playerId1, playerId2, playerId3))
        .build();
    
    // When  
    DTR dtr = team.calculateInitialDTR();
    
    // Then
    assertThat(dtr.getValue()).isEqualTo(3.01);
}

// ✅ BIEN: Integration test para repositories
@DataJpaTest
class TeamRepositoryTest {
    
    @Test
    void shouldPersistTeamCorrectly() {
        // Test con base de datos real (H2)
    }
}

// ✅ BIEN: E2E test para comandos
@SpringBootTest
class CreateTeamCommandE2ETest {
    
    @Test
    void shouldCreateTeamThroughMinecraftCommand() {
        // Simular comando completo de jugador
    }
}
```

**2. Mocking Strategy**
```java
// ✅ BIEN: Mock dependencies, no domain objects
@ExtendWith(MockitoExtension.class)
class CreateTeamUseCaseTest {
    
    @Mock TeamRepository teamRepository;
    @Mock EventPublisher eventPublisher;
    @Mock PermissionService permissionService;
    
    @InjectMocks CreateTeamUseCase useCase;
    
    @Test
    void shouldCreateTeamWhenValidInput() {
        // Given
        given(permissionService.canCreateTeam(playerId)).willReturn(true);
        given(teamRepository.existsByName(teamName)).willReturn(false);
        
        // When & Then
        Result<Team> result = useCase.execute(command);
        
        assertThat(result.isSuccess()).isTrue();
        verify(teamRepository).save(any(Team.class));
        verify(eventPublisher).publish(any(TeamCreatedEvent.class));
    }
}
```

### 🔐 Security Best Practices

**1. Input Validation**
```java
// ✅ BIEN: Validación en múltiples capas
@Component
public class CommandValidator {
    
    public ValidationResult validate(CreateTeamCommand command) {
        return ValidationResult.builder()
            .check("teamName", validateTeamName(command.teamName()))
            .check("playerId", validatePlayerId(command.playerId()))
            .build();
    }
    
    private ValidationResult validateTeamName(TeamName name) {
        if (containsProfanity(name.value())) {
            return ValidationResult.error("Team name contains inappropriate content");
        }
        return ValidationResult.success();
    }
}

// ❌ MAL: Confiar en input del cliente
public void createTeam(String rawTeamName) {
    // Directamente usar rawTeamName sin validar
    Team team = new Team(rawTeamName);
}
```

**2. Permission Checking**
```java
// ✅ BIEN: Permission checking consistente
@PreAuthorize("hasPermission(#command.playerId, 'CREATE_TEAM')")
public Result<Team> createTeam(CreateTeamCommand command) {
    // Lógica del comando
}

// ✅ BIEN: Rate limiting
@RateLimited(requests = 5, window = Duration.ofMinutes(1))
public Result<Void> sendTeamInvite(SendInviteCommand command) {
    // Previene spam de invitaciones
}
```

### ⚡ Performance Best Practices

**1. Caching Strategy**
```java
// ✅ BIEN: Multi-level caching
@Component
public class TeamCacheManager {
    
    private final LoadingCache<TeamId, Team> l1Cache; // In-memory
    private final RedisTemplate<String, Team> l2Cache; // Distributed
    private final TeamRepository repository; // Database
    
    public Optional<Team> findTeam(TeamId teamId) {
        // L1 -> L2 -> Database fallback
        return Optional.ofNullable(l1Cache.get(teamId))
            .or(() -> getFromRedis(teamId))
            .or(() -> getFromDatabase(teamId));
    }
}

// ✅ BIEN: Lazy loading para relaciones
public class Team {
    @LazyLoad
    public List<Claim> getClaims() {
        return claimRepository.findByTeamId(this.id);
    }
}
```

**2. Database Optimization**
```sql
-- ✅ BIEN: Índices apropiados
CREATE INDEX idx_teams_name ON teams(name);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_events_timestamp ON domain_events(created_at);

-- ✅ BIEN: Queries optimizadas
SELECT t.*, COUNT(p.id) as member_count
FROM teams t
LEFT JOIN players p ON t.id = p.team_id
WHERE t.is_active = true
GROUP BY t.id;
```

**3. Async Processing**
```java
// ✅ BIEN: Operaciones costosas en background
@Async("hcfTaskExecutor")
public CompletableFuture<Void> updateStatistics(PlayerKilledEvent event) {
    // Actualización de stats en background
    statisticsService.updateKillStats(event);
    return CompletableFuture.completedFuture(null);
}

// ✅ BIEN: Batch operations
@Scheduled(fixedRate = 30000) // Every 30 seconds
public void processPendingDTRRegeneration() {
    List<Team> teamsToRegen = teamRepository.findTeamsReadyForRegen();
    teamService.batchRegenerateDTR(teamsToRegen);
}
```

## Anti-Patterns a Evitar

### ❌ God Object Anti-Pattern

```java
// ❌ MAL: Clase que hace demasiado
public class HCFManager {
    // 50+ métodos
    // 30+ dependencias
    // Maneja teams, combat, events, etc.
    
    public void handleEverything() {
        // Lógica de 500+ líneas
    }
}

// ✅ BIEN: Responsabilidades separadas
public class TeamService { /* Solo teams */ }
public class CombatService { /* Solo combat */ }
public class EventService { /* Solo events */ }
```

### ❌ Circular Dependencies

```java
// ❌ MAL: Dependencias circulares
public class TeamManager {
    @Autowired private UserManager userManager;
}

public class UserManager {
    @Autowired private TeamManager teamManager; // Circular!
}

// ✅ BIEN: Dependencias unidireccionales
public class TeamService {
    private final UserRepository userRepository; // Repository, not service
}

public class UserService {
    private final TeamRepository teamRepository;
}
```

### ❌ Magic Numbers/Strings

```java
// ❌ MAL: Valores mágicos hardcodeados
if (team.getMembers().size() > 15) { /* límite de equipo */ }
if (dtr <= 0.0) { /* raideable */ }

// ✅ BIEN: Constantes con nombres descriptivos
public class HCFConstants {
    public static final int MAX_TEAM_SIZE = 15;
    public static final double RAIDABLE_DTR_THRESHOLD = 0.0;
    public static final Duration COMBAT_TAG_DURATION = Duration.ofSeconds(45);
}
```

## Configuration Management

### 📝 Configuración Tipada y Validada

```java
// ✅ BIEN: Configuration beans tipadas
@ConfigurationProperties(prefix = "strafehcf.combat")
@Data
@Validated
public class CombatConfig {
    
    @NotNull
    @Positive
    private Duration combatTagDuration = Duration.ofSeconds(45);
    
    @NotNull
    @Positive
    private Duration enderpearlCooldown = Duration.ofSeconds(16);
    
    @DecimalMin("0.0")
    @DecimalMax("1.0") 
    private double strengthLevel1Multiplier = 0.65;
}

// ✅ BIEN: Validation en startup
@Component
public class ConfigValidator implements ApplicationListener<ContextRefreshedEvent> {
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        validateAllConfigurations();
    }
}
```

## Error Handling

### 🚨 Manejo Robusto de Errores

```java
// ✅ BIEN: Result pattern para operaciones que pueden fallar
public sealed interface Result<T> {
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(String error, Throwable cause) implements Result<T> {}
    
    default boolean isSuccess() {
        return this instanceof Success<T>;
    }
    
    default T getValue() {
        return switch (this) {
            case Success<T> success -> success.value();
            case Failure<T> failure -> throw new ResultException(failure.error());
        };
    }
}

// ✅ BIEN: Uso del Result pattern
public Result<Team> createTeam(CreateTeamCommand command) {
    try {
        // Validaciones
        var validationResult = validator.validate(command);
        if (!validationResult.isValid()) {
            return Result.failure("Validation failed: " + validationResult.errors());
        }
        
        // Lógica de negocio
        Team team = teamFactory.create(command);
        teamRepository.save(team);
        
        return Result.success(team);
        
    } catch (Exception e) {
        logger.error("Failed to create team", e);
        return Result.failure("Team creation failed", e);
    }
}
```

## Logging and Monitoring

### 📊 Logging Estructurado

```java
// ✅ BIEN: Logging estructurado con MDC
@Component
public class TeamService {
    
    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
    
    public Result<Team> createTeam(CreateTeamCommand command) {
        try (MDCCloseable mdc = MDC.putCloseable("operation", "createTeam")) {
            MDC.put("playerId", command.playerId().toString());
            MDC.put("teamName", command.teamName().value());
            
            logger.info("Starting team creation");
            
            // Lógica...
            
            logger.info("Team created successfully", 
                kv("teamId", team.getId()),
                kv("memberCount", team.getMemberCount()));
                
            return Result.success(team);
        }
    }
}

// ✅ BIEN: Métricas de aplicación
@Component
public class HCFMetrics {
    
    private final Counter teamCreations;
    private final Timer commandExecutionTime;
    
    public HCFMetrics(MeterRegistry registry) {
        this.teamCreations = Counter.builder("hcf.teams.created")
            .register(registry);
        this.commandExecutionTime = Timer.builder("hcf.command.execution")
            .register(registry);
    }
    
    public void recordTeamCreation() {
        teamCreations.increment();
    }
}
```

## Documentation Standards

### 📚 Documentación de Código

```java
/**
 * Manages HCF team operations including creation, member management, and DTR calculations.
 * 
 * <p>This service handles all core team functionality while ensuring data consistency
 * and proper event propagation. All operations are transactional and thread-safe.
 * 
 * @since 1.0.0
 */
@Service
@Transactional
public class TeamService {
    
    /**
     * Creates a new HCF team with the specified leader.
     * 
     * @param command the team creation parameters including leader and team name
     * @return a Result containing the created Team or failure information
     * @throws IllegalArgumentException if command parameters are invalid
     * @since 1.0.0
     */
    public Result<Team> createTeam(@Valid CreateTeamCommand command) {
        // Implementation
    }
}
```

---

*Best Practices compiladas: 28 de Agosto 2025*
*Basado en análisis de GelicHCF y estándares modernos*
*Target: Código mantenible y escalable para StrafeHCF*

## Checklist de Calidad

### ✅ Code Quality Checklist

**Antes de cada commit:**
- [ ] Código sigue naming conventions
- [ ] Funciones tienen una sola responsabilidad
- [ ] No hay magic numbers o strings
- [ ] Tests unitarios escritos
- [ ] Documentación actualizada
- [ ] SonarQube quality gate pasa
- [ ] No security vulnerabilities

**Antes de cada release:**
- [ ] Code coverage > 85%
- [ ] Performance benchmarks pasan
- [ ] Integration tests pasan
- [ ] Documentation completa
- [ ] Changelog actualizado
- [ ] Migration scripts probados
