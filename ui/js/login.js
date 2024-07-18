// Import necessary functions from api.js, router.js, and utilities.js modules.
import { loginUser } from "./api.js";
import { loadPage } from "./router.js";
import { hideNavbar, removeInvalidState, setFieldInvalid } from "./utilities.js";

// Set up event listeners when the login script is loaded.
document.getElementById('login-script').onload = function () {
    // Hide the navigation bar.
    hideNavbar();

    // Add an event listener to the submit button to handle the login process.
    document.getElementById('submit').addEventListener("click", login);

    // Add an event listener to the register button to redirect to the registration page.
    document.getElementById("register-button").addEventListener("click", redirectToRegister);
}

// Function to handle the login process.
async function login() {
    // Clear any existing error messages.
    document.getElementById("user-details-error").innerText = "";

    // Get the username, password, and user type from the input fields.
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const userType = document.getElementById("userType").value;

    // Validate the login form.
    const valid = validateLoginForm(username, password);

    // If the form is valid, proceed with the login process.
    if (valid) {
        // Call the loginUser API function to authenticate the user.
        const response = await loginUser(username, password, userType);

        // If the login is successful, handle the successful login.
        if (response.ok) {
            // Get the authentication token from the response.
            const token = await response.text();

            // Store the token in session storage.
            sessionStorage.setItem("authentication_token", token);

            // Decode the JWT token to get the subject, which contains the username and role.
            const subject = decodeJwt(token).sub;
            const username = subject.split(":")[0];
            const role = subject.split(":")[1];

            // Store the username and role in session storage.
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("role", role);

            // Redirect the user to the appropriate dashboard based on their role.
            if (role === "BROKER") {
                loadPage("broker-dashboard");
            } else {
                loadPage("investor-dashboard");
            }

        } else {
            // If the login fails, display an error message and mark the fields as invalid.
            document.getElementById("user-details-error").innerText = await response.text();
            setFieldInvalid("username");
            setFieldInvalid("password");
        }
    }
}

// Function to redirect the user to the registration page.
async function redirectToRegister() {
    loadPage("register");
}

// Function to validate the login form.
function validateLoginForm(username, password) {
    // Remove any invalid state from the username and password fields.
    removeInvalidState("username");
    removeInvalidState("password");

    // Check if the username or password is empty or null.
    if (username === "" || username === null || password === "" || password === null) {
        // If the username is empty or null, mark the username field as invalid.
        if (username === "" || username === null) {
            setFieldInvalid("username", "Username is required.");
        }

        // If the password is empty or null, mark the password field as invalid.
        if (password === "" || password === null) {
            setFieldInvalid("password", "Password is required.");
        }

        // Return false indicating that the form is not valid.
        return false;
    }

    // Return true indicating that the form is valid.
    return true;
}

// Function to decode a JWT token.
function decodeJwt(token) {
    // Split the token to get the payload part (second part).
    var base64Url = token.split('.')[1];

    // Replace characters to make it base64 compliant.
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

    // Decode the base64 string.
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    // Parse the decoded string as JSON and return it.
    return JSON.parse(jsonPayload);
}
