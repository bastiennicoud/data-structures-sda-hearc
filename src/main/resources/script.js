const registerSearch = function () {

    console.log("Register search listener")

    let searchInput = document.querySelector("#search")

    searchInput.addEventListener("input", (e) => {
        console.log(e.target.value)
        performSearch(e.target.value)
    })
}

const performSearch = async function (needle) {

    try {

        let data = await fetch('/search', {
            method: 'POST',
            body: JSON.stringify({
                needle
            })
        }).then(res => res.json())

        console.log("Search results : ", data)

        // Display search results
        let searchBox = document.querySelector('#search_results')

        let searchResultsHtml = ``

        if (data.length === 0) {
            searchResultsHtml += `
            <div class="result_item">
                <div class="result_title">
                    <p>Aucun resultats correspondant a votre recherche...</p>
                </div>
            </div>
            `
        } else {

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

        searchBox.innerHTML = searchResultsHtml

    } catch (e) {

        console.error("Impossible to perform the search", e)

    }

}

window.onload = function () {
    registerSearch()
}