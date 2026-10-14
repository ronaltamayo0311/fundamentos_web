import { defaults, operationPaths, idKeys } from "./config.js";
const STORAGE_KEY = "admin-api-settings-v1";
export const settings = {
  ...defaults,
  ...JSON.parse(localStorage.getItem(STORAGE_KEY) || "{}"),
  token: "",
};
export function saveSettings(next) {
  Object.assign(settings, next);
  localStorage.setItem(
    STORAGE_KEY,
    JSON.stringify({
      baseUrl: settings.baseUrl,
      userPath: settings.userPath,
      rolePath: settings.rolePath,
      demo: settings.demo,
    }),
  );
}
const demoDb = {
  user: [
    {
      id_user: 1,
      username: "ronal",
      name: "Ronal Tamayo",
      role: "SUPER_ADMIN",
      state: "ACTIVO",
    },
  ],
  role: [
    {
      id_role: 1,
      name: "SUPER_ADMIN",
      description: "Administración general",
      date_creation: "2026-10-12",
      state: "ACTIVO",
    },
  ],
};
let nextIds = { user: 2, role: 2 };
function demoRequest(entity, operation, payload) {
  const records = demoDb[entity],
    key = idKeys[entity];
  const index = records.findIndex(
    (row) => String(row[key]) === String(payload[key]),
  );
  let data;
  if (operation === "list") data = [...records];
  else if (operation === "get") data = index < 0 ? null : { ...records[index] };
  else if (operation === "create") {
    data = { ...payload, [key]: nextIds[entity]++ };
    records.push(data);
  } else if (operation === "update") {
    if (index < 0) throw new Error("Registro no encontrado");
    data = { ...records[index], ...payload };
    records[index] = data;
  } else if (operation === "remove") {
    if (index < 0) throw new Error("Registro no encontrado");
    data = records.splice(index, 1)[0];
  }
  return data;
}
export async function apiRequest(entity, operation, payload = {}) {
  if (settings.demo) return demoRequest(entity, operation, payload);
  const segment = entity === "user" ? settings.userPath : settings.rolePath;
  const route = operationPaths[entity][operation];
  const url = `${settings.baseUrl.replace(/\/$/, "")}/${segment.replace(/^\/+|\/+$/g, "")}${route}`;
  const headers = { "Content-Type": "application/json" };
  if (settings.token) headers.Authorization = `Bearer ${settings.token}`;
  let response;
  try {
    response = await fetch(url, {
      method: "POST",
      headers,
      body: JSON.stringify(payload),
    });
  } catch (error) {
    throw new Error(
      `No fue posible conectar con ${url}. Verifica que Spring Boot esté iniciado y que CORS permita este origen. (${error.message})`,
    );
  }
  let result;
  try {
    result = await response.json();
  } catch {
    throw new Error(`Respuesta no JSON (HTTP ${response.status})`);
  }
  if (!response.ok)
    throw new Error(result.message || `Error HTTP ${response.status}`);
  // El ejemplo de backend usa la clave literal "succes" (una sola s al final).
  if (result.succes === false || result.success === false)
    throw new Error(result.message || "La operación no fue exitosa");
  return result.data ?? result;
}
