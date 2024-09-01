package Framework;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import Framework.Data.WaveInfo;
import Framework.DataTypes.GlobalContext;
import Framework.Delegates.SoundPlayerDelegates;

import static android.content.ContentValues.TAG;
import static android.media.AudioManager.USE_DEFAULT_STREAM_TYPE;
import static android.media.AudioSystem.MODE_CURRENT;

public class SoundPlayer {

    public SoundPlayerDelegates soundPlayerDelegate;

    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";

    private static final int HEADER_SIZE = 44;

    private static final String CHARSET = "ASCII";

    private Timer sldrUpdateTimer;

    public boolean  IsPlaying()
    {
        if(audioPlayer != null && audioPlayer.isPlaying())
        {
            return true;
        }

        return false;
    }

    /* ... */

    public static WaveInfo readHeader(byte[] wavData)
            throws IOException {

        ByteArrayInputStream wavStream = new ByteArrayInputStream(wavData);

        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        wavStream.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());

        buffer.rewind();
        buffer.position(buffer.position() + 20);
        int format = buffer.getShort();
       // checkFormat(format == 1, "Unsupported encoding: " + format); // 1 means Linear PCM
        int channels = buffer.getShort();
       // checkFormat(channels == 1 || channels == 2, "Unsupported channels: " + channels);
        int rate = buffer.getInt();
       // checkFormat(rate <= 48000 && rate >= 11025, "Unsupported rate: " + rate);
        buffer.position(buffer.position() + 6);
        int bits = buffer.getShort();
       // checkFormat(bits == 16, "Unsupported bits: " + bits);
        int dataSize = 0;
        while (buffer.getInt() != 0x61746164) { // "data" marker
            Log.d(TAG, "Skipping non-data chunk");
            int size = buffer.getInt();
            wavStream.skip(size);

            buffer.rewind();
            wavStream.read(buffer.array(), buffer.arrayOffset(), 8);
            buffer.rewind();
        }
        dataSize = buffer.getInt();
        //checkFormat(dataSize > 0, "wrong datasize: " + dataSize);

        WaveInfo wInfo = new WaveInfo();
        wInfo.channel = channels;
        wInfo.rate = rate;
        wInfo.dataSize = dataSize;

        return wInfo;
    }


    public void audioPlayerDidFinishPlaying() {
        if(soundPlayerDelegate != null)
        {
            soundPlayerDelegate.PlayingSoudFinished();
        }

    }
    MediaPlayer audioPlayer = new MediaPlayer();



    public void PlaySound(String fileName, int StartSec, int EndSec, Float Volume, Float FadeDuration)
    {
        File file = new File(fileName);
        Uri myUri = Uri.fromFile(file);

        PlaySound(myUri, 1.0f, 0.0f, StartSec, EndSec);
    }

    public void PlaySound(String fileName)
    {
        Uri myUri = Uri.parse("android.resource://"+"fouriermachines.ani.clinet.articulation"+"/raw/"+fileName);

        PlaySound(myUri, 1.0f, 0.0f);
    }

    public void PlaySound(String fileName, Float Volume, Float FadeDuration)
    {
        Uri myUri = Uri.parse("android.resource://"+"fouriermachines.ani.clinet.articulation"+"/raw/"+fileName);

        PlaySound(myUri, Volume, FadeDuration);
    }

    public void PauseSound()
    {
        if(audioPlayer != null)
        {
            audioPlayer.stop();
            StopSoundPlayerTimer();
        }
    }

    private File getTempFile(Context context, String url) {
        File file;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            return File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return null;
    }

    AudioTrack atStreamer;

    public void ReadyAudioStream()
    {
        int bufsize = AudioTrack.getMinBufferSize(16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        atStreamer = new AudioTrack(AudioManager.STREAM_MUSIC,
                16000, //sample rate
                AudioFormat.CHANNEL_OUT_MONO, //2 channel
                AudioFormat.ENCODING_PCM_16BIT, // 16-bit
                bufsize,
                AudioTrack.MODE_STREAM );
        atStreamer.setVolume(atStreamer.getMaxVolume());
        atStreamer.play();
    }

    public void PlayAudioStream(byte[] Stream)
    {
        atStreamer.write(Stream, 0, Stream.length);
    }

    public void CloseAudioStream()
    {
        atStreamer.stop();
        atStreamer.release();
    }

    CountDownTimer _CountDownTimer;
    public void PlayWavData(final String WAV_UTF8, Float Volume, Float FadeDuration){
        if(soundPlayerDelegate != null && soundPlayerDelegate.CanPlaySound()) {

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Write the byte array to the track
                        byte[] wavData = Base64.decode(WAV_UTF8.getBytes(), Base64.DEFAULT);
                        WaveInfo wInfo = readHeader(wavData);

                        byte[] audData = Arrays.copyOfRange(wavData, HEADER_SIZE, wavData.length);

                        Log.d("SOUNDPLAY","Started");
                        Log.d("SOUNDPLAY","wav length  :"+wavData.length+" #Header length : "+HEADER_SIZE);
                        Log.d("SOUNDPLAY","aud length  :"+audData.length);

                        int intSize = android.media.AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                AudioFormat.ENCODING_PCM_16BIT);
                       final AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                AudioFormat.ENCODING_PCM_16BIT, audData.length, AudioTrack.MODE_STREAM);

                       if(audData.length < 2)
                       {
                           Log.d("SOUNDPLAY","Invalid audio");
                           if(at != null) {
                               at.stop();
                               at.release();
                           }
                           audioPlayerDidFinishPlaying();
                           Log.d("SOUNDPLAY","REALEASED");
                           return;
                       }

                        if (at != null) {
                            at.setPositionNotificationPeriod(1600); // 100ms => 0.1s * 16000(sample rate)
                            at.setNotificationMarkerPosition(audData.length/2);
                            at.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                                @Override
                                public void onPeriodicNotification(AudioTrack track) {
                                    Log.d("SOUNDPLAY","Track Notified");
                                    if(_CountDownTimer != null)
                                    _CountDownTimer.cancel();

                                    _CountDownTimer = new CountDownTimer(500,  500) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Log.d("SOUNDPLAY","Counter Timeout");
                                            try {
                                                if(at.getState() == AudioTrack.STATE_INITIALIZED) {
                                                    at.stop();
                                                    at.release();
                                                    audioPlayerDidFinishPlaying();
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                Log.e("SOUNDPLAY", e.getMessage());
                                            }
                                            Log.d("SOUNDPLAY","REALEASED by Timeout");
                                        }
                                    }.start();
                                }
                                @Override
                                public void onMarkerReached(AudioTrack track) {
                                    Log.d("SOUNDPLAY","Marker reached");

                                    if(at.getState() == AudioTrack.PLAYSTATE_PLAYING) {
                                        _CountDownTimer.cancel();
                                        try {
                                            if(at.getState() == AudioTrack.STATE_INITIALIZED) {
                                                at.stop();
                                                at.release();
                                                audioPlayerDidFinishPlaying();
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("SOUNDPLAY", e.getMessage());
                                        }
                                    }
                                    Log.d("SOUNDPLAY","REALEASED");
                                }
                            });

                            AudioManager manager = (AudioManager)GlobalContext.context.getSystemService(Context.AUDIO_SERVICE) ;

                            manager.setStreamVolume(AudioManager.STREAM_MUSIC , 10 ,MODE_CURRENT);
                            at.play();
                            at.write(audData, 0, audData.length);



                            //at.stop();
                            //at.release();
                            Log.d("SOUNDPLAY","Written");

                        }

                    } catch (Exception ex) {
                        Log.e("SOUNDPLAY","Error : "+ex.getMessage());
                    }
                }
            });
            t.setPriority(Thread.NORM_PRIORITY);
            t.start();
        }
    }

    public void PlaySound(Uri myUri, final Float Volume, Float FadeDuration, final int StartSec, int EndSec)  {
        if(soundPlayerDelegate != null && soundPlayerDelegate.CanPlaySound())
        {
            audioPlayer = new MediaPlayer();
            try {
                // mediaPlayer.setDataSource(String.valueOf(myUri));

                audioPlayer.setDataSource(GlobalContext.context,myUri);

                audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        audioPlayer.setVolume(Volume, Volume);
                        audioPlayer.seekTo(StartSec);
                        mp.start();
                         StartSoundPlayerTimer();
                    }
                });
                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        audioPlayerDidFinishPlaying();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                soundPlayerDelegate.ReleaseAnyLocksOnPlayError();
            }
            try {
                audioPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                soundPlayerDelegate.ReleaseAnyLocksOnPlayError();
            }
        }
    }



    public void PlaySound(Uri myUri, Float Volume, Float FadeDuration)  {
        if(soundPlayerDelegate != null && soundPlayerDelegate.CanPlaySound())
        {
            audioPlayer = new MediaPlayer();
            try {
                // mediaPlayer.setDataSource(String.valueOf(myUri));

                audioPlayer.setDataSource(GlobalContext.context,myUri);
                audioPlayer.setVolume(Volume, Volume);
                audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        StartSoundPlayerTimer();
                    }
                });
                audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        audioPlayerDidFinishPlaying();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                soundPlayerDelegate.ReleaseAnyLocksOnPlayError();
            }
            try {
                audioPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                soundPlayerDelegate.ReleaseAnyLocksOnPlayError();
            }
            audioPlayer.start();



        }
    }

    void StartSoundPlayerTimer()
    {
        sldrUpdateTimer =  new Timer();
        TimerTask sldrTask = new TimerTask() {
            @Override
            public void run() {
                if(soundPlayerDelegate != null)
                {
                    soundPlayerDelegate.PlayingSoudProgress(audioPlayer.getCurrentPosition());
                }
            }
        };
        sldrUpdateTimer.scheduleAtFixedRate(sldrTask, 0, 100);
    }

    void StopSoundPlayerTimer()
    {
        sldrUpdateTimer.cancel();
        sldrUpdateTimer.purge();
    }

}
