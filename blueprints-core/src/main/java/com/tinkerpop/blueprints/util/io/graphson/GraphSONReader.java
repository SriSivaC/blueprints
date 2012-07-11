package com.tinkerpop.blueprints.util.io.graphson;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * GraphSONReader reads the data from a TinkerPop JSON stream to a graph.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class GraphSONReader {
    private final Graph graph;

    /**
     * @param graph the graph to populate with the JSON data
     */
    public GraphSONReader(final Graph graph) {
        this.graph = graph;
    }

    /**
     * Input the JSON stream data into the graph.
     * In practice, usually the provided graph is empty.
     *
     * @param jsonInputStream an InputStream of JSON data
     * @throws IOException thrown when the JSON data is not correctly formatted
     */
    public void inputGraph(final InputStream jsonInputStream) throws IOException {
        GraphSONReader.inputGraph(this.graph, jsonInputStream, 1000);
    }

    /**
     * Input the JSON stream data into the graph.
     * In practice, usually the provided graph is empty.
     *
     * @param jsonInputStream an InputStream of JSON data
     * @param bufferSize      the amount of elements to hold in memory before committing a transactions (only valid for TransactionalGraphs)
     * @throws IOException thrown when the JSON data is not correctly formatted
     */
    public void inputGraph(final InputStream jsonInputStream, int bufferSize) throws IOException {
        GraphSONReader.inputGraph(this.graph, jsonInputStream, bufferSize);
    }

    /**
     * Input the JSON stream data into the graph.
     * In practice, usually the provided graph is empty.
     *
     * @param graph           the graph to populate with the JSON data
     * @param jsonInputStream an InputStream of JSON data
     * @throws IOException thrown when the JSON data is not correctly formatted
     */
    public static void inputGraph(final Graph graph, final InputStream jsonInputStream) throws IOException {
        GraphSONReader.inputGraph(graph, jsonInputStream, 1000);
    }

    /**
     * Input the JSON stream data into the graph.
     * More control over how data is streamed is provided by this method.
     *
     * @param inputGraph      the graph to populate with the JSON data
     * @param jsonInputStream an InputStream of JSON data
     * @param bufferSize      the amount of elements to hold in memory before committing a transactions (only valid for TransactionalGraphs)
     * @throws IOException thrown when the JSON data is not correctly formatted
     */
    public static void inputGraph(final Graph inputGraph, final InputStream jsonInputStream, int bufferSize) throws IOException {
        boolean hasEmbeddedTypes = false;
        final JsonFactory jsonFactory = new MappingJsonFactory();
        final JsonParser jp = jsonFactory.createJsonParser(jsonInputStream);

        // if this is a transactional graph then we're buffering
        final BatchGraph graph = BatchGraph.wrap(inputGraph, bufferSize);

        ElementFactory elementFactory = new GraphElementFactory(graph);

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            final String fieldname = jp.getCurrentName() == null ? "" : jp.getCurrentName();
            if (fieldname.equals(GraphSONTokens.EMBEDDED_TYPES)) {
                jp.nextToken();
                hasEmbeddedTypes = jp.getBooleanValue();
            } else if (fieldname.equals(GraphSONTokens.VERTICES)) {
                jp.nextToken();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    final JsonNode node = jp.readValueAsTree();
                    GraphSONUtility.vertexFromJson(node, elementFactory, hasEmbeddedTypes, null);
                }
            } else if (fieldname.equals(GraphSONTokens.EDGES)) {
                jp.nextToken();
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    final JsonNode node = jp.readValueAsTree();
                    final Vertex inV = graph.getVertex(GraphSONUtility.getTypedValueFromJsonNode(node.get(GraphSONTokens._IN_V)));
                    final Vertex outV = graph.getVertex(GraphSONUtility.getTypedValueFromJsonNode(node.get(GraphSONTokens._OUT_V)));
                    GraphSONUtility.edgeFromJSON(node, outV, inV, elementFactory, hasEmbeddedTypes, null);
                }
            }
        }

        jp.close();

        graph.stopTransaction(TransactionalGraph.Conclusion.SUCCESS);
    }




}
