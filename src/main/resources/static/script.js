const API_URL = "/api/employees";
const $ = (id) => document.getElementById(id);
const form = $("employeeForm");
const table = $("employeeTable");
const message = $("message");
let employees = [];

function showMessage(text, isError = false) {
  message.textContent = text;
  message.classList.toggle("error", isError);
}
async function apiRequest(url, options = {}) {
  const response = await fetch(url, options);
  if (!response.ok) {
    const detail = await response.text();
    throw new Error(detail || `HTTP ${response.status} ${response.statusText}`);
  }
  if (response.status === 204) return null;
  const type = response.headers.get("content-type") || "";
  if (type.includes("application/json")) return response.json();
  return response.text();
}
function money(value) {
  return new Intl.NumberFormat("en-IN", {style:"currency",currency:"INR",maximumFractionDigits:2}).format(Number(value || 0));
}
function updateStats(list) {
  $("totalCount").textContent = list.length;
  $("departmentCount").textContent = new Set(list.map(e => (e.department || "").trim()).filter(Boolean)).size;
  const avg = list.length ? list.reduce((sum,e) => sum + Number(e.salary || 0),0) / list.length : 0;
  $("averageSalary").textContent = money(avg);
}
function renderEmployees() {
  const term = $("searchInput").value.trim().toLowerCase();
  const filtered = employees.filter(e => [e.name,e.email,e.department,String(e.id)].some(v => String(v || "").toLowerCase().includes(term)));
  $("resultCount").textContent = `${filtered.length} of ${employees.length} employees`;
  table.replaceChildren();
  if (!filtered.length) {
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.colSpan = 6;
    cell.className = "empty";
    cell.textContent = employees.length ? "No matching employees found." : "No employees yet. Add your first employee above.";
    row.appendChild(cell); table.appendChild(row); return;
  }
  filtered.forEach(employee => {
    const row = document.createElement("tr");
    const id = document.createElement("td"); id.textContent = employee.id ?? ""; row.appendChild(id);
    const name = document.createElement("td"); name.textContent = employee.name || ""; name.className = "person"; row.appendChild(name);
    const email = document.createElement("td"); email.textContent = employee.email || ""; row.appendChild(email);
    const dept = document.createElement("td"); dept.textContent = employee.department || "—"; row.appendChild(dept);
    const salary = document.createElement("td"); salary.textContent = money(employee.salary); row.appendChild(salary);
    const actions = document.createElement("td"); actions.className = "actions";
    const edit = document.createElement("button"); edit.type = "button"; edit.className = "btn secondary"; edit.textContent = "Edit"; edit.addEventListener("click",() => startEdit(employee));
    const del = document.createElement("button"); del.type = "button"; del.className = "btn danger"; del.textContent = "Delete"; del.addEventListener("click",() => deleteEmployee(employee.id));
    actions.append(edit,del); row.appendChild(actions); table.appendChild(row);
  });
}
async function loadEmployees() {
  table.innerHTML = '<tr><td colspan="6" class="empty">Loading employees…</td></tr>';
  try {
    const data = await apiRequest(API_URL);
    if (!Array.isArray(data)) throw new Error("The GET API did not return a JSON array.");
    employees = data;
    updateStats(employees); renderEmployees();
  } catch (error) {
    table.innerHTML = '<tr><td colspan="6" class="empty">Could not load employees. Check that Spring Boot is running.</td></tr>';
    showMessage(`Could not load employees: ${error.message}`, true);
  }
}
function startEdit(employee) {
  $("employeeId").value = employee.id;
  $("name").value = employee.name || "";
  $("email").value = employee.email || "";
  $("department").value = employee.department || "";
  $("salary").value = employee.salary ?? 0;
  $("formTitle").textContent = "Edit employee";
  $("formMode").textContent = "EDITING RECORD";
  $("saveButton").textContent = "Update employee";
  $("cancelButton").hidden = false;
  showMessage("");
  window.scrollTo({top:0,behavior:"smooth"});
}
function resetForm() {
  form.reset(); $("employeeId").value = "";
  $("formTitle").textContent = "Add employee";
  $("formMode").textContent = "NEW RECORD";
  $("saveButton").textContent = "Save employee";
  $("cancelButton").hidden = true;
}
form.addEventListener("submit", async event => {
  event.preventDefault();
  const id = $("employeeId").value;
  const salaryText = $("salary").value;
  const employee = {
    name: $("name").value.trim(),
    email: $("email").value.trim(),
    department: $("department").value.trim(),
    salary: Number(salaryText)
  };
  if (!employee.name || !employee.email || salaryText === "" || !Number.isFinite(employee.salary) || employee.salary < 0) {
    showMessage("Enter a name, valid email and non-negative salary.", true); return;
  }
  const editing = id !== "";
  const url = editing ? `${API_URL}/${encodeURIComponent(id)}` : API_URL;
  $("saveButton").disabled = true;
  try {
    await apiRequest(url, {method: editing ? "PUT" : "POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(employee)});
    showMessage(editing ? "Employee updated successfully." : "Employee created successfully.");
    resetForm(); await loadEmployees();
  } catch (error) {
    showMessage(`Save failed: ${error.message}`, true);
  } finally { $("saveButton").disabled = false; }
});
async function deleteEmployee(id) {
  const employee = employees.find(e => String(e.id) === String(id));
  if (!window.confirm(`Delete ${employee?.name || "this employee"} (ID ${id})? This cannot be undone.`)) return;
  try {
    await apiRequest(`${API_URL}/${encodeURIComponent(id)}`,{method:"DELETE"});
    showMessage("Employee deleted successfully."); await loadEmployees();
  } catch (error) { showMessage(`Delete failed: ${error.message}`,true); }
}
$("cancelButton").addEventListener("click",() => {resetForm();showMessage("Edit cancelled.");});
$("refreshButton").addEventListener("click",loadEmployees);
$("searchInput").addEventListener("input",renderEmployees);
loadEmployees();
