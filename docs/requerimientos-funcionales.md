# Requerimientos Funcionales - StrafeHCF

## Visión del Producto

**StrafeHCF** será un plugin HCF moderno y optimizado para Minecraft 1.21.8, diseñado específicamente para el servidor **StrafePVP**. El objetivo es crear una experiencia HCF premium con arquitectura limpia, rendimiento superior y funcionalidades bien balanceadas.

## Requerimientos Core (P0) - MVP

### 🏰 RF-001: Sistema de Facciones/Teams

**Descripción:** Sistema fundamental para crear y gestionar equipos de jugadores.

**Funcionalidades:**
- ✅ Creación/disolución de equipos
- ✅ Gestión de miembros (invite/kick/promote)
- ✅ Sistema de roles (Leader, Co-Leader, Officer, Member)
- ✅ Límite configurable de miembros (default: 15)
- ✅ Sistema de alianzas básico
- ✅ Chat interno del equipo

**Criterios de Aceptación:**
```gherkin
Given un jugador sin equipo
When ejecuta /team create <nombre>
Then se crea un equipo con el jugador como líder

Given un líder de equipo  
When ejecuta /team invite <jugador>
Then el jugador recibe invitación para unirse
```

**Prioridad:** P0 (Crítico)
**Estimación:** 40 horas desarrollo

---

### ⚰️ RF-002: Sistema de Deathban

**Descripción:** Sistema de muerte temporal que banea jugadores por tiempo limitado al morir.

**Funcionalidades:**
- ✅ Deathban automático al morir (45min default)
- ✅ Tiempos reducidos por rango/donación
- ✅ Sistema de vidas para revivir instantáneamente
- ✅ Arena de deathban con PvP especial
- ✅ Información de muerte (killer, location, items)
- ✅ Kit especial para arena de deathban

**Configuración:**
```yaml
deathban:
  default_time: 45m
  rank_reductions:
    vip: 35m
    vip_plus: 30m
    elite: 25m
  arena:
    enabled: true
    kill_reduction: 5m
```

**Prioridad:** P0 (Crítico)
**Estimación:** 25 horas desarrollo

---

### 🗡️ RF-003: Sistema de Combate HCF

**Descripción:** Mecánicas de combate específicas del gamemode HCF.

**Funcionalidades:**
- ✅ Combat Tag (45s sin logout después de combate)
- ✅ Cooldowns de items (Golden Apple, Enderpearl)
- ✅ Restricciones de logout en combate
- ✅ Sistema de strength nerf
- ✅ Protección para nuevos jugadores (PvP Timer)

**Combat Timers:**
```yaml
combat_timers:
  combat_tag: 45s
  enderpearl: 16s
  golden_apple: 45s
  enchanted_golden_apple: 3h
  pvp_protection: 30m
```

**Prioridad:** P0 (Crítico)
**Estimación:** 30 horas desarrollo

---

### 🏛️ RF-004: Sistema de Claims

**Descripción:** Sistema territorial para que los equipos reclamen y protejan territorio.

**Funcionalidades:**
- ✅ Claiming tool (Golden Hoe)
- ✅ Protección de bloques/cofres en claims
- ✅ Sistema de costs por bloque
- ✅ Límite de claims por equipo
- ✅ Overclaiming cuando equipo es raideable
- ✅ Visualización de borders

**Configuración:**
```yaml
claiming:
  max_claims: 1
  min_size: 10x10
  max_size: 50x50
  cost_per_block: 0.5
  buffer_distance: 5
```

**Prioridad:** P0 (Crítico)
**Estimación:** 35 horas desarrollo

---

### 📊 RF-005: Sistema DTR (Deaths Till Raidable)

**Descripción:** Sistema que determina cuándo un equipo puede ser raideado basado en muertes.

**Funcionalidades:**
- ✅ DTR basado en número de miembros
- ✅ Pérdida de DTR por muerte
- ✅ Regeneración automática de DTR
- ✅ Estado "Raideable" cuando DTR ≤ 0
- ✅ Freeze de DTR después de muerte

**Fórmula DTR:**
```
DTR = (Miembros * 1.0) + 0.01
Max DTR = 7.2 (para equipos grandes)
DTR Loss = 1.0 por muerte
Regen = 0.1 cada 30 segundos (después de 45min freeze)
```

**Prioridad:** P0 (Crítico)
**Estimación:** 20 horas desarrollo

---

### 👑 RF-006: Sistema KOTH (King of the Hill)

**Descripción:** Eventos competitivos donde equipos controlan puntos específicos.

**Funcionalidades:**
- ✅ Creación/gestión de KOTHs
- ✅ Sistema de captura por tiempo
- ✅ Rewards automáticos al conquistar
- ✅ Programación de eventos
- ✅ Broadcast automático de estado

**Configuración KOTH:**
```yaml
koth:
  capture_time: 15m
  contest_radius: 10
  max_simultaneous: 1
  rewards:
    - "give %player% diamond 64"
    - "eco give %player% 1000"
```

**Prioridad:** P0 (Crítico)
**Estimación:** 30 horas desarrollo

## Requerimientos Importantes (P1)

### 🎯 RF-007: Clases de PvP

**Descripción:** Clases especializadas que modifican el gameplay.

**Clases Incluidas:**
- ✅ **Diamond** - Clase estándar sin modificaciones
- ✅ **Archer** - Bonus de daño con bow, armor de leather
- ✅ **Bard** - Buffs para el equipo, armor de gold

**Habilidades:**
```yaml
classes:
  archer:
    armor: leather_only
    bow_damage_multiplier: 1.25
    speed_bonus: 1
    
  bard:
    armor: gold_only  
    buffs:
      strength: 8s
      resistance: 6s
      regeneration: 5s
```

**Prioridad:** P1 (Importante)
**Estimación:** 25 horas desarrollo

---

### 📋 RF-008: Sistema de Estadísticas

**Descripción:** Tracking y display de estadísticas de jugadores y equipos.

**Funcionalidades:**
- ✅ Kills/Deaths individuales
- ✅ KDR calculation
- ✅ Killstreaks
- ✅ Team statistics (F-Top)
- ✅ Top rankings (kills, deaths, KDR)

**Prioridad:** P1 (Importante)
**Estimación:** 20 horas desarrollo

---

### 🔧 RF-009: Herramientas de Staff

**Descripción:** Herramientas para administración y moderación del servidor.

**Funcionalidades:**
- ✅ Staff mode con vanish
- ✅ Inventory inspection
- ✅ Player freeze
- ✅ Teleportation tools
- ✅ Random teleport

**Prioridad:** P1 (Importante)
**Estimación:** 15 horas desarrollo

---

### 🖥️ RF-010: Interface Systems

**Descripción:** Sistemas de UI para mejorar experiencia del jugador.

**Componentes:**
- ✅ **Scoreboard** - Información dinámica (DTR, timers, stats)
- ✅ **Tablist** - Lista de jugadores organizada
- ✅ **Nametags** - Colores por relación de equipo
- ✅ **Action Bar** - Información contextual

**Prioridad:** P1 (Importante)
**Estimación:** 20 horas desarrollo

## Requerimientos Deseables (P2)

### 🏛️ RF-011: Eventos Citadel

**Descripción:** Eventos semanales de gran escala con loot especial.

**Funcionalidades:**
- ✅ Loot chests configurables
- ✅ Sistema de respawn de loot
- ✅ Protección especial del área
- ✅ Rewards escalonados

**Prioridad:** P2 (Deseable)
**Estimación:** 20 horas desarrollo

---

### 💰 RF-012: Sistema Económico

**Descripción:** Economía integrada para transacciones y rewards.

**Funcionalidades:**
- ✅ Balance per player
- ✅ Costs de claiming
- ✅ Rewards de eventos
- ✅ Integration con Vault

**Prioridad:** P2 (Deseable)
**Estimación:** 15 horas desarrollo

---

### 🎁 RF-013: Sistema de Kits

**Descripción:** Kits predefinidos para diferentes rangos y situaciones.

**Funcionalidades:**
- ✅ Kits por rango
- ✅ Cooldowns individuales
- ✅ Kit signs
- ✅ Kit de inicio automático

**Prioridad:** P2 (Deseable)
**Estimación:** 15 horas desarrollo

## Requerimientos Futuros (P3)

### 🏆 RF-014: Sistema de Conquest

**Descripción:** Eventos de múltiples puntos de captura.

**Prioridad:** P3 (Futuro)
**Estimación:** 25 horas desarrollo

---

### 🧙‍♂️ RF-015: Clases PvP Avanzadas

**Descripción:** Clases adicionales (Rogue, Mage, Ghost).

**Prioridad:** P3 (Futuro)
**Estimación:** 30 horas desarrollo

---

### 🔌 RF-016: Integraciones Avanzadas

**Descripción:** APIs de clientes (Lunar, CheatBreaker) y sistemas externos.

**Prioridad:** P3 (Futuro)
**Estimación:** 20 horas desarrollo

## Requerimientos No Funcionales

### 🚀 RNF-001: Performance

**Criterios:**
- Response time promedio < 50ms
- Soporte para 500+ jugadores concurrentes
- Memory usage < 2GB bajo carga normal
- TPS estable > 19.0 bajo carga

### 🔒 RNF-002: Seguridad

**Criterios:**
- Validación de todos los inputs
- Protección contra exploits comunes
- Rate limiting en comandos críticos
- Audit logging para acciones de staff

### 📚 RNF-003: Mantenibilidad

**Criterios:**
- Code coverage > 85%
- Documentación completa de APIs
- Configuración centralizada
- Logging estructurado

### ♿ RNF-004: Usabilidad

**Criterios:**
- Comandos intuitivos y consistentes
- Mensajes de error descriptivos
- Help system integrado
- Configuración user-friendly

## Matriz de Priorización

| Feature | Business Value | Technical Complexity | User Impact | Priority |
|---------|---------------|---------------------|-------------|----------|
| Teams System | 10 | 7 | 10 | P0 |
| Deathban | 9 | 5 | 9 | P0 |
| Combat System | 9 | 6 | 9 | P0 |
| Claims | 8 | 7 | 8 | P0 |
| DTR System | 8 | 4 | 8 | P0 |
| KOTH Events | 7 | 6 | 8 | P0 |
| PvP Classes | 6 | 6 | 7 | P1 |
| Statistics | 5 | 3 | 6 | P1 |
| Staff Tools | 4 | 4 | 3 | P1 |
| UI Systems | 6 | 5 | 7 | P1 |

## Estimación Total

**MVP (P0):** 180 horas ≈ 4.5 semanas (1 developer)
**Version 1.0 (P0+P1):** 275 horas ≈ 7 semanas (1 developer)
**Version 2.0 (P0+P1+P2):** 325 horas ≈ 8 semanas (1 developer)

---

*Requerimientos definidos: 28 de Agosto 2025*
*Revisión planificada: Cada sprint de 2 semanas*
*Stakeholder: Equipo StrafePVP*
