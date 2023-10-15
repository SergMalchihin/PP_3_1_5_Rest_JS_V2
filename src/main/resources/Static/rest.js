$(async function () {
    await getAuthUser();
    await getAllUsers();
    await newUser();
    await removeUser();
    await updateUser();

})

async function getAuthUser() {
    fetch("/api/users/user")
        .then(response => response.json())
        .then(data => {
            document.querySelector('#userName').textContent = data.email;
            document.querySelector('#userRole').textContent = (data.roles.map(role => " " + role.roleName.substring(5)).join(' '));
            let user = `$(
                    <tr>
                    <td>${data.id}</td>
                    <td>${data.firstName}</td>
                    <td>${data.lastName}</td>
                    <td>${data.email}</td>
                    <td>${data.roles.map(role => " " + role.roleName.substring(5))}</td>)</tr>`;
            $('#userInfo').append(user);
        })
        .catch(error => console.log(error))
}

// READ
async function getAllUsers() {
    const userTable = $('#tbodyAllUserTable');
    userTable.empty();
    fetch("/api/admin/users")
        .then(res => res.json())
        .then(data => {
            data.forEach(user => {
                let tableWithUsers = `$(
                        <tr>
                            https://users.metu.edu.tr/e163109/Term_Paper_CENG_562.pdf<td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.map(role => " " + role.roleName.substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-bs-toggle="modal"
                                 data-id="${user.id}" data-bs-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                                 data-id="${user.id}" data-bs-target="#delete">Delete</button>
                            </td>
                        </tr>)`;
                userTable.append(tableWithUsers);
            })
        }).catch(error => console.log(error))
}

// CREATE
async function newUser() {
    fetch("/api/admin/roles")
        .then(response => response.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.roleName.substring(5);
                $('#rolesNew')[0].appendChild(el);
            })
        }).catch(error => console.log(error));

    const createForm = document.forms["createForm"];
    const createLink = document.querySelector('#addNewUser');
    const createButton = document.querySelector('#createUserButton');


    createLink.addEventListener('click', (event) => {
        event.preventDefault();
        createForm.style.display = 'block';
    });
    createForm.addEventListener('submit', addNewUser)
    createButton.addEventListener('click', addNewUser);

    async function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        for (let i = 0; i < createForm.role.options.length; i++) {
            if (createForm.role.options[i].selected) newUserRoles.push({
                id: createForm.role.options[i].value,
                roles: createForm.role.options[i].text
            })
        }

        fetch("/api/admin/users", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: createForm.firstName.value,
                lastName: createForm.lastName.value,
                email: createForm.email.value,
                password: createForm.password.value,
                roles: newUserRoles
            })
        }).then(() => {
            createForm.reset();
            $(async function () {
                await getAllUsers();
            })
        })
    }
}

// DELETE
$('#delete').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function getUser(id) {
    let response = await fetch("/api/admin/users/" + id);
    return await response.json();
}

async function showDeleteModal(id) {
    let user = await getUser(id)
    const form = document.forms["deleteForm"];

    form.idDeleteUser.value = user.id;
    form.firstNameDeleteUser.value = user.firstName;
    form.lastNameDeleteUser.value = user.lastName;
    form.emailDeleteUser.value = user.email;


    $('#rolesDeleteUser').empty();

    user.roles.forEach(role => {
        let el = document.createElement("option");
        el.text = role.roleName.substring(5);
        el.value = role.id;
        $('#rolesDeleteUser')[0].appendChild(el);

    });

}

$('#deleteUserButton').click(() => {
    removeUser();
});

async function removeUser() {

    const deleteForm = document.forms["deleteForm"];
    const id = deleteForm.idDeleteUser.value;


    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("/api/admin/users/" + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            getAllUsers();
            $('#topCloseButtonDelete').click();

        }).catch(error => console.log(error))
    })

}

//EDIT
$('#edit').on('show.bs.modal', (ev) => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showEditModal(id);
});

async function showEditModal(id) {

    let user = await getUser(id);
    const form = document.forms["editForm"];

    form.idEditUser.value = user.id;
    form.firstNameEditUser.value = user.firstName;
    form.lastNameEditUser.value = user.lastName;
    form.emailEditUser.value = user.email;
    form.passwordEditUser.value = user.password;

    $('#rolesEditUser').empty();
    fetch("/api/admin/roles")
        .then(response => response.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.roleName.substring(5);
                $('#rolesEditUser')[0].appendChild(el);
            })
        })
}

$('#editUserButton').click(() => {
    updateUser();
});

async function updateUser() {

    const editForm = document.forms["editForm"];
    const id = editForm.idEditUser.value;

    editForm.addEventListener("submit", async (ev) => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.rolesEditUser.options.length; i++) {
            if (editForm.rolesEditUser.options[i].selected) editUserRoles.push({
                id: editForm.rolesEditUser.options[i].value,
                role: editForm.rolesEditUser.options[i].text
            })
        }

        fetch("/api/admin/users/"+id, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: id,
                firstName: editForm.firstNameEditUser.value,
                lastName: editForm.lastNameEditUser.value,
                email: editForm.emailEditUser.value,
                password: editForm.passwordEditUser.value,
                roles: editUserRoles,
            }),
        })
            .then(() => {
                getAllUsers();
                $('#editFormCloseButton').click();
            });
    });
}
