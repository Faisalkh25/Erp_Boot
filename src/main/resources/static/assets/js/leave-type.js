
const apiUrl = 'http://localhost:8080/api/leavetypes';

document.addEventListener("DOMContentLoaded", () => {
    loadLeaveTypes();

    document.getElementById("leaveTypeForm").addEventListener("submit", (e) => {
        e.preventDefault();
        addLeaveType();
    });

    document.getElementById("editForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("edit_dept_id").value;
        updateLeaveType(id);
    });

    document.getElementById("confirmDeleteBtn").addEventListener("click", () => {
        deleteLeaveType(confirmDeleteId);
    });
});

function loadLeaveTypes() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("leaveTypeTableBody");
            tbody.innerHTML = "";

            data.forEach(leave => {
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td class="text-center">${leave.leavetype_id}</td>
                    <td class="text-center">${leave.leave_type}</td>
                    <td class="text-center">
                        <i class="fa-solid fa-pen-to-square text-primary me-2"
                            onclick="showEditModal(${leave.leavetype_id})"
                            style="cursor: pointer; font-size: 18px;"></i>
                        <i class="fa-solid fa-trash text-danger"
                            onclick="showDeleteModal(${leave.leavetype_id})"
                            style="cursor: pointer; font-size: 18px;"></i>
                    </td>
                `;

                tbody.appendChild(tr);
            });
        });
}

function addLeaveType() {
    const leaveTypeInput = document.getElementById("leave_type").value;

    fetch(apiUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ leave_type: leaveTypeInput })
    })
        .then(res => res.json())
        .then(() => {
            resetForm();
            loadLeaveTypes();
            showAlert("Leave Type added successfully!", "success");
        });
}

function showEditModal(id) {
    fetch(`${apiUrl}/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("edit_dept_id").value = data.leavetype_id;
            document.getElementById("level_type").value = data.leave_type;
            new bootstrap.Modal(document.getElementById('editLeveltypeModal')).show();
        });
}

function updateLeaveType(id) {
    const updatedLeaveType = {
        leave_type: document.getElementById("level_type").value
    };

    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedLeaveType)
    })
        .then(res => res.json())
        .then(() => {
            loadLeaveTypes();
            document.getElementById("editForm").reset();
            bootstrap.Modal.getInstance(document.getElementById('editLeveltypeModal')).hide();
            showAlert("Record Updated Successfully!", "success");
        });
}

let confirmDeleteId = null;

function showDeleteModal(id) {
    confirmDeleteId = id;
    new bootstrap.Modal(document.getElementById('deleteConfirmModal')).show();
}

function deleteLeaveType(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE'
    })
        .then(() => {
            loadLeaveTypes();
            bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal')).hide();
            showAlert("Record Deleted Successfully!", "danger");
        });
}

function resetForm() {
    document.getElementById("leaveTypeForm").reset();
    document.getElementById("leavetype_id").value = "";
}

function showAlert(message, type = "success") {
    const wrapper = document.createElement("div");
    wrapper.innerHTML = `
      <div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>`;
    const placeholder = document.getElementById("alert-placeholder");
    placeholder.innerHTML = "";
    placeholder.appendChild(wrapper);
  
    setTimeout(() => {
      const alertInstance = bootstrap.Alert.getOrCreateInstance(wrapper.querySelector(".alert"));
      if (alertInstance) alertInstance.close();
    }, 3000);
  }
