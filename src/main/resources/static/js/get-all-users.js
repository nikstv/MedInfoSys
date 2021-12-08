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
            "<td>" + user.firstName + "</td>\n" +
            "<td>" + user.lastName + "</td>\n" +
            "<td>" + user.personalCitizenNumber + "</td>\n" +
            "<td>" + userRoles + "</td>\n" +
            "<td>\n" +
            "<a class=\"btn btn-info\" href=\"/user/" + user.id + "/details\">View</a>\n" +
            "<a class=\"btn btn-info\" href=\"/admin/user/" + user.id + "/edit\">Edit</a>\n" +
            "<form method=\"POST\" action=\"/admin/user/" + user.id + "/invalidate-session\" class=\"post-button-form invalidateAcc\">\n" +
            "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
            "<button class=\"btn btn-info\" type=\"submit\">Invalidate</button>\n" +
            "</form>\n" +
            "<form method=\"POST\" action=\"/admin/user/" + user.id + "/delete-account\" class=\"post-button-form deleteAcc\">\n" +
            "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
            "<button class=\"btn btn-info\" type=\"submit\">Delete</button>\n" +
            "</form>\n" +
            "<form method=\"POST\" action=\"/admin/user/" + user.id + "/lock-account-toogle\" class=\"post-button-form lockAcc\">\n" +
            "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
            "<button class=\"btn btn-info\" type=\"submit\">Lock</button>\n" +
            "</form>\n" +
            "</td>\n" +
            "</tr>"

        tableBody.appendChild(row);

        let isAdmin = document.querySelector("#isAdmin");

        if (isAdmin.value === "false") {
            let invalidateAcc = document.querySelector(".invalidateAcc");
            let deleteAcc = document.querySelector(".deleteAcc");
            let lockAcc = document.querySelector(".lockAcc");

            invalidateAcc.remove();
            deleteAcc.remove();
            lockAcc.remove();
        } else if (user.accountNonLocked === false) {
            let lockPostForms = document.querySelectorAll(".lockAcc");
            let lastLockButton = lockPostForms[lockPostForms.length - 1].querySelector("button");
            lastLockButton.textContent = "Unlock";
        }
    }
});