import heapq
from collections import deque


def dfs(edges, start, goal):
    stack = [(start, [start])]
    visited = set()

    while stack:
        node, path = stack.pop()
        if node == goal:
            return path

        if node in visited:
            continue
        visited.add(node)

        for neighbor, _ in edges.get(node, []):
            if neighbor not in visited:
                stack.append((neighbor, path + [neighbor]))

    return None

def bfs(edges, start, goal):
    queue = deque([(start, [start])])
    visited = set()
    while queue:
        node, path = queue.popleft()
        if node == goal:
            return path
        if node in visited:
            continue
        visited.add(node)
        for neighbor, _ in edges.get(node, []):
            queue.append((neighbor, path + [neighbor]))
    return None


def dijkstra(edges, start_id, end_id):
    pq = [(0, start_id, [start_id])]
    visited = set()
    while pq:
        cost, node, path = heapq.heappop(pq)
        if node == end_id:
            return path
        if node in visited:
            continue
        visited.add(node)
        for neighbor, w in edges.get(node, []):
            if neighbor not in visited:
                heapq.heappush(pq, (cost + w, neighbor, path + [neighbor]))
    return None
