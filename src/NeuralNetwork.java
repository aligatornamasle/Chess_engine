public class NeuralNetwork {
    Layer[] layers;

    public NeuralNetwork(int[] layerSizes) {
        layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i + 1], layerSizes[i]);
        }
    }

    public double predict(double[] inputs) {
        double[] outputs = inputs;
        for (Layer layer : layers) {
            outputs = layer.feedForward(outputs);
        }
        return outputs[0]; // Final output
    }
}
