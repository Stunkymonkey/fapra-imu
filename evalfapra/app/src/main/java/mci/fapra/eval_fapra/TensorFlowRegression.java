package mci.fapra.eval_fapra;

import android.content.Context;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TensorFlowRegression {
    static {
        System.loadLibrary("tensorflow_inference");
    }

    private TensorFlowInferenceInterface inferenceInterface;
    private static final String MODEL_FILE = "file:///android_asset/testmodel_N5X.pb";
    private static final String INPUT_NODE = "lstm_1_input:0";
    private static final String[] OUTPUT_NODES = {"dense_1/BiasAdd:0"};
    private static final String OUTPUT_NODE = "dense_1/BiasAdd:0";

    private static final long[] INPUT_SIZE = {1, Constants.WINDOW_SIZE, Constants.SENSORS};

    private static final int OUTPUT_SIZE = 2;

    public TensorFlowRegression(final Context context) {
        inferenceInterface = new TensorFlowInferenceInterface(context.getAssets(), MODEL_FILE);
    }

    public float[] predict(float[] data) {
        float[] result = new float[OUTPUT_SIZE];

        inferenceInterface.feed(INPUT_NODE, data, INPUT_SIZE);
        inferenceInterface.run(OUTPUT_NODES);
        inferenceInterface.fetch(OUTPUT_NODE, result);

        return result;
    }
}
