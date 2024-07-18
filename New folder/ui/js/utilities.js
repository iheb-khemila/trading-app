// Function to show the navigation bar by removing the 'hide' class from the navbar element.
export function showNavbar() {
    // Get the element with the ID 'navbar' and remove the 'hide' class to make it visible.
    document.getElementById("navbar").classList.remove("hide");
}

// Function to hide the navigation bar by adding the 'hide' class to the navbar element.
export function hideNavbar() {
    // Get the element with the ID 'navbar' and add the 'hide' class to make it hidden.
    document.getElementById("navbar").classList.add("hide");
}

// Function to mark a form field as invalid and display an error message.
export function setFieldInvalid(id, error) {
    // Add the 'invalid-input' class to the element with the given ID to visually indicate invalid input.
    document.getElementById(id).classList.add('invalid-input');

    // Set the error message for the input field. If no error message is provided, set it to an empty string.
    document.getElementById(id + '-error').innerText = error === undefined || error === null ? "" : error;
}

// Function to remove the invalid state from a form field.
export function removeInvalidState(id) {
    // Remove the 'invalid-input' class from the element with the given ID to visually indicate valid input.
    document.getElementById(id).classList.remove('invalid-input');

    // Clear the error message for the input field.
    document.getElementById(id + '-error').innerText = "";
}

// Function to format a string to have the first letter capitalized and the rest in lowercase.
export function formatKind(kind) {
    // Convert the first character to uppercase and the rest to lowercase, then return the formatted string.
    return kind.charAt(0).toUpperCase() + kind.slice(1).toLowerCase();
}
