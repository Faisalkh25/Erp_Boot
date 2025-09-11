//helper function
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
//

function getEmpId() {
      return localStorage.getItem('empId'); 
}

let currentEmployee = null;
let ccEmployeeIds = [];
let ccEmployeeNames = [];

document.addEventListener('DOMContentLoaded', () => {
       const empId = getEmpId();
       if(!empId) {
          console.warn('No empId in localstorage. Please login.');
          return;
       }

     const leaveTypeSelect =  document.getElementById('leave-type');
     const applyToSelect = document.getElementById('apply-to');
     const ccToInput = document.getElementById('cc-to');
     const ccModal = document.getElementById('ccModal');
     const ccModalBody = ccModal ? ccModal.querySelector('.modal-body tbody') : null;
     const saveCcBtn = document.getElementById('saveCcBtn');
     const attachmentInput = document.getElementById('attachment');
     const fromDateInput = document.getElementById('from-date');
     const toDateInput = document.getElementById('to-date');
     const nodInput = document.getElementById('NOD');
     const balanceInput = document.getElementById('balance');
     const applyLeaveForm = document.getElementById('applyLeaveForm');

     //initialize
     loadEmployeeDetails(empId)
     .then(() => Promise.all([loadLeaveTypes(), loadEmployeesForCc()]))
     .catch(err => {
          console.error('Initialization error', err);
          Swal.fire('Error', 'Failed to initialize leave form.', 'error');
     });

    //calculate number of days
    [fromDateInput, toDateInput].forEach(inp => {
         if(inp) inp.addEventListener('change', computeNOD);
    });

    //when leave changes. calculate leave balance (As of now i have not created leave balance api)
    if(leaveTypeSelect) {
        leaveTypeSelect.addEventListener('change', () => {
            //TODO: BALANCE API HERE IF AVAILABLE
            balanceInput.value = '';
        });
    }

    //saving cc modal
    if(saveCcBtn) {
         saveCcBtn.addEventListener('click', () => {
              ccEmployeeIds = [];
              ccEmployeeNames = [];

              //get all checked inputs in the modal
              if(ccModalBody) {
                 ccModalBody.querySelectorAll('input[type="checkbox"]').forEach(chk => {
                     if(chk.checked) {
                        const empId = chk.value;
                        const empName = chk.dataset.name || chk.dataset.email || chk.nextSibling?.textContent?.trim() || empId;
                        ccEmployeeIds.push(parseInt(empId));
                        ccEmployeeNames.push(empName);
                    }
                 });
              }
              ccToInput.value = ccEmployeeNames.join(', ');
              //close modal
              const bsModal = bootstrap.Modal.getInstance(ccModal) || new bootstrap.Modal(ccModal);
              bsModal.hide();
         });
    }

    //submit form
    if(applyLeaveForm) {
         applyLeaveForm.addEventListener('submit', async (e) => {
             e.preventDefault();

             //build dto
             const dto = {
                   employeeId: parseInt(getEmpId()),
                   leaveTypeId: parseInt(leaveTypeSelect.value),
                   sessionFrom: document.getElementById('form-session').value,
                   sessionTo: document.getElementById('to-session').value,
                  fromDate: document.getElementById('from-date').value,
                  toDate: document.getElementById('to-date').value,
                  contactDetails: document.getElementById('contact').value,
                  applyToId: parseInt(applyToSelect.value), 
                  ccEmployeeIds: ccEmployeeIds,
                  reason: document.getElementById('description').value

             };

             if(!dto.leaveTypeId) { Swal.fire('Validation', 'Please select leave type', 'warning'); return; }
             if(!dto.fromDate || !dto.toDate) { Swal.fire('Validation', 'Please select from and to dates', 'warning'); return; }
             if(!dto.applyToId) { Swal.fire('Validation', 'Apply To is required', 'warning'); return; }

             const formData =  new FormData();
             formData.append('leaveDto', JSON.stringify(dto));

             const file = attachmentInput && attachmentInput.files && attachmentInput.files[0];
             if(file) formData.append('file', file);

             const headers = getAuthHeaders(false);

             try {
                 const res = await fetch("/api/leaves", {
                      method: 'POST',
                      headers,
                      body:formData
                 });

                 if(!res.ok) {
                    const text = await res.text();
                    throw new Error(text || `HTTP ${res.status}`);
                 }

                 const saved = await res.json();
                 Swal.fire({ icon: 'success', title: 'Applied', text: 'Leave applied successfully!', timer:3000, showConfirmButton: true});
                 applyLeaveForm.reset();
                 ccEmployeeIds = [];
                 ccEmployeeNames = [];
                 ccToInput.value = '';
                 nodInput.value = '';
                 balanceInput.value = '';
             } catch (error) {
                console.error('Apply leave failed', err);
                Swal.fire('Error', err.message || 'Failed to apply leave', 'error');
             }

            });
    }

    //helper function
    //load details of current employee(to know wbout confirm/probation/ & reporting manager)

    async function loadEmployeeDetails(empId) {
        console.log("Fetching employee details for empId:", empId);  // debug log   
         const res = await fetch(`/api/employees/${empId}`, {
            headers: getAuthHeaders()
         });

         if(!res.ok) {
            throw new Error('Failed to fetch employee details');
         }
         const emp = await res.json();
         currentEmployee = emp;

         populateApplyTo(emp);
    }

    function populateApplyTo(emp) {
         applyToSelect.innerHTML = '<option value="" disabled selected>-Select-</option>';

         if(emp.reportingManager1Id && emp.reportingManager1Name) {
              applyToSelect.innerHTML += `
               <option value="${emp.reportingManager1Id}">${emp.reportingManager1Name}</option>
              `;
         }
    }

    //load leave types and filtering for probation employees
    async function loadLeaveTypes() {
        const res = await fetch('/api/leavetypes', {
            headers: getAuthHeaders()
        });
        if(!res.ok) {
            throw new Error("Failed to fetch leave types");
        }
        const types = await res.json();

        //will detect if probation/confirm
        const isProbation = currentEmployee && (String(currentEmployee.employee_status || '').toLowerCase().includes('provation'));
    
        // const allowedIfProbation = ['Loss of Pay/Leave without Pay/Unpaid Leave', 'Mark Attendance'];

        leaveTypeSelect.innerHTML = '<option value="" disabled selected>-Select Type-</option>';

        types.forEach(t => {
             const label = t.leave_type || '';
             const id = t.leavetype_id; 
            

             if(isProbation) {
                 if(!t.allowedForProbation) return;
             }
             const opt = document.createElement('option');
             opt.value = id;
             opt.textContent = label;
             leaveTypeSelect.appendChild(opt);
        });
    }

     // Load all employees into CC modal (table body)
     async function loadEmployeesForCc() {
        if (!ccModalBody) return;
        ccModalBody.innerHTML = ''; // clear

        const res = await fetch('/api/employees', { headers: getAuthHeaders() });
        if (!res.ok) {
            throw new Error('Failed to fetch employees for CC');
        }
        const list = await res.json();

        // Build table rows with checkbox
        list.forEach(emp => {
            const tr = document.createElement('tr');
            const tdName = document.createElement('td');
            const chk = document.createElement('input');
            chk.type = 'checkbox';
        
            // set value to employee id
            const id = emp.empId || emp.id || emp.emp_id;
            chk.value = id;
        
            // include empCode in dataset for reference if needed
            const code = emp.empCode || emp.emp_code || '';
            chk.dataset.name = `${emp.first_name || ''} ${emp.last_name || ''} (${code})`.trim() || emp.email || id;
        
            tdName.appendChild(chk);
        
            // show name + empCode next to checkbox
            const span = document.createTextNode(
                ' ' + ((emp.first_name || '') + (emp.last_name ? ' ' + emp.last_name : '') + ` (${code})`).trim()
            );
            tdName.appendChild(span);
        
            const tdEmail = document.createElement('td');
            tdEmail.textContent = emp.email || emp.personal_email || '';
        
            tr.appendChild(tdName);
            tr.appendChild(tdEmail);
            ccModalBody.appendChild(tr);
        });
        
    }

    // compute number of days inclusive (simple)
    function computeNOD() {
        const f = fromDateInput.value;
        const t = toDateInput.value;
        if (!f || !t) {
            nodInput.value = '';
            return;
        }
        const d1 = new Date(f);
        const d2 = new Date(t);
        if (isNaN(d1.getTime()) || isNaN(d2.getTime())) {
            nodInput.value = '';
            return;
        }
        if (d2 < d1) {
            Swal.fire('Validation', '"To" date must be same or after "From" date', 'warning');
            nodInput.value = '';
            return;
        }
        const msPerDay = 24 * 60 * 60 * 1000;
        const diffDays = Math.round((d2 - d1) / msPerDay) + 1; 
        nodInput.value = diffDays;
    }

     
})
