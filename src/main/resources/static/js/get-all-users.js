async function getAllUsers(link) {
    let rawData = await fetch(link);
    let dataJson = await rawData.json();
    return dataJson;
}

document.addEventListener("DOMContentLoaded", async () => {
    let csrf = document.getElementsByName("_csrf")[0];
    let link = document.getElementById("link").value;
    let isAdmin = document.querySelector("#isAdmin");

    let tableBody = document.getElementById("usersTable")
    tableBody.innerHTML = "";

    let users = await getAllUsers(link);
    for (const user of users) {
        let userRoles = "";
        for (const role of user.roles) {
            userRoles += role.role + " ";
        }

        let row = document.createElement("tr");
        row.innerHTML = "<tr>\n" +
            "<td>" + user.id + "</td>\n" +
            "<td>" + user.firstName + "</td>\n" +
            "<td>" + user.lastName + "</td>\n" +
            "<td>" + user.personalCitizenNumber + "</td>\n" +
            "<td>" + userRoles + "</td>\n" +
            "<td class=\"user-action-buttons\">\n" +
            "</td>\n" +
            "</tr>"

        tableBody.appendChild(row);
        let buttons = document.querySelectorAll(".user-action-buttons");

        buttons[buttons.length - 1].innerHTML += "<a class=\"btn btn-info\" href=\"/user/" + user.id + "/details\">View/Edit</a>\n";

        if (isAdmin.value === "true") {
            buttons[buttons.length - 1].innerHTML +=
                "<form method=\"POST\" action=\"/admin/user/" + user.id + "/invalidate-session\" class=\"post-button-form invalidateAcc\">\n" +
                "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
                "<button class=\"btn btn-info\" type=\"submit\">Invalidate</button>\n" +
                "</form>\n";

            if (user.accountNonLocked === true) {
                buttons[buttons.length - 1].innerHTML +=
                    "<form method=\"POST\" action=\"/admin/user/" + user.id + "/lock-account-toogle\" class=\"post-button-form lockAcc\">\n" +
                    "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
                    "<button class=\"btn btn-info\" type=\"submit\">Lock</button>\n" +
                    "</form>\n";
            } else {
                buttons[buttons.length - 1].innerHTML +=
                    "<form method=\"POST\" action=\"/admin/user/" + user.id + "/lock-account-toogle\" class=\"post-button-form lockAcc\">\n" +
                    "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
                    "<button class=\"btn btn-info\" type=\"submit\">Unlock</button>\n" +
                    "</form>\n";
            }

            buttons[buttons.length - 1].innerHTML +=
                "<form method=\"POST\" action=\"/admin/user/" + user.id + "/delete-account\" class=\"post-button-form deleteAcc\">\n" +
                "<input type=\"hidden\" name=\"_csrf\" value=" + csrf["value"] + ">" +
                "<button class=\"btn btn-danger\" type=\"submit\">Delete</button>\n" +
                "</form>\n";
        } else {
            buttons[buttons.length - 1].innerHTML += "<a class=\"btn btn-info\" href=\"/doctor/physical-examination/" + user.id + "\">Physical examination</a>\n";
        }
    }
});