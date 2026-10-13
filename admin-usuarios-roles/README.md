# Panel de administración de usuarios y roles

Aplicación de HTML5, CSS3 y JavaScript nativo sin frameworks ni dependencias externas.

## Ejecución

1. Inicia tu backend Spring Boot en `http://localhost:8080`.
2. Abre una terminal en la carpeta del proyecto.
3. Ejecuta `python -m http.server 5500` (en Windows suele funcionar `py -m http.server 5500`).
4. Abre `http://localhost:5500` en el navegador.
5. En **Configuración API** verifica la URL base `http://localhost:8080/api/v1` y rutas `/user` y `/role`.
6. Para explorar las pantallas sin backend, activa **Modo demostración**. Los cambios de demostración no se guardan tras recargar la página.

No se recomienda abrir `index.html` mediante `file://`; sirve la carpeta por HTTP para que funcionen correctamente los módulos JavaScript.

## Contrato API *propuesto* (ajustar al backend real)

Solo se conoce la ruta base `POST http://localhost:8080/api/v1/user`, el uso de JSON y un ejemplo de respuesta de credenciales. **NO están confirmados** los endpoints de CRUD. El proyecto asume estos contratos para poder funcionar una vez implementados:

| Función | POST | Body JSON |
| --- | --- | --- |
| Listar usuarios | `/api/v1/user/list` | `{}` |
| Buscar usuario por ID | `/api/v1/user/find` | `{"id_user": 1}` |
| Crear usuario | `/api/v1/user/create` | `{"username":"ana","password":"clave","name":"Ana Pérez","role":"ADMIN","state":"ACTIVO"}` |
| Editar usuario | `/api/v1/user/update` | `{"id_user":1,"username":"ana","name":"Ana Pérez","role":"ADMIN","state":"ACTIVO"}` |
| Eliminar usuario | `/api/v1/user/delete` | `{"id_user":1}` |
| Listar roles | `/api/v1/role/list` | `{}` |
| Consultar rol | `/api/v1/role/find` | `{"id_role":1}` |
| Crear rol | `/api/v1/role/create` | `{"name":"ADMIN","description":"Administrador","date_creation":"2026-10-12","state":"ACTIVO"}` |
| Editar rol | `/api/v1/role/update` | `{"id_role":1,"name":"ADMIN","description":"Administrador","date_creation":"2026-10-12","state":"ACTIVO"}` |
| Eliminar rol | `/api/v1/role/delete` | `{"id_role":1}` |

En `js/config.js` se definen las rutas de cada operación. Si tu API recibe todas las operaciones por la misma URL `/api/v1/user` usando un identificador de acción en el cuerpo, modifica `apiRequest()` en `js/api.js` para construir el body según ese contrato.

### Estructura de respuesta esperada

Para **listados**, el frontend espera `data` como array:

```json
{
  "succes": true,
  "message": "Usuarios consultados",
  "data": [{"id_user":1,"username":"ronal","name":"Ronal Tamayo","role":"SUPER_ADMIN","state":"ACTIVO"}],
  "timestamp": "2026-10-12T14:40:19.942-05:00"
}
```

Para operaciones individuales, `data` puede ser un objeto. Se admiten tanto `succes` (tal como viene en el ejemplo) como `success`. La respuesta del usuario que se facilitó tiene la forma de una **validación de credenciales**; no confirma cómo responde el listado de usuarios.

> Nota de modelos: los modelos Java nombran atributos `idUser`, `idRole` y `dateCreation`, mientras que el ejemplo JSON usa `id_user`. El frontend lee ambos estilos al mostrar registros, pero envía identificadores en `snake_case` para corresponder con la respuesta presentada. Ajusta los cuerpos si tu backend espera `camelCase`.

## Seguridad, CORS y despliegue

- La API debe permitir el origen `http://localhost:5500` en su configuración de CORS.
- Los endpoints sensibles deben validar permisos y autenticación en Spring Security; ocultar botones no es control de acceso.
- Las contraseñas no se muestran en el listado, y al editar un usuario el campo vacío no actualiza la contraseña.
- Se admite un JWT opcional para la sesión en memoria, pero esta demostración no implementa login ni gestión de sesiones seguras.
- No introduzcas contraseñas reales en el modo demostración.
- En producción, utiliza HTTPS y privilegios mínimos.

## Archivos

- `index.html`: vistas, tablas, formularios y navegación.
- `css/styles.css`: diseño responsive.
- `js/app.js`: interfaz, validación básica, eventos y renderizado seguro del contenido.
- `js/api.js`: llamadas `fetch` y tratamiento de respuestas.
- `js/config.js`: URL base, rutas de operaciones y claves de ID.
