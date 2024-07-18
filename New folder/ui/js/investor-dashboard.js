// Import necessary functions from utilities.js and api.js modules.
import { showNavbar, formatKind } from './utilities.js';
import {
    getInvestorsPortfolios,
    getInvestorProfile,
    sellPortfolio,
    getAllBroker,
    createPortfolio,
    getAllAssetsByBrokerId,
    addAssetToPortfolio
} from "./api.js";

// Declare a global variable to store portfolios.
let portfolios;

// Set up an event listener to initialize the investor dashboard when the script loads.
document.getElementById('investor-dashboard-script').onload = async function () {
    // Add an event listener to the "Create Portfolio" button to open the portfolio modal.
    document.getElementById("create-portfolio-button").addEventListener('click', openPortfolioModal);

    // Fetch and display the investor profile and portfolios.
    await getInvestor();

    // Display the navigation bar.
    showNavbar();
}

// Function to fetch and display investor profile and portfolios.
async function getInvestor() {
    // Fetch investor profile data from the API.
    const investor = await getInvestorProfile();

    // Update the investor's name and portfolio count in the dashboard.
    document.getElementById("investor-name").innerText = `${investor.firstname} ${investor.lastname}`;
    document.getElementById("portfolio-count").innerText = investor.portfolios.length;

    // Fetch and display all portfolios for the investor.
    await callGetAllPortfolios();
}

// Function to retrieve and display all portfolios.
async function callGetAllPortfolios() {
    // Fetch portfolios from the API.
    portfolios = await getInvestorsPortfolios();

    // Clear the portfolio table before populating it.
    document.getElementById("portfolio-table").getElementsByTagName('tbody')[0].innerHTML = "";

    // Iterate through each portfolio and add it to the table.
    portfolios.forEach((portfolio, index) => {
        // Insert a new row for the portfolio.
        let row = document.getElementById("portfolio-table").getElementsByTagName('tbody')[0].insertRow(index);

        // Populate the row with portfolio data.
        row.insertCell(0).innerHTML = portfolio.companyName;
        row.insertCell(1).innerHTML = portfolio.creationDate;

        // Create action buttons for viewing, adding assets, and selling the portfolio.
        const action = row.insertCell(2);
        action.innerHTML = `
            <a id='view-assets-${portfolio.id}' class='link mx-auto'>View Assets</a><br/><br/>
            <a id='add-asset-${portfolio.id}' class='link mx-auto'>Add Asset</a><br/><br/>
            <a id='sell-portfolio-${portfolio.id}' class='link mx-auto'>Sell</a>
        `;
        action.classList.add("col-small");

        // Add event listeners to the action buttons.
        document.getElementById(`view-assets-${portfolio.id}`).addEventListener('click', async (event) => {
            await openAssetTable(event.target.id.split("-")[2]);
        });

        document.getElementById(`sell-portfolio-${portfolio.id}`).addEventListener('click', (event) => {
            callSellPortfolio(event.target.id.split("-")[2]);
        });

        document.getElementById(`add-asset-${portfolio.id}`).addEventListener('click', (event) => {
            openAddAssetToPortfolioModal(event.target.id.split("-")[2]);
        });
    });
}

// Function to open the asset table modal.
async function openAssetTable(index) {
    // Refresh investor information.
    await getInvestor();

    // Display the view asset modal.
    document.getElementById("view-asset-modal").classList.add("show");

    // Set event listener to close the modal on click.
    document.getElementById('view-asset-modal-close').addEventListener('click', () => {
        document.getElementById("view-asset-modal").classList.remove("show");
    });

    // Populate the assets table for the selected portfolio.
    populatePortfolioAssetTable(index);
}

// Function to open the modal for adding assets to a portfolio.
async function openAddAssetToPortfolioModal(id) {
    // Display the add asset to portfolio modal.
    document.getElementById("add-asset-to-portfolio-modal").classList.add("show");

    // Set event listener to close the modal on click.
    document.getElementById('add-asset-to-portfolio-modal-close').addEventListener('click', () => {
        document.getElementById("add-asset-to-portfolio-modal").classList.remove("show");
    });

    // Clear the asset dropdown content.
    document.getElementById("asset").innerHTML = "";

    // Get the selected portfolio based on the ID.
    const portfolio = portfolios.find(portfolio => portfolio.id.toString() === id.toString());

    // Fetch the list of assets provided by the broker of the selected portfolio.
    let assets = await getAllAssetsByBrokerId(portfolio.brokerId);

    // Filter out assets that are already in the portfolio.
    assets = removeMatchingElementsById(assets, portfolio.assets);

    // Clear the portfolio asset table before populating it.
    document.getElementById("portfolio-asset-table").getElementsByTagName('tbody')[0].innerHTML = "";

    // Populate the asset dropdown with available assets.
    assets.forEach(asset => {
        const el = document.createElement("option");
        el.value = asset.asset_id;
        el.innerHTML = `${asset.name} | ${formatKind(asset.kind)}`;
        document.getElementById('asset').appendChild(el);
    });

    // Add event listener to the save button to call the API.
    document.getElementById('add-asset-to-portfolio-modal-save').addEventListener('click', async () => {
        await callAddAssetToPortfolio(portfolio.id);
    });
}

// Function to remove elements from list1 that have matching asset_id in list2 and vice versa.
function removeMatchingElementsById(list1, list2) {
    // Create a set of asset IDs from list2.
    const idsInList2 = new Set(list2.map(item => item.asset_id));

    // Filter out elements from list1 that have matching asset IDs in list2.
    const filteredList1 = list1.filter(item => !idsInList2.has(item.asset_id));

    // Create a set of asset IDs from list1.
    const idsInList1 = new Set(list1.map(item => item.asset_id));

    // Filter out elements from list2 that have matching asset IDs in list1.
    const filteredList2 = list2.filter(item => !idsInList1.has(item.asset_id));

    // Return the combined filtered lists.
    return [...filteredList1, ...filteredList2];
}

// Function to open the create portfolio modal.
async function openPortfolioModal() {
    // Clear the broker dropdown content.
    document.getElementById("broker").innerHTML = "";

    // Display the create portfolio modal.
    document.getElementById("create-portfolio-modal").classList.add("show");

    // Set event listener to close the modal on click.
    document.getElementById('create-portfolio-modal-close').addEventListener('click', () => {
        document.getElementById("create-portfolio-modal").classList.remove("show");
    });

    // Fetch all brokers and populate the broker dropdown.
    const brokers = await getAllBroker();
    brokers.forEach(broker => {
        const el = document.createElement("option");
        el.value = broker.id;
        el.innerHTML = broker.username;
        document.getElementById('broker').appendChild(el);
    });

    // Add event listener to the save button to create a portfolio.
    document.getElementById('create-portfolio-modal-save').addEventListener('click', async () => {
        await callCreatePortfolio();
    });
}

// Function to populate the assets table of a portfolio.
function populatePortfolioAssetTable(id) {
    // Clear the existing table content.
    document.getElementById("portfolio-asset-table").getElementsByTagName('tbody')[0].innerHTML = "";

    // Find the selected portfolio and display its assets.
    portfolios.find(portfolio => portfolio.id.toString() === id.toString()).assets.forEach((asset, index) => {
        let row = document.getElementById("portfolio-asset-table").getElementsByTagName('tbody')[0].insertRow(index);
        row.insertCell(0).innerHTML = asset.name;
        row.insertCell(1).innerHTML = formatKind(asset.kind);
    });
}

// Function to call the API to sell a portfolio.
async function callSellPortfolio(id) {
    // Call the API to sell the portfolio.
    const response = await sellPortfolio(id);

    if (response.ok) {
        // Refresh investor data after selling the portfolio.
        await getInvestor();
    } else {
        // Display error message in a toast.
        document.getElementById("toast-body").innerText = await response.text();
        document.getElementById("toast").classList.remove('hide');
        setTimeout(() => {
            document.getElementById("toast").classList.add("hide");
        }, 5000);
    }
}

// Function to call the API to add an asset to a portfolio.
async function callAddAssetToPortfolio(portfolioId) {
    // Hide the add asset to portfolio modal.
    document.getElementById("add-asset-to-portfolio-modal").classList.remove("show");

    // Get the selected asset ID.
    const assetId = document.getElementById("asset").value;

    // Call the API to add the asset to the portfolio.
    await addAssetToPortfolio(assetId, portfolioId);
}

// Function to call the API to create a new portfolio.
async function callCreatePortfolio() {
    // Get the selected broker ID.
    const broker = document.getElementById("broker").value;

    // Call the API to create the portfolio.
    await createPortfolio(broker);

    // Hide the create portfolio modal.
    document.getElementById("create-portfolio-modal").classList.remove("show");

    // Refresh investor data after creating the portfolio.
    await getInvestor();
}
