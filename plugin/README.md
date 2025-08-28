# StrafeHCF Plugin

Plugin HCF moderno y optimizado para Minecraft 1.21.8, desarrollado específicamente para el servidor StrafePVP.

## Arquitectura

Este plugin sigue los principios de **Arquitectura Hexagonal** (Ports and Adapters) con **Domain-Driven Design (DDD)** y **Clean Architecture**.

### Estructura del Proyecto

```
src/main/java/net/strafepvp/hcf/
├── 📦 StrafeHCFPlugin.java              # Plugin principal
├── 🏗️ application/                      # Capa de Aplicación
│   ├── HCFApplicationService.java       # Servicio principal
│   ├── config/                          # Gestión de configuración
│   └── lifecycle/                       # Ciclo de vida del plugin
├── 🏛️ core/                             # Lógica de Negocio (Framework Independent)
│   ├── domain/                          # Entidades y Value Objects
│   │   ├── entities/                    # Team, Player, Claim, etc.
│   │   └── valueobjects/               # TeamId, TeamName, PlayerId, DTR, etc.
│   ├── usecases/                        # Casos de Uso (Application Services)
│   └── ports/                           # Interfaces para dependencias externas
└── 📱 adapters/                         # Adaptadores (Framework Integration)
    ├── spigot/                          # Integración con Spigot/Paper
    │   ├── commands/                    # Comandos del plugin
    │   ├── events/                      # Event listeners
    │   └── SpigotCommandManager.java    # Gestor de comandos
    └── persistence/                     # Persistencia de datos
        └── DatabaseManager.java         # Gestor de base de datos
```

## Características Principales

### ✅ Implementadas (Versión 1.0.0-SNAPSHOT)

- **🏗️ Arquitectura Base**: Hexagonal con inyección de dependencias
- **📝 Sistema de Configuración**: YAML tipado y validado
- **💾 Base de Datos**: PostgreSQL con HikariCP pooling
- **🎮 Comandos Básicos**: `/hcf` y `/team` con tab completion
- **🔧 Gestión de Ciclo de Vida**: Inicialización y cierre limpio

### 🚧 En Desarrollo (Roadmap)

- **👥 Sistema de Equipos**: Creación, gestión, roles, DTR
- **⚔️ Sistema de Combate**: Combat tag, cooldowns, PvP protection
- **⚰️ Deathban**: Muerte temporal con arena especial
- **🗺️ Claims**: Sistema territorial con protección
- **👑 KOTH**: Eventos King of the Hill
- **🎭 Clases PvP**: Diamond, Archer, Bard

## Requisitos

- **Java**: 21 LTS
- **Minecraft**: 1.21.8 (Paper recomendado)
- **Base de Datos**: PostgreSQL 15+ (H2 para desarrollo)
- **Memoria**: Mínimo 2GB RAM
- **Dependencias**: Vault (requerido), PlaceholderAPI, HolographicDisplays (opcional)

## Instalación

1. **Compilar el plugin**:
   ```bash
   cd plugin/
   mvn clean package
   ```

2. **Instalar en servidor**:
   ```bash
   cp target/StrafeHCF-1.0.0-SNAPSHOT.jar /path/to/server/plugins/
   ```

3. **Configurar base de datos** (editar `plugins/StrafeHCF/config.yml`):
   ```yaml
   database:
     type: POSTGRESQL
     host: localhost
     port: 5432
     name: strafehcf
     username: strafehcf
     password: your_password
   ```

4. **Reiniciar servidor** y verificar logs

## Configuración

La configuración principal se encuentra en `config.yml`:

- **Database**: Configuración de conexión a base de datos
- **Teams**: Límites de miembros, DTR, alianzas
- **Combat**: Timers, cooldowns, protecciones
- **Deathban**: Tiempos de baneo, arena
- **Claims**: Herramientas, costos, límites
- **KOTH**: Eventos, rewards, programación
- **Classes**: Habilidades de PvP por clase

## Comandos

### Administración
- `/hcf info` - Información del plugin
- `/hcf status` - Estado de servicios
- `/hcf reload` - Recargar configuración

### Equipos
- `/team` - Ver información del equipo
- `/team create <nombre>` - Crear equipo
- `/team invite <jugador>` - Invitar jugador
- `/team info [equipo]` - Ver información de equipos
- `/team chat <mensaje>` - Chat de equipo

## Desarrollo

### Tecnologías

- **Java 21**: Lenguaje principal
- **Maven**: Build tool y gestión de dependencias
- **Paper API**: Integración con Minecraft
- **PostgreSQL + HikariCP**: Base de datos
- **JUnit 5 + Mockito**: Testing framework

### Principios de Diseño

- **SOLID Principles**: Single Responsibility, Open/Closed, etc.
- **Domain-Driven Design**: Lógica de negocio en el dominio
- **Hexagonal Architecture**: Independencia de frameworks
- **Clean Code**: Código legible y mantenible

### Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con coverage
mvn test jacoco:report

# Ver reporte de coverage
open target/site/jacoco/index.html
```

### Estructura de Testing

```
src/test/java/net/strafepvp/hcf/
├── unit/                    # Tests unitarios (domain + use cases)
├── integration/             # Tests de integración (database, etc.)
└── e2e/                     # Tests end-to-end (comandos completos)
```

## Contribuir

1. **Fork** del repositorio
2. **Crear rama** para feature: `git checkout -b feature/nueva-funcionalidad`
3. **Commit** cambios: `git commit -m "Add: nueva funcionalidad"`
4. **Push** a la rama: `git push origin feature/nueva-funcionalidad`
5. **Crear Pull Request**

### Convenciones

- **Commits**: Usar format "Type: Description" (Add, Fix, Update, etc.)
- **Código**: Seguir Google Java Style Guide
- **Tests**: Mínimo 85% code coverage
- **Documentación**: JavaDoc para APIs públicas

## Roadmap

### Fase 1: Foundation (Semanas 1-3) ✅
- ✅ Arquitectura base
- ✅ Sistema de configuración
- ✅ Base de datos
- ✅ Comandos básicos

### Fase 2: Core Features (Semanas 4-6) 🚧
- 🚧 Sistema de equipos completo
- 🚧 DTR y combate
- 🚧 Deathban
- 🚧 Claims básicos

### Fase 3: Advanced Features (Semanas 7-9) 📅
- 📅 KOTH events
- 📅 Clases PvP
- 📅 UI systems
- 📅 Staff tools

### Fase 4: Launch Ready (Semanas 10-12) 📅
- 📅 Performance optimization
- 📅 Complete testing
- 📅 Migration tools
- 📅 Production deployment

## Licencia

Este proyecto es **privado** y propietario de **StrafePVP**. Todos los derechos reservados.

## Autor

**mayk0l** - Desarrollador principal
- GitHub: [@mayk0l](https://github.com/mayk0l)
- Servidor: StrafePVP

---

*Última actualización: 28 de Agosto 2025*
