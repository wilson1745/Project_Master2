package wilson.com.project_master2.Modules.recorders;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.List;

import io.github.privacystreams.audio.Audio;
import io.github.privacystreams.audio.AudioOperators;
import io.github.privacystreams.core.Callback;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.purposes.Purpose;
import wilson.com.project_master2.Modules.detection.FeatureExtractor;
import wilson.com.project_master2.Modules.detection.NoiseModel;
import wilson.com.project_master2.Modules.interfaces.DebugView;

public class AudioRecorder extends Thread {

   private boolean stopped = false;
   private static AudioRecord recorder = null;
   private static int N = 0;
   private NoiseModel noiseModel;
   private DebugView debugView;
   private short[] buffer;
   private FeatureExtractor featureExtractor;

   public AudioRecorder(NoiseModel noiseModel, DebugView debugView) {
      this.noiseModel = noiseModel;
      this.debugView = debugView;
      this.featureExtractor = new FeatureExtractor(noiseModel);
   }

   @Override
   public void run() {
      capture();
   }

   private void capture() {
      int i = 0;
      //聲音線程的最高級別，優先程度較THREAD_PRIORITY_AUDIO要高。代碼中無法設置為該優先級。值為-19
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
      //all types of personal data can be accessed and processed with a UQI
      //UQI uqi = new UQI(context);

      /*uqi.getData(Audio.recordPeriodic(durationPerRecord, interval), Purpose.HEALTH("Sleep monitoring"))
              .setField("amp", AudioOperators.getAmplitudeSamples(Audio.AUDIO_DATA))
              .forEach("amp", new Callback<List<Integer>>() {
                 @Override
                 protected void onInput(List<Integer> input) {
                    short shortArray[] = new short[input.size()];
                    for (int i = 0; i < shortArray.length; ++i) {
                       shortArray[i] = input.get(i).shortValue();
                    }
                    process(shortArray);
                 }
              });*/
      if(buffer == null) {
         buffer  = new short[1600];
      }

      if(N == 0 || (recorder == null || recorder.getState() != AudioRecord.STATE_INITIALIZED)) {
         N = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
         if(N < 1600) {
            N = 1600;
         }
         recorder = new AudioRecord(
                 MediaRecorder.AudioSource.MIC,
                 16000,
                 AudioFormat.CHANNEL_IN_MONO,
                 AudioFormat.ENCODING_PCM_16BIT,
                 N
         );
      }
      recorder.startRecording();

      while(!this.stopped) {
         N = recorder.read(buffer, 0, buffer.length);
         process(buffer);
      }
      recorder.stop();
      recorder.release();
   }

   private void process(short[] buffer) {
      featureExtractor.update(buffer);

      if (debugView != null) {
         /*debugView.addPoint2(noiseModel.getNormalizedRLH(), noiseModel.getNormalizedVAR());
         debugView.setLux((float) (noiseModel.getNormalizedRMS()));*/
         debugView.addPoint2(noiseModel.getLastRLH(), noiseModel.getNormalizedVAR());
         debugView.setLux((float) (noiseModel.getLastRMS()));
         debugView.post(new Runnable() {
            @Override
            public void run() {
               debugView.invalidate();
            }
         });
      }
   }

   public void close() {
      stopped = true;
   }
}