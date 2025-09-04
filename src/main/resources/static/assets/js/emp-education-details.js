// Helper function for generating header
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

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("employeeEducationForm");
    const tableBody = document.getElementById("educationTableBody");

    let editing = false;   // true if editing existing record
    let educationId = null; // store id for editing

   function loadEducationRecords() {
       const empId = getEmpId();

       fetch(`http://localhost:8080/api/education-details/${empId}`, {
           method: 'GET',
           headers: getAuthHeaders()
       })
       .then(res => {
           if(!res.ok) throw new Error("Failed to fetch education records");
           return res.json();
       })
       .then(records => {
           tableBody.innerHTML = '';

           if(!records || records.length === 0) {
              tableBody.innerHTML = `
                 <tr> 
                    <td colspan="7" class="text-center text-muted">No data available</td>
                    </tr> 
              `;
              return;
           }
           records.forEach(record => {
               const row = document.createElement("tr");
               row.innerHTML = `
                   <td class="text-center">${record.qualification || ''}</td>
                   <td class="text-center">${record.university || ''}</td>
                   <td class="text-center">${record.roleNumber || ''}</td>
                   <td class="text-center">${record.subject || ''}</td>
                   <td class="text-center">${record.passingYear || ''}</td>
                   <td class="text-center">${record.qualification || ''}</td>
                   <td class="text-center">${record.marks || ''}</td>

                   <td class="text-center">
                    <i class="fa-solid fa-pen-to-square text-primary me-2 editBtn" style="cursor: pointer;"></i>
                    <i class="fa-solid fa-trash text-danger deleteBtn" style="cursor: pointer;"></i>
                </td>
                `;

                //edit
                row.querySelector(".editBtn").addEventListener("click", () => {
                     fillFormForEdit(record);
                });

                //delete
                row.querySelector(".deleteBtn").addEventListener("click", () => {
                    deleteEducation(record.educationDetailsId);
                });

                tableBody.appendChild(row);
           });
       })
       .catch(err => console.error(err));
   }


    //  Form submit: Save
    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const dto = {
            qualification: document.getElementById("qualification").value,
            university: document.getElementById("university").value,
            roleNumber: document.getElementById("role_no").value, 
            subject: document.getElementById("subject").value,
            passingYear: document.getElementById("year").value,
            marks: document.getElementById("marks").value,
            employeeId: getEmpId()
        };

        let url, method;
        if(editing) {
              url = `http://localhost:8080/api/education-details/${educationId}`;
              method = "PUT";
        } else {
             url = "http://localhost:8080/api/education-details";
             method = "POST";
        }

        if (editing) {
            dto.educationDetailsId = educationId;
        }

        fetch(url, {
            method: method,
            headers: getAuthHeaders(),
            body: JSON.stringify(dto)
        })
        .then(res => {
             if(!res.ok) return res.text().then(text => { throw new Error(text) });
             return res.json();
        })
        .then(() => {
            alert("Education details saved successfully!");
            form.reset();
            editing = false;
            educationId = null;
            loadEducationRecords(); 

            // Close modal
            const modalEl = document.getElementById("employeeEducationModal");
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();
        })
        .catch(err => console.error(err));
    });

    //  Fill form for edit
   function fillFormForEdit(data) {
    editing = true;
    educationId = data.educationDetailsId || data.id;

    document.getElementById("qualification").value = data.qualification || "";
    document.getElementById("university").value = data.university || "";
    document.getElementById("role_no").value = data.roleNumber || "";
    document.getElementById("subject").value = data.subject || "";
    document.getElementById("year").value = data.passingYear || "";
    document.getElementById("marks").value = data.marks || "";

    const modalEl = document.getElementById("employeeEducationModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
}


    //  Delete record
    function deleteEducation(id) {
        if (!confirm("Are you sure you want to delete this record?")) return;

        fetch(`http://localhost:8080/api/education-details/${id}`, {
            method: "DELETE",
            headers: getAuthHeaders()
        })
        .then(res => {
            if (!res.ok) throw new Error("Failed to delete education record");
            loadEducationRecords();
        })
        .catch(err => console.error(err));
    }

    //  Get logged-in employee ID
    function getEmpId() {
        return localStorage.getItem("empId");
    }

    // Initial load
    loadEducationRecords();
});
