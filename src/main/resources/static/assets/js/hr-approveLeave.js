//helper function for generating header
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

//load all leaves
async function loadAllLeaves() {
  try {
    const response = await fetch("http://localhost:8080/api/hr/leaves/leave-all", {
      method: "GET",
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      throw new Error("Failed to fetch leave applications");
    }

    const leaves = await response.json();

    // split by status
    const pendingLeaves = leaves.filter(l => !l.status || l.status === "PENDING");
    const approvedLeaves = leaves.filter(l => l.status === "APPROVED");
    const rejectedLeaves = leaves.filter(l => l.status === "REJECTED");

    renderLeaveTable("#leaveApplicationTable tbody", pendingLeaves, true);   // show action buttons only for pending
    renderLeaveTable("#approvedLeaveTable tbody", approvedLeaves, false);
    renderLeaveTable("#rejectedLeaveTable tbody", rejectedLeaves, false);

  } catch (error) {
    console.error("Error loading leaves: ", error);
  }
}

//render rows into given table
function renderLeaveTable(selector, leaves, showActions) {
  const tbody = document.querySelector(selector);
  tbody.innerHTML = "";

  if (!leaves || leaves.length === 0) {
    tbody.innerHTML = `<tr><td colspan="12" class="text-center">No leave applications found</td></tr>`;
    return;
  }

  leaves.forEach((leave) => {
    const row = document.createElement("tr");

    row.innerHTML = `
      <td>${leave.status || "Pending"}</td>
      <td>${leave.employeeId || "-"}</td>
      <td>${leave.leaveTypeName || "-"}</td>
      <td>${leave.applyToName || "-"}</td>
      <td>${leave.fromDate || "-"}</td>
      <td>${leave.toDate || "-"}</td>
      <td>${leave.sessionFrom || "-"}</td>
      <td>${leave.sessionTo || "-"}</td>
      <td>${leave.qty || "-"}</td>
      <td>${leave.dateCreated ? leave.dateCreated.split("T")[0] : "N/A"}</td>
      <td>${leave.attachmentPath ? `<a href="/${leave.attachmentPath}" target="_blank">View</a>` : "N/A"}</td>
      <td>
        ${
          showActions
            ? `
              <button class="btn btn-success btn-sm me-1 approve-btn" data-leave-id="${leave.leaveApplicationId}">Approve</button>
              <button class="btn btn-danger btn-sm reject-btn" data-leave-id="${leave.leaveApplicationId}">Reject</button>
              `
            : "-"
        }
      </td>
    `;

    tbody.appendChild(row);
  });

  // add button listeners only if action buttons are shown
  if (showActions) {
    document.querySelectorAll(".approve-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleLeaveAction(btn.dataset.leaveId, "APPROVED"));
    });

    document.querySelectorAll(".reject-btn").forEach((btn) => {
      btn.addEventListener("click", () => handleLeaveAction(btn.dataset.leaveId, "REJECTED"));
    });
  }
}

//handle approve/reject
async function handleLeaveAction(leaveId, status) {
  const confirmMsg = `Are you sure you want to ${status.toLowerCase()} this leave?`;
  if (!confirm(confirmMsg)) {
    return;
  }

  try {
    const endpoint =
      status === "APPROVED"
        ? `http://localhost:8080/api/hr/leaves/${leaveId}/approve`
        : `http://localhost:8080/api/hr/leaves/${leaveId}/reject`;

    const response = await fetch(endpoint, {
      method: "PUT",
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      throw new Error("Failed to update leave status");
    }

    alert(`Leave ${status.toLowerCase()} successfully!`);
    loadAllLeaves(); // refresh tables
  } catch (error) {
    console.error("Error updating leave: ", error);
    alert("Error updating leave status");
  }
}

//auto load
document.addEventListener("DOMContentLoaded", loadAllLeaves);
console.log("this is hr approve leave.js")





