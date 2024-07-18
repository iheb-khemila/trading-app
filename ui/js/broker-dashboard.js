import { removeInvalidState, setFieldInvalid, showNavbar, formatKind } from './utilities.js'
import {getAllAssets, deleteAsset, addAsset} from './api.js'

document.getElementById('broker-dashboard-script').onload = async function () {
    // Add the event listener to open Add asset modal to add asset button.
    document.getElementById('add-asset-button').addEventListener('click', openAddAssetModal)

    // Show the navbar.
    showNavbar()

    // Get all assets and populate table.
    await callGetAllAssets()
}


async function callDeleteAsset(id) {

    // Call API.
    const response = await deleteAsset(id)

    if(response.ok) {
        await callGetAllAssets()
    } else {
        // Set error in toast text and show for 5 seconds
        document.getElementById("toast-body").innerText = await response.text()
        document.getElementById("toast").classList.remove('hide')

        setTimeout(() => {
            document.getElementById("toast").classList.add("hide")
        }, 5000)
    }
}

async function callGetAllAssets() {
    const assets = await getAllAssets()

    // Clear the assets table.
    document.getElementById("asset-table").getElementsByTagName('tbody')[0].innerHTML = "";


    assets.forEach((asset, index) => {
        // Insert a new row.
        let row = document.getElementById("asset-table").getElementsByTagName('tbody')[0].insertRow(index)

        // Fill each row with the relevant data.
        row.insertCell(0).innerHTML = asset.name;
        row.insertCell(1).innerHTML = formatKind(asset.kind)

        // Add action buttons to the actions column.
        const action = row.insertCell(2)
        action.innerHTML = "<a id='" + asset.asset_id + "' class='link mx-auto'>Delete Asset</a>"
        action.classList.add("col-small")

        // Add event listener to action button.
        document.getElementById(asset.asset_id).addEventListener('click', (event) => {
            callDeleteAsset(event.target.id)
        });
    })
}

function openAddAssetModal() {
    // Make add asset modal appear.
    document.getElementById("add-asset-modal").classList.add("show")

    // Add event listener to close modal on click.
    document.getElementById("modal-close").addEventListener('click', closeModal)

    // Add event listener to make call to the API on click.
    document.getElementById("modal-save").addEventListener('click', saveAsset)
}

function closeModal() {
    // Hide the modal.
    document.getElementById("add-asset-modal").classList.remove("show")

    // Reset the input values in modal.
    document.getElementById("asset-name").value = ""
    document.getElementById("asset-type").value = "SHARE"
}

async function saveAsset() {
    // Get input values.
    const assetName = document.getElementById("asset-name").value
    const assetType = document.getElementById("asset-type").value

    // Check if the content is valid.
    const valid = validateAsset(assetName)

    if(valid) {
        // Call API
        const response = await addAsset(assetName, assetType)

        if(response.ok) {
            closeModal()

            await callGetAllAssets()
        } else {
            // Set the global form error text and highlight inputs with a red outline.
            document.getElementById("asset-error").innerText = await response.text()
            setFieldInvalid("asset-name")
            setFieldInvalid("asset-type")
        }
    }
}

function validateAsset(assetName) {
    // Remove any invalid states.
    removeInvalidState("asset-name")

    // Check if asset name is empty or null and invalidate field.
    if(assetName === "" || assetName === null) {
        setFieldInvalid("asset-name", "Asset Name is required.")
        return false;
    }

    return true;
}