# Análisis de Características - GelicHCF

## Resumen Ejecutivo

GelicHCF implementa un sistema HCF completo con **más de 100 características** divididas en módulos core, eventos especiales, sistemas de combate, gestión de equipos y integraciones externas. El análisis revela un gamemode maduro pero con complejidad alta debido a la acumulación de features a lo largo del tiempo.

## Características Core del HCF

### 🏰 Sistema de Facciones/Teams

**Funcionalidad Principal:**
- Creación y gestión de equipos (max 15 miembros)
- Sistema de claiming territorial
- DTR (Deaths Till Raidable) dinámico
- Sistema de alianzas
- Roles internos (Leader, Co-Leader, Officer, Member)

**Configuración Clave:**
```yaml
TEAMS:
  TEAM_SIZE: 15
  ALLIES: 0  # Disabled
  MAX_CLAIMS: 1
  DTR_PER_MEMBER: [1.01, 2.02, 3.03, ..., 7.2]
```

**Mecánicas Avanzadas:**
- **Regen System**: DTR regenera automáticamente cada 30s
- **Claiming**: Sistema de claims con wand tool
- **Protection**: Diferentes niveles de protección por zona
- **Economy Integration**: Costos por bloque y multiplicadores

### ⚰️ Sistema de Deathban

**Concepto:** Los jugadores son "baneados" temporalmente al morir, simulando consecuencias reales.

**Características:**
- Tiempo base: 45 minutos
- Reducción por rango: VIP (35min), VIP+ (30min), etc.
- Arena de deathban con kits especiales
- Sistema de vidas para revivir
- Signos interactivos para información

**Mecánicas Especiales:**
- Kill reduction: -5 minutos por kill en arena
- Location tracking: Guardado de ubicación de muerte
- Killer tracking: Información de quién te mató

### 🗡️ Sistema de Combate HCF

**PvP Classes:**
```
Diamond (Default) - Combate estándar
Archer           - Bow damage bonus, speed, leather armor only
Bard             - Team buffs, gold armor, no direct combat
Ghost            - Invisibility abilities, chain armor
Rogue            - Backstab damage, leather armor
Mage             - Magic abilities, configurable armor
```

**Combat Mechanics:**
- Combat Tag: 45 segundos sin logout
- Strength Nerf: Reducción de daño por poción
- Pearling Restrictions: Cooldowns y limitaciones por zona
- Rod Timer: 3 segundos entre fishing rod hits

**Cooldown System:**
```yaml
TIMERS_COOLDOWN:
  COMBAT_TAG: 45
  ENDER_PEARL: 16
  GAPPLE: 10800  # 3 horas
  APPLE: 45
  PVP_TIMER: 1800  # Para nuevos jugadores
```

## Eventos y Actividades

### 👑 King of the Hill (KOTH)

**Sistema de Eventos:**
- Eventos programados o manuales
- Sistema de captura por tiempo
- Rewards automáticos
- Múltiples KOTHs simultáneos (configurable)

**Configuración:**
- Control percentage required
- Capturing messages cada 20s
- Commands ejecutados al capturar
- Integration con Discord webhooks

### 🏛️ Eventos Especiales

**Citadel:**
- Evento semanal de gran escala
- Sistema de loot personalizado
- Requiere coordinación de equipo
- Loot tables configurables con probabilidades

**Conquest:**
- Múltiples puntos de captura
- Sistema de puntos por equipo
- Eventos de larga duración
- Estrategia territorial

**End of the World (EOTW):**
- Evento final del mapa
- Todos los equipos se vuelven raideables
- Pre-EOTW timer con restricciones
- Commands automáticos al finalizar

### 🌟 Start of the World (SOTW)

**Características:**
- Período de gracia inicial
- Flying permitido (configurable)
- Timers especiales para setup
- Kit de inicio automático

## Sistemas de Progresión

### 🎁 Sistema de Kits

**Tipos de Kits:**
- Kits por rango/donación
- Kits especiales de eventos
- Kit de deathban arena
- Cooldowns individuales por kit

**Kit Signs:**
- Signos interactivos para reclamar
- Información visual de cooldowns
- Integration con permisos

### 💰 Sistema Económico

**Balance Management:**
- Balances por defecto según rango
- Integration con Vault
- Costos de claiming
- Rewards por kills/eventos

**Spawner Economy:**
- Spawners como currency
- Crowbar tool para recolectar
- Tipos configurables de spawners
- Stack system para optimización

### 🏆 Estadísticas y Rankings

**Tracking System:**
- Kills/Deaths individuales
- KDR calculation
- Killstreaks tracking
- Team statistics (F-Top)

**Hologram Integration:**
- Top 10 rankings automáticos
- Updates en tiempo real
- Placeholders customizables
- Multiple categories

## Features de Calidad de Vida

### 🔧 Utilidades de Administración

**Staff Tools:**
- Staff mode con vanish
- Inspection tools
- Freeze system
- Random teleport
- Inventory restoration system

**Moderation Features:**
- Report system con Discord integration
- Request system para ayuda
- Chat moderation
- Command restrictions

### 🎨 Customización Visual

**Scoreboard System:**
- Información dinámica de equipo
- Timers visibles
- Customizable per gamemode
- Animation support

**Nametag System:**
- Colores por relación de equipo
- Prefixes especiales (King, Staff, etc.)
- Integration con sistemas de rangos
- Distance-based visibility

**Tablist Enhancement:**
- Información organizada por equipos
- Ping y status indicators
- Custom headers/footers
- Integration con PlaceholderAPI

### 🌍 Gestión de Mundo

**World Management:**
- Borders configurables por mundo
- Warzone definitions
- Safe zones automáticas
- Portal restrictions

**Special Zones:**
- Spawn protection
- Event areas
- Mountains (Ore/Glowstone)
- Road systems

## Integraciones Externas

### 👥 Client Integrations

**Supported Clients:**
- Lunar Client API (waypoints, mods)
- CheatBreaker API
- Custom mod support
- Protocol version handling

**Features:**
- Waypoint sharing
- Minimap integration
- Custom UI elements
- Performance optimizations

### 🔌 Plugin Hooks

**Rank Systems (14+ supported):**
- AquaCore, Zoot, Zoom, Atom, etc.
- Automatic detection
- Fallback to Vault
- Permission integration

**Abilities Systems:**
- PandaAbilityAPI
- SladeAPI
- Custom ability support
- Cooldown integration

**Other Integrations:**
- HolographicDisplays
- PlaceholderAPI
- ViaVersion/ProtocolSupport
- WorldEdit integration

## Sistemas Técnicos Avanzados

### 📊 Data Management

**Storage Options:**
- JSON file system
- MongoDB integration
- Automatic backups
- Migration utilities

**Caching System:**
- In-memory user data
- Cooldown management
- Team information cache
- Performance optimization

### 🔄 Multi-Version Support

**Version Compatibility:**
- 1.7.10 - 1.20 support
- Version-specific modules
- API abstraction layer
- Legacy protocol support

**Implementation:**
```
Azurite1.18/  - Minecraft 1.18 specific code
Azurite1.19/  - Minecraft 1.19 specific code  
Azurite1.20/  - Minecraft 1.20 specific code
```

### 🛡️ Anti-Grief Systems

**Protection Mechanisms:**
- Claim protection
- Anti-glitch listeners
- Combat logging prevention
- Inventory restoration

**Exploit Prevention:**
- Bow boosting restrictions
- Enderpearl limitations
- Bed placement blocking
- Obsidian generator fixes

## Métricas de Características

### 📈 Complejidad por Módulo

```
High Complexity (8-10):
- TeamManager (claiming, DTR, relations)
- EventManager (KOTH, Citadel, Conquest)
- TimerManager (30+ different timers)

Medium Complexity (5-7):
- UserManager (stats, data persistence)
- PvPClassManager (6 different classes)
- DeathbanManager (arena, lives, tracking)

Low Complexity (1-4):
- NametagManager
- TablistManager
- TipManager
```

### 🎯 Feature Priorities para StrafeHCF

**Must Have (P0):**
1. Team/Faction System
2. Deathban System
3. Basic PvP Classes (Diamond, Archer, Bard)
4. KOTH Events
5. Combat Tag System

**Should Have (P1):**
1. Citadel Events
2. Advanced Statistics
3. Staff Tools
4. Economy Integration
5. Multi-version Support

**Could Have (P2):**
1. Conquest System
2. Advanced PvP Classes
3. Multiple Client Integrations
4. Complex Ability Systems
5. Advanced Holograms

**Won't Have (P3):**
1. Legacy Version Support (<1.18)
2. 14+ Rank System Integrations
3. Complex Mountain Systems
4. Falltrap/Base Token Systems

---

*Total Features Identificados: 150+*
*Core Features: 45*
*Quality of Life Features: 60+*
*Admin Features: 25+*
*Integration Features: 20+*
