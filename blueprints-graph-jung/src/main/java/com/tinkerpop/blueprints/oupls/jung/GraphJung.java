package com.tinkerpop.blueprints.oupls.jung;


import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.WrapperGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * An implementation of the JUNG graph interface provided by Blueprints graph.
 * In this way, a Blueprints graph is modeled as a JUNG graph.
 * This JUNG model can be used with any algorithms/tools that require a JUNG graph.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GraphJung<T extends Graph> implements edu.uci.ics.jung.graph.Graph<Vertex, Edge>, WrapperGraph<T> {

    private final T graph;

    public GraphJung(final T graph) {
        this.graph = graph;
    }

    public T getBaseGraph() {
        return this.graph;
    }

    public boolean addVertex(final Vertex vertex) {
        if (null != graph.getVertex(vertex.getId()))
            graph.addVertex(vertex.getId());
        return true;
    }

    public boolean removeVertex(final Vertex vertex) {
        this.graph.removeVertex(vertex);
        return true;
    }

    public boolean containsVertex(final Vertex vertex) {
        return this.graph.getVertex(vertex.getId()) != null;
    }

    public int getEdgeCount(final EdgeType edgeType) {
        return this.getEdgeCount();
    }

    public boolean addEdge(final Edge edge, final Collection<? extends Vertex> vertices) {
        if (vertices.size() == 2) {
            Iterator<? extends Vertex> itty = vertices.iterator();
            this.addEdge(edge, itty.next(), itty.next());
        } else {
            throw new IllegalArgumentException();
        }
        return true;
    }

    public boolean addEdge(final Edge edge, final Collection<? extends Vertex> vertices, final EdgeType edgeType) {
        return this.addEdge(edge, vertices);

    }

    public boolean addEdge(final Edge edge, final Vertex outVertex, final Vertex inVertex) {
        this.graph.addEdge(edge.getId(), outVertex, inVertex, edge.getLabel());
        return true;
    }

    public boolean addEdge(final Edge edge, final Vertex outVertex, final Vertex inVertex, final EdgeType edgeType) {
        return this.addEdge(edge, outVertex, inVertex);
    }

    public boolean removeEdge(final Edge edge) {
        this.graph.removeEdge(edge);
        return true;
    }

    public boolean containsEdge(final Edge edge) {
        return this.graph.getEdge(edge.getId()) != null;
    }

    public Collection<Edge> getEdges(final EdgeType edgeType) {
        return this.getEdges();
    }

    public Edge findEdge(final Vertex outVertex, final Vertex inVertex) {
        for (Edge edge : outVertex.getEdges(Direction.OUT)) {
            if (edge.getVertex(Direction.IN).equals(inVertex)) {
                return edge;
            }
        }
        return null;
    }

    public Collection<Edge> findEdgeSet(final Vertex outVertex, final Vertex inVertex) {
        Set<Edge> edges = new HashSet<Edge>();
        for (Edge edge : outVertex.getEdges(Direction.OUT)) {
            if (edge.getVertex(Direction.IN).equals(inVertex)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public boolean isIncident(final Vertex vertex, final Edge edge) {
        return edge.getVertex(Direction.IN).equals(vertex) || edge.getVertex(Direction.OUT).equals(vertex);
    }

    public Collection<Edge> getIncidentEdges(final Vertex vertex) {
        Set<Edge> edges = new HashSet<Edge>();
        for (Edge edge : vertex.getEdges(Direction.IN)) {
            edges.add(edge);
        }
        for (Edge edge : vertex.getEdges(Direction.OUT)) {
            edges.add(edge);
        }
        return edges;
    }

    public EdgeType getDefaultEdgeType() {
        return EdgeType.DIRECTED;
    }

    public EdgeType getEdgeType(final Edge edge) {
        return EdgeType.DIRECTED;
    }

    public int getIncidentCount(final Edge edge) {
        if (edge.getVertex(Direction.IN).equals(edge.getVertex(Direction.OUT)))
            return 1;
        else
            return 2;
    }

    public int getVertexCount() {
        Iterable<Vertex> itty = this.graph.getVertices();
        if (itty instanceof Collection) {
            return ((Collection) itty).size();
        } else {
            int count = 0;
            for (Vertex vertex : itty) {
                count++;
            }
            return count;
        }
    }

    public int getEdgeCount() {
        int count = 0;
        for (Edge edge : this.graph.getEdges()) {
            count++;
        }
        return count;
    }

    public Collection<Edge> getEdges() {
        Iterable<Edge> itty = this.graph.getEdges();
        if (itty instanceof Collection) {
            return (Collection<Edge>) itty;
        } else {
            List<Edge> edges = new ArrayList<Edge>();
            for (Edge e : itty) {
                edges.add(e);
            }
            return edges;
        }
    }

    public Collection<Vertex> getVertices() {
        Iterable<Vertex> itty = this.graph.getVertices();
        if (itty instanceof Collection) {
            return (Collection<Vertex>) itty;
        } else {
            List<Vertex> vertices = new ArrayList<Vertex>();
            for (Vertex v : itty) {
                vertices.add(v);
            }
            return vertices;
        }
    }

    public Collection<Vertex> getIncidentVertices(final Edge edge) {
        List<Vertex> vertices = new ArrayList<Vertex>();
        vertices.add(edge.getVertex(Direction.IN));
        vertices.add(edge.getVertex(Direction.OUT));
        return vertices;
    }

    public Vertex getDest(final Edge edge) {
        return edge.getVertex(Direction.IN);
    }

    public Vertex getSource(final Edge edge) {
        return edge.getVertex(Direction.OUT);
    }

    public Pair<Vertex> getEndpoints(final Edge edge) {
        return new Pair<Vertex>(edge.getVertex(Direction.OUT), edge.getVertex(Direction.IN));
    }

    public boolean isNeighbor(final Vertex outVertex, final Vertex inVertex) {
        for (Edge edge : outVertex.getEdges(Direction.OUT)) {
            if (edge.getVertex(Direction.IN).equals(inVertex))
                return true;
        }
        for (Edge edge : outVertex.getEdges(Direction.IN)) {
            if (edge.getVertex(Direction.OUT).equals(inVertex))
                return true;
        }
        return false;
    }

    public int getNeighborCount(final Vertex vertex) {
        return this.getNeighbors(vertex).size();
    }

    public Collection<Vertex> getNeighbors(final Vertex vertex) {
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Edge e : vertex.getEdges(Direction.OUT)) {
            vertices.add(e.getVertex(Direction.IN));
        }
        for (Edge e : vertex.getEdges(Direction.IN)) {
            vertices.add(e.getVertex(Direction.OUT));
        }
        return vertices;
    }

    public Vertex getOpposite(final Vertex vertex, final Edge edge) {
        if (edge.getVertex(Direction.OUT).equals(vertex))
            return edge.getVertex(Direction.IN);
        else
            return edge.getVertex(Direction.OUT);
    }

    public Collection<Edge> getOutEdges(final Vertex vertex) {

        Iterable<Edge> itty = vertex.getEdges(Direction.OUT);
        if (itty instanceof Collection) {
            return (Collection<Edge>) itty;
        } else {
            List<Edge> edges = new ArrayList<Edge>();
            for (Edge edge : itty) {
                edges.add(edge);
            }
            return edges;
        }
    }

    public Collection<Edge> getInEdges(final Vertex vertex) {
        Iterable<Edge> itty = vertex.getEdges(Direction.IN);
        if (itty instanceof Collection) {
            return (Collection<Edge>) itty;
        } else {
            Set<Edge> edges = new HashSet<Edge>();
            for (Edge edge : itty) {
                edges.add(edge);
            }
            return edges;
        }
    }

    public int getPredecessorCount(final Vertex vertex) {
        return this.getPredecessors(vertex).size();
    }

    public Collection<Vertex> getPredecessors(final Vertex vertex) {
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Edge edge : vertex.getEdges(Direction.IN)) {
            vertices.add(edge.getVertex(Direction.OUT));
        }
        return vertices;
    }

    public int getSuccessorCount(final Vertex vertex) {
        return this.getSuccessors(vertex).size();
    }

    public Collection<Vertex> getSuccessors(final Vertex vertex) {
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Edge edge : vertex.getEdges(Direction.OUT)) {
            vertices.add(edge.getVertex(Direction.IN));
        }
        return vertices;
    }

    public int inDegree(final Vertex vertex) {
        Iterable<Edge> itty = vertex.getEdges(Direction.IN);
        if (itty instanceof Collection) {
            return ((Collection) itty).size();
        } else {
            int count = 0;
            for (Edge edge : itty) {
                count++;
            }
            return count;
        }
    }

    public int outDegree(final Vertex vertex) {
        Iterable<Edge> itty = vertex.getEdges(Direction.OUT);
        if (itty instanceof Collection) {
            return ((Collection) itty).size();
        } else {
            int count = 0;
            for (Edge edge : itty) {
                count++;
            }
            return count;
        }
    }


    public int degree(final Vertex vertex) {
        return this.outDegree(vertex) + this.inDegree(vertex);
    }

    public boolean isDest(final Vertex vertex, final Edge edge) {
        return edge.getVertex(Direction.IN).equals(vertex);
    }

    public boolean isSource(final Vertex vertex, final Edge edge) {
        return edge.getVertex(Direction.OUT).equals(vertex);
    }

    public boolean isPredecessor(final Vertex outVertex, final Vertex inVertex) {
        for (Edge edge : outVertex.getEdges(Direction.IN)) {
            if (edge.getVertex(Direction.OUT).equals(inVertex))
                return true;
        }
        return false;
    }

    public boolean isSuccessor(final Vertex outVertex, final Vertex inVertex) {
        for (Edge edge : outVertex.getEdges(Direction.OUT)) {
            if (edge.getVertex(Direction.IN).equals(inVertex))
                return true;
        }
        return false;
    }

    public String toString() {
        return "graphjung[" + this.graph.toString() + "]";
    }
}
