# MÓDULO 4 – Conocimientos en versionamiento

## Escenario

Estás trabajando en una rama `feature/new-login`. Mientras tanto, un compañero ha fusionado un cambio crítico en la rama `main` que soluciona un bug de seguridad. Necesitas incorporar urgentemente ese cambio específico de main en tu rama `feature/new-login` sin traer el resto de las actualizaciones de main.

## Pregunta

¿Qué comando o estrategia de Git usarías y por qué?

---

## Respuesta

**Comando que usaría:**

```bash
git checkout feature/new-login
git cherry-pick <hash-del-commit-de-seguridad>
```

**Por qué:**

`git cherry-pick` aplica un commit individual de otra rama sobre la rama actual. A diferencia de `git merge` o `git rebase`, no trae todo el historial de `main`, solo los cambios de ese commit específico. Esto permite incorporar urgentemente el parche de seguridad sin mezclar características no probadas ni cambios incompletos de `main`.

**Pasos para implementarlo:**

1. Posicionarse en la rama de trabajo:
```bash
git checkout feature/new-login
```

2. Aplicar el commit específico de seguridad:
```bash
git cherry-pick <hash-del-commit>
```

3. Si hay conflictos, resolverlos y continuar:
```bash
git cherry-pick --continue
```