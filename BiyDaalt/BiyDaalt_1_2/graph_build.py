from collections import defaultdict

import geopandas as gpd
from shapely.geometry import LineString, MultiLineString

from utils import haversine

edges = defaultdict(list)
nodes = {}
node_id_counter = 0


def get_node_id(coord):
    global node_id_counter
    if coord not in nodes:
        nodes[coord] = node_id_counter
        node_id_counter += 1
    return nodes[coord]


def build_graph(shp_path="UbMap/ub_roads_full.shp"):
    global edges, nodes, node_id_counter
    edges.clear()
    nodes.clear()
    node_id_counter = 0

    gdf = gpd.read_file(shp_path)

    if gdf.crs is None:
        gdf.set_crs(epsg=4326, inplace=True)
    elif gdf.crs.to_string() != "EPSG:4326":
        gdf = gdf.to_crs(epsg=4326)

    for _, row in gdf.iterrows():
        geom = row.geometry
        if geom is None:
            continue

        parts = [geom] if isinstance(geom, LineString) else list(geom.geoms) if isinstance(geom,MultiLineString) else []
        oneway = row["oneway"] if "oneway" in gdf.columns else None

        for part in parts:
            coords = list(part.coords)
            for i in range(len(coords) - 1):
                start, end = coords[i], coords[i + 1]
                start_latlon = (start[1], start[0])
                end_latlon = (end[1], end[0])

                start_id = get_node_id(start_latlon)
                end_id = get_node_id(end_latlon)
                weight = haversine(start_latlon, end_latlon)

                edges[start_id].append((end_id, weight))
                if str(oneway).lower() not in ("yes", "true", "1"):
                    edges[end_id].append((start_id, weight))

    return edges, nodes
