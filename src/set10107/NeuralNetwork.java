package set10107;

import java.util.Arrays;

public class NeuralNetwork {

	protected int numInput;
	protected int numHidden;
	protected int numOutput;
	protected double[] inputs;
	protected double[][] ihWeights;
	protected double[] hBiases;
	protected double[] hOutputs;
	protected double[][] hoWeights;
	protected double[] oBiases;
	protected double[] outputs;
//	protected int numWeights;

	
	public NeuralNetwork() {
		this.numInput = Parameters.numInput;
		this.numHidden = Parameters.numHidden;
		this.numOutput = Parameters.numOutput;
		this.inputs = new double[numInput];
		this.ihWeights = makeMatrix(numInput, numHidden);
		this.hBiases = new double[numHidden];
		this.hOutputs = new double[numHidden];
		this.hoWeights = makeMatrix(numHidden, numOutput);
		this.oBiases = new double[numOutput];
		this.outputs = new double[numOutput];		
		Parameters.numGenes = (this.numInput * this.numHidden) + (this.numHidden * this.numOutput) + this.numHidden
				+ this.numOutput; // = numGenes
		
		System.out.println("Creating a " + numInput + "-" + numHidden + "-" + numOutput + " neural network");
	}

	private static double[][] makeMatrix(int rows, int cols) {
		// helper for NN ctor
		double[][] result = new double[rows][];
		for (int r = 0; r < result.length; ++r)
			result[r] = new double[cols];
		return result;
	}

	public void setWeights(double[] weights) {
		// sets weights and biases from weights[]
		int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
		if (weights.length != numWeights) {
			System.out.println("Bad weights array length: ");
			System.exit(0);
		}
		int k = 0; // points into weights param

		for (int i = 0; i < numInput; ++i)
			for (int j = 0; j < numHidden; ++j)
				ihWeights[i][j] = weights[k++];
		for (int i = 0; i < numHidden; ++i)
			hBiases[i] = weights[k++];
		for (int i = 0; i < numHidden; ++i)
			for (int j = 0; j < numOutput; ++j)
				hoWeights[i][j] = weights[k++];
		for (int i = 0; i < numOutput; ++i)
			oBiases[i] = weights[k++];
	}

	public double[] getWeights() {
		// returns current weights and biases
		int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
		double[] result = new double[numWeights];
		int k = 0;
		for (int i = 0; i < ihWeights.length; ++i)
			for (int j = 0; j < ihWeights[0].length; ++j)
				result[k++] = ihWeights[i][j];
		for (int i = 0; i < hBiases.length; ++i)
			result[k++] = hBiases[i];
		for (int i = 0; i < hoWeights.length; ++i)
			for (int j = 0; j < hoWeights[0].length; ++j)
				result[k++] = hoWeights[i][j];
		for (int i = 0; i < oBiases.length; ++i)
			result[k++] = oBiases[i];
		return result;
	}

	public double[] computeOutputs(double[] xValues) {
		// feed-forward mechanism for NN classifier
		if (xValues.length != numInput)
			System.out.println("Bad xValues array length");

		double[] hSums = new double[numHidden];
		double[] oSums = new double[numOutput];

		for (int i = 0; i < xValues.length; ++i)
			this.inputs[i] = xValues[i];

		for (int j = 0; j < numHidden; ++j)
			for (int i = 0; i < numInput; ++i)
				hSums[j] += this.inputs[i] * this.ihWeights[i][j];

		for (int i = 0; i < numHidden; ++i)
			hSums[i] += this.hBiases[i];

		for (int i = 0; i < numHidden; ++i)
			this.hOutputs[i] = hyperTanFunction(hSums[i]);

		for (int j = 0; j < numOutput; ++j)
			for (int i = 0; i < numHidden; ++i)
				oSums[j] += hOutputs[i] * hoWeights[i][j];

		for (int i = 0; i < numOutput; ++i)
			oSums[i] += oBiases[i];

		double[] finalOutputs = new double[numOutput];

		for (int i = 0; i < numOutput; ++i)
			finalOutputs[i] = hyperTanFunction(oSums[i]);

		outputs = Arrays.copyOf(finalOutputs, finalOutputs.length);

		double[] retResult = new double[numOutput];

		retResult = Arrays.copyOf(outputs, outputs.length);
		return retResult;
	} // ComputeOutputs

	private static double hyperTanFunction(double x) {
		if (x < -20.0)
			return -1.0;
		else if (x > 20.0)
			return 1.0;
		else
			return Math.tanh(x);
	}

	

	public double meanSquaredError(double[][] trainData, double[] weights) {
		// how far off are computed values from desired values
		this.setWeights(weights);

		double[] xValues = new double[numInput]; // inputs
		double[] tValues = new double[numOutput]; // targets
		double sumSquaredError = 0.0;
		for (int i = 0; i < trainData.length; ++i) {
			// assumes data has x-values followed by y-values
			xValues = Arrays.copyOf(trainData[i], numInput);

			for (int j = 0; j < numOutput; j++)
				tValues[j] = trainData[i][numInput + j];

			double[] yValues = this.computeOutputs(xValues);
			for (int j = 0; j < yValues.length; ++j)
				sumSquaredError += ((yValues[j] - tValues[j]) * (yValues[j] - tValues[j]));
		}
		return sumSquaredError / trainData.length;
	}

	public double testNetwork(double[][] testData) {
		double totalError = 0;
		double[] xValues = new double[numInput]; // inputs
		double[] tValues = new double[numOutput]; // targets
		double[] yValues; // computed outputs

		for (int i = 0; i < testData.length; ++i) {
			xValues = Arrays.copyOf(testData[i], numInput);

			for (int j = 0; j < numOutput; j++)
				tValues[j] = testData[i][numInput + j];

			yValues = this.computeOutputs(xValues);

			for (int j = 0; j < yValues.length; ++j)
				totalError += ((yValues[j] - tValues[j]) * (yValues[j] - tValues[j]));

		}
		return (totalError / testData.length);
	}

} // NeuralNetwork
