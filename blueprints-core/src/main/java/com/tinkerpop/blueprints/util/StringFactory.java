package com.tinkerpop.blueprints.util;

import com.tinkerpop.blueprints.*;


/**
 * A collection of helpful methods for creating standard toString() representations of graph-related objects.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StringFactory {

    public static final String V = "v";
    public static final String E = "e";
    public static final String L_BRACKET = "[";
    public static final String R_BRACKET = "]";
    public static final String AT = " @ ";
    public static final String DASH = "-";
    public static final String ARROW = "->";
    public static final String COLON = ":";

    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String EMPTY_STRING = "";

    public static String vertexString(final Vertex vertex) {
        return V + L_BRACKET + vertex.getId() + R_BRACKET;
    }

    public static String vertexString(final TimeAwareVertex vertex) {
        return V + L_BRACKET + vertex.getId() + AT + vertex.getTimeId() + R_BRACKET;
    }

    public static String edgeString(final Edge edge) {
        return E + L_BRACKET + edge.getId() + R_BRACKET + L_BRACKET + edge.getVertex(Direction.OUT).getId() + DASH + edge.getLabel() + ARROW + edge.getVertex(Direction.IN).getId() + R_BRACKET;
    }

    public static String edgeString(final TimeAwareEdge edge) {
        return E + L_BRACKET + edge.getId() + AT + edge.getTimeId() + R_BRACKET +
                   L_BRACKET + edge.getVertex(Direction.OUT).getId() + AT + ((TimeAwareVertex)edge.getVertex(Direction.OUT)).getTimeId() +
                   " " + DASH + edge.getLabel() + ARROW + " " + edge.getVertex(Direction.IN).getId() + AT + ((TimeAwareVertex)edge.getVertex(Direction.IN)).getTimeId() + R_BRACKET;
    }

    public static String graphString(final Graph graph, final String internalString) {
        return graph.getClass().getSimpleName().toLowerCase() + L_BRACKET + internalString + R_BRACKET;
    }

    public static String indexString(final Index index) {
        return "index" + L_BRACKET + index.getIndexName() + COLON + index.getIndexClass().getSimpleName() + R_BRACKET;
    }
}
