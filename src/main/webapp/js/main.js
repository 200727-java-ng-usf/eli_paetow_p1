const APP_VIEW = document.getElementById('app-view');

window.onload = function() {
    loadLogin();
    document.getElementById('toLogin').addEventListener('click', loadLogin);
    document.getElementById('toHome').addEventListener('click', loadHome);
    document.getElementById('toLogout').addEventListener('click', logout);

    document.getElementById('toRegister').setAttribute('hidden', true);
    document.getElementById('toAllUsers').setAttribute('hidden', true);
    document.getElementById('toAllReimbs').setAttribute('hidden', true);
    document.getElementById('toSubmit').setAttribute('hidden', true);
    document.getElementById('toAuthorReimbs').setAttribute('hidden', true);
    document.getElementById('toLogout').setAttribute('hidden', true);

}


//----------------------LOAD VIEWS-------------------------

function loadLogin() {

    console.log('in loadLogin()');

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'login.view', true); // third parameter (default true) indicates we want to make this req async
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureLoginView();
        }
    }

}

function loadRegister() {

    console.log('in loadRegister()');

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'register.view'); // third parameter of this method is optional (defaults to true)
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureRegisterView();
        }
    }

}

function loadHome() {

    console.log('in loadHome()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }


    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'home.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureHomeView();
        }
    }

}

function loadAllUsers() {

    console.log('in loadAllUsers()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'users.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureAllUsersView();
        }
    }

}

function loadAllReimbs() {

    console.log('in loadAllReimbs()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'reimbs.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureAllReimbsView();
        }
    }

}


function loadSubmit() {

    console.log('in loadSubmit()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'submit.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureSubmitView();
        }
    }

}

function loadAuthorReimbs() {

    console.log('in loadAuthorReimbs()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'reimbs.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureAuthorReimbsView();
        }
    }


}

function loadReimbDetails() {

    console.log('in loadReimbDetails()');

    if (!localStorage.getItem('authUser')) {
        console.log('No user logged in, navigating to login screen');
        loadLogin();
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'reimbs');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {

            let array = JSON.parse(xhr.responseText);
            console.log(array);

            let roleSpecificOption = "";

            let authUser = JSON.parse(localStorage.getItem('authUser'));

            if (authUser.role == 'FinManager') {
                // if the user is a manager, make the option approve or deny
                console.log('loading FinManager option...');
                roleSpecificOption = "<div class='form-group'>"
                                        + "<label for='approveOrDeny'> Finance manager can approve or deny reimbursement</label>"
                                        +"</br>"
                                        + "<label for='options'> Yes to approve. No to deny.</label>"
                                        + "<input type='text' class='form-control' id='approveOrDeny'>"
                                        + "</div>"
                                        + "<div id='reimb-button-container'>"
                                        + "<button type='submit' class='btn btn-primary' id='approveItOrDenyIt'>Enter</button>";
            } else {
                // else the user must be an employee
                console.log('loading Employee option');
                roleSpecificOption = "<div class='form-group'>"
                                        + "<div id='reimb-button-container'>"
                                        + "<button type='submit' class='btn btn-primary' id='chooseToUpdateReimb'>Edit This Reimbursement</button>";
            }

            //format resolved field
            if (array.resolved == null) {
                array.resolved == "";
            } else {
                array.resolved = new Date(array.resolved);
                array.resolved = array.resolved.toLocaleDateString("en-US");
            }

            // format submitted field
            console.log(array.submitted);
            array.submitted = new Date(array.submitted);
            console.log(array.submitted);

            // format resolverId field
            if(array.resolverId == '0') {
                array.resolverId = "";
            }

            // format amount field
            array.amount = array.amount.toLocaleString("en-US", {
                style: "currency",
                currency: "USD"
              });

            APP_VIEW.innerHTML = "<h1>Reimbursement Details:</h1>"
                                + roleSpecificOption
                                + "<p> ID: " + array.id + "</p>"
                                + "<p> Amount: " + array.amount + "</p>"
                                + "<p> Submitted: " + array.submitted.toLocaleDateString("en-US") + "</p>"
                                + "<p> Resolved: " + array.resolved + "</p>"
                                + "<p> Description: " + array.description + "</p>"
                                + "<p> Author: " + array.authorId + "</p>"
                                + "<p> Resolver: " + array.resolverId + "</p>"
                                + "<p> Status: " + array.reimbursementStatus + "</p>"
                                + "<p> Type: " + array.reimbursementType + "</p>";

                if (authUser.role == 'FinManager') {

                    document.getElementById('approveItOrDenyIt').addEventListener('click', approveItOrDenyIt);
                } else {

                    document.getElementById('chooseToUpdateReimb').addEventListener('click', loadUpdateReimb);
                }




        }
    }

}

function loadUpdateUser() {

    console.log('in loadUpdateUser()');

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'updateuser.view');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            APP_VIEW.innerHTML = xhr.responseText;
            configureUpdateUserView();
        }
    }

}

function loadUpdateReimb() {

    console.log('in loadUpdateReimb()');


    let isReimbPending = JSON.parse(localStorage.getItem('authReimb'));
    console.log(isReimbPending.reimbursementStatus);
    if (isReimbPending.reimbursementStatus != 'PENDING') { // check is the reimb is not pending

        alert('sorry, but this reimbursement has already been resolved!');
        localStorage.removeItem('authReimb'); // clear the storage so that if the employee goes to look at their reimbs again, it will show them all
        console('this should be null now (the authReimb should be removed): ' + localStorage.getItem('authReimb'));

    } else {

        console.log('reimb is pending!');

        let xhr = new XMLHttpRequest();

        xhr.open('GET', 'updatereimb.view');
        xhr.send();

        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                APP_VIEW.innerHTML = xhr.responseText;
                configureUpdateReimbView();
            }
    }
    }


}



//----------------CONFIGURE VIEWS--------------------

function configureLoginView() {

    console.log('in configureLoginView()');

    document.getElementById('login-message').setAttribute('hidden', true);
    document.getElementById('login-button-container').addEventListener('mouseover', validateLoginForm);
    document.getElementById('login').addEventListener('click', login);

}

function configureRegisterView() {

    console.log('in configureRegisterView()');

    document.getElementById('reg-message').setAttribute('hidden', true);

    document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
    document.getElementById('email').addEventListener('blur', isEmailAvailable);

    document.getElementById('register').setAttribute('disabled', true);
    document.getElementById('reg-button-container').addEventListener('mouseover', validateRegisterForm);
    document.getElementById('register').addEventListener('click', register);

}

function configureHomeView() {

    console.log('in configureHomeView');

    let authUser = JSON.parse(localStorage.getItem('authUser'));
    let options = document.getElementById('role-specific-options');

    document.getElementById('loggedInUsername').innerText = authUser.username;

    console.log(authUser.role);


    // hide the login option and show the profile and logout option
    document.getElementById('toLogin').setAttribute('hidden', true);
    document.getElementById('toLogout').removeAttribute('hidden');

    // if the authUser's role is admin, configure the admin page
    if (authUser.role == 'Admin') {

        console.log('user is an admin! Configuring admin view');

        // Show the options specific to admins (Register, See All Users to edit).
        document.getElementById('toAllUsers').removeAttribute('hidden');
        document.getElementById('toRegister').removeAttribute('hidden');

        // link the options to methods that load the partials
        document.getElementById('toAllUsers').addEventListener('click', loadAllUsers);
        document.getElementById('toRegister').addEventListener('click', loadRegister);

        console.log('Event listeners added to admin home configuration');

    } else if (authUser.role == 'FinManager') {

        console.log('user is a finance manager! Configuring manager view');

        // show the options specific to finance managers (See All Reimbs to approve/deny).
        document.getElementById('toAllReimbs').removeAttribute('hidden');

        // link the option to a method that loads the partial
        document.getElementById('toAllReimbs').addEventListener('click', loadAllReimbs);

    } else if (authUser.role == 'Employee') {

        console.log ('user is an employee! Configuring employee view');

        // show the options specific to employees (Submit or See Your Reimbs to edit).
        document.getElementById('toSubmit').removeAttribute('hidden');
        document.getElementById('toAuthorReimbs').removeAttribute('hidden');

        // link the options to methods that load the partials
        document.getElementById('toSubmit').addEventListener('click', loadSubmit);
        document.getElementById('toAuthorReimbs').addEventListener('click', loadAuthorReimbs);

    }

}

function configureAllUsersView() {

    console.log('in configureAllUsersView');
    let authUser = JSON.parse(localStorage.getItem('authUser'));
    document.getElementById('loggedInUsername').innerText = authUser.username;
    document.getElementById('findUserToEdit').addEventListener('click', findUserToEdit);

    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'users');
    xhr.send();

    var doIt = true;

    xhr.onreadystatechange = function() {


        if (xhr.readyState = 4 && xhr.status == 200 && doIt == true) {

            let array = JSON.parse(xhr.responseText); // the response from a GET request to reimbs
            console.log(array);
            let table = document.createElement("table"); // create a table
            table.setAttribute('id', 'userTableToAddFilterTo');

            // data tables
            $(document).ready( function () {
                $('#userTableToAddFilterTo').DataTable();
            } );

            document.getElementById('anotherTable').append(table); // attach the table
            let head = document.createElement("thead"); // create the table head
            let body = document.createElement("tbody"); // creating a tbody element

            table.appendChild(head); // append the head
            head.innerHTML = "<tr>"
                            + "<th>ID</th>"
                            + "<th>Username</th>"
                            + "<th>Password</th>"
                            + "<th>First Name</th>"
                            + "<th>Last Name</th>"
                            + "<th>Email</th>"
                            + "<th>Role</th>";
                            console.log('test');

            table.appendChild(body);

            for (let i=0; i < array.length; i++) {

                let row = document.createElement("tr"); // create a row for that object

                row.innerHTML = "<td>" + array[i].id + "</td>"
                                    + "<td>" + array[i].username + "</td>"
                                    + "<td>" + array[i].password + "</td>"
                                    + "<td>" + array[i].firstName + "</td>"
                                    + "<td>" + array[i].lastName + "</td>"
                                    + "<td>" + array[i].email + "</td>"
                                    + "<td>" + array[i].role + "</td>";

                body.appendChild(row); // append each row to the body after finding the information about the object


            }

            doIt = false;
        }

    }

}

function configureAllReimbsView() {

    console.log('in configureAllReimbsView');
    let authUser = JSON.parse(localStorage.getItem('authUser'));

    document.getElementById('viewReimbDetails').addEventListener('click', findReimbDetails);

    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'reimbs');
    xhr.send();

    var doIt = true;

    xhr.onreadystatechange = function() {
        if (xhr.readyState = 4 && xhr.status == 200 && doIt == true) {

            let array = JSON.parse(xhr.responseText); // the response from a GET request to reimbs
            console.log(array);
            let table = document.createElement("table"); // create a table
            table.setAttribute('id', 'reimbTableToAddFilterTo');

            // data tables
            $(document).ready( function () {
                $('#reimbTableToAddFilterTo').DataTable();
            } );

            document.getElementById('anotherReimbTable').append(table); // attach the table
            let head = document.createElement("thead"); // create the table head
            let body = document.createElement("tbody"); // creating a tbody element

            table.appendChild(head); // append the head
            head.innerHTML = "<tr>"
                            + "<th>ID</th>"
                            + "<th>Author</th>"
                            + "<th>Description</th>"
                            + "<th>Amount</th>"
                            + "<th>Submitted</th>"
                            + "<th>Type</th>"
                            + "<th>Status</th>"
                            + "<th>Resolved</th>"
                            + "<th>Resolver</th>";

            table.appendChild(body);

            for (let i=0; i < array.length; i++) {

                let row = document.createElement("tr"); // create a row for that object



                if (array[i].resolverId == '0') {
                    array[i].resolverId = '';
                }

                array[i].submitted = new Date(array[i].submitted); // change submitted to the correct format

                if (array[i].resolved != null) {
                    array[i].resolved = new Date(array[i].resolved).toLocaleDateString("en-US"); // if the resolved date is not null, format it
                }
                if (array[i].resolved == null) {
                    array[i].resolved = '';
                }

                row.innerHTML = "<td>" + array[i].id + "</td>"
                                    + "<td>" + array[i].authorId + "</td>"
                                    + "<td>" + array[i].description + "</td>"
                                    + "<td>$" + array[i].amount + ".00</td>"
                                    + "<td>" + array[i].submitted.toLocaleDateString("en-US") + "</td>"
                                    + "<td>" + array[i].reimbursementType + "</td>"
                                    + "<td>" + array[i].reimbursementStatus + "</td>"
                                    + "<td>" + array[i].resolved + "</td>"
                                    + "<td>" + array[i].resolverId + "</td>";

                body.appendChild(row);

            }

            doIt = false; // So that the table won't print twice

        }
    }

}


function configureSubmitView() {

    console.log('in configureSubmitView()');

    document.getElementById('submit-message').setAttribute('hidden', true);

    document.getElementById('submit').setAttribute('disabled', true);
    document.getElementById('submit-button-container').addEventListener('mouseover', validateSubmitForm);
    document.getElementById('submit').addEventListener('click', submit);

}

function configureAuthorReimbsView() {

    console.log('in configureAuthorReimbsView()');

    let authUser = JSON.parse(localStorage.getItem('authUser'));

    document.getElementById('viewReimbDetails').addEventListener('click', findReimbDetails);

    console.log(authUser.id);

    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'reimbs');
    xhr.send();

    var doIt = true;

    xhr.onreadystatechange = function() {
        if (xhr.readyState = 4 && xhr.status == 200 && doIt == true) {

            var array = JSON.parse(xhr.responseText); // the response from a GET request to reimbs
            let table = document.getElementById("reimbsTable"); // accessing the HTML tag with this ID
            table.setAttribute('id', 'authorReimbTableToAddFilterTo');

            // data tables
            $(document).ready( function () {
                $('#authorReimbTableToAddFilterTo').DataTable();
            } );

            let head = document.createElement("thead"); // create the table head
            let body = document.createElement("tbody"); // creating a tbody element

            table.appendChild(head); // append the head
            head.innerHTML = "<tr>"
                            + "<th>ID</th>"
                            + "<th>Author</th>"
                            + "<th>Description</th>"
                            + "<th>Amount</th>"
                            + "<th>Submitted</th>"
                            + "<th>Type</th>"
                            + "<th>Status</th>"
                            + "<th>Resolved</th>"
                            + "<th>Resolver</th>";

            table.appendChild(body);

            for (let i=0; i < array.length; i++) {

                let row = document.createElement("tr"); // create a row for that object

                array[i].submitted = new Date(array[i].submitted); // reformat

                if (array[i].resolved != null) {
                    array[i].resolved = new Date(array[i].resolved).toLocaleDateString("en-US"); // if the resolved date is not null, format it
                } else {
                    array[i].resolved = "";
                }

                if (array[i].resolverId == '0') {
                    array[i].resolverId = ""; // no resolver yet. This makes it empty in the table!
                }

                row.innerHTML = "<td>" + array[i].id + "</td>"
                                    + "<td>" + array[i].authorId + "</td>"
                                    + "<td>" + array[i].description + "</td>"
                                    + "<td>$" + array[i].amount + ".00</td>"
                                    + "<td>" + array[i].submitted.toLocaleDateString("en-US") + "</td>"
                                    + "<td>" + array[i].reimbursementType + "</td>"
                                    + "<td>" + array[i].reimbursementStatus + "</td>"
                                    + "<td>" + array[i].resolved + "</td>"
                                    + "<td>" + array[i].resolverId + "</td>";

                body.appendChild(row);

            }

            doIt = false;


        }
    }

}

function configureUpdateUserView() {

    console.log('in configureUpdateUserView()');

    document.getElementById('update-message').setAttribute('hidden', true);

    document.getElementById('username-update').addEventListener('blur', isUsernameAvailable);
    document.getElementById('email-update').addEventListener('blur', isEmailAvailable);


    document.getElementById('update').addEventListener('click', updateUser);
    document.getElementById('makeUserInactive').addEventListener('click', makeUserInactive);

}

function configureUpdateReimbView() {

    console.log('in configureUpdateReimbView()');

    document.getElementById('update-reimb-message').setAttribute('hidden', true);

    document.getElementById('deleteReimb').addEventListener('click', deleteReimb);


    document.getElementById('updateReimb').addEventListener('click', updateReimb);

}


//------------------OPERATIONS-----------------------

function login() {

    console.log('in login()');

    let un = document.getElementById('login-username').value;
    let pw = document.getElementById('login-password').value;

    let credentials = {
        username: un,
        password: pw
    }

    let credentialsJSON = JSON.stringify(credentials);

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'auth');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(credentialsJSON);

    xhr.onreadystatechange = function () {

        if (xhr.readyState == 4 && xhr.status == 200) {

            document.getElementById('login-message').setAttribute('hidden', true);
            localStorage.setItem('authUser', xhr.responseText);
            console.log('current logged in user is: ' + localStorage.getItem('authUser'));
            loadHome();

        } else if (xhr.readyState == 4 && xhr.status == 401) {

            document.getElementById('login-message').removeAttribute('hidden');
            let err = JSON.parse(xhr.responseText);
            document.getElementById('login-message').innerText = err.message;

        }

    }

}

function register() {

    console.log('in register()');

    let fn = document.getElementById('fn').value;
    let ln = document.getElementById('ln').value;
    let email = document.getElementById('email').value;
    let un = document.getElementById('reg-username').value;
    let pw = document.getElementById('reg-password').value;
    let role = document.getElementById('reg-role').value;

    let newUser = {
        firstName: fn,
        lastName: ln,
        email: email,
        username: un,
        password: pw,
        role: role
    }

    let newUserJSON = JSON.stringify(newUser);

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'users');
    xhr.send(newUserJSON);

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 201) {
            logout(); // user should be logged in as admin to make a new user. So, return to home after done registering new user
        } else if (xhr.readyState == 4 && xhr.status != 201) {
            document.getElementById('reg-message').removeAttribute('hidden');
            let err = JSON.parse(xhr.responseText);
            document.getElementById('reg-message').innerText = err.message;
        }
    }
}

function updateUser() {

    console.log('in updateUser()');

    let newfn = document.getElementById('fn-update').value;
    let newln = document.getElementById('ln-update').value;
    let newEmail = document.getElementById('email-update').value;
    let newun = document.getElementById('username-update').value;
    let newpw = document.getElementById('password-update').value;
    let newRole = document.getElementById('role-update').value;

    console.log(localStorage.getItem('userToUpdate'));

    let oldUser = JSON.parse(localStorage.getItem('userToUpdate')); // the userToUpdate was set in findUserToUpdate()
    console.log(oldUser);
    console.log(oldUser.username);

    // if any of the fields in the update form are null, assign the original user fields to that field.
    if (newfn == null || newfn == "") {
        newfn = oldUser.firstName;
        console.log(newfn);
    }
    if (newln == null || newln == "") {
        newln = oldUser.lastName;
    }
    if (newEmail == null || newEmail == "") {
        newEmail = oldUser.email;
    }
    if (newun == null || newun == "") {
        newun = oldUser.username;
    }
    if (newpw == null || newpw == "") {
        newpw = oldUser.password;
    }
    if (newRole == null || newRole == "") {
        newRole = oldUser.role;
    }
    else { // if a number was entered in the role field, change it to an enum constant
        if (newRole == '1') {
            console.log('Update to Admin chosen!');
            newRole = 'ADMIN';
        }
        if (newRole == '2') {
            console.log('Update to FinManager chosen!');
            newRole = 'FINANCE_MANAGER';
        }
        if (newRole == '3') {
            console.log('Update to Employee chosen!');
            newRole = 'EMPLOYEE';
        }
        if (newRole == '4') {
            console.log('Update to Inactive chosen!');
            newRole = 'INACTIVE';
        }

    }
    console.log("This should be the new role or the old if nothing was entered in update role field: " + newRole);

    // this will have either the values entered in the form or the old information if nothing was entered
    let updatedUser = {
        firstName: newfn,
        lastName: newln,
        email: newEmail,
        username: newun,
        password: newpw,
        role: newRole
    }

    let updatedUserJSON = JSON.stringify(updatedUser);

    let xhr = new XMLHttpRequest();

    xhr.open('PUT', 'users');
    xhr.send(updatedUserJSON);

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 201) { // 201 CREATED bc new data?
            console.log('loading all users page!')
            loadAllUsers();
        } else if (xhr.readyState == 4 && xhr.status != 201) {
            document.getElementById('update-message').removeAttribute('hidden');
            let err = JSON.parse(xhr.responseText);
            document.getElementById('update-message').innerText = err.message;
        }
    }


}

function makeUserInactive() {

    console.log('in makeUserInactive()');
    console.log(localStorage.getItem('userToUpdate'));

    let oldUser = JSON.parse(localStorage.getItem('userToUpdate')); // the userToUpdate was set in findUserToUpdate()
    console.log(oldUser);
    console.log(oldUser.username);

    // old data
    let fn = oldUser.firstName;
    let ln = oldUser.lastName;
    let oldEmail = oldUser.email;
    let un = oldUser.username;
    let pw = oldUser.password;

    let userToMakeInactive = {
        firstName: fn,
        lastName: ln,
        email: oldEmail,
        username: un,
        password: pw,
        role: 'INACTIVE'
    }

    let userToMakeInactiveJSON = JSON.stringify(userToMakeInactive);
    console.log(userToMakeInactive);

    let xhr = new XMLHttpRequest();

    xhr.open('PUT', 'users');
    xhr.send(userToMakeInactiveJSON); // just send the updated status; the ID of the user to update should be a session attribute already

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 201) { // 200 OK. Use this code for update?
            console.log('sending the user to the servlet to make them inactive!');
            localStorage.removeItem('userToUpdate');
            loadAllUsers();
        }
    }

}

function updateReimb() {

    console.log('in updateReimb()');

    let newAmount = document.getElementById('amount-update').value;
    let newType = document.getElementById('type-update').value;
    let newDescription = document.getElementById('description-update').value;

    console.log(localStorage.getItem('userToUpdate'));

    let oldReimb = JSON.parse(localStorage.getItem('authReimb')); // the userToUpdate was set in findUserToUpdate()
    console.log(oldReimb);
    console.log(oldReimb.amount);

    // if any of the fields in the update form are null, assign the original user fields to that field.
    if (newAmount == null || newAmount == "") {
        newAmount = oldReimb.amount;
        console.log(newAmount);
    }
    if (newType == null || newType == "") {
        newType = oldReimb.reimbursementType;
        console.log(newType);
    }
    if (newDescription == null || newDescription == "") {
        newDescription = oldReimb.description;
        console.log(newDescription);
    } else { // if the employee DID enter a number into the type field for update, change it to an enum constant
            if (newType == '1') {
                console.log('Update to Lodging chosen!');
                newType = 'LODGING';
            }
            if (newType == '2') {
                console.log('Update to Travel chosen!');
                newType = 'TRAVEL';
            }
            if (newType == '3') {
                console.log('Update to Food chosen!');
                newType = 'FOOD';
            }
            if (newType == '4') {
                console.log('Update to Other chosen!');
                newType = 'OTHER';
            }
    }


    // this will have either the values entered in the form or the old information if nothing was entered
    let updatedReimb = {
        amount: newAmount,
        reimbursementType: newType,
        description: newDescription
    }

    let updatedReimbJSON = JSON.stringify(updatedReimb);

    let xhr = new XMLHttpRequest();

    xhr.open('PUT', 'reimbs');
    xhr.send(updatedReimbJSON);

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 201) { // 201 CREATED bc new data?
            // clear the local storage of the reimb so that all will load again when they want to view all of their reimbursements
            localStorage.removeItem('authReimb');
            loadAuthorReimbs(); // loadAUTHORReimbs because if a user is here, they are an employee
        } else if (xhr.readyState == 4 && xhr.status != 400) {
            document.getElementById('update-reimb-message').removeAttribute('hidden');
            let err = JSON.parse(xhr.responseText);
            document.getElementById('update-reimb-message').innerText = err.message;
        }
    }


}

function findReimbDetails() {

    console.log('in findReimbDetails()');

    let id = document.getElementById('reimbId').value;

    let reimb = {
        id: id
    }

    let reimbJSON = JSON.stringify(reimb);
    console.log(reimbJSON);
    console.log(reimbJSON.id);

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'auth'); // needs to be a post so that the body of send method will not be ignored
    xhr.send(reimbJSON);

    xhr.onreadystatechange = function () {

        if (xhr.readyState == 4 && xhr.status == 200) { // 201 created bc a new object will be created from the reimbId sent even though the reimb already exists.
            localStorage.setItem('authReimb', xhr.responseText);
            console.log(reimb);
            loadReimbDetails();
        }

    }

}



function findUserToEdit() {

    console.log('in findUserToEdit()');

    let id = document.getElementById('userId').value;
    console.log(id);

    let user = {
        id: id
    }

    let userJSON = JSON.stringify(user);
    console.log(userJSON);
    console.log(userJSON[0]);

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'auth');
    xhr.send(userJSON);

    xhr.onreadystatechange = function() {

        if (xhr.readyState == 4 && xhr.status == 200) {

            localStorage.setItem('userToUpdate', xhr.responseText);
            console.log(user);
            console.log('This should print the userToUpdates information: ' + localStorage.getItem('userToUpdate'));
            loadUpdateUser();
        }
    }
}

function submit() {

    console.log('in submit()');

    let amount = document.getElementById('amount').value;
    let type = document.getElementById('type').value;
    let description = document.getElementById('description').value;
    let authUser = JSON.parse(localStorage.getItem('authUser'));

    // change type to enum constant
    if (type == '1') {
        console.log('lodging chosen!');
        type = 'LODGING';
    }
    if (type == '2') {
        console.log('travel chosen!');
        type = 'TRAVEL';
    }
    if (type == '3') {
        console.log('food chosen!');
        type = 'FOOD';
    }
    if (type == '4') {
        console.log('other chosen!');
        type = 'OTHER';
    }


    let newReimb = {
        amount: amount,
        reimbursementType: type,
        description: description,
        submitted: new Date(),
        authorId: authUser.id
    }

    let newReimbJSON = JSON.stringify(newReimb);
    console.log(newReimbJSON);

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'reimbs');
    xhr.send(newReimbJSON);

    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 201) {
            console.log('sending the reimbursement to the servlet to submit!')
            loadAuthorReimbs();
        } else if (xhr.readyState == 4 && xhr.status != 400) {
            document.getElementById('submit-message').removeAttribute('hidden');
            let err = JSON.parse(xhr.responseText);
            document.getElementById('submit-message').innerText = err.message;
        }
    }


}

function deleteReimb() {

    console.log('in delteReimb()');


    // open a delete to reimbs and in reimbs delete the current reimb selected in the session
    let xhr = new XMLHttpRequest();

    xhr.open('DELETE', 'reimbs');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) { // 200 OK. Use this code for delete?
            localStorage.removeItem('authReimb');
            loadAuthorReimbs();
        }
    }

}



function approveItOrDenyIt() {

    // if the Reimbursement has a status other than PENDING, then alert the user they are overriding the status
    let reimbToCheckForOverride = JSON.parse(localStorage.getItem('authReimb'));
    if (reimbToCheckForOverride.reimbursementStatus != 'PENDING') {
        alert('This reimbursement has already been resolved, do you want to override it?');
    }

    console.log('in approveItOrDenyIt()');

    let choice = document.getElementById('approveOrDeny').value;
    let authUser = JSON.parse(localStorage.getItem('authUser'));
    let statusId = 1; // initialize statusId to 1

    // make the choice 2 or 3 depending on approve or deny

    if (choice == 'yes') {
        console.log('reimbursement approved by fin manager');
        statusId = 'APPROVED';
    }
    else if (choice == 'no' || 'No') {
        console.log('reimbursement denied by fin manager');
        statusId = 'DENIED';
    }

    let reimbResolverUpdates = {
        resolved: new Date(),
        resolverId: authUser.id,
        reimbursementStatus: statusId
    }

    console.log(reimbResolverUpdates);
    let reimbResolverUpdatesJSON = JSON.stringify(reimbResolverUpdates);
    console.log(reimbResolverUpdatesJSON)

    let xhr = new XMLHttpRequest();

    xhr.open('PUT', 'reimbs');
    xhr.send(reimbResolverUpdatesJSON);

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 201) {
            localStorage.removeItem('authReimb'); // remove the authReimb so that all reimbs will show up with a get request to reimb servlet
            loadAllReimbs();
        }
    }
}

function logout() {

    console.log('in logout()');

    let xhr = new XMLHttpRequest();

    xhr.open('GET', 'auth');
    xhr.send();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('logout successful!');
            localStorage.removeItem('authUser');
            localStorage.clear(); // clear the local storage

            // need to re-hide the user-role-specific options
            document.getElementById('toRegister').setAttribute('hidden', true); // initially hidden
            document.getElementById('toAllUsers').setAttribute('hidden', true); // initially hidden
            document.getElementById('toAllReimbs').setAttribute('hidden', true); // initially hidden
            document.getElementById('toSubmit').setAttribute('hidden', true); // initially hidden
            document.getElementById('toAuthorReimbs').setAttribute('hidden', true); // initially hidden
            loadLogin();
        }
    }
}

function isUsernameAvailable() {

    console.log('in isUsernameAvailable()');

    let username = document.getElementById('reg-username').value;

    if (!username) {
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'username.validate');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(username));

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('Provided username is available!');
            document.getElementById('reg-message').setAttribute('hidden', true);
        } else if (xhr.readyState == 4 && xhr.status == 409 ) {
            document.getElementById('reg-message').removeAttribute('hidden')
            document.getElementById('reg-message').innerText = 'The provided username is already taken!';
            document.getElementById('register').setAttribute('disabled', true);
        }
    }

}

function isEmailAvailable() {

    console.log('in isEmailAvailable()');

    let email = document.getElementById('email').value;

    if (!email) {
        return;
    }

    let xhr = new XMLHttpRequest();

    xhr.open('POST', 'email.validate');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(email));

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 204) {
            console.log('Provided email is available!');
            document.getElementById('reg-message').setAttribute('hidden', true);
        } else if (xhr.readyState == 4 && xhr.status == 409) {
            document.getElementById('reg-message').removeAttribute('hidden');
            document.getElementById('reg-message').innerText = 'The provided email address is already taken!';
            document.getElementById('register').setAttribute('disabled', true);
        }
    }
}


//---------------------FORM VALIDATION-------------------------

function validateLoginForm() {

    console.log('in validateLoginForm()');

    let msg = document.getElementById('login-message').innerText;

    if (msg == 'User authentication failed!') {
        return;
    }

    let un = document.getElementById('login-username').value;
    let pw = document.getElementById('login-password').value;

    if (!un || !pw) {
        document.getElementById('login-message').removeAttribute('hidden');
        document.getElementById('login-message').innerText = 'You must provided values for all fields in the form!';
        document.getElementById('login').setAttribute('disabled', true);
    } else {
        document.getElementById('login').removeAttribute('disabled');
        document.getElementById('login-message').setAttribute('hidden', true);
    }

}

function validateRegisterForm() {

    console.log('in validateRegisterForm()');

    let fn = document.getElementById('fn').value;
    let ln = document.getElementById('ln').value;
    let email = document.getElementById('email').value;
    let un = document.getElementById('reg-username').value;
    let pw = document.getElementById('reg-password').value;

    if (!fn || !ln || !email || !un || !pw) {
        document.getElementById('reg-message').removeAttribute('hidden');
        document.getElementById('reg-message').innerText = 'You must provided values for all fields in the form!'
        document.getElementById('register').setAttribute('disabled', true);
    } else {
        document.getElementById('register').removeAttribute('disabled');
        document.getElementById('reg-message').setAttribute('hidden', true);
    }


}

function validateSubmitForm() {

    console.log('in validateSubmitForm()');

    let amount = document.getElementById('amount').value;
    let type = document.getElementById('type').value;
    let description = document.getElementById('description').value;

    if (!amount || !type || !description) {
        document.getElementById('submit-message').removeAttribute('hidden');
        document.getElementById('submit-message').innerText = 'You must provided values for all fields in the form!'
        document.getElementById('submit').setAttribute('disabled', true);
    } else {
        document.getElementById('submit').removeAttribute('disabled');
        document.getElementById('submit-message').setAttribute('hidden', true);
    }


}