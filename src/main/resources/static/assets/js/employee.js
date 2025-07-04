const API = 'http://localhost:8080/api';

// Load dropdowns dynamically
async function loadDropdown(endpoint, selector, placeholder = 'Select') {
  try {
    const res = await fetch(`${API}/${endpoint}`);
    const arr = await res.json();
    // console.log(`Data for ${endpoint}:`, arr);
    const sel = document.getElementById(selector);

    const fieldMap = {
      departments: { id: 'deptId', name: 'dept_name' },
      levels: { id: 'level_id', name: 'level' },
      shifts: { id: 'shift_id', name: 'shift_name' },
      roles: { id: 'role_id', name: 'role_name' }
    };

    const { id: idField, name: nameField } = fieldMap[endpoint];

    sel.innerHTML = `<option value="">-- ${placeholder} --</option>` +
    arr.map(o => {
      "Option:", o[idField], o[nameField]; //  Debug each option
      return `<option value="${o[idField]}">${o[nameField]}</option>`;
    }).join('');
} catch (error) {
  console.error(`Error loading ${endpoint}:`, error);
}
}

//function for loading reporting managers By Department
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
    password: f.password.value,
    
    
    departmentId: f.dept.value ? parseInt(f.dept.value) : null,
    levelId: f.level.value ? parseInt(f.level.value) : null,
    shiftId: f.shift.value ? parseInt(f.shift.value) : null,
    roleId: f.role.value ? parseInt(f.role.value) : null
  };

  if (f.rep_name1.value) {
    emp.reportingManager1Id = parseInt(f.rep_name1.value);
  }

  if (f.rep_name2.value) {
    emp.reportingManager2Id = parseInt(f.rep_name2.value);
  }

  return emp;
}


// Submit Form
async function submitForm(e) {
  e.preventDefault();
  const f = document.getElementById('employeeForm');
  const formData = new FormData();
  const emp = buildEmpFromForm(); 
 
  formData.append('employee', JSON.stringify(buildEmpFromForm()));

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
  } else {
    console.error('Error submitting form:', await res.text());
  }
}


// Load employee records
async function loadEmployees() {
  const res = await fetch(`${API}/employees`);
  const arr = await res.json();
  const tbody = document.querySelector('#employeeTable tbody');

  if (!tbody) {
    console.warn("Table body element not found");
    return;
  }

  tbody.innerHTML = arr.map(emp => `
    <tr>
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
        <button data-id="${emp.emp_id}" class="edit btn btn-warning btn-sm">Edit</button>
        <button data-id="${emp.emp_id}" class="delete btn btn-danger btn-sm">Delete</button>
      </td>
    </tr>
  `).join('');

  tbody.querySelectorAll('.edit').forEach(btn => {
    btn.addEventListener('click', () => fillForm(btn.dataset.id));
  });

  tbody.querySelectorAll('.delete').forEach(btn => {
    btn.addEventListener('click', () => deleteEmp(btn.dataset.id));
  });
}



// Fill form with data
async function fillForm(id) {
  const res = await fetch(`${API}/employees/${id}`);
  const emp = await res.json();
  const f = document.getElementById('employeeForm');

  f.dataset.id = emp.emp_id;
  f.empCode.value = emp.emp_code;
  f.firstName.value = emp.first_name;
  f.lastName.value = emp.last_name;
  f.dob.value = emp.dateOfBirth;
  f.gender.value = emp.gender;
  f.email.value = emp.email;
  f.personalEmail.value = emp.personal_email;
  f.address.value = emp.address;
  f.contact.value = emp.contact;
  f.joiningDate.value = emp.joining_date;
  f.company.value = emp.company;
  f.employeeStatus.value = emp.employee_status;
  f.joiningStatus.value = emp.joining_status;
  f.workingStatus.value = emp.working_status;
  f.dept.value = emp.departmentId || '';
  f.level.value = emp.levelId || '';
  f.shift.value = emp.shiftId || '';
  f.role.value = emp.roleId || '';
  f.rep_name1.value = emp.reportingManager1Id || '';
  f.rep_name2.value = emp.reportingManager2Id || '';
}

// Delete employee
async function deleteEmp(id) {
  if (!confirm('Are you sure you want to delete this employee?')) return;
  await fetch(`${API}/employees/${id}`, { method: 'DELETE' });
  await loadEmployees();
}

// Page load
document.addEventListener('DOMContentLoaded', () => {
  loadAllDropdowns();
  loadEmployees();
  document.getElementById('employeeForm').addEventListener('submit', submitForm);

  document.getElementById('dept').addEventListener('change', (e) => {
    const selectedDeptId = e.target.value;
    if (selectedDeptId) {
      loadReportingManagersByDepartment(selectedDeptId);
    }
  });
  

});



// Optional scroll arrows
function scrollTable(direction) {
  const container = document.getElementById("scrollTableContainer").querySelector(".custom-scroll");
  const scrollAmount = 200;
  container.scrollLeft += (direction === "left" ? -scrollAmount : scrollAmount);
}
