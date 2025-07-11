// ✅ Ensure DOM is ready
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("projectDetailsForm");
  const editForm = document.getElementById("editProjectForm");
  const teamDropdownList = document.getElementById("teamDropdownList");
  const rateInput = document.getElementById("project_rate");
  const manPerHourInput = document.getElementById("rate-per-hour");
  const rateCalcInput = document.getElementById("calcultion");
  const profitBox = document.getElementById("profitBox");

  const editModalEl = document.getElementById("editProjectModal");
  const editModal = new bootstrap.Modal(editModalEl);

  // ✅ Populate dropdowns on page load
  populateDropdown("http://localhost:8080/api/projects", "project_name", "projectName", "projectId");         // Project dropdown
  populateDropdown("http://localhost:8080/api/clients", "client_name", "clientName", "clientId");             // Client dropdown
  populateDropdown("http://localhost:8080/api/types", "project_type", "typeName", "typeId");                  // Type dropdown
  populateDropdown("http://localhost:8080/api/priority", "project_priority", "priorityName", "priorityId");   // Priority dropdown
  populateDropdown("http://localhost:8080/api/status", "project_status", "statusName", "statusId");           // Status dropdown
  populateProjectLeaderDropdown();                                                                             // Project Leader dropdown
  populateRateTypeDropdown();                                                                                  // Rate Type (Currency)
  loadTeamCheckboxes();                                                                                        // Team member checkboxes
  loadProjectTable();                                                                                          // Project list table

  // ---------------------------------------
  // POPULATE GENERIC DROPDOWN WITH API DATA
  // ---------------------------------------
  async function populateDropdown(endpoint, id, label, value) {
    try {
      const res = await fetch(endpoint);
      const data = await res.json();
      const select = document.getElementById(id);
      select.innerHTML = `<option value="" disabled selected>-Select-</option>`;
      data.forEach(item => {
        select.innerHTML += `<option value="${item[value]}">${item[label]}</option>`;
      });
    } catch (err) {
      console.error(`Error loading ${id}:`, err);
    }
  }

  // -------------------------------------------------
  // POPULATE PROJECTS LEADER DROPDOWN FOR CREATE FORM
  // -------------------------------------------------
  async function populateProjectLeaderDropdown() {
    try {
      const res = await fetch("http://localhost:8080/api/employees");
      const data = await res.json();
      const select = document.getElementById("project_leader");
      select.innerHTML = `<option value="" disabled selected>-Select-</option>`;
      data.forEach(emp => {
        const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
        if (emp.emp_id) {
          select.innerHTML += `<option value="${emp.emp_id}">${fullName}</option>`;
        }
      });
    } catch (err) {
      console.error("Error loading project leaders:", err);
    }
  }

  // -----------------------------------------
  // LOAD TEAM MEMBER CHECKBOX FOR CARETE FORM
  // -----------------------------------------
  async function loadTeamCheckboxes() {
    try {
      const res = await fetch("http://localhost:8080/api/employees");
      const employees = await res.json();
      teamDropdownList.innerHTML = "";

      employees.forEach(emp => {
        const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
        if (!emp.emp_id) return;

        const li = document.createElement("li");
        li.innerHTML = `
          <div class="form-check ms-2">
            <input class="form-check-input team-checkbox" type="checkbox" value="${emp.emp_id}" id="emp_${emp.emp_id}">
            <label class="form-check-label" for="emp_${emp.emp_id}">${fullName}</label>
          </div>
        `;
        teamDropdownList.appendChild(li);
      });
    } catch (err) {
      console.error("Error loading team members:", err);
    }
  }

  // -------------------------------------------
  // GET SELECTED TEAM MEMBER ID'S FROM CHECKBOX
  // -------------------------------------------
  function getSelectedTeamMembers() {
    return Array.from(document.querySelectorAll(".team-checkbox:checked"))
      .map(cb => parseInt(cb.value))
      .filter(id => !isNaN(id));
  }
  // --------------------------------------------------------
  // CALCULATE RATE PER HOUR AND PROFIT WHEN TEAM IS SELECTED
  // --------------------------------------------------------
  teamDropdownList.addEventListener("click", async (e) => {
    if (!e.target.classList.contains("team-checkbox")) return;

    const selectedIds = getSelectedTeamMembers();
    const rate = parseFloat(rateInput.value);
    if (selectedIds.length === 0) {
      manPerHourInput.value = "0.00";
      rateCalcInput.value = "0.00";
      return;
    }

    const requiredIds = ["project_name", "client_name", "project_type", "project_priority", "project_status", "project_leader", "start_date", "end_date"];
    for (let id of requiredIds) {
      const val = document.getElementById(id).value;
      if (!val || val.trim() === "") return;
    }

    if (!rate || isNaN(rate)) return alert("⚠️ Please enter a valid rate.");

    const payload = {
      projectId: parseInt(document.getElementById("project_name").value),
      clientId: parseInt(document.getElementById("client_name").value),
      typeId: parseInt(document.getElementById("project_type").value),
      priorityId: parseInt(document.getElementById("project_priority").value),
      statusId: parseInt(document.getElementById("project_status").value),
      startDate: document.getElementById("start_date").value,
      endDate: document.getElementById("end_date").value,
      submissionDate: document.getElementById("submit_date").value || null,
      projectLeaderId: parseInt(document.getElementById("project_leader").value),
      rate: rate,
      rateType: document.getElementById("rate_type").value,
      teamMemberIds: selectedIds,
      remarks: "",
      description: ""
    };

    try {
      const res = await fetch("http://localhost:8080/api/projectDetails/calculate-cost", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      const result = await res.json();
      if (!res.ok) return showAlert(result.message || "❌ Project cost exceeds rate", "danger");

      manPerHourInput.value = result.manPerHour?.toFixed(2);
      rateCalcInput.value = result.rateCalculation?.toFixed(2);
      profitBox.value = (rate - result.rateCalculation).toFixed(2);

    } catch (err) {
      console.error("❌ Error calculating cost:", err);
    }
  });

  // ----------------------------------------
  // HANDLE FORM SUBMISSION TO CREATE PROJECT
  // ----------------------------------------
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const selectedIds = getSelectedTeamMembers();
    const payload = {
      projectId: parseInt(document.getElementById("project_name").value),
      clientId: parseInt(document.getElementById("client_name").value),
      typeId: parseInt(document.getElementById("project_type").value),
      priorityId: parseInt(document.getElementById("project_priority").value),
      statusId: parseInt(document.getElementById("project_status").value),
      startDate: document.getElementById("start_date").value,
      endDate: document.getElementById("end_date").value,
      submissionDate: document.getElementById("submit_date").value,
      projectLeaderId: parseInt(document.getElementById("project_leader").value),
      rate: parseFloat(rateInput.value),
      rateType: document.getElementById("rate_type").value,
      teamMemberIds: selectedIds,
      remarks: document.getElementById("remark").value,
      description: document.getElementById("description").value
    };

    try { 
      const res = await fetch("http://localhost:8080/api/projectDetails", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        showAlert("✅ Project Added Successfully.", "success");
        form.reset();
        loadProjectTable();
      } else {
        const msg = await res.text();
        alert("❌ " + msg);
      }
    } catch (err) {
      console.error("❌ Error saving project:", err);
    }
  });

  // ------------------------------------
  // LOAD ALL PROJECT RECORDS OINTO TABLE
  // ------------------------------------
  async function loadProjectTable() {
    try {
      const res = await fetch("http://localhost:8080/api/projectDetails");
      const data = await res.json();
      const tbody = document.getElementById("projectListBody");
      tbody.innerHTML = "";

      data.forEach(project => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td class="text-center">${project.projectDetailsId}</td>
          <td class="text-center">${project.projectName}</td>
          <td class="text-center">${project.clientName}</td>
          <td class="text-center">${project.typeName}</td>
          <td class="text-center">${project.startDate}</td>
          <td class="text-center">${project.endDate}</td>
          <td class="text-center">${project.submissionDate}</td>
          <td class="text-center">${project.rate}</td>
          <td class="text-center">${project.rateType}</td>
          <td class="text-center">${project.priorityName}</td>
          <td class="text-center">${project.projectLeaderName}</td>
          <td class="text-center"><marquee>${project.teamMemberName?.join(", ") || '-'}</marquee></td>
<td class="text-center">${project.manPerHour?.toFixed(2)}</td>
<td class="text-center">${project.rateCalculation?.toFixed(2)}</td>
          <td class="text-center">${project.statusName}</td>
          <td class="text-center">${project.remarks}</td>
          <td class="text-center">${project.description}</td>
          <td class="text-center">
            <button class="btn btn-sm btn-primary" onclick="openEditModal(${project.projectDetailsId})">Edit</button>
            <button class="btn btn-sm btn-danger delete-btn" data-id="${project.projectDetailsId}">Delete</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    } catch (err) {
      console.error("❌ Failed to load project records:", err);
    }
  }

  // -------------------------------------------------------
  // POPULATE CURRENCY (RATE TYPE) DROPDOWN FOR EXTERNAL API
  // -------------------------------------------------------
  async function populateRateTypeDropdown() {
    try {
      const res = await fetch('https://restcountries.com/v3.1/all?fields=currencies');
      const countries = await res.json();

      const currencyMap = new Map();
      countries.forEach(country => {
        if (country.currencies) {
          Object.entries(country.currencies).forEach(([code, currency]) => {
            if (!currencyMap.has(code)) {
              currencyMap.set(code, {
                name: currency.name || code,
                symbol: currency.symbol || ""
              });
            }
          });
        }
      });

      const rateTypeSelect = document.getElementById("rate_type");
      rateTypeSelect.innerHTML = `<option value="" disabled selected>-Select Amount Type-</option>`;
      currencyMap.forEach((info, code) => {
        const option = document.createElement("option");
        option.value = code;
        option.text = `${info.name} (${code}) ${info.symbol}`;
        rateTypeSelect.appendChild(option);
      });

    } catch (err) {
      console.error("Error loading currencies:", err);
    }
  }

  // ------------------------------------
  // EXPOSE EDIT MODEL FUNCATION GLOBALLY
  // ------------------------------------

  window.openEditModal = openEditModal;



  // ---------------------------------------
  // LOAD TEAM CHECKBOX FOR EDIT MODEL FORM
  // ---------------------------------------
  async function loadEditTeamCheckboxes() {
    try {
      const res = await fetch("http://localhost:8080/api/employees");
      const employees = await res.json();
      const list = document.getElementById("edit_team_dropdown_list");

      list.innerHTML = "";

      employees.forEach(emp => {
        const fullName = `${emp.first_name} ${emp.last_name || ""}`.trim();
        const li = document.createElement("li");
        li.innerHTML = `
          <div class="form-check ms-2">
            <input class="form-check-input edit-team-checkbox" type="checkbox" value="${emp.emp_id}" id="edit_emp_${emp.emp_id}">
            <label class="form-check-label" for="edit_emp_${emp.emp_id}">
              ${fullName}
            </label>
          </div>
        `;
        list.appendChild(li);
      });
    } catch (err) {
      console.error("❌ Error loading team members for edit modal:", err);
    }
  }

  // ---------------------------------------------
  // OPEN EDIT MODEL FORM AND PREFILLED RECORDS JS
  // ---------------------------------------------
  async function openEditModal(projectDetailsId) {
    try {
      const res = await fetch(`http://localhost:8080/api/projectDetails/${projectDetailsId}`);
      if (!res.ok) {
        const message = await res.text();
        alert("❌ " + message);
        return;
      }

      const data = await res.json();

      await populateDropdown("http://localhost:8080/api/projects", "edit_project_name", "projectName", "projectId");
      await populateDropdown("http://localhost:8080/api/clients", "edit_client_name", "clientName", "clientId");
      await populateDropdown("http://localhost:8080/api/types", "edit_project_type", "typeName", "typeId");
      await populateDropdown("http://localhost:8080/api/priority", "edit_project_priority", "priorityName", "priorityId");
      await populateDropdown("http://localhost:8080/api/status", "edit_project_status", "statusName", "statusId");
      await populateProjectLeaderDropdown("edit_project_leader");
      await loadEditTeamCheckboxes();

      document.getElementById("edit_project_id").value = data.projectDetailsid;
      document.getElementById("edit_project_name").value = data.projectId;
      document.getElementById("edit_client_name").value = data.clientId;
      document.getElementById("edit_project_type").value = data.typeId;
      document.getElementById("edit_project_priority").value = data.priorityId;
      document.getElementById("edit_project_status").value = data.statusId;
      document.getElementById("edit_project_leader").value = data.projectLeaderId;
      document.getElementById("edit_start_date").value = data.startDate;
      document.getElementById("edit_end_date").value = data.endDate;
      document.getElementById("edit_submit_date").value = data.submissionDate;
      document.getElementById("edit_project_rate").value = data.rate;
      document.getElementById("edit_rate_type").value = data.rateType;
      document.getElementById("edit_remark").value = data.remarks;
      document.getElementById("edit_description").value = data.description;

      const checkboxes = document.querySelectorAll(".edit-team-checkbox");
      checkboxes.forEach(cb => {
        cb.checked = data.teamMemberIds?.includes(parseInt(cb.value));
      });

      editModal.show();

    } catch (err) {
      console.error("❌ Failed to load project for editing:", err);
    }
  }

  // -------------------------------
  // FOR EDIT FORM TO UPDATE RECORDS
  // -------------------------------
  editForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("edit_project_id").value;
    const selectedIds = Array.from(document.querySelectorAll(".edit-team-checkbox:checked")).map(cb => parseInt(cb.value));

    const payload = {
      projectId: parseInt(document.getElementById("edit_project_name").value),
      clientId: parseInt(document.getElementById("edit_client_name").value),
      typeId: parseInt(document.getElementById("edit_project_type").value),
      priorityId: parseInt(document.getElementById("edit_project_priority").value),
      statusId: parseInt(document.getElementById("edit_project_status").value),
      projectLeaderId: parseInt(document.getElementById("edit_project_leader").value),
      startDate: document.getElementById("edit_start_date").value,
      endDate: document.getElementById("edit_end_date").value,
      submissionDate: document.getElementById("edit_submit_date").value,
      rate: parseFloat(document.getElementById("edit_project_rate").value),
      rateType: document.getElementById("edit_rate_type").value,
      remarks: document.getElementById("edit_remark").value,
      description: document.getElementById("edit_description").value,
      teamMemberIds: selectedIds
    };

    try {
      const res = await fetch(`http://localhost:8080/api/projectDetails/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        alert("✅ Project updated!");
        editModal.hide();
        loadProjectTable();
      } else {
        const msg = await res.text();

      }
    } catch (err) {
      console.error("❌ Update error:", err);
    }
  });
});







// --------------------------------
// SHOW ALERT MESSAGE JS START HERE
// --------------------------------
function showAlert(message, type = "success") {
  const alertPlaceholder = document.getElementById("alertPlaceholder");
  const wrapper = document.createElement("div");

  wrapper.innerHTML = `
    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
      <strong>${type === "success" ? "Success!" : "Error!"}</strong> ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  `;

  alertPlaceholder.innerHTML = "";
  alertPlaceholder.append(wrapper);

  setTimeout(() => {
    const alert = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector(".alert"));
    alert.close();
  }, 4000);
}


// -------------------------------
// SCROLL TABLE DATA JS START HERE
// -------------------------------
function scrollTable(direction) {
  const container = document.querySelector("#scrollTableContainer .custom-scroll");
  const scrollAmount = 200;
  if (container) {
    container.scrollLeft += direction === "left" ? -scrollAmount : scrollAmount;
  }
}
