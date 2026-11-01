import java.util.Random;

public class Neuron {
    double[] weights;
    double bias;
    double output;

    public Neuron(int inputSize) {
        weights = new double[inputSize];
        Random random = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = random.nextDouble() - 0.5; // Initialize between -0.5 and 0.5
        }
        bias = random.nextDouble() - 0.5;
    }

    public double activate(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        output = sigmoid(sum); // You can use ReLU or tanh too
        return output;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
