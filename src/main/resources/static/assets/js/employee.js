const API = 'http://localhost:8080/api';

// Load dropdowns (Add or Edit)
async function loadDropdown(endpoint, selectorId, placeholder = 'Select', selectedValue = null) {
  try {
    const res = await fetch(`${API}/${endpoint}`);
    const arr = await res.json();
    const sel = document.getElementById(selectorId);

    const fieldMap = {
      departments: { id: 'deptId', name: 'dept_name' },
      levels: { id: 'level_id', name: 'level' },
      shifts: { id: 'shift_id', name: 'shift_name' },
      roles: { id: 'role_id', name: 'role_name' }
    };

    const { id: idField, name: nameField } = fieldMap[endpoint];

    sel.innerHTML = `<option value="">-- ${placeholder} --</option>` + 
      arr.map(o => 
        `<option value="${o[idField]}" ${selectedValue == o[idField] ? 'selected' : ''}>${o[nameField]}</option>`
      ).join('');

  } catch (error) {
    console.error(`Error loading ${endpoint}:`, error);
  }
}


// Load managers by department for Add form
async function loadReportingManagersByDepartment(deptId) {
  try {
    const res = await fetch(`${API}/managers/by-department/${deptId}`);
    const data = await res.json();

    const sel1 = document.getElementById('rep_name1');
    const sel2 = document.getElementById('rep_name2');

    sel1.innerHTML = `<option value="">-- Select Manager 1 --</option>`;
    sel2.innerHTML = `<option value="">-- Select Manager 2 --</option>`;

    data.forEach(rm => {
      if (rm.rm_name1) {
        sel1.innerHTML += `<option value="${rm.rm_id}">${rm.rm_name1}</option>`;
      }
      if (rm.rm_name2) {
        sel2.innerHTML += `<option value="${rm.rm_id}">${rm.rm_name2}</option>`;
      }
    });
  } catch (error) {
    console.error("Error loading managers:", error);
  }
}

// Load managers for edit form
async function loadReportingManagersByDepartment(deptId, sel1Id = 'rep_name1', sel2Id = 'rep_name2', selected1 = null, selected2 = null) {
  try {
    const res = await fetch(`${API}/managers/by-department/${deptId}`);
    const data = await res.json();

    if (!Array.isArray(data)) {
      console.error("Expected array, got:", data);
      return;
    }

    const sel1 = document.getElementById(sel1Id);
    const sel2 = document.getElementById(sel2Id);

    sel1.innerHTML = `<option value="">-- Select Manager 1 --</option>`;
    sel2.innerHTML = `<option value="">-- Select Manager 2 --</option>`;

    data.forEach(rm => {
      if (rm.rm_name1) {
  sel1.innerHTML += `<option value="${rm.rm_id}" ${String(selected1) === String(rm.rm_id) ? 'selected' : ''}>${rm.rm_name1}</option>`;
}
if (rm.rm_name2) {
  sel2.innerHTML += `<option value="${rm.rm_id}" ${String(selected2) === String(rm.rm_id) ? 'selected' : ''}>${rm.rm_name2}</option>`;
}

    });

  } catch (error) {
    console.error("Error loading reporting managers:", error);
  }
}



async function loadAllDropdowns() {
  await Promise.all([
    loadDropdown('departments', 'dept', 'Department'),
    loadDropdown('levels', 'level', 'Level'),
    loadDropdown('shifts', 'shift', 'Shift'),
    loadDropdown('roles', 'role', 'Role')
  ]);
}

function resetForm() {
  const f = document.getElementById('employeeForm');
  f.reset();
  delete f.dataset.id;
}

// Build DTO object from form
function buildEmpFromForm() {
  const f = document.getElementById('employeeForm');
  const emp = {
    emp_code: +f.empCode.value,
    first_name: f.firstName.value,
    last_name: f.lastName.value,
    dateOfBirth: f.dob.value,
    gender: f.gender.value,
    email: f.email.value,
    personal_email: f.personalEmail.value,
    address: f.address.value,
    contact: f.contact.value,
    joining_date: f.joiningDate.value,
    company: f.company.value,
    employee_status: f.employeeStatus.value,
    joining_status: f.joiningStatus.value,
    working_status: f.workingStatus.value,
    departmentId: f.dept.value ? parseInt(f.dept.value) : null,
    levelId: f.level.value ? parseInt(f.level.value) : null,
    shiftId: f.shift.value ? parseInt(f.shift.value) : null,
    roleId: f.role.value ? parseInt(f.role.value) : null,
    password: f.password.value  // ✅ Added password here
  };

  if (f.rep_name1.value) emp.reportingManager1Id = parseInt(f.rep_name1.value);
  if (f.rep_name2.value) emp.reportingManager2Id = parseInt(f.rep_name2.value);
  return emp;
}


// Submit New Form
async function submitForm(e) {
  e.preventDefault();
  const f = document.getElementById('employeeForm');
  const formData = new FormData();
  const emp = buildEmpFromForm();
  formData.append('employee', JSON.stringify(emp));

  const fileInput = f.querySelector('.file-upload');
  if (fileInput && fileInput.files.length > 0) {
    formData.append('image', fileInput.files[0]);
  }

  const id = f.dataset.id;
  const res = await fetch(id ? `${API}/employees/${id}` : `${API}/employees`, {
    method: id ? 'PUT' : 'POST',
    body: formData
  });

  if (res.ok) {
    resetForm();
    await loadEmployees();
    showSuccessAlert(id ? 'Employee updated successfully' : 'Employee added successfully');
  } else {
    console.error('Error submitting form:', await res.text());
  }
}

// Load employees
async function loadEmployees() {
  const res = await fetch(`${API}/employees`);
  const arr = await res.json();
  const tbody = document.querySelector('#employeeTable tbody');
  if (!tbody) return;

  tbody.innerHTML = arr.map(emp => `
    <tr>
      <td>${emp.profile_picture ? `<img src="http://localhost:8080/uploads/${emp.profile_picture}" style="width:50px; height:50px; border-radius:50%; object-fit:cover;">` : '-'}</td>
      <td>${emp.emp_id || '-'}</td>
      <td>${emp.emp_code || '-'}</td>
      <td>${emp.first_name || ''}</td>
      <td>${emp.last_name || ''}</td>
      <td>${emp.dateOfBirth || '-'}</td>
      <td>${emp.gender || '-'}</td>
      <td>${emp.personal_email || '-'}</td>
      <td>${emp.email || '-'}</td>
      <td>${emp.address || '-'}</td>
      <td>${emp.contact || '-'}</td>
      <td>${emp.joining_date || '-'}</td>
      <td>${emp.levelName || '-'}</td>
      <td>${emp.company || '-'}</td>
      <td>${emp.shiftName || '-'}</td>
      <td>${emp.employee_status || '-'}</td>
      <td>${emp.joining_status || '-'}</td>
      <td>${emp.working_status || '-'}</td>
      <td>${emp.departmentName || '-'}</td>
      <td>${emp.reportingManager1Name || '-'}</td>
      <td>${emp.reportingManager2Name || '-'}</td>
      <td>${emp.roleName || '-'}</td>
      <td>
<button onclick="fillForm(${emp.emp_id})" class="btn btn-warning btn-sm">Edit</button>
<button onclick="confirmDelete(${emp.emp_id})" class="btn btn-danger btn-sm">Delete</button>


      </td>
    </tr>
  `).join('');

  tbody.querySelectorAll('.edit').forEach(btn => {
    btn.addEventListener('click', () => fillForm(btn.dataset.id));
  });

  tbody.querySelectorAll('.delete').forEach(btn => {
  btn.addEventListener('click', () => {
    selectedDeleteEmpId = btn.dataset.id;
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
  });
});


}

// Fill Edit Modal
async function fillForm(id) {
  const res = await fetch(`${API}/employees/${id}`);
  const emp = await res.json();

  const f = document.getElementById('editEmpForm');
  f.dataset.id = emp.empId;

  document.getElementById('emp_id_hidden').value = emp.empId;
  f.editempCode.value = emp.emp_code;
  f.editfirstName.value = emp.first_name;
  f.editlastName.value = emp.last_name;
  f.editdob.value = emp.dateOfBirth;
  f.editgender.value = emp.gender;
  f.editemail.value = emp.email;
  f.editpersonalEmail.value = emp.personal_email;
  f.editaddress.value = emp.address;
  f.editcontact.value = emp.contact;
  f.editjoiningDate.value = emp.joining_date;
  f.editcompany.value = emp.company;
  f.editemployeeStatus.value = emp.employee_status;
  f.editjoiningStatus.value = emp.joining_status;
  f.editworkingStatus.value = emp.working_status;

  console.log("Selected Dept ID:", emp.departmentId);
console.log("Selected Role ID:", emp.roleId);
console.log("RM1:", emp.reportingManager1Id, "RM2:", emp.reportingManager2Id);


  // Load dropdowns with selected value
  // Load dropdowns with selected value
await loadDropdown('departments', 'editdept', 'Department', emp.departmentId || emp.department?.deptId);
await loadDropdown('levels', 'editlevel', 'Level', emp.levelId || emp.level?.level_id);
await loadDropdown('shifts', 'editshift', 'Shift', emp.shiftId || emp.shift?.shift_id);
await loadDropdown('roles', 'editrole', 'Role', emp.roleId || emp.role?.role_id);

await loadReportingManagersByDepartment(
  emp.departmentId || emp.department?.deptId,
  'editrep_name1',
  'editrep_name2',
  emp.reportingManager1Id || emp.reportingManager1?.rm_id,
  emp.reportingManager2Id || emp.reportingManager2?.rm_id
);


  // Show modal
  const modal = new bootstrap.Modal(document.getElementById('editModal'));
  modal.show();
}



// Submit Edit Modal
document.getElementById('editEmpForm').addEventListener('submit', async function (e) {
  e.preventDefault();
  const id = document.getElementById('emp_id_hidden').value;
  const f = e.target;

  const emp = {
    emp_code: +f.editempCode.value,
    first_name: f.editfirstName.value,
    last_name: f.editlastName.value,
    dateOfBirth: f.editdob.value,
    gender: f.editgender.value,
    email: f.editemail.value,
    personal_email: f.editpersonalEmail.value,
    address: f.editaddress.value,
    contact: f.editcontact.value,
    joining_date: f.editjoiningDate.value,
    company: f.editcompany.value,
    employee_status: f.editemployeeStatus.value,
    joining_status: f.editjoiningStatus.value,
    working_status: f.editworkingStatus.value,
    departmentId: +f.editdept.value,
    levelId: +f.editlevel.value,
    shiftId: +f.editshift.value,
    roleId: +f.editrole.value,
    reportingManager1Id: f.editrep_name1.value ? +f.editrep_name1.value : null,
    reportingManager2Id: f.editrep_name2.value ? +f.editrep_name2.value : null
  };

  const formData = new FormData();
  formData.append('employee', JSON.stringify(emp));

  const res = await fetch(`${API}/employees/${id}`, {
    method: 'PUT',
    body: formData
  });

  if (res.ok) {
    bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
    await loadEmployees();
    showSuccessAlert("Employee updated successfully");
  } else {
    console.error(await res.text());
  }
});

function confirmDelete(empId) {
  selectedDeleteEmpId = empId;
  const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
  modal.show();
}


// Delete
document.getElementById('confirmDeleteBtn').addEventListener('click', async () => {
  if (!selectedDeleteEmpId) return;

  try {
    const res = await fetch(`${API}/employees/${selectedDeleteEmpId}`, {
      method: 'DELETE'
    });

    if (res.ok) {
      
      showDangerAlert("Employee deleted successfully");
      await loadEmployees();
    } else {
            showDangerAlert("Employee deleted successfully");
      await loadEmployees();
    }
  } catch (err) {
    console.error("Error during delete:", err);
  } finally {
    selectedDeleteEmpId = null;
    const modalEl = document.getElementById('deleteConfirmModal');
    if (bootstrap.Modal.getInstance(modalEl)) {
      bootstrap.Modal.getInstance(modalEl).hide();
    }
  }
});



// Alert
function showSuccessAlert(message) {
  const alert = document.createElement('div');
  alert.className = 'alert alert-success alert-dismissible fade show fixed-top mx-auto m-3';
  alert.role = 'alert';
  alert.style.width = 'fit-content';
  alert.innerHTML = `${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
  document.body.appendChild(alert);
  setTimeout(() => alert.remove(), 3000);
}

function showDangerAlert(message) {
  const alert = document.createElement('div');
  alert.className = 'alert alert-danger alert-dismissible fade show fixed-top mx-auto m-3';
  alert.role = 'alert';
  alert.style.width = 'fit-content';
  alert.innerHTML = `${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
  document.body.appendChild(alert);
  setTimeout(() => alert.remove(), 3000);
}


// On page load
// ... [your code unchanged till this point]

// On page load
document.addEventListener('DOMContentLoaded', () => {
  loadAllDropdowns();
  loadEmployees();

  document.getElementById('employeeForm').addEventListener('submit', submitForm);

  // Add Form - Department change listener
  document.getElementById('dept').addEventListener('change', (e) => {
    if (e.target.value) loadReportingManagersByDepartment(e.target.value);
  });

  // ✅ Edit Form - Department change listener (Fix Added)
  document.getElementById('editdept').addEventListener('change', async (e) => {
    const deptId = e.target.value;
    if (deptId) {
      await loadReportingManagersByDepartment(
        deptId,
        'editrep_name1',
        'editrep_name2'
      );
    }
  });
});



// Optional scroll arrows
function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;
  container.scrollLeft += (direction === "left" ? -scrollAmount : scrollAmount);
}


function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;

  if (direction === "left") {
    container.scrollLeft -= scrollAmount;
  } else {
    container.scrollLeft += scrollAmount;
  }
}

