package saltybot.graph;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DirectedGraph<T, W extends Weight> {

    private final Map<T, Map<T, Edge<T, W>>> adjacency;

    public DirectedGraph() {
        this.adjacency = new HashMap<T, Map<T, Edge<T, W>>>() {
            @Override
            public Map<T, Edge<T, W>> get(final Object key) {
                Map<T, Edge<T, W>> map = super.get(key);
                if (map == null) {
                    map = new HashMap<>();
                    this.put((T)key, map);
                }
                return map;
            }
        };
    }

    public Collection<Edge<T, W>> retrieve(final T source) {
        final Map<T, Edge<T, W>> edges = adjacency.get(source);
        return edges.values();
    }

    public Edge<T, W> retrieve(final T source, final T sink) {
        final Map<T, Edge<T, W>> edges = adjacency.get(source);
        return edges.get(sink);
    }

    public Edge<T, W> insert(final T source,
                             final T sink,
                             final W weight) {
        final Edge<T, W> edge = new Edge<>(source, sink, weight);
        adjacency.get(source).put(sink, edge);
        return edge;
    }

    public Edge<T, W> update(final T source,
                             final T sink,
                             final Function<W, W> action) {
        final Edge<T, W> edge = retrieve(source, sink);
        final W weight = edge == null ? null : edge.getWeight();
        return insert(source, sink, action.apply(weight));
    }

    public List<Edge<T, W>> findShortestPath(final T start, final T finish) {

        final Queue<T> vertices = new PriorityQueue<>();
        final Map<T, Double> distances = new HashMap<>(adjacency.size());
        final Map<T, Edge<T, W>> trail = new HashMap<>(adjacency.size());

        vertices.offer(start);
        distances.put(start, 0d);

        while (!vertices.isEmpty()) {
            final T vertex = vertices.poll();
            final Map<T, Edge<T, W>> adjacent = adjacency.get(vertex);

            if (vertex == finish) break; //it is possible to stop once we reach the finish vertex

            for (final Edge<T, W> edge: adjacent.values()) {
                final double distance = distances.getOrDefault(edge.getSink(), Double.POSITIVE_INFINITY);
                final double nextDistance = distances.get(edge.getSource()) + edge.getWeightValue();

                if (nextDistance < distance) {
                    distances.put(edge.getSink(), nextDistance);
                    trail.put(edge.getSink(), edge);
                    vertices.add(edge.getSink());
                }
            }
        }

        //condense the shortest path to a list
        final Deque<Edge<T, W>> path = new ArrayDeque<>(trail.size());
        for (T current = finish; trail.containsKey(current); ) {
            final Edge<T, W> edge = trail.get(current);
            path.offerFirst(edge);
            current = edge.getSource();
        }
        return path.stream().collect(Collectors.toList());
    }
}
