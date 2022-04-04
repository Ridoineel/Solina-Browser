
document.getElementById("searchForm").addEventListener("submit", (event) => {
    event.preventDefault();

    let textField = document.getElementById("textField");

    window.location = "https://www.google.com/search?&q=" + textField.value;
});