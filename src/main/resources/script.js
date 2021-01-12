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

    } catch (e) {

        console.error("Impossible to perform the search", e)

    }

}

window.onload = function () {
    registerSearch()
}