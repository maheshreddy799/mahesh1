document.getElementById("dataForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const searchInput = document.getElementById("searchInput").value;

    fetch('/bin/dynamicDataServlet', {
        method: 'POST',
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `searchInput=${encodeURIComponent(searchInput)}`
    })
    .then(response => response.json())
    .then(data => {
        let resultHtml = '';
        if (data && data.length > 0) {
            resultHtml = '<ul>';
            data.forEach(item => {
                resultHtml += `<li>${item}</li>`;
            });
            resultHtml += '</ul>';
        } else {
            resultHtml = 'No data found.';
        }
        document.getElementById("result").innerHTML = resultHtml;
    })
    .catch(() => {
        document.getElementById("result").innerHTML = 'Error fetching data.';
    });
});
