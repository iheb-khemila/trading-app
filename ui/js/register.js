// Import necessary functions from utilities.js, router.js, and api.js modules.
import { hideNavbar, setFieldInvalid } from "./utilities.js";
import { loadPage } from "./router.js";
import { registerInvestor, registerBroker, loginUser } from "./api.js";

// Set up event listeners when the register script is loaded.
document.getElementById('register-script').onload = function () {
    // Hide the navigation bar.
    hideNavbar();

    // Hide the investor registration form by default.
    hideRegisterInvestor();

    // Add an event listener to the register button to handle the registration process.
    document.getElementById('register-user-button').addEventListener("click", register);

    // Add an event listener to the sign-in button to redirect to the sign-in page.
    document.getElementById("sign-in-button").addEventListener("click", redirectToSignIn);

    // Add an event listener to the user type dropdown to show/hide forms based on the selected user type.
    document.getElementById("userType").addEventListener('change', () => {
        const value = document.getElementById("userType").value;

        if (value === "BROKER") {
            // Hide the investor form and show the broker form if BROKER is selected.
            hideRegisterInvestor();
        } else {
            // Hide the broker form and show the investor form if INVESTOR is selected.
            hideRegisterBroker();
        }
    });
}

// Function to handle the registration process.
async function register() {
    // Get the user type, username, and password from the input fields.
    const userType = document.getElementById("userType").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    if (userType === "BROKER") {
        // If the user type is BROKER, get the company name and validate the broker details.
        const companyName = document.getElementById("company").value;

        const valid = validateBrokerDetails(username, password, companyName);

        if (valid) {
            // Call the registerBroker API function to register the broker.
            const response = await registerBroker(username, password, companyName);

            if (response.ok) {
                // If registration is successful, redirect to the login page.
                loadPage("login");
            } else {
                // If registration fails, mark the fields as invalid and display the error message.
                setFieldInvalid("username");
                setFieldInvalid("password");
                setFieldInvalid("company");

                document.getElementById("register-error").innerText = await response.text();
                document.getElementById('sign-in-button').style.paddingTop = "8px";
            }
        }
    } else {
        // If the user type is INVESTOR, get the first name and last name, and validate the investor details.
        const firstName = document.getElementById("first-name").value;
        const lastName = document.getElementById("last-name").value;

        const valid = validateInvestorDetails(username, password, firstName, lastName);

        if (valid) {
            // Call the registerInvestor API function to register the investor.
            const response = await registerInvestor(username, password, firstName, lastName);

            if (response.ok) {
                // If registration is successful, automatically log in the user and redirect to the login page.
                await loginUser(username, password, userType);
                loadPage("login");
            } else {
                // If registration fails, mark the fields as invalid and display the error message.
                setFieldInvalid("username");
                setFieldInvalid("password");
                setFieldInvalid("first-name");
                setFieldInvalid("last-name");

                document.getElementById("register-error").innerText = await response.text();
                document.getElementById('sign-in-button').style.paddingTop = "8px";
            }
        }
    }
}

// Function to validate broker details.
function validateBrokerDetails(username, password, companyName) {
    // Check if the username, password, or company name is empty or null.
    if (username === "" || username === null || password === "" || password === null || companyName === "" || companyName === null) {

        // Mark the username field as invalid if it's empty or null.
        if (username === "" || username === null) {
            setFieldInvalid("username", "Username is required.");
        }

        // Mark the password field as invalid if it's empty or null.
        if (password === "" || password === null) {
            setFieldInvalid("password", "Password is required.");
        }

        // Mark the company name field as invalid if it's empty or null.
        if (companyName === "" || companyName === null) {
            setFieldInvalid("company", "Company is required.");
        }

        // Return false indicating that the form is not valid.
        return false;
    }

    // Return true indicating that the form is valid.
    return true;
}

// Function to validate investor details.
function validateInvestorDetails(username, password, firstName, lastName) {
    // Check if the username, password, first name, or last name is empty or null.
    if (username === "" || username === null || password === "" || password === null || firstName === "" || firstName === null || lastName === "" || lastName === null) {

        // Mark the username field as invalid if it's empty or null.
        if (username === "" || username === null) {
            setFieldInvalid('username', "Username is required.");
        }

        // Mark the password field as invalid if it's empty or null.
        if (password === "" || password === null) {
            setFieldInvalid('password', "Password is required.");
        }

        // Mark the first name field as invalid if it's empty or null.
        if (firstName === "" || firstName === null) {
            setFieldInvalid('first-name', "First Name is required.");
        }

        // Mark the last name field as invalid if it's empty or null.
        if (lastName === "" || lastName === null) {
            setFieldInvalid('last-name', "Last Name is required.");
        }

        // Return false indicating that the form is not valid.
        return false;
    }

    // Return true indicating that the form is valid.
    return true;
}

// Function to redirect the user to the sign-in page.
function redirectToSignIn() {
    loadPage("login");
}

// Function to hide the broker registration form and show the investor registration form.
function hideRegisterBroker() {
    document.getElementById("register-investor").classList.remove("hide");
    document.getElementById("register-broker").classList.add("hide");
}

// Function to hide the investor registration form and show the broker registration form.
function hideRegisterInvestor() {
    document.getElementById("register-broker").classList.remove("hide");
    document.getElementById("register-investor").classList.add("hide");
}
