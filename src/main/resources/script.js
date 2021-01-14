/**
 * Register a event listner on input events that triggers the search function
 */
const registerSearch = function () {

    console.log("Register search listener")

    let searchInput = document.querySelector("#search")

    searchInput.addEventListener("input", (e) => {
        console.log(e.target.value)
        performSearch(e.target.value)
    })
}

/**
 * Call the Java API with the search needle, and display the results in the web page
 *
 * @param needle {String} The search string from the input
 * @returns {Promise<void>}
 */
const performSearch = async function (needle) {

    try {

        // Call the api search endpoint with the needle as payload
        let data = await fetch('/search', {
            method: 'POST',
            body: JSON.stringify({
                needle
            })
        }).then(res => res.json())

        // Get the reference to the html container for the results
        let searchBox = document.querySelector('#search_results')

        let searchResultsHtml = ``

        if (data.length === 0) {

            // If there is no results, display a simple message
            searchResultsHtml += `
            <div class="result_item">
                <div class="result_title">
                    <p>Aucun resultats correspondant a votre recherche...</p>
                </div>
            </div>
            `

        } else {

            // If there is results, iterate trough the result to generate a result item per result
            for (let e of data) {
                console.log(e)
                searchResultsHtml += `
            <div class="result_item">
                <div class="result_title">
                    <p>${e.title}</p>
                </div>
                <div class="result_type">
                    <div><p>${e.type}</p></div>
                </div>
                <div class="result_content">
                    <p>${e.description}</p>
                </div>
            </div>
            `
            }
        }

        // Put the results in the page
        searchBox.innerHTML = searchResultsHtml

    } catch (e) {

        console.error("Impossible to perform the search", e)

    }

}

// On page load, call the search registration function
window.onload = function () {
    registerSearch()
}