import { settings, saveSettings, apiRequest } from "./api.js";
import { idKeys } from "./config.js";
const $ = (id) => document.getElementById(id);
const store = { user: [], role: [] };
let editor = { entity: "user", id: null };
let toastTimer;
const get = (row, ...keys) =>
  keys.map((k) => row?.[k]).find((v) => v !== undefined && v !== null) ?? "";
const idOf = (entity, row) =>
  get(row, idKeys[entity], entity === "user" ? "idUser" : "idRole");
const normalize = (raw) =>
  Array.isArray(raw) ? raw : Array.isArray(raw?.content) ? raw.content : [];
function toast(message, error = false) {
  const el = $("toast");
  el.textContent = message;
  el.className = `toast show${error ? " error" : ""}`;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => (el.className = "toast"), 5000);
}
function cell(tr, value) {
  const td = document.createElement("td");
  td.textContent = String(value ?? "—");
  tr.append(td);
  return td;
}
function stateCell(tr, value) {
  const td = document.createElement("td");
  const span = document.createElement("span");
  span.className = `state ${String(value).toUpperCase() === "ACTIVO" ? "active" : ""}`;
  span.textContent = value || "No definido";
  td.append(span);
  tr.append(td);
}
function actions(tr, entity, row) {
  const td = document.createElement("td");
  td.className = "actions";
  const edit = document.createElement("button");
  edit.className = "btn secondary";
  edit.textContent = "Editar";
  edit.addEventListener("click", () => openEditor(entity, row));
  const del = document.createElement("button");
  del.className = "btn danger";
  del.textContent = "Eliminar";
  del.addEventListener("click", () => removeRow(entity, row));
  td.append(edit, del);
  tr.append(td);
}
function render(entity) {
  const isUser = entity === "user";
  const body = $(isUser ? "usersBody" : "rolesBody");
  const query = $(isUser ? "userSearch" : "roleSearch")
    .value.trim()
    .toLowerCase();
  const filter = isUser ? $("userStatusFilter").value : "";
  body.replaceChildren();
  const data = store[entity].filter((row) => {
    const searchable = isUser
      ? [get(row, "username"), get(row, "name"), get(row, "role")]
      : [get(row, "name"), get(row, "code")];
    return (
      searchable.join(" ").toLowerCase().includes(query) &&
      (!filter || String(get(row, "state")).toUpperCase() === filter)
    );
  });
  for (const row of data) {
    const tr = document.createElement("tr");
    cell(tr, idOf(entity, row));
    if (isUser) {
      cell(tr, get(row, "username"));
      cell(tr, get(row, "name"));
      cell(tr, get(row, "role"));
      stateCell(tr, get(row, "state"));
    } else {
      cell(tr, get(row, "name"));
      cell(tr, get(row, "code"));
      cell(tr, get(row, "description"));
      cell(tr, get(row, "dateCreation"));
      stateCell(tr, get(row, "state"));
    }
    actions(tr, entity, row);
    body.append(tr);
  }
  $(isUser ? "usersEmpty" : "rolesEmpty").hidden = data.length > 0;
}
async function load(entity) {
  try {
    const data = await apiRequest(entity, "list", {});
    if (!Array.isArray(data) && !Array.isArray(data?.content))
      throw new Error(
        "El endpoint de listado debe responder con data como arreglo de registros",
      );
    store[entity] = normalize(data);
    render(entity);
    $("apiStatus").textContent = settings.demo
      ? "Modo demostración"
      : "API disponible";
  } catch (e) {
    store[entity] = [];
    render(entity);
    $("apiStatus").textContent = "API no disponible";
    toast(e.message, true);
  }
}
function show(view) {
  for (const name of ["users", "roles", "settings"])
    $(name + "View").hidden = name !== view;
  document
    .querySelectorAll(".nav-item")
    .forEach((btn) =>
      btn.classList.toggle("active", btn.dataset.view === view),
    );
  $("sidebar").classList.remove("open");
  if (view === "users") load("user");
  if (view === "roles") load("role");
}
function field(
  name,
  label,
  type = "text",
  required = true,
  value = "",
  extra = {},
) {
  const wrapper = document.createElement("label");
  wrapper.className = "field";
  wrapper.textContent = label;
  let input;
  if (type === "select") {
    input = document.createElement("select");
    for (const opt of extra.options || []) {
      const el = document.createElement("option");
      el.value = opt;
      el.textContent = opt;
      input.append(el);
    }
  } else {
    input = document.createElement("input");
    input.type = type;
  }
  input.name = name;
  input.required = required;
  input.value = String(value ?? "");
  if (type === "password") input.autocomplete = "new-password";
  wrapper.append(input);
  return wrapper;
}
function openEditor(entity, row = null) {
  editor = { entity, id: row ? idOf(entity, row) : null };
  $("dialogTitle").textContent =
    `${row ? "Editar" : "Nuevo"} ${entity === "user" ? "usuario" : "rol"}`;
  const fields = $("editorFields");
  fields.replaceChildren();
  if (entity === "user") {
    fields.append(
      field(
        "username",
        "Nombre de usuario",
        "text",
        true,
        get(row, "username"),
      ),
      field("name", "Nombre completo", "text", true, get(row, "name")),
      field(
        "password",
        row ? "Nueva contraseña (dejar vacía para conservar)" : "Contraseña",
        "password",
        !row,
      ),
      field(
        "role",
        "Rol",
        "select",
        true,
        get(row, "role") || get(store.role[0], "name") || "SUPER_ADMIN",
        {
          options: [
            ...new Set(
              [
                ...store.role.map((r) => get(r, "name")),
                get(row, "role"),
                "SUPER_ADMIN",
              ].filter(Boolean),
            ),
          ],
        },
      ),
      field("state", "Estado", "select", true, get(row, "state") || "ACTIVO", {
        options: ["ACTIVO", "INACTIVO"],
      }),
    );
  } else {
    fields.append(
      field("name", "Nombre del rol", "text", true, get(row, "name")),
      field(
        "description",
        "Descripción",
        "text",
        true,
        get(row, "description"),
      ),
      field(
        "date_creation",
        "Fecha de creación",
        "date",
        false,
        String(get(row, "date_creation", "dateCreation")).slice(0, 10) ||
          new Date().toISOString().slice(0, 10),
      ),
      field("state", "Estado", "select", true, get(row, "state") || "ACTIVO", {
        options: ["ACTIVO", "INACTIVO"],
      }),
    );
  }
  $("editorDialog").showModal();
}
async function saveEditor(event) {
  event.preventDefault();
  const { entity, id } = editor;
  const payload = Object.fromEntries(new FormData($("editorForm")).entries());
  if (entity === "user" && !payload.password) delete payload.password;
  if (entity === "role" && !payload.date_creation) delete payload.date_creation;
  if (id !== null) payload[idKeys[entity]] = Number(id);
  const button = $("submitEditor");
  button.disabled = true;
  try {
    await apiRequest(entity, id === null ? "create" : "update", payload);
    $("editorDialog").close();
    toast(`${entity === "user" ? "Usuario" : "Rol"} guardado correctamente`);
    await load(entity);
  } catch (e) {
    toast(e.message, true);
  } finally {
    button.disabled = false;
  }
}
async function removeRow(entity, row) {
  const id = idOf(entity, row);
  if (
    !window.confirm(
      `¿Eliminar ${entity === "user" ? "al usuario" : "el rol"} «${get(row, "name", "username")}»?`,
    )
  )
    return;
  try {
    await apiRequest(entity, "remove", { [idKeys[entity]]: id });
    toast("Registro eliminado");
    await load(entity);
  } catch (e) {
    toast(e.message, true);
  }
}
function init() {
  for (const btn of document.querySelectorAll(".nav-item"))
    btn.addEventListener("click", () => show(btn.dataset.view));
  $("menuToggle").addEventListener("click", () =>
    $("sidebar").classList.toggle("open"),
  );
  $("newUserBtn").addEventListener("click", () => openEditor("user"));
  $("newRoleBtn").addEventListener("click", () => openEditor("role"));
  $("refreshUsers").addEventListener("click", () => load("user"));
  $("refreshRoles").addEventListener("click", () => load("role"));
  $("userSearch").addEventListener("input", () => render("user"));
  $("roleSearch").addEventListener("input", () => render("role"));
  $("userStatusFilter").addEventListener("change", () => render("user"));
  $("editorForm").addEventListener("submit", saveEditor);
  $("closeDialog").addEventListener("click", () => $("editorDialog").close());
  $("cancelDialog").addEventListener("click", () => $("editorDialog").close());
  $("apiBase").value = settings.baseUrl;
  $("userPath").value = settings.userPath;
  $("rolePath").value = settings.rolePath;
  $("demoMode").checked = settings.demo;
  $("saveSettings").addEventListener("click", () => {
    saveSettings({
      baseUrl: $("apiBase").value.trim(),
      userPath: $("userPath").value.trim(),
      rolePath: $("rolePath").value.trim(),
      demo: $("demoMode").checked,
      token: $("authToken").value.trim(),
    });
    toast("Configuración guardada");
    show("users");
  });
  show("users");
  load("role");
}
init();
