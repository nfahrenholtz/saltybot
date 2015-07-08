package saltybot.graph;

public class Edge<T, W extends Weight> {

    private final T source, sink;
    private final W weight;

    public Edge(final T source, final T sink, final W weight) {
        this.source = source;
        this.sink = sink;
        this.weight = weight;
    }

    public double getWeightValue() {
        return weight.getValue();
    }

    public W getWeight() {
        return weight;
    }

    public T getSource() {
        return source;
    }

    public T getSink() {
        return sink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Edge<?, ?> edge = (Edge<?, ?>) o;
        return source.equals(edge.source) && sink.equals(edge.sink);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + sink.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", sink=" + sink +
                ", weight=" + getWeightValue() +
                '}';
    }
}
