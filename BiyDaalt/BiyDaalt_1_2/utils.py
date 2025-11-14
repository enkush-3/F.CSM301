from math import radians, sin, cos, sqrt, atan2


def haversine(coord1, coord2):
    R = 6371000
    lat1, lon1 = map(radians, coord1)
    lat2, lon2 = map(radians, coord2)
    dlat, dlon = lat2 - lat1, lon2 - lon1
    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    return R * 2 * atan2(sqrt(a), sqrt(1 - a))


def find_nearest_node(coord, nodes, haversine_func=haversine):
    min_dist, nearest_id = float("inf"), None
    for c, nid in nodes.items():
        dist = haversine_func(coord, c)
        if dist < min_dist:
            min_dist, nearest_id = dist, nid
    return nearest_id
