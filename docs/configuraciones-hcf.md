# Configuraciones HCF - StrafeHCF

## Introducción

Este documento detalla todas las configuraciones específicas del gamemode HCF para **StrafeHCF**, basadas en el análisis de GelicHCF pero optimizadas para una experiencia moderna y balanceada.

## Configuración Principal

### 🏗️ application.yml

```yaml
# StrafeHCF Main Configuration
strafehcf:
  version: "1.0.0"
  minecraft-version: "1.21.8"
  
  # Server Configuration
  server:
    max-players: 500
    tps-target: 19.5
    memory-limit: "2GB"
    
  # Database Configuration
  database:
    type: "postgresql"
    host: "localhost"
    port: 5432
    name: "strafehcf"
    username: "${DB_USERNAME:strafehcf}"
    password: "${DB_PASSWORD:changeme}"
    
  # Cache Configuration  
  cache:
    type: "redis"
    host: "localhost"
    port: 6379
    ttl: 3600 # 1 hour default
    
  # Feature Flags
  features:
    teams: true
    deathban: true
    combat: true
    claims: true
    koth: true
    citadel: true
    classes: true
    stats: true
    
  # Game Mode
  gamemode:
    kitmap: false # Pure HCF mode
    hardcore: true
    difficulty: "HARD"
```

## Configuración de Equipos (Teams)

### 👥 teams.yml

```yaml
teams:
  # Basic Settings
  max-size: 15
  max-allies: 0 # No alliances
  max-claims: 1
  min-name-length: 3
  max-name-length: 12
  
  # DTR (Deaths Till Raidable) Configuration
  dtr:
    # DTR per member count
    per-member: 1.0
    bonus: 0.01 # Small bonus to avoid exact 0
    max-dtr: 7.2 # Cap for large teams
    
    # DTR Loss on Death
    loss:
      normal: 1.0
      nether: 1.0
      end: 1.0
      event: 1.0
      
    # DTR Regeneration
    regen:
      amount: 0.1 # DTR gained per interval
      interval: 30 # seconds between regen
      freeze-time: 2700 # 45 minutes freeze after death
      
    # Display Settings
    format: "0.00" # Show 2 decimal places
    low-dtr-threshold: 1.0
    
  # Team Colors by Status
  colors:
    normal: "&a" # Green
    low-dtr: "&e" # Yellow  
    raidable: "&c" # Red
    regenerating: "&a" # Green
    frozen: "&c" # Red
    
  # Team Symbols
  symbols:
    normal: "◀"
    regenerating: "▲"
    frozen: "■"
    
  # Claiming Configuration  
  claiming:
    min-size: 10 # 10x10 minimum
    max-size: 50 # 50x50 maximum  
    buffer-distance: 5 # blocks between claims
    cost-per-block: 0.5 # economy cost
    
    # Claiming Tool
    wand:
      material: "GOLDEN_HOE"
      name: "&6&lClaiming Tool"
      lore:
        - "&eLeft/Right Click: Select corners"
        - "&eShift + Left Click: Purchase claim"
        - "&eRight Click Air: Cancel selection"
        
  # Warzone Settings
  warzone:
    overworld: 1000 # blocks from spawn
    nether: 500
    end: 200
    break-limit: # Additional buffer for mining
      overworld: 200
      nether: 100
      
  # Team Chat
  chat:
    team:
      enabled: true
      prefix: "&2[Team] "
      shortcut: "@" # @message for team chat
    ally:
      enabled: false # No allies in pure HCF
      
  # Relation Colors (Nametags)
  relations:
    teammate: "&2" # Dark Green
    enemy: "&c" # Red
    neutral: "&f" # White
    
  # Special Prefixes  
  prefixes:
    leader: "&4[L] "
    officer: "&6[O] "
    member: ""
    staff: "&b[S] "
```

## Sistema de Combate

### ⚔️ combat.yml

```yaml
combat:
  # Combat Tag Settings
  combat-tag:
    duration: 45 # seconds
    logout-allowed: false
    commands-blocked:
      - "/spawn"
      - "/home"  
      - "/tpa"
      - "/back"
    entry-denied:
      - "spawn"
      - "safezone"
      
  # PvP Protection (New Players)
  pvp-protection:
    duration: 1800 # 30 minutes
    enabled: true
    remove-on-attack: true
    commands-allowed:
      - "/team"
      - "/help"
      - "/list"
      
  # Item Cooldowns
  cooldowns:
    enderpearl: 16 # seconds
    golden-apple: 45 # seconds  
    enchanted-golden-apple: 10800 # 3 hours
    fishing-rod: 3 # seconds (rod timer)
    
  # Potion Effects
  potions:
    strength-nerf:
      enabled: true
      level-1-multiplier: 0.65
      level-2-multiplier: 0.50
      level-3-multiplier: 0.40
      
    # Milk Bucket Behavior
    milk:
      remove-negative-only: true
      
  # Combat Restrictions
  restrictions:
    bow-boosting: false
    bed-placement: false
    enderpearl-into-spawn: false
    enderpearl-in-spawn: false
    ender-eye-throwing: true
    
  # Logout System
  logout:
    combat-log-duration: 10 # seconds NPC stays
    safe-logout-time: 5 # seconds to safely logout
    
  # Death Configuration
  death:
    ban-duration: 2700 # 45 minutes default
    ban-reductions: # by rank
      vip: 2100 # 35 minutes
      vip-plus: 1800 # 30 minutes
      premium: 1500 # 25 minutes
      elite: 1200 # 20 minutes
    lives-system: true
    arena-enabled: true
    kill-reduction: 300 # 5 minutes off ban per arena kill
    
  # Portal Restrictions
  portals:
    nether-ban-on-death: false # -1 = disabled
    end-ban-on-death: false # -1 = disabled
```

## Clases de PvP

### 🎭 classes.yml

```yaml
classes:
  # Diamond Class (Default)
  diamond:
    enabled: true
    name: "&bDiamond"
    armor-type: "DIAMOND"
    description: "Standard combat class with no special abilities"
    
  # Archer Class  
  archer:
    enabled: true
    name: "&2Archer"
    armor-type: "LEATHER" # Leather only
    description: "Bow specialist with increased damage"
    
    # Abilities
    abilities:
      bow-damage-multiplier: 1.25
      speed-boost: 1 # Speed I when holding bow
      tag-duration: 10 # seconds of archer tag
      
    # Restrictions
    restrictions:
      sword-damage-reduction: 0.25 # 25% less sword damage
      
  # Bard Class
  bard:
    enabled: true  
    name: "&6Bard"
    armor-type: "GOLD" # Gold only
    description: "Support class providing team buffs"
    
    # Buff Configuration
    buffs:
      strength:
        duration: 6
        amplifier: 0
        energy-cost: 45
        item: "BLAZE_POWDER"
        
      resistance:
        duration: 5  
        amplifier: 0
        energy-cost: 40
        item: "IRON_INGOT"
        
      regeneration:
        duration: 5
        amplifier: 0  
        energy-cost: 35
        item: "GHAST_TEAR"
        
      speed:
        duration: 8
        amplifier: 1
        energy-cost: 30
        item: "FEATHER"
        
    # Energy System
    energy:
      max: 100
      regen-rate: 1 # per second
      regen-delay: 10 # seconds after buff use
      
    # Restrictions  
    restrictions:
      cannot-use-swords: true
      cannot-place-blocks: false
```

## Eventos

### 👑 events.yml

```yaml
events:
  # KOTH (King of the Hill)
  koth:
    enabled: true
    max-simultaneous: 1
    
    # Capture Settings
    capture-time: 900 # 15 minutes
    contest-radius: 15 # blocks
    capture-messages:
      frequency: 20 # seconds between messages
      
    # Requirements
    requirements:
      min-team-size: 3
      no-allies-allowed: true
      
    # Rewards
    rewards:
      - "give %player% diamond 32"
      - "give %player% golden_apple 16"
      - "eco give %player% 1000"
      
  # Citadel (Weekly Event)
  citadel:
    enabled: true
    
    # Timing
    capture-time: 1800 # 30 minutes
    loot-respawn-time: 7200 # 2 hours
    
    # Loot Configuration
    loot:
      min-items: 3
      max-items: 6
      
      # Loot Table (Item, Quantity, Chance%)
      items:
        - "diamond_sword,1,30,SHARPNESS:2;UNBREAKING:3"
        - "diamond_helmet,1,25,PROTECTION:2;UNBREAKING:3"  
        - "diamond_chestplate,1,25,PROTECTION:2;UNBREAKING:3"
        - "diamond_leggings,1,25,PROTECTION:2;UNBREAKING:3"
        - "diamond_boots,1,25,PROTECTION:2;UNBREAKING:3"
        - "enchanted_golden_apple,2,20"
        - "golden_apple,16,40"
        - "ender_pearl,16,35"
        - "diamond_block,8,30"
        - "emerald_block,4,20"
        
    # Requirements
    requirements:
      min-team-size: 5
      min-teams: 3 # at least 3 teams must be online
      
  # EOTW (End of the World)
  eotw:
    enabled: true
    
    # Timing
    pre-eotw-duration: 2700 # 45 minutes warning
    
    # Effects during EOTW
    effects:
      all-teams-raidable: true
      no-new-players: true
      dtr-freeze: true
      
    # Final KOTH
    final-koth:
      name: "EOTW"
      capture-time: 1800 # 30 minutes
      
  # SOTW (Start of the World)  
  sotw:
    enabled: true
    
    # Grace Period
    grace-period: 7200 # 2 hours
    
    # Effects during SOTW
    effects:
      flying-enabled: false # No flying even for ranks
      pvp-disabled: false # PvP still enabled
      claiming-enabled: true
      
    # Starter Kit
    starter-kit:
      enabled: true
      items:
        - "stone_sword,1"
        - "leather_helmet,1"  
        - "leather_chestplate,1"
        - "leather_leggings,1"
        - "leather_boots,1"
        - "cooked_beef,16"
        - "water_bucket,2"
```

## Estadísticas y Rankings

### 📊 stats.yml

```yaml
stats:
  # Tracking Configuration
  tracking:
    kills: true
    deaths: true  
    killstreaks: true
    playtime: true
    
  # Kill Requirements
  kill-requirements:
    sword-only: true # Only sword kills count
    min-distance: 5 # blocks from spawn
    
  # Display Settings
  display:
    decimal-places: 2
    show-rank-prefixes: true
    
  # Leaderboards
  leaderboards:
    update-frequency: 300 # 5 minutes
    max-entries: 50
    
    # Individual Stats  
    individual:
      kills: true
      deaths: false # Don't show death leaderboard
      kdr: true
      killstreak: true
      
    # Team Stats (F-Top)
    team:
      points: true # Based on team performance
      members: true
      koth-caps: true
      
  # Point System
  points:
    kill: 1
    koth-capture: 50  
    citadel-capture: 100
    death-penalty: -1
    
  # Killstreak Configuration
  killstreaks:
    announcements:
      5: "&6%player% is on a 5 killstreak!"
      10: "&c%player% is on a 10 killstreak!"
      15: "&4%player% is DOMINATING with 15 kills!"
    
    # Killstreak Rewards
    rewards:
      5:
        - "give %player% golden_apple 2"
      10:  
        - "give %player% enchanted_golden_apple 1"
      15:
        - "broadcast &4%player% achieved a 15 killstreak!"
```

## Interfaz de Usuario

### 🖥️ ui.yml

```yaml
ui:
  # Scoreboard Configuration
  scoreboard:
    enabled: true
    title: "&c&lStrafeHCF"
    update-frequency: 20 # ticks (1 second)
    
    # Lines (supports placeholders)
    lines:
      - "&7&m--------------------"
      - "&bOnline: &f%online%/%max%"
      - ""
      - "%team_info%" # Dynamic team info
      - "%dtr_info%" # DTR information
      - ""
      - "%timers%" # Active timers
      - ""
      - "&bKills: &f%kills%"
      - "&bDeaths: &f%deaths%"  
      - "&bKDR: &f%kdr%"
      - ""
      - "&ewww.strafepvp.net"
      - "&7&m--------------------"
      
  # Nametags
  nametags:
    enabled: true
    distance: 100 # blocks
    
    # Format: {prefix}{color}{name}{suffix}
    format: "%prefix%%color%%name%%suffix%"
    
    # Health Display
    health:
      enabled: true
      format: " &c❤ %health%"
      
  # Tablist  
  tablist:
    enabled: true
    
    # Header/Footer
    header:
      - "&c&lStrafeHCF"
      - "&7Play.StrafePVP.net"
      
    footer:
      - "&bOnline: &f%online%"
      - "&bTPS: &f%tps%"
      
    # Player Format
    format: "%prefix%%color%%name%%suffix%"
    
  # Action Bar
  actionbar:
    enabled: true
    # Used for claiming feedback, timer info, etc.
    
  # Boss Bar  
    bossbar:
    enabled: true
    # Used for important timers (KOTH, Combat Tag, etc.)
```

## Economía y Kits

### 💰 economy.yml

```yaml
economy:
  # Basic Settings
  enabled: true
  currency-symbol: "$"
  starting-balance: 500
  
  # Starting Balances by Rank
  rank-bonuses:
    vip: 200
    vip-plus: 400  
    premium: 600
    elite: 1000
    
  # Claiming Costs
  claiming:
    cost-per-block: 0.50
    sell-multiplier: 0.75 # Get 75% back when unclaiming
    
  # Transaction Limits
  limits:
    max-balance: 1000000
    min-transaction: 1
    max-transaction: 50000

# Kit System
kits:
  # Starter Kit (Free)
  starter:
    permission: "" # No permission needed
    cooldown: 86400 # 24 hours
    items:
      - "stone_sword,1"
      - "leather_helmet,1"
      - "leather_chestplate,1"  
      - "leather_leggings,1"
      - "leather_boots,1"
      - "cooked_beef,16"
      - "water_bucket,1"
      
  # VIP Kit
  vip:
    permission: "strafehcf.kit.vip" 
    cooldown: 43200 # 12 hours
    items:
      - "iron_sword,1,SHARPNESS:1"
      - "iron_helmet,1,PROTECTION:1"
      - "iron_chestplate,1,PROTECTION:1"
      - "iron_leggings,1,PROTECTION:1"
      - "iron_boots,1,PROTECTION:1"
      - "golden_apple,8"
      - "ender_pearl,4"
      
  # Deathban Arena Kit
  deathban:
    permission: "" # Available in deathban arena
    cooldown: 0 # No cooldown in arena
    items:
      - "diamond_sword,1,SHARPNESS:1"
      - "diamond_helmet,1,PROTECTION:1"
      - "diamond_chestplate,1,PROTECTION:1"
      - "diamond_leggings,1,PROTECTION:1"  
      - "diamond_boots,1,PROTECTION:1"
      - "golden_apple,16"
      - "ender_pearl,16"
      - "cooked_beef,64"
```

## Herramientas de Staff

### 🔧 staff.yml

```yaml
staff:
  # Staff Mode
  staff-mode:
    auto-enable: true # Enable on join for staff
    vanish-on-enable: true
    
    # Items in Staff Mode
    items:
      compass:
        slot: 0
        name: "&bTeleport Tool"
        material: "COMPASS"
        action: "teleport"
        
      inspection:
        slot: 1  
        name: "&bInspect Player"
        material: "BOOK"
        action: "inspect"
        
      freeze:
        slot: 2
        name: "&bFreeze Player"  
        material: "ICE"
        action: "freeze"
        
      vanish-toggle:
        slot: 8
        name: "&bToggle Vanish"
        material: "INK_SAC"
        action: "vanish"
        
  # Moderation Tools
  moderation:
    # Freeze System
    freeze:
      message-interval: 5 # seconds
      kick-on-logout: true
      broadcast-staff: true
      
      messages:
        - "&c&lYou have been FROZEN!"
        - "&cDo not log out or you will be banned."
        - "&cJoin our Discord for assistance."
        
    # Inspection GUI
    inspection:
      title: "&bInspecting: %player%"
      include-effects: true
      include-stats: true
      
  # Reports System  
  reports:
    enabled: true
    cooldown: 300 # 5 minutes between reports
    
    # Discord Integration
    discord:
      enabled: true
      webhook-url: "${DISCORD_WEBHOOK_URL}"
      
  # Restore System
  restore:
    max-backups: 5 # per player
    backup-on-death: true
    backup-interval: 3600 # hourly automatic backups
```

## Configuración de Mundo

### 🌍 world.yml

```yaml
world:
  # World Borders
  borders:
    overworld: 4000 # 4000x4000
    nether: 2000 # 2000x2000  
    end: 1000 # 1000x1000
    
  # Spawn Settings
  spawn:
    protection-radius: 100
    build-permission: "strafehcf.spawn.build"
    
  # Game Rules
  gamerules:
    do-daylight-cycle: false # Always day
    do-weather-cycle: false # No rain
    do-mob-spawning: true
    keep-inventory: false
    
  # Block Settings  
  blocks:
    disable-beds: true
    disable-tnt: false # TNT enabled for raiding
    
    # Auto-smelt on mine
    auto-smelt:
      enabled: false
      permission: "strafehcf.autosmelt"
      
  # Mob Configuration
  mobs:
    natural-spawn: true
    spawn-in-warzone: true
    endermen-hostile: true
    
    # Mob Stacking (Performance)
    stacking:
      enabled: true
      max-stack: 10
      radius: 8
      
  # Performance Settings
  performance:
    chunk-gc-period: 600 # seconds
    entity-limit-per-chunk: 50
    tile-entity-limit-per-chunk: 20
```

---

*Configuraciones definidas: 28 de Agosto 2025*
*Versión objetivo: Minecraft 1.21.8*  
*Framework: Spring Boot + Spigot API*
*Total archivos de configuración: 8*
