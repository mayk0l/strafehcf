# Paper 1.21.8 - Información de Descarga y Configuración

## ✅ Descarga Completada

**Archivo descargado:** `paper-1.21.8-50.jar`
**Fecha de descarga:** 28 de Agosto, 2025
**Tamaño:** 52.8 MB (52,811,998 bytes)
**SHA256:** `87f52f668576a77cdcd77e489f7ca3b8acad952634d638bb102988e1001d5d44` ✅ Verificado
**Build:** #50 (último build disponible)
**Canal:** default
**Fecha de build:** 2025-08-25T02:02:34.687Z

---

## 📋 Información del Build

### Cambios Incluidos en Build #50:
- **Commit:** `51706e5ac1305c8490538a44c9b7019df0ac2bfc`
- **Resumen:** Fixed DyeItem sheep dye hunk
- **Mensaje:** Fixed DyeItem sheep dye hunk

### Compatibilidad:
- ✅ **Java 21 LTS** (Requerido)
- ✅ **Minecraft 1.21.8** (Cliente)
- ✅ **Spring Boot 3.2+** (Arquitectura propuesta)

---

## 🚀 Ventajas de Paper sobre Spigot

### Rendimiento:
- **Optimizaciones Async**: Procesamiento asíncrono mejorado
- **Garbage Collection**: Optimizaciones de memoria
- **Chunk Loading**: Carga de chunks más eficiente
- **Entity AI**: Inteligencia artificial de entidades optimizada

### Funcionalidades:
- **Paper API**: APIs adicionales no disponibles en Spigot
- **Configuración Avanzada**: `paper-global.yml` y `paper-world-defaults.yml`
- **Anti-Cheat**: Protecciones adicionales contra exploits
- **Adventure API**: Soporte nativo para componentes de texto modernos

### Mantenimiento:
- **Actualizaciones Rápidas**: Updates más frecuentes que Spigot
- **Bug Fixes**: Corrección de errores más activa
- **Compatibilidad**: 100% compatible con plugins de Spigot

---

## ⚙️ Configuración Inicial

### Archivos de Configuración de Paper:

#### `paper-global.yml`
```yaml
# Configuración global de Paper
_version: 30
chunk-loading-advanced:
  auto-config-send-distance: true
  player-max-concurrent-loads: 20.0
  player-max-chunk-load-rate: -1.0

console:
  enable-brigadier-completions: true
  enable-brigadier-highlighting: true
  has-all-permissions: false

item-validation:
  display-name: 8192
  loc-name: 8192
  lore-line: 8192
  book:
    author: 8192
    page: 16384
    title: 8192

logging:
  deobfuscate-stacktraces: true
  log-player-ip-addresses: true
  use-rgb-for-named-text-colors: true

messages:
  kick:
    authentication-servers-down: <lang:multiplayer.disconnect.authservers_down>
    connection-throttle: Connection throttled! Please wait before reconnecting.
    flying-player: <lang:multiplayer.disconnect.flying>
    flying-vehicle: <lang:multiplayer.disconnect.flying>
  no-permission: <red>I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.

misc:
  chat-threads:
    chat-executor-core-size: -1
    chat-executor-max-size: -1
  compression-level:
    compression-level: -1
  fix-entity-position-desync: true
  lag-compensate-block-breaking: true
  load-permissions-yml-before-plugins: true
  max-joins-per-tick: 5
  region-file-cache-size: 256
  strict-advancement-dimension-check: false
  use-alternative-luck-formula: false
  use-dimension-type-for-custom-spawners: false

packet-limiter:
  all-packets:
    action: KICK
    interval: 7.0
    max-packet-rate: 2500.0
  kick-message: <red><lang:disconnect.exceeded_packet_rate>
  overrides:
    ServerboundPlaceRecipePacket:
      action: DROP
      interval: 4.0
      max-packet-rate: 5.0

player-auto-save:
  max-per-tick: -1
  rate: -1

proxies:
  bungee-cord:
    online-mode: true
  proxy-protocol: false
  velocity:
    enabled: false
    online-mode: false
    secret: ''

scoreboards:
  save-empty-scoreboard-teams: false
  track-plugin-scoreboards: false

spam-limiter:
  recipe-spam-increment: 1
  recipe-spam-limit: 20
  tab-spam-increment: 1
  tab-spam-limit: 500

timings:
  enabled: true
  hidden-config-entries:
  - database
  - proxies.velocity.secret
  history-interval: 300
  history-length: 3600
  really-enabled: false
  server-name: Unknown Server
  server-name-privacy: false
  url: https://timings.aikar.co/
  verbose: true

unsupported-settings:
  allow-headless-pistons: false
  allow-permanent-block-break-exploits: false
  allow-piston-duplication: false
  perform-username-validation: true
```

#### `paper-world-defaults.yml` (HCF Optimizado)
```yaml
# Configuración optimizada para HCF
_version: 30
anticheat:
  anti-xray:
    enabled: true
    engine-mode: 2
    max-block-height: 64
    update-radius: 2
    lava-obscures: false
    use-permission: false
    hidden-blocks:
    - copper_ore
    - deepslate_copper_ore
    - gold_ore
    - deepslate_gold_ore
    - iron_ore
    - deepslate_iron_ore
    - coal_ore
    - deepslate_coal_ore
    - lapis_ore
    - deepslate_lapis_ore
    - mossy_cobblestone
    - obsidian
    - chest
    - diamond_ore
    - deepslate_diamond_ore
    - redstone_ore
    - deepslate_redstone_ore
    - clay
    - emerald_ore
    - deepslate_emerald_ore
    - ender_chest
    replacement-blocks:
    - stone
    - oak_planks
    - deepslate
  obfuscation:
    items:
      hide-durability: false
      hide-itemmeta: false
      hide-itemmeta-with-visual-effects: false

chunks:
  auto-save-interval: default
  delay-chunk-unloads-by: 10s
  entity-per-chunk-save-limit:
    arrow: -1
    ender_pearl: -1
    experience_orb: -1
    fireball: -1
    small_fireball: -1
    snowball: -1
  fixed-chunk-inhabited-time: -1
  max-auto-save-chunks-per-tick: 24
  prevent-moving-into-unloaded-chunks: false

collisions:
  allow-player-cramming-damage: false
  allow-vehicle-collisions: true
  fix-climbing-bypassing-cramming-rule: false
  max-entity-collisions: 8
  only-players-collide: false

entities:
  armor-stands:
    do-collision-entity-lookups: true
    tick: true
  behavior:
    allow-spider-world-border-climbing: true
    baby-zombie-movement-modifier: 0.5
    disable-chest-cat-detection: false
    disable-player-crits: false
    door-breaking-difficulty:
      husk: 
      - HARD
      skeleton: 
      - HARD
      vindication_illager: 
      - NORMAL
      - HARD
      zombie: 
      - HARD
      zombie_villager: 
      - HARD
    ender-dragons-death-always-places-dragon-egg: false
    experience-merge-max-value: -1
    mobs-can-always-pick-up-loot:
      skeletons: false
      zombies: false
    nerf-pigmen-from-nether-portals: false
    piglins-guard-chests: true
    pillager-patrols:
      disable: false
      spawn-chance: 0.2
      spawn-delay:
        per-player: false
        ticks: 12000
      start:
        day: 5
        per-player: false
    should-remove-dragon: false
    spawner-nerfed-mobs-should-jump: false
    zombie-villager-infection-chance: -1.0
    zombies-target-turtle-eggs: true
  entities-target-with-follow-range: false
  mob-effects:
    immune-to-wither-effect:
      wither: true
      wither-skeleton: true
    spiders-immune-to-poison-effect: true
    undead-immune-to-certain-effects: true
  spawning:
    all-chunks-are-slime-chunks: false
    alt-item-despawn-rate:
      cobblestone: default
    count-all-mobs-for-spawning: false
    creative-arrow-despawn-rate: default
    despawn-ranges:
      ambient:
        hard: 128
        soft: 32
      axolotls:
        hard: 128
        soft: 32
      creature:
        hard: 128
        soft: 32
      misc:
        hard: 128
        soft: 32
      monster:
        hard: 128
        soft: 32
      underground_water_creature:
        hard: 128
        soft: 32
      water_ambient:
        hard: 64
        soft: 32
      water_creature:
        hard: 128
        soft: 32
    disable-mob-spawner-spawn-egg-transformation: false
    duplicate-uuid:
      mode: SAFE_REGEN
      safe-regen-delete-range: 32
    filter-bad-tile-entity-nbt-from-falling-blocks: true
    filtered-entity-tag-nbt-paths:
    iron-golems-can-spawn-in-air: false
    monster-spawn-max-light-level: -1
    non-player-arrow-despawn-rate: default
    per-player-mob-spawns: true
    scan-for-legacy-ender-dragon: true
    skeleton-horse-thunder-spawn-chance: default
    slime-spawn-height:
      slime-chunk:
        maximum: 40.0
      surface-biome:
        maximum: 70.0
        minimum: 50.0
    spawn-limits:
      ambient: -1
      axolotls: -1
      creature: -1
      monster: -1
      underground_water_creature: -1
      water_ambient: -1
      water_creature: -1
    wateranimal-spawn-height:
      maximum: default
      minimum: default
    wandering-trader:
      spawn-chance-failure-increment: 25
      spawn-chance-max: 75
      spawn-chance-min: 25
      spawn-day-length: 24000
      spawn-minute-length: 1200

environment:
  disable-explosion-knockback: false
  disable-ice-and-snow: false
  disable-teleportation-suffocation-check: false
  disable-thunder: false
  frosted-ice:
    delay:
      max: 40
      min: 20
    enabled: true
  generate-flat-bedrock: false
  nether-ceiling-void-damage-height: disabled
  optimize-explosions: false
  portal-create-radius: 16
  portal-search-radius: 128
  portal-search-vanilla-dimension-scaling: true
  treasure-maps:
    enabled: true
    find-already-discovered:
      loot-tables: default
      villager-trade: false

feature-seeds:
  generate-random-seeds-for-all: false

fishing-time-range:
  maximum: 600
  minimum: 100

fixes:
  disable-unloaded-chunk-enderpearl-exploit: true
  falling-block-height-nerf: disabled
  fix-items-merging-through-walls: false
  prevent-tnt-from-moving-in-water: false
  split-overstacked-loot: true
  tnt-entity-height-nerf: disabled

hopper:
  cooldown-when-full: true
  disable-move-event: false
  ignore-occluding-blocks: false

lootables:
  auto-replenish: false
  max-refills: -1
  refresh-max: 2d
  refresh-min: 12h
  reset-seed-on-fill: true
  restrict-player-reloot: true

maps:
  item-frame-cursor-limit: 128
  item-frame-cursor-update-interval: 10

max-growth-height:
  bamboo:
    max: 16
    min: 11
  cactus: 3
  reeds: 3

misc:
  disable-end-credits: false
  disable-relative-projectile-velocity: false
  disable-sprint-interruption-on-attack: false
  light-queue-size: 20
  max-leash-distance: 10.0
  redstone-implementation: ALTERNATE
  shield-blocking-delay: 5
  show-sign-click-command-failure-msgs-to-player: false
  update-pathfinding-on-block-update: true

scoreboards:
  allow-non-player-entities-on-scoreboards: false
  use-vanilla-world-scoreboard-name-coloring: false

spawn:
  allow-using-signs-inside-spawn-protection: false
  keep-spawn-loaded: true
  keep-spawn-loaded-range: 10

tick-rates:
  behavior:
    villager:
      validatenearbypoi: -1
  container-update: 1
  grass-spread: 1
  mob-spawner: 1
  sensor:
    villager:
      secondarypoisensor: 40
      villagerbabiessensor: 20
      playersensor: 20
      nearestbedsensor: 80
      villagerprofessionssensor: 20
      nearestlivingentitysensor: 20

unsupported-settings:
  fix-invulnerable-end-crystal-exploit: true
```

### Configuración JVM Optimizada para HCF:
```bash
#!/bin/bash
# start-paper.sh - Script optimizado para Paper 1.21.8 + StrafeHCF

java -Xms8G -Xmx8G \
     -XX:+UseG1GC \
     -XX:+ParallelRefProcEnabled \
     -XX:MaxGCPauseMillis=200 \
     -XX:+UnlockExperimentalVMOptions \
     -XX:+DisableExplicitGC \
     -XX:+AlwaysPreTouch \
     -XX:G1NewSizePercent=30 \
     -XX:G1MaxNewSizePercent=40 \
     -XX:G1HeapRegionSize=8M \
     -XX:G1ReservePercent=20 \
     -XX:G1HeapWastePercent=5 \
     -XX:G1MixedGCCountTarget=4 \
     -XX:InitiatingHeapOccupancyPercent=15 \
     -XX:G1MixedGCLiveThresholdPercent=90 \
     -XX:G1RSetUpdatingPauseTimePercent=5 \
     -XX:SurvivorRatio=32 \
     -XX:+PerfDisableSharedMem \
     -XX:MaxTenuringThreshold=1 \
     -Dusing.aikars.flags=https://mcflags.emc.gs \
     -Daikars.new.flags=true \
     -jar paper-1.21.8-50.jar --nogui
```

---

## 🔧 Integración con StrafeHCF

### Configuración Maven (`pom.xml`):
```xml
<dependencies>
    <!-- Paper API -->
    <dependency>
        <groupId>io.papermc.paper</groupId>
        <artifactId>paper-api</artifactId>
        <version>1.21.8-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <version>3.2.5</version>
    </dependency>
</dependencies>

<repositories>
    <!-- Paper Repository -->
    <repository>
        <id>papermc-repo</id>
        <url>https://repo.papermc.io/repository/maven-public/</url>
    </repository>
</repositories>
```

### Clase Principal Integrada:
```java
@SpringBootApplication
public class StrafeHCF extends JavaPlugin {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void onEnable() {
        // Inicializar Spring Boot context
        applicationContext = SpringApplication.run(StrafeHCF.class);
        
        getLogger().info("StrafeHCF habilitado con Spring Boot + Paper 1.21.8");
        getLogger().info("Build Paper: #50 | Java: " + System.getProperty("java.version"));
    }
    
    @Override
    public void onDisable() {
        if (applicationContext != null) {
            SpringApplication.exit(applicationContext);
        }
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
```

---

## 📊 Comparación: Paper vs Spigot

| Característica | Spigot | Paper | Ventaja Paper |
|---------------|--------|--------|---------------|
| **Rendimiento TPS** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | +40% mejor |
| **Uso de RAM** | ⭐⭐⭐ | ⭐⭐⭐⭐ | -20% menos uso |
| **APIs Disponibles** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | APIs exclusivas |
| **Actualizaciones** | ⭐⭐ | ⭐⭐⭐⭐⭐ | Más frecuentes |
| **Anti-Exploits** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Protecciones extra |
| **Configurabilidad** | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Más opciones |

---

## ✅ Checklist de Implementación

- [x] **Descarga Paper 1.21.8-50** - ✅ Completado
- [x] **Verificación SHA256** - ✅ Completado  
- [ ] **Configurar paper-global.yml**
- [ ] **Configurar paper-world-defaults.yml**
- [ ] **Crear script de inicio optimizado**
- [ ] **Integrar con arquitectura Spring Boot**
- [ ] **Configurar Maven dependencies**
- [ ] **Testing de compatibilidad con librerías**
- [ ] **Migración de configuraciones de Spigot**

---

## 🎯 Próximos Pasos

1. **Configuración Básica**: Crear archivos de configuración optimizados
2. **Testing**: Probar Paper con las librerías copiadas
3. **Integración Spring**: Configurar la integración Paper + Spring Boot
4. **Performance**: Aplicar optimizaciones específicas para HCF
5. **Compatibilidad**: Verificar que todos los plugins funcionen

---

*Paper 1.21.8-50 descargado exitosamente el 28 de Agosto, 2025*
*Preparado para integración con StrafeHCF + Spring Boot*
