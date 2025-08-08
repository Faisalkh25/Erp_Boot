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

// On page load, fetch employee profile and set welcome name
document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");

    if (token) {
        fetch("http://localhost:8080/api/employees/profile", {
            method: "GET",
            headers: getAuthHeaders()
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch employee profile");
                }
                return response.json();
            })
            .then(employee => {
                const fullName = `${employee.first_name} ${employee.last_name}`;
                localStorage.setItem("employeeName", fullName);

                const heading = document.getElementById("welcomeHeading");
                if (heading) {
                    heading.textContent = `Welcome ${fullName}..!`;
                }
            })
            .catch(error => {
                console.error("Error loading employee data:", error);
            });
    } else {
        console.warn("JWT token not found. Please login first.");
    }
});