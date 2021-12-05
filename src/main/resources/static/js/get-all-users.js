async function getAllUsers(link) {
    let rawData = await fetch(link);
    let dataJson = await rawData.json();
    return dataJson;
}

document.addEventListener("DOMContentLoaded", async () => {
    let csrf = document.getElementsByName("_csrf")[0];
    let link = document.getElementById("link").value;

    let tableBody = document.getElementById("usersTable")
    tableBody.innerHTML = "";

    let users = await getAllUsers(link);

    for (const user of users) {
        let userRoles = "";
        let roles = user.roles;
        for (const role of roles) {
            userRoles += role.role + " ";
        }

        let row = document.createElement("tr");
        row.innerHTML = "<tr>\n" +
            "<td>" + user.id + "</td>\n" +
            "<td>" + user.username + "</td>\n" +
            "<td>" + user.firstName + "</td>\n" +
            "<td>" + user.lastName + "</td>\n" +
            "<td>" + user.personalCitizenNumber + "</td>\n" +
            "<td>" + userRoles + "</td>\n" +
            "<td>\n" +
            "<a class=\"btn btn-info\" href=\"/user/" + user.id + "/details\">View</a>\n" +
            "<a class=\"btn btn-info\" href=\"/admin/user/" + user.id + "/edit\">Edit</a>\n" +
            "<form method=\"POST\" action=\"/admin/user/" + user.id + "/invalidate-session\" class=\"post-button-form\">\n" +
            "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
            "<button class=\"btn btn-info\" type=\"submit\">Invalidate sessions</button>\n" +
            "</form>\n" +
            "</td>\n" +
            "</tr>"
        tableBody.appendChild(row);
    }
});