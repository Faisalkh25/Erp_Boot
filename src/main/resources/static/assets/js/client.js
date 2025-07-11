
const API = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {
  loadProjects();             // Load dropdown
  loadClients();              // Load table

  document.getElementById("clientForm").addEventListener("submit", handleClientFormSubmit);
  document.getElementById("editClientForm").addEventListener("submit", updateClient);
  document.getElementById("confirmDeleteBtn").addEventListener("click", deleteClient);
});

//  Success Alert Function
function showSuccessAlert(message) {
  const alertBox = document.getElementById("alertBox");
  const alertMessage = document.getElementById("alertMessage");

  alertMessage.textContent = message;
  alertBox.classList.remove("d-none");

  setTimeout(() => {
    alertBox.classList.add("d-none");
  }, 3000);
}

//  Load Projects in Dropdown
async function loadProjects(selectedId = null, targetDropdown = "projectDropdown") {
  const res = await fetch(`${API}/projects`);
  const projects = await res.json();
  const select = document.querySelector("select[name='projectName[]']");
  if (!select) return;

  select.innerHTML = `<option disabled selected>- Select Project -</option>`;
  projects.forEach(project => {
    const opt = document.createElement("option");
    opt.value = project.projectId;
    opt.text = project.projectName;
    if (selectedId && selectedId === project.projectId) {
      opt.selected = true;
    }
    select.appendChild(opt);
  });
}

//  Insert New Client
async function handleClientFormSubmit(e) {
  e.preventDefault();

  const clientName = document.getElementById("client_name").value;
  const projectId = document.querySelector("select[name='projectName[]']").value;

  const clientData = {
    clientName,
    project: { projectId: parseInt(projectId) }
  };

  const res = await fetch(`${API}/clients`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(clientData),
  });

  if (res.ok) {
    document.getElementById("clientForm").reset();
    loadClients();
    showSuccessAlert("Client added successfully!");
  }
}

//  Load Clients into Table
async function loadClients() {
  const res = await fetch(`${API}/clients`);
  const clients = await res.json();

  const tableBody = document.getElementById("clientTable");
  tableBody.innerHTML = "";

  clients.forEach(client => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td class="text-center">${client.clientId}</td>
      <td class="text-center">${client.clientName}</td>
      <td class="text-center">${client.project?.projectName || "N/A"}</td>
      <td class="text-center">
        <i class="fa-solid fa-pen-to-square text-primary me-2"
            onclick="openEditModal(${client.clientId})"
            style="cursor: pointer; font-size: 18px;"></i>
        <i class="fa-solid fa-trash text-danger"
            onclick="confirmDelete(${client.clientId})"
            style="cursor: pointer; font-size: 18px;"></i>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

//  Open Edit Modal
async function openEditModal(id) {
  const res = await fetch(`${API}/clients/${id}`);
  const client = await res.json();

  document.getElementById("edit_client_id").value = client.clientId;
  document.getElementById("edit_client_name").value = client.clientName;

  await loadEditProjects(client.project?.projectId || null);

  const editModal = new bootstrap.Modal(document.getElementById("editProjectModal"));
  editModal.show();
}

//  Load Projects in Edit Modal
async function loadEditProjects(selectedId = null) {
  const res = await fetch(`${API}/projects`);
  const projects = await res.json();
  const select = document.getElementById("edit_project_dropdown");

  select.innerHTML = `<option disabled selected>- Select Project -</option>`;
  projects.forEach(project => {
    const opt = document.createElement("option");
    opt.value = project.projectId;
    opt.text = project.projectName;
    if (selectedId === project.projectId) {
      opt.selected = true;
    }
    select.appendChild(opt);
  });
}

//  Update Client
async function updateClient(e) {
  e.preventDefault();

  const id = document.getElementById("edit_client_id").value;
  const name = document.getElementById("edit_client_name").value;
  const projectId = document.getElementById("edit_project_dropdown").value;

  const clientData = {
    clientId: parseInt(id),
    clientName: name,
    project: { projectId: parseInt(projectId) }
  };

  const res = await fetch(`${API}/clients/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(clientData),
  });

  if (res.ok) {
    loadClients();
    bootstrap.Modal.getInstance(document.getElementById("editProjectModal")).hide();
    showSuccessAlert("Client updated successfully!");
  }
}

//  Delete Setup
let clientIdToDelete = null;
function confirmDelete(id) {
  clientIdToDelete = id;
  const deleteModal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
  deleteModal.show();
}

//  Delete Client
async function deleteClient() {
  if (clientIdToDelete) {
    await fetch(`${API}/clients/${clientIdToDelete}`, {
      method: "DELETE",
    });
    loadClients();
    bootstrap.Modal.getInstance(document.getElementById("deleteConfirmModal")).hide();
    showSuccessAlert("Client deleted successfully!", "error");
  }
} 