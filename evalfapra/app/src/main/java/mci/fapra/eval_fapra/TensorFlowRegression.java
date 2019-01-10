package mci.fapra.eval_fapra;

import android.content.Context;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class TensorFlowRegression {
    static {
        System.loadLibrary("tensorflow_inference");
    }

    private TensorFlowInferenceInterface inferenceInterface;
    private static final String MODEL_FILE = "file:///android_asset/testmodel_N5X.pb";
    private static final String INPUT_NODE = "conv2d_1_input:0";
    private static final String[] OUTPUT_NODES = {"dense_3:0"};
    private static final String OUTPUT_NODE = "dense_3:0";

    private static final int N_CHANNELS = 1;
    private static final int N_FEATURES = 3;
    private static final int N_STEPS = 90;
    private static final long[] INPUT_SIZE = {1, N_STEPS, N_FEATURES, N_CHANNELS};

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
