# RedLocal 🚀 - Conectando Comunidad y Trabajo

**RedLocal** es una plataforma móvil diseñada para dinamizar la economía local. Permite a los vecinos publicar necesidades inmediatas (limpieza, reparaciones, servicios) y a los técnicos profesionales encontrar oportunidades y gestionar su agenda en un solo lugar.

## 📊 Estado Actual del Proyecto (Lo que llevamos)

- [x] **Arquitectura Base**: Implementación de navegación adaptativa con Material 3.
- [x] **Diseño de Interfaz (UI)**: Paleta de colores "Trust & Clean" y tarjetas de alta fidelidad.
- [x] **Gestión de Roles**: Selector de modo "Cliente" y modo "Técnico".
- [x] **Filtros Dinámicos**: Sistema de filtrado por categorías (Gasfitería, Limpieza, etc.).
- [x] **Autenticación (Auth)**: Integración de **Firebase Auth** (Login y Registro).
- [x] **Manejo de Estado**: Publicación de ofertas en tiempo real (en memoria local).

## 🗺️ Roadmap: Lo que falta (Próximos Pasos)

### Fase 1: Persistencia de Datos (En curso 🛠️)
- **Firebase Firestore**: Migrar la lista de ofertas de la memoria local a una base de datos en la nube para que sean permanentes.
- **Perfiles de Técnicos**: Crear perfiles editables donde el técnico pueda subir sus certificados y fotos de trabajos previos.

### Fase 2: Funcionalidad de Negocio
- **Sistema de Mensajería**: Chat interno entre cliente y técnico para coordinar detalles.
- **Agenda Real**: Calendario interactivo donde el técnico define su disponibilidad y el cliente reserva.
- **Geolocalización**: Ver ofertas y técnicos en un mapa según la cercanía GPS.

### Fase 3: Monetización y Pagos (Pasarela de Pago 💳)
- **Integración con Stripe o Mercado Pago**: Implementar el pago seguro del servicio a través de la app.
- **Sistema de Escrow**: Retener el pago hasta que el cliente confirme que el trabajo fue terminado satisfactoriamente.
- **Suscripciones Pro**: Funciones extra para técnicos (destacar perfil, notificaciones prioritarias).

### Fase 4: Reputación y Confianza
- **Reviews & Ratings**: Sistema de estrellas y comentarios reales post-servicio.
- **Verificación de Identidad**: Validación de técnicos con documentos de identidad (KYC).

## 🚀 Configuración para Desarrolladores

1. Clona el repositorio.
2. **Firebase Setup**: 
   - Crea un proyecto en Firebase.
   - Habilita `Authentication` (Email/Password).
   - Descarga `google-services.json` y ponlo en `app/`.
3. Sincroniza Gradle y corre la app.

---
Desarrollado con ❤️ para fortalecer las comunidades locales.
