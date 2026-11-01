# MÓDULO 5 – Evaluación de Liderazgo Técnico y Gestión

## Escenario

Tu equipo tiene 8 desarrolladores. Hay:

- 40% deuda técnica en servicios claves
- 10 incidentes críticos en el último mes
- Presión del negocio para entregar una funcionalidad en 3 semanas
- Falta de estándares y práctica irregular de code review
- Dos desarrolladores junior con brechas técnicas importantes

---

## Preguntas y Respuestas

### 1. ¿Cuáles serían tus 5 prioridades en las primeras 2 semanas?

1. **Establecer code review obligatorio** en todos los pull requests (ningún merge a main sin aprobación de al menos un par).
2. **Contener incidentes críticos:** hacer post-mortem de los 10 incidentes del mes, identificar causas raíz y aplicar parches inmediatos.
3. **Reducir deuda técnica en los servicios clave:** priorizar los 3 servicios más críticos y asignar tiempo sprint para refactorizar.
4. **Nivelar a los juniors:** pair programming con seniors, mentoría asignada y plan de capacitación enfocado en sus brechas técnicas.
5. **Alinear alcance con el negocio:** negociar un MVP para las 3 semanas, identificando qué es realmente indispensable y qué puede posponerse.

### 2. ¿Cómo organizarías al equipo para mejorar velocidad y calidad?

- Dividir al equipo en 2-3 sub-equipos pequeños (2-3 personas cada uno), cada uno dueño de uno o más servicios clave.
- Implementar daily standups de 15 minutos enfocados en bloqueos.
- Definir una definición de "Done" clara (código revisado, pruebas unitarias, documentación básica).
- Usar sprints de 1 semana para tener retroalimentación rápida y poder ajustar prioridades.
- Rotar responsabilidades de code review para que todos conozcan la base de código.

### 3. ¿Qué métricas implementarías para evaluar el desempeño del área?

- **MTTR (Mean Time To Recovery):** tiempo promedio para recuperar servicio tras un incidente.
- **Tasa de cambio fallido:** % de despliegues que generan incidentes en producción.
- **Tiempo de ciclo (lead time):** desde que se inicia una tarea hasta que está en producción.
- **Cobertura de pruebas:** % de código cubierto por pruebas unitarias.
- **Densidad de defectos:** bugs reportados por mil líneas de código o por funcionalidad entregada.
- **Deuda técnica por servicio:** porcentaje de código marcado como deuda técnica en cada servicio.

### 4. ¿Qué prácticas técnicas establecerías como obligatorias?

- Code review en el 100% de los pull requests antes de mergear a main.
- Pruebas unitarias obligatorias para toda funcionalidad nueva (cobertura mínima del 80% en servicios críticos).
- Convenciones de commits (ej. Conventional Commits: feat, fix, docs, refactor, test).
- CI/CD pipeline que ejecute pruebas y análisis estático en cada push.
- Documentación de decisiones técnicas (ADR) para cambios arquitectónicos importantes.
- No hacer merge directo a main; siempre a través de pull request.

### 5. ¿Cómo gestionarías la presión del negocio sin comprometer la calidad?

- **Negociar alcance:** presentar al negocio un MVP realista para las 3 semanas y explicar qué se puede entregar con calidad y qué requiere más tiempo.
- **Comunicar riesgos temprano:** no esperar a la última semana para advertir que algo no llegará.
- **Entregas incrementales:** entregar valor en pequeñas entregas funcionales en lugar de un "big bang" al final.
- **Proteger al equipo:** limitar interrupciones y cambios de alcance durante el sprint.
- **No sacrificar pruebas ni code review:** estos son la red de seguridad que permite moverse rápido sin romper producción.