import os

from fastapi import FastAPI
from fastapi.responses import JSONResponse
from fastapi.staticfiles import StaticFiles

from algorithms import dfs, bfs, dijkstra
from graph_build import build_graph
from utils import find_nearest_node

edges, nodes = build_graph("UbMap/ub_roads_full.shp")

app = FastAPI(title="UB Shortest Path API")

static_dir = os.path.join(os.path.dirname(__file__), "web")
app.mount("/map", StaticFiles(directory=static_dir, html=True), name="static")


@app.get("/path")
def get_path(start_lat: float, start_lon: float, end_lat: float, end_lon: float, algo: str = "dijkstra"):
    start_id = find_nearest_node((start_lat, start_lon), nodes)
    end_id = find_nearest_node((end_lat, end_lon), nodes)

    if start_id is None or end_id is None:
        return JSONResponse({"error": "Nearest node not found"}, status_code=404)

    if algo.lower() == "dijkstra":
        path = dijkstra(edges, start_id, end_id)
    elif algo.lower() == "dfs":
        path = dfs(edges, start_id, end_id)
    elif algo.lower() == "bfs":
        path = bfs(edges, start_id, end_id)
    else:
        return JSONResponse({"error": "Invalid algorithm"}, status_code=400)

    if not path:
        return JSONResponse({"error": "Path not found"}, status_code=404)

    id_to_coord = {v: k for k, v in nodes.items()}
    path_coords = [{"lat": id_to_coord[n][0], "lng": id_to_coord[n][1]} for n in path]
    return {"path": path_coords}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)
