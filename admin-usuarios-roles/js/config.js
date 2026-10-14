// CONFIGURACIÓN CENTRAL DE CONTRATOS API.
// IMPORTANTE: Las rutas de operaciones son EJEMPLOS, no endpoints confirmados.
// Cámbialas por las rutas exactas expuestas por tus controladores Spring Boot.
export const defaults = {
  baseUrl: "http://localhost:8080/api/v1",
  userPath: "/user",
  rolePath: "/role",
  demo: false,
};
export const operationPaths = {
  user: {
    list: "/list",
    get: "/find",
    create: "/create",
    update: "/update",
    remove: "/delete",
  },
  role: {
    list: "/list",
    get: "/find",
    create: "/create",
    update: "/update",
    remove: "/delete",
  },
};
export const idKeys = { user: "id_user", role: "id_role" };
// Si tu API usa una única ruta POST y el tipo de operación viaja en el body,
// adapta apiRequest() en api.js. Los modelos no definen por sí solos estas rutas.
