public class Layer {
    Neuron[] neurons;

    public Layer(int numNeurons, int inputSize) {
        neurons = new Neuron[numNeurons];
        for (int i = 0; i < numNeurons; i++) {
            neurons[i] = new Neuron(inputSize);
        }
    }

    public double[] feedForward(double[] inputs) {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].activate(inputs);
        }
        return outputs;
    }
}