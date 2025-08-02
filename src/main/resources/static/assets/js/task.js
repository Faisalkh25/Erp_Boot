// =================== HELPER: Get Auth Header ===================
function getAuthHeaders(isJson = true) {
  const token = localStorage.getItem('token');
  const headers = {
    "Authorization": `Bearer ${token}`
  };
  if (isJson) {
    headers["Content-Type"] = "application/json";
  }
  return headers;
}

// =================== HELPER: Decode empId from JWT ===================
function getEmpIdFromToken() {
  const token = localStorage.getItem('token');
  if (!token) return null;

  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload)); // Decode Base64 payload
    return decodedPayload.empId; // Extract empId claim
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
}

// =================== DROPDOWNS ON PAGE LOAD ===================
const API_BASE = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', async function () {
  try {
    const res = await fetch(`${API_BASE}/employee-dashboard/dropdowns`, {
      headers: getAuthHeaders()
    });
    if (!res.ok) throw new Error("Failed to fetch dropdown data");

    const data = await res.json();

    populateDropdownData('task-type', data.types, 'typeId', 'typeName');
    populateDropdownData('task-priority', data.priorities, 'priorityId', 'priorityName');
    populateDropdownData('task-status', data.statuses, 'statusId', 'statusName');
    populateDropdownData('task-subject', data.subjects, 'subjectId', 'subjectName');

    if (data.projects && data.projects.length > 0) {
      populateDropdownData('task-project', data.projects, 'projectId', 'projectName');
    } else {
      const projectSelect = document.getElementById('task-project');
      projectSelect.innerHTML = `<option value="">No Projects Assigned</option>`;
    }

  } catch (err) {
    console.error("❌ Failed to load dropdowns: ", err);
  }
});

// =================== POPULATE DROPDOWN UTILITY ===================
function populateDropdownData(selectId, dataList, valueKey, textKey) {
  const select = document.getElementById(selectId);
  if (!select) return;

  select.innerHTML = '<option value="">-Select-</option>';
  dataList.forEach(item => {
    const option = document.createElement('option');
    option.value = item[valueKey];
    option.textContent = item[textKey];
    select.appendChild(option);
  });
}

// =================== HANDLE FORM SUBMIT ===================
const form = document.getElementById("taskForm");
if (form) {
  form.addEventListener("submit", handleTaskSubmit);
}

async function handleTaskSubmit(e) {
  e.preventDefault();

  const empId = getEmpIdFromToken();
  const token = localStorage.getItem("token");

  if (!empId || !token) {
    alert("⚠️ Please login first!");
    return;
  }

  const taskData = {
    typeId: parseInt(document.getElementById("task-type").value),
    priorityId: parseInt(document.getElementById("task-priority").value),
    statusId: parseInt(document.getElementById("task-status").value),
    subjectId: parseInt(document.getElementById("task-subject").value),
    projectId: parseInt(document.getElementById("task-project").value),
    employeeId: empId, // from decoded token
    taskQuantity: parseInt(document.getElementById("task-quantity").value),
    taskDescription: document.getElementById("task-description").value,
    startDate: document.getElementById("task-start-date").value,
    endDate: document.getElementById("task-end-date").value
  };

  try {
    const response = await fetch(`${API_BASE}/addTask`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(taskData)
    });

    if (!response.ok) {
      throw new Error("❌ Failed to submit task");
    }

    alert("✅ Task submitted successfully!");
    form.reset();
  } catch (err) {
    console.error("❌ Error submitting task:", err);
    alert("❌ Error submitting task. Please try again.");
  }
}
