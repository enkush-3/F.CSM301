// === Газрын зураг үүсгэх ===
const map = L.map("map").setView([47.92, 106.92], 12);
L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap contributors"
}).addTo(map);

let points = [];
let currentAlgo = "dijkstra";

// === Алгоритм сонгох товчлуурууд ===
document.querySelector(".DFS").onclick = () => setAlgorithm("dfs");
document.querySelector(".BFS").onclick = () => setAlgorithm("bfs");
document.querySelector(".Dijkstra").onclick = () => setAlgorithm("dijkstra");

function setAlgorithm(algo) {
    currentAlgo = algo;
    console.log("Сонгосон алгоритм:", algo.toUpperCase());
}

// === Газрын зураг дээр дарахад ===
map.on("click", (e) => {
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
    marker.bindTooltip(label, {permanent: true, direction: "top", offset: [0, -8], interactive: false}).openTooltip();

    if (points.length === 2) {
        const [start, end] = points;

        fetch(`/path?start_lat=${start[0]}&start_lon=${start[1]}&end_lat=${end[0]}&end_lon=${end[1]}&algo=${currentAlgo}`)
            .then(r => r.json())
            .then(data => {
                if (data.path) {
                    const pathCoords = data.path.map(p => [p.lat, p.lng]);

                    let color = "purple";
                    if (currentAlgo === "bfs") color = "green";
                    else if (currentAlgo === "dfs") color = "blue";

                    L.polyline(pathCoords, {color: color, weight: 4}).addTo(map);
                } else {
                    alert("Зам олдсонгүй!");
                }
            })
            .catch(err => {
                console.error("Алдаа:", err);
                alert("Сервертэй холбогдоход алдаа гарлаа.");
            });

        points = [];
    }
});

// === Цэвэрлэх товч ===
document.querySelector(".Clear").onclick = () => {
    map.eachLayer(layer => {
        if (!(layer instanceof L.TileLayer)) map.removeLayer(layer);
    });
    points = [];
};
