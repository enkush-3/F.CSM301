let map = L.map("map").setView([47.92, 106.92], 12);
L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap contributors"
}).addTo(map);

let points = [];
const colors = ["green", "blue", "purple"];
let currentAlgo = "dijkstra";

document.querySelector(".DFS").onclick = () => currentAlgo = "dfs";
document.querySelector(".BFS").onclick = () => currentAlgo = "bfs";
document.querySelector(".Dijkstra").onclick = () => currentAlgo = "dijkstra";

map.on("click", function (e) {
    points.push([e.latlng.lat, e.latlng.lng]);

    let markerOptions = {
        radius: 4,
        color: "black",
        weight: 1,
        fillColor: points.length === 1 ? "green" : "red",
        fillOpacity: 0.9
    };
    let marker = L.circleMarker(e.latlng, markerOptions).addTo(map);

    let label = points.length === 1 ? "Эхлэх цэг" : "Төгсгөл цэг";
    marker.bindTooltip(label, {permanent: true, direction: "top", offset: [0, -8]}).openTooltip();



    if (points.length === 2) {
        fetch(`/path?start_lat=${points[0][0]}&start_lon=${points[0][1]}&end_lat=${points[1][0]}&end_lon=${points[1][1]}&algo=${currentAlgo}`)
            .then(r => r.json())
            .then(data => {
                if (data.path) {
                    const pathCoords = data.path.map(p => [p.lat, p.lng]);
                    let color;
                    if (currentAlgo == "bfs") color = "green";
                    else if (currentAlgo == "dfs") color = "blue";
                    else if (currentAlgo == "dijkstra") color = "purple";

                    L.polyline(pathCoords, {color: color, weight: 3}).addTo(map);
                } else {
                    alert("Path not found");
                }
            });
        points = [];
    }
});

document.querySelector(".Clear").onclick = () => {
    map.eachLayer((layer) => {
        if (!(layer instanceof L.TileLayer)) {
            map.removeLayer(layer);
        }
    });
    points = [];
};
